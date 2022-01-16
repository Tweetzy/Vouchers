package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.FileUtil;
import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.RewardType;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 11 2022
 * Time Created: 5:22 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandImport extends AbstractVoucherCommand {

	public CommandImport() {
		super("import");
	}

	@Override
	protected void onCommand() {
		final File file = FileUtil.getFile("data.yml");

		if (!file.exists()) {
			returnTell("&cVouchers could not find v1.0 data.yml file");
		}

		final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection section = yamlConfiguration.getConfigurationSection("vouchers");

		if (section == null || section.getKeys(false).size() == 0) {
			returnTell("&cCould not find any vouchers in the data.yml file");
		}

		section.getKeys(false).forEach(node -> {
			final ConfigurationSection voucherSection = section.getConfigurationSection(node);
			if (voucherSection == null) return;

			final VoucherSettings voucherSettings = new VoucherSettings(node);
			voucherSettings.setAskConfirm(voucherSection.getBoolean("options.ask to confirm", false));
			voucherSettings.setRemoveOnUse(voucherSection.getBoolean("options.remove on use", true));
			voucherSettings.setGlowing(voucherSection.getBoolean("options.glowing", false));
			voucherSettings.setSendTitle(voucherSection.getBoolean("options.send title", true));
			voucherSettings.setSendActionBar(voucherSection.getBoolean("options.send actionbar", true));
			voucherSettings.setTitle(replace(voucherSection.getString("titles.title", "&eVoucher Redeemed")));
			voucherSettings.setSubtitle(replace(voucherSection.getString("titles.subtitle", "&eVoucher Redeemed")));
			voucherSettings.setActionbar(replace(voucherSection.getString("titles.actionbar", "&eVoucher Redeemed")));
			voucherSettings.setCooldown(
					voucherSection.getBoolean("options.cool down.use") ? voucherSection.getInt("options.cool down.time") : -1
			);

			final StrictList<VoucherReward> rewards = new StrictList<>();

			voucherSection.getStringList("execution.commands").forEach(cmd -> {
				rewards.add(new VoucherReward(
						RewardType.COMMAND,
						null,
						cmd.replace("%player%", "{player}"),
						100D
				));
			});

			final Voucher voucher = new Voucher(
					node,
					CompMaterial.fromString(voucherSection.getString("material", "GRASS_BLOCK")),
					voucherSection.getString("display name", "&e" + node),
					new StrictList<>(voucherSection.getStringList("lore")),
					voucherSettings,
					rewards
			);

			Vouchers.getVoucherManager().addVoucher(voucher);
			tell("&aImported voucher&f: &e" + node);
		});

		Vouchers.getVoucherManager().getVoucherHolder().save();
		tell("&aImported any vouchers that could be imported, it is highly recommend that you check the vouchers to ensure everything is correct");
	}

	private String replace(String str) {
		return str.replace("%voucher_id%", "{voucher_id}");
	}
}
