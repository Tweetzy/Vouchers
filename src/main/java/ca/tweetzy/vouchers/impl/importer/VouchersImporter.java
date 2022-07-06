/*
 * Vouchers
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.vouchers.impl.importer;

import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.Importer;
import ca.tweetzy.vouchers.api.voucher.Reward;
import ca.tweetzy.vouchers.impl.reward.CommandReward;
import ca.tweetzy.vouchers.impl.reward.ItemReward;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class VouchersImporter implements Importer {

	@Override
	public String getName() {
		return "Vouchers";
	}

	@Override
	public String getAuthor() {
		return "Kiran Hart";
	}

	@Override
	public void load() {
		final File file = new File(Vouchers.getInstance().getDataFolder() + File.separator + "vouchers-v3.yml");

		if (!file.exists()) {
			return;
		}

		final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		final ConfigurationSection section = configuration.getConfigurationSection("vouchers");

		if (section == null || section.getKeys(false).size() == 0) return;

		// loop through all vouchers
		section.getKeys(false).forEach(voucherNode -> {

			final List<Reward> voucherRewards = new ArrayList<>();
			final ConfigurationSection rewardSection = section.getConfigurationSection(voucherNode + ".rewards");

			if (rewardSection != null && rewardSection.getKeys(false).size() != 0) {

				rewardSection.getKeys(false).forEach(rewardKey -> {

					if (rewardSection.getString(rewardKey + ".type").equalsIgnoreCase("command")) {
						voucherRewards.add(
								new CommandReward(
										rewardSection.getString(rewardKey + ".command"),
										rewardSection.getDouble(rewardKey + ".chance"),
										0
								)
						);
					} else {
//						voucherRewards.add(
//								new ItemReward(
//										rewardSection.getString(rewardKey + ".item"),
//										rewardSection.getDouble(rewardKey + ".chance"),
//										0
//								)
//						);
					}

				});
			}


//			final Voucher voucher = new ActiveVoucher();

		});
	}
}


//		Common.runAsync(() -> {
//
//final File file = FileUtil.getOrMakeFile("vouchers-v3.yml");
//		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
//
//		Vouchers.getVoucherManager().getVouchers().forEach(voucher -> {
//
//		configuration.set("vouchers." + voucher.getId() + ".icon", voucher.getIcon().toItem());
//		configuration.set("vouchers." + voucher.getId() + ".display name", voucher.getDisplayName());
//		configuration.set("vouchers." + voucher.getId() + ".description", voucher.getDescription());
//		configuration.set("vouchers." + voucher.getId() + ".setting.reward mode", voucher.getSettings().getRewardMode().name());
//		configuration.set("vouchers." + voucher.getId() + ".setting.sound", voucher.getSettings().getSound().name());
//		configuration.set("vouchers." + voucher.getId() + ".setting.glow", voucher.getSettings().isGlowing());
//		configuration.set("vouchers." + voucher.getId() + ".setting.ask confirm", voucher.getSettings().askConfirm());
//		configuration.set("vouchers." + voucher.getId() + ".setting.require permission", voucher.getSettings().requiresUsePermission());
//		configuration.set("vouchers." + voucher.getId() + ".setting.permission", voucher.getSettings().getPermission());
//		configuration.set("vouchers." + voucher.getId() + ".setting.broadcast redeem", voucher.getSettings().broadcastRedeem());
//		configuration.set("vouchers." + voucher.getId() + ".setting.send title", voucher.getSettings().sendTitle());
//		configuration.set("vouchers." + voucher.getId() + ".setting.send subtitle", voucher.getSettings().sendSubtitle());
//		configuration.set("vouchers." + voucher.getId() + ".setting.send actionbar", voucher.getSettings().sendActionBar());
//		configuration.set("vouchers." + voucher.getId() + ".setting.broadcast msg", voucher.getSettings().getBroadcastMessage());
//		configuration.set("vouchers." + voucher.getId() + ".setting.title", voucher.getSettings().getTitle());
//		configuration.set("vouchers." + voucher.getId() + ".setting.subtitle", voucher.getSettings().getSubtitle());
//		configuration.set("vouchers." + voucher.getId() + ".setting.actionbar", voucher.getSettings().getActionBar());
//		configuration.set("vouchers." + voucher.getId() + ".setting.redeem msg", voucher.getSettings().getRedeemMessage());
//		configuration.set("vouchers." + voucher.getId() + ".setting.cooldown", voucher.getSettings().getCooldown());
//
//		voucher.getRewards().forEach(reward -> {
//final String rewardUUID = UUID.randomUUID().toString();
//
//		configuration.set("vouchers." + voucher.getId() + ".rewards." + rewardUUID + ".chance", reward.getChance());
//		configuration.set("vouchers." + voucher.getId() + ".rewards." + rewardUUID + ".type", reward.getRewardType().name());
//
//		if (reward.getRewardType() == RewardType.COMMAND)
//		configuration.set("vouchers." + voucher.getId() + ".rewards." + rewardUUID + ".command", reward.getCommand());
//		else
//		configuration.set("vouchers." + voucher.getId() + ".rewards." + rewardUUID + ".item", reward.getItem());
//
//		});
//
//		});
//
//		try {
//		configuration.save(file);
//		Common.logFramed("&aExported vouchers into import file for v3");
//		} catch (IOException e) {
//		e.printStackTrace();
//		}
//
//		});