package ca.tweetzy.vouchers.impl;

import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.sync.SynchronizeResult;
import ca.tweetzy.vouchers.api.voucher.BaseVoucher;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.api.voucher.VoucherSettings;
import ca.tweetzy.vouchers.api.voucher.VoucherType;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.Message;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.api.voucher.reward.Reward;
import ca.tweetzy.vouchers.api.voucher.reward.RewardMode;
import ca.tweetzy.vouchers.api.voucher.reward.RewardType;
import ca.tweetzy.vouchers.impl.message.VoucherActionBarMessage;
import ca.tweetzy.vouchers.impl.message.VoucherBroadcastMessage;
import ca.tweetzy.vouchers.impl.message.VoucherChatMessage;
import ca.tweetzy.vouchers.impl.message.VoucherTitleMessage;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import ca.tweetzy.vouchers.model.TimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StandardVoucher extends BaseVoucher {

	private final String id;
	private String item;
	private String name;
	private List<String> description;
	private final VoucherSettings settings;
	private final List<Message> messages;
	private final List<Reward> rewards;

	public StandardVoucher(
			@NonNull final String id,
			@NonNull final String item,
			@NonNull final String name,
			@NonNull final List<String> description,
			@NonNull final VoucherSettings settings,
			@NonNull final List<Message> messages,
			@NonNull final List<Reward> rewards
	) {
		super(VoucherType.STANDARD);
		this.id = id;
		this.item = item;
		this.name = name;
		this.description = description;
		this.settings = settings;
		this.messages = messages;
		this.rewards = rewards;
	}

	public static StandardVoucher empty(@NonNull final String id) {
		final List<Message> msgs = new ArrayList<>();
		final List<Reward> rewardList = new ArrayList<>();

		msgs.add(new VoucherBroadcastMessage("&aDefault broadcast message"));
		msgs.add(new VoucherChatMessage("&aHi &e%player &7this is a chat msg"));
		msgs.add(new VoucherActionBarMessage("&aThis is an actionbar msg"));
		msgs.add(new VoucherTitleMessage("&aDefault title msg", "&bDefault subtitle msg", 20, 20, 20));

		rewardList.add(new ItemReward(
				CompMaterial.CAKE.parseItem(),
				100,
				0,
				List.of(new VoucherChatMessage("&bYou won some cake"), new VoucherTitleMessage("&bReward Won", "&e+1 Cake", 20, 20, 20))));

		rewardList.add(new CommandReward(
				"eco give %player% 1000",
				100,
				0,
				List.of(new VoucherChatMessage("&bYou won &a$100"), new VoucherBroadcastMessage("&e%player% &7has won &a$1000"))
		));

		return new StandardVoucher(
				id.toLowerCase(),
				"PAPER",
				"&e%s Voucher".formatted(id),
				List.of("&7This is the default lore for new vouchers", "&7You can change this in the file or gui"),
				new VoucherOptions(
						true,
						true,
						"vouchers.use.%s".formatted(id.toLowerCase()),
						true,
						CompSound.ENTITY_BAT_TAKEOFF,
						true,
						true,
						-1,
						false,
						1,
						RewardMode.AUTOMATIC
				),
				msgs,
				rewardList
		);
	}

	@Override
	public @NonNull String getId() {
		return this.id.toLowerCase();
	}

	@Override
	public String getItem() {
		return this.item;
	}

	@Override
	public void setItem(@NotNull String item) {
		this.item = item;
	}

	@Override
	public @NonNull String getDisplayName() {
		return this.name;
	}

	@Override
	public @NonNull List<String> getDescription() {
		return this.description;
	}

	@Override
	public void setDisplayName(@NonNull String displayName) {
		this.name = displayName;
	}

	@Override
	public void setDescription(@NonNull List<String> description) {
		this.description = description;
	}

	@Override
	public VoucherSettings getSettings() {
		return this.settings;
	}

	@Override
	public List<Message> getMessages() {
		return this.messages;
	}

	@Override
	public List<Reward> getRewards() {
		return this.rewards;
	}

	@Override
	public void store(@NonNull Consumer<Voucher> stored) {
		Vouchers.getInstance().getServer().getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
			File directory = new File(Vouchers.getInstance().getDataFolder() + "/voucher-files/");
			if (!directory.exists()) {
				directory.mkdir();
			}

			try (Writer writer = new FileWriter(String.format("%s/voucher-files/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()))) {
				Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
				gson.toJson(getAsJSON(), writer);
				stored.accept(this);
			} catch (IOException e) {
			}
		});
	}

	@Override
	public void sync(@Nullable Consumer<SynchronizeResult> syncResult) {
		Vouchers.getInstance().getServer().getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
			File file = new File(String.format("%s/voucher-files/%s.json", Vouchers.getInstance().getDataFolder(), getId().toLowerCase()));

			if (file.exists()) {
				try (Reader reader = new FileReader(file)) {
					Gson gson = new Gson();
					JsonObject existingData = gson.fromJson(reader, JsonObject.class);
					JsonObject newData = gson.fromJson(getAsJSON(), JsonObject.class);

					// Update only changed fields
					updateChangedFields(existingData, newData);

					// Write the updated data back to the file
					try (Writer writer = new FileWriter(file)) {
						Gson gsonWriter = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
						gsonWriter.toJson(existingData, writer);

						if (syncResult != null) {
							syncResult.accept(SynchronizeResult.SUCCESS);
						}
					} catch (IOException e) {
						if (syncResult != null) {
							syncResult.accept(SynchronizeResult.FAILURE);
						}
					}
				} catch (IOException e) {
					if (syncResult != null) {
						syncResult.accept(SynchronizeResult.FAILURE);
					}
				}
			}

//			else {
//				// If the file does not exist, simply store the new data
//				store(null);
//				if (syncResult != null) {
//					syncResult.accept();
//				}
//			}
		});
	}

	// Helper method to update only changed fields
	private void updateChangedFields(JsonObject existingData, JsonObject newData) {
		for (String key : newData.keySet()) {
			if (!existingData.has(key) || !existingData.get(key).equals(newData.get(key))) {
				existingData.add(key, newData.get(key));
			}
		}

		// Optionally, remove fields that are present in existing data but not in new data
		for (String key : existingData.keySet()) {
			if (!newData.has(key)) {
				existingData.remove(key);
			}
		}
	}

	@Override
	public JsonObject getAsJSON() {
		final JsonObject voucherObject = new JsonObject();
		voucherObject.addProperty("item", this.item);
		// Appearance
		final JsonObject appearanceObject = new JsonObject();
		appearanceObject.addProperty("display_name", this.name);
		appearanceObject.addProperty("glow", this.settings.useGlow());
		// description
		final JsonArray descriptionArray = new JsonArray();
		this.description.forEach(descriptionArray::add);
		appearanceObject.add("description", descriptionArray);
		voucherObject.add("appearance", appearanceObject);

		// permission
		final JsonObject permissionsObject = new JsonObject();
		permissionsObject.addProperty("requires_permission", this.settings.usePermission());
		permissionsObject.addProperty("use_permission", this.settings.getPermission());
		voucherObject.add("permissions", permissionsObject);

		// sounds
		final JsonObject soundsObject = new JsonObject();
		soundsObject.addProperty("play_sound", this.settings.useSound());
		soundsObject.addProperty("use_sound", this.settings.getSound().name());
		voucherObject.add("sounds", soundsObject);

		final JsonObject usageObject = new JsonObject();
		usageObject.addProperty("remove_on_use", this.settings.isRemoveOnUse());
		usageObject.addProperty("ask_for_confirm", this.settings.isAskForConfirm());
		usageObject.addProperty("max_uses", this.settings.getMaximumUses());

		final JsonObject cooldownObject = new JsonObject();
		cooldownObject.addProperty("use_cooldown", this.settings.useCooldown());
		cooldownObject.addProperty("cooldown_time", TimeConverter.convertSecondsToHumanReadable(this.settings.getCooldown()));
		usageObject.add("cooldown", cooldownObject);
		voucherObject.add("usage", usageObject);

		// messages
		voucherObject.add("messages", convertMessagesToObject(this.messages));
		// rewards
		final JsonObject rewardOptionsObject = new JsonObject();
		rewardOptionsObject.addProperty("reward_mode", this.settings.getRewardMode().name());
		final JsonArray rewardsObject = new JsonArray();
		this.rewards.forEach(reward -> {
			final JsonObject rewardObject = new JsonObject();
			rewardObject.addProperty("chance", reward.getChance());
			rewardObject.addProperty("delay", reward.getDelay());
			rewardObject.add("messages", convertMessagesToObject(reward.getMessages()));

			if (reward instanceof CommandReward commandReward) {
				rewardObject.addProperty("type", RewardType.COMMAND.name());
				rewardObject.addProperty("command", commandReward.getCommand());
			}

			if (reward instanceof ItemReward itemReward) {
				rewardObject.addProperty("type", RewardType.ITEM.name());
				rewardObject.addProperty("item", QuickItem.toString(itemReward.getItem()));
			}

			rewardsObject.add(rewardObject);
		});

		rewardOptionsObject.add("rewards", rewardsObject);
		voucherObject.add("reward_options", rewardOptionsObject);

		return voucherObject;
	}

	private JsonObject convertMessagesToObject(List<Message> messages) {
		final JsonObject messagesObject = new JsonObject();

		final JsonArray broadcastMessages = new JsonArray();
		final JsonArray chatMessages = new JsonArray();
		final JsonArray actionBarMessages = new JsonArray();
		final JsonArray titleMessages = new JsonArray();

		messages.stream().map(msg -> (BaseMessage) msg).forEach(message -> {
			if (message.getMessageType() == MessageType.BROADCAST)
				broadcastMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.CHAT)
				chatMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.ACTION_BAR)
				actionBarMessages.add(message.getPrimaryContent());
			if (message.getMessageType() == MessageType.TITLE) {
				final VoucherTitleMessage titleMessage = (VoucherTitleMessage) message;
				final JsonObject titleObject = new JsonObject();
				titleObject.addProperty("title", titleMessage.getPrimaryContent());
				titleObject.addProperty("subtitle", titleMessage.getSecondaryContent());
				titleObject.addProperty("fade_in", titleMessage.getFadeInTime());
				titleObject.addProperty("stay_duration", titleMessage.getFadeInTime());
				titleObject.addProperty("fade_out", titleMessage.getFadeInTime());
				titleMessages.add(titleObject);
			}
		});

		messagesObject.add("broadcast", broadcastMessages);
		messagesObject.add("chat_messages", chatMessages);
		messagesObject.add("actionbar", actionBarMessages);
		messagesObject.add("titles", titleMessages);

		return messagesObject;
	}

	@Override
	public long getTimeCreated() {
		return 0;
	}

	@Override
	public long getLastUpdated() {
		return 0;
	}
}
