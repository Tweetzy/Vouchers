package ca.tweetzy.vouchers.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.flight.nbtapi.NBT;
import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.QuickItem;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.*;
import ca.tweetzy.vouchers.impl.ActiveVoucher;
import ca.tweetzy.vouchers.impl.VoucherMessage;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public final class CommandSync extends Command {

	public CommandSync() {
		super(AllowedExecutor.BOTH, "sync");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length == 0) {
			return ReturnType.INVALID_SYNTAX;
		}

		final boolean syncAll = args.length == 1 && args[0].equalsIgnoreCase("*");

		if (syncAll) {
			Bukkit.getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> {
				File vouchersDirectory = new File(Vouchers.getInstance().getDataFolder() + "/vouchers");
				File[] files = vouchersDirectory.listFiles();

				if (files == null) return;

				for (File file : files) {
					handleSync(sender, file);
				}
			});
		} else {
			final File file = new File(Vouchers.getInstance().getDataFolder() + "/vouchers/" + args[0].toLowerCase() + ".json");
			if (!file.exists()) {
				return ReturnType.FAIL;
			}

			Bukkit.getScheduler().runTaskAsynchronously(Vouchers.getInstance(), () -> handleSync(sender, file));
		}


		return ReturnType.SUCCESS;
	}

	private void handleSync(CommandSender sender, File file) {
		final String voucherId = file.getName().replace(".json", "").toLowerCase();
		final JsonObject object;
		try {
			object = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		Voucher voucher = Vouchers.getVoucherManager().find(voucherId);
		boolean requiresCreationAfter = false;
		if (voucher == null) {
			// create voucher if it doesn't exist
			voucher = new ActiveVoucher(voucherId, "&e" + voucherId, CompMaterial.PAPER.parseItem(), List.of("&7Sample Lore"), RewardMode.AUTOMATIC, new VoucherSettings(), new ArrayList<>(), EquipmentSlot.HAND);
			requiresCreationAfter = true;
		}

		// update
		voucher.setItem(QuickItem.of(object.get("item").getAsString()).make());
		voucher.setName(object.get("displayName").getAsString());

		final ArrayList<String> desc = new ArrayList<>();
		final JsonArray descArr = object.get("description").getAsJsonArray();
		descArr.forEach(element -> desc.add(element.getAsString()));

		voucher.setDescription(desc);

		voucher.setRewardMode(Enum.valueOf(RewardMode.class, object.get("rewardMode").getAsString().toUpperCase()));
		voucher.getOptions().setMaxUses(object.get("maxUses").getAsInt());
		voucher.getOptions().setCooldown(object.get("cooldown").getAsInt());
		voucher.getOptions().setAskConfirm(object.get("askForConfirm").getAsBoolean());
		voucher.getOptions().setRemoveOnUse(object.get("removeOnUse").getAsBoolean());
		voucher.getOptions().setGlowing(object.get("glowing").getAsBoolean());
		voucher.getOptions().setRequiresPermission(object.get("requirePermission").getAsBoolean());
		voucher.getOptions().setPermission(object.get("permission").getAsString());
		voucher.getOptions().setPlayingSound(object.get("playSound").getAsBoolean());
		voucher.getOptions().setSound(CompSound.matchCompSound(object.get("sound").getAsString().toUpperCase()).orElse(CompSound.ENTITY_EXPERIENCE_ORB_PICKUP));

		final ArrayList<Message> messages = new ArrayList<>();
		JsonArray messageArrays = object.get("broadcastMessages").getAsJsonArray();
		messageArrays.forEach(element -> messages.add(new VoucherMessage(MessageType.BROADCAST, element.getAsString(), 0, 0, 0)));

		messageArrays = object.get("chatMessages").getAsJsonArray();
		messageArrays.forEach(element -> messages.add(new VoucherMessage(MessageType.CHAT, element.getAsString(), 0, 0, 0)));

		messageArrays = object.get("actionbarMessages").getAsJsonArray();
		messageArrays.forEach(element -> messages.add(new VoucherMessage(MessageType.ACTION_BAR, element.getAsString(), 0, 0, 0)));

		JsonObject titleTitleSubObj = object.get("titleMessage").getAsJsonObject();
		if (titleTitleSubObj.has("message")) {
			messages.add(new VoucherMessage(
					MessageType.TITLE,
					titleTitleSubObj.get("message").getAsString(),
					titleTitleSubObj.get("fadeIn").getAsInt(),
					titleTitleSubObj.get("stay").getAsInt(),
					titleTitleSubObj.get("fadeOut").getAsInt()
			));
		}


		titleTitleSubObj = object.get("subtitleMessage").getAsJsonObject();
		if (titleTitleSubObj.has("message")) {
			messages.add(new VoucherMessage(
					MessageType.SUBTITLE,
					titleTitleSubObj.get("message").getAsString(),
					titleTitleSubObj.get("fadeIn").getAsInt(),
					titleTitleSubObj.get("stay").getAsInt(),
					titleTitleSubObj.get("fadeOut").getAsInt()
			));
		}

		voucher.getOptions().setMessages(messages);

		JsonArray rewardJson = object.get("rewards").getAsJsonArray();
		final ArrayList<Reward> rewardArray = new ArrayList<>();

		rewardJson.forEach(rewardElement -> {
			final JsonObject rewardObj = rewardElement.getAsJsonObject();
			final RewardType rewardType = Enum.valueOf(RewardType.class, rewardObj.get("type").getAsString());

			final int delay = rewardObj.get("delay").getAsInt();
			final double chance = rewardObj.get("chance").getAsDouble();

			if (rewardType == RewardType.ITEM) {
				ItemStack item = NBT.itemStackFromNBT(NBT.parseNBT(rewardObj.get("item").getAsString()));
				rewardArray.add(new ItemReward(item, chance));
			}

			if (rewardType == RewardType.COMMAND) {
				final CommandReward commandReward = new CommandReward(
						rewardObj.get("command").getAsString(),
						chance,
						delay
				);

				commandReward.setClaimMessage(rewardObj.get("claimMessage").getAsString());
				rewardArray.add(commandReward);
			}
		});


		voucher.setRewards(rewardArray);

		if (!requiresCreationAfter)
			voucher.sync(true);
		else
			Vouchers.getDataManager().createVoucher(voucher, (error, result) -> {
				if (error == null) {
					Vouchers.getVoucherManager().add(result);
				}
			});


		Common.tell(sender, "&aSynced &e" + voucherId + " &adata file to voucher db");
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.sync";
	}

	@Override
	public String getSyntax() {
		return "<voucherId/*>";
	}

	@Override
	public String getDescription() {
		return "Used to sync changes from a voucher file to internal storage.";
	}
}
