package ca.tweetzy.vouchers.model.manager;

import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.manager.KeyValueManager;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.impl.StandardVoucher;
import ca.tweetzy.vouchers.impl.VoucherOptions;
import ca.tweetzy.vouchers.impl.message.VoucherActionBarMessage;
import ca.tweetzy.vouchers.impl.message.VoucherBroadcastMessage;
import ca.tweetzy.vouchers.impl.message.VoucherChatMessage;
import ca.tweetzy.vouchers.impl.message.VoucherTitleMessage;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class VoucherManager extends KeyValueManager<String, Voucher> {

	public VoucherManager() {
		super("Voucher");
	}

	public boolean doesVoucherWithIdExists(@NonNull final String voucherId) {
		return this.getManagerContent().containsKey(ChatColor.stripColor(voucherId).toLowerCase());
	}

	@Override
	public void load() {
		// load existing voucher files
		Bukkit.getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {

			File vouchersDirectory = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files");
			File[] files = vouchersDirectory.listFiles();

			if (files == null) return;

			for (File file : files) {
				final String voucherId = file.getName().replace(".json", "").toLowerCase();
				final Voucher voucher = loadVoucherFromFile(file);

				if (voucher != null)
					add(voucherId, voucher);
			}

		});
	}

	public Voucher loadVoucherFromFile(File file) {
		final String voucherId = file.getName().replace(".json", "").toLowerCase();
		final JsonObject object;
		try {
			object = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (!object.has("appearance")) {
			return null;
		}

		final JsonObject appearanceObject = object.get("appearance").getAsJsonObject();
		final String displayName = appearanceObject.has("display_name") ? appearanceObject.get("display_name").getAsString() : "Un-named voucher";

		final ArrayList<String> description = new ArrayList<>();

		if (appearanceObject.has("description")) {
			final JsonArray descArr = appearanceObject.get("description").getAsJsonArray();
			descArr.forEach(element -> description.add(element.getAsString()));
		}

		final VoucherOptions options = new VoucherOptions();
		options.setUseGlow(appearanceObject.has("glow") && appearanceObject.get("glow").getAsBoolean());

		// permissions
		if (object.has("permissions")) {
			final JsonObject permissionObject = object.get("permissions").getAsJsonObject();
			options.setUsePermission(permissionObject.has("requires_permission") && permissionObject.get("requires_permission").getAsBoolean());
			options.setPermission(permissionObject.has("use_permission") ? permissionObject.get("use_permission").getAsString() : "vouchers.use.%s".formatted(voucherId));
		}

		// sounds
		if (object.has("sounds")) {
			final JsonObject soundsObject = object.get("sounds").getAsJsonObject();
			options.setUseSound(soundsObject.has("play_sound") && soundsObject.get("play_sound").getAsBoolean());
			options.setSound(soundsObject.has("use_sound") ? CompSound.of(soundsObject.get("use_sound").getAsString()).orElse(CompSound.ENTITY_BAT_TAKEOFF) : CompSound.ENTITY_BAT_TAKEOFF);
		}

		// usage
		if (object.has("usage")) {
			final JsonObject usageObject = object.get("usage").getAsJsonObject();
			options.setRemoveOnUse(usageObject.has("remove_on_use") && usageObject.get("remove_on_use").getAsBoolean());
			options.setAskForConfirmation(usageObject.has("ask_for_confirm") && usageObject.get("ask_for_confirm").getAsBoolean());
			options.setMaximumUses(usageObject.has("maximum_uses") ? usageObject.get("maximum_uses").getAsInt() : -1);

			if (usageObject.has("cooldown")) {
				final JsonObject cooldownObject = usageObject.get("cooldown").getAsJsonObject();
				options.setUseCooldown(cooldownObject.has("use_cooldown") && cooldownObject.get("use_cooldown").getAsBoolean());

				if (cooldownObject.has("cooldown_time")) {
					final String rawCooldownTime = cooldownObject.get("cooldown_time").getAsString();
					final long milliseconds = TimeConverter.convertHumanReadableTime(rawCooldownTime);

					options.setCooldown(TimeUnit.MILLISECONDS.toSeconds(milliseconds));
				}
			}

		}

		// rewards
		final List<Reward> rewardList = new ArrayList<>();

		if (object.has("reward_options")) {
			final JsonObject rewardOptionsObject = object.get("reward_options").getAsJsonObject();

			// rewards list
			if (rewardOptionsObject.has("rewards")) {
				final JsonArray rewardObjects = rewardOptionsObject.get("rewards").getAsJsonArray();
				// todo set reward mode
				final RewardMode rewardMode = rewardOptionsObject.has("reward_mode") ? Enum.valueOf(RewardMode.class, rewardOptionsObject.get("reward_mode").getAsString()) : RewardMode.AUTOMATIC;
				options.setRewardMode(rewardMode);

				rewardObjects.forEach(rewardObjectElement -> {
					final JsonObject rewardObject = rewardObjectElement.getAsJsonObject();
					// TODO figure out the type of reward it is
					if (!rewardObject.has("type") || !rewardObject.has("chance") || !rewardObject.has("delay")) return;

					final RewardType rewardType = Enum.valueOf(RewardType.class, rewardObject.get("type").getAsString());
					final double chance = rewardObject.get("chance").getAsDouble();
					final int delay = rewardObject.get("delay").getAsInt();
					final List<Message> rewardMessages = extractMessages(rewardObject);

					if (rewardType == RewardType.COMMAND) {
						rewardList.add(new CommandReward(
								rewardObject.get("command").getAsString(),
								chance,
								delay,
								rewardMessages
						));
					} else {
						rewardList.add(new ItemReward(
								QuickItem.of(rewardObject.get("item").getAsString()).make(),
								chance,
								delay,
								rewardMessages
						));
					}
				});
			}
		}

		return new StandardVoucher(
				voucherId,
				object.has("item") ? object.get("item").getAsString(): "PAPER",
				displayName,
				description,
				options,
				extractMessages(object),
				rewardList
		);
	}

	public List<Message> extractMessages(@NonNull final JsonObject object) {
		final List<Message> messageList = new ArrayList<>();

		if (object.has("messages")) {
			final JsonObject messagesObject = object.get("messages").getAsJsonObject();

			// broadcast messages
			if (messagesObject.has("broadcast"))
				messagesObject.get("broadcast").getAsJsonArray().forEach(element -> {
					final String line = element.getAsString();
					messageList.add(new VoucherBroadcastMessage(line));
				});

			if (messagesObject.has("chat_messages"))
				messagesObject.get("chat_messages").getAsJsonArray().forEach(element -> {
					final String line = element.getAsString();
					messageList.add(new VoucherChatMessage(line));
				});

			if (messagesObject.has("actionbar"))
				messagesObject.get("actionbar").getAsJsonArray().forEach(element -> {
					final String line = element.getAsString();
					messageList.add(new VoucherActionBarMessage(line));
				});

			if (messagesObject.has("titles"))
				messagesObject.get("titles").getAsJsonArray().forEach(element -> {
					final JsonObject titleObject = element.getAsJsonObject();
					messageList.add(new VoucherTitleMessage(
							titleObject.get("title").getAsString(),
							titleObject.get("subtitle").getAsString(),
							titleObject.get("fade_in").getAsInt(),
							titleObject.get("stay_duration").getAsInt(),
							titleObject.get("fade_out").getAsInt()
					));
				});
		}

		return messageList;
	}
}
