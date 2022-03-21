package ca.tweetzy.vouchers.commands;


import ca.tweetzy.tweety.collection.StrictList;
import ca.tweetzy.tweety.conversation.TitleInput;
import ca.tweetzy.tweety.model.Common;
import ca.tweetzy.tweety.remain.CompMaterial;
import ca.tweetzy.tweety.util.PlayerUtil;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.impl.Voucher;
import ca.tweetzy.vouchers.impl.VoucherReward;
import ca.tweetzy.vouchers.impl.VoucherSettings;
import ca.tweetzy.vouchers.menus.MenuVoucherEdit;
import ca.tweetzy.vouchers.menus.MenuVoucherList;
import ca.tweetzy.vouchers.settings.Locale;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

/**
 * Date Created: February 27 2022
 * Time Created: 11:14 p.m.
 *
 * @author Kiran Hart
 */
@Command({"vouchers"})
public final class VouchersCommand {

	@Default
	public void main(final BukkitCommandActor actor) {
		Vouchers.getGuiManager().showGUI(actor.requirePlayer(), new MenuVoucherList());
	}

	@Subcommand({"create", "new"})
	@CommandPermission("vouchers.command.create")
	public void create(final Player player) {
		new TitleInput(player, Locale.VOUCHER_CREATION_TITLE.getString(), Locale.VOUCHER_CREATION_SUBTITLE.getString()) {
			@Override
			public boolean onResult(String val) {
				if (Vouchers.getVoucherManager().findVoucher(val) != null) {
					Common.tell(player, Locale.ERROR_VOUCHER_ALREADY_EXISTS.getString().replace("{voucher_id}", val));
					return false;
				}

				Voucher voucher = new Voucher(val, CompMaterial.PAPER, val, new StrictList<>("&7sample lore"), new VoucherSettings(val), new StrictList<>(new VoucherReward()));
				Vouchers.getVoucherManager().addVoucher(voucher);
				Vouchers.getGuiManager().showGUI(player, new MenuVoucherEdit(voucher));

				Common.tell(player, Locale.SUCCESS_CREATED.getString().replace("{voucher_id}", val));
				return true;
			}
		};
	}

	@Subcommand("edit")
	@CommandPermission("vouchers.command.edit")
	public void edit(final Player player, String voucherId) {
		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);
		if (voucher == null) {
			Common.tell(player, Locale.ERROR_VOUCHER_DOES_NOT_EXIST.getString().replace("{voucher_id}", voucherId));
			return;
		}

		Vouchers.getGuiManager().showGUI(player, new MenuVoucherEdit(voucher));
	}

	@Subcommand({"delete", "remove"})
	@CommandPermission("vouchers.command.delete")
	public void delete(final Player player, String voucherId) {
		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);
		if (voucher == null) {
			Common.tell(player, Locale.ERROR_VOUCHER_DOES_NOT_EXIST.getString().replace("{voucher_id}", voucherId));
			return;
		}

		Vouchers.getVoucherManager().deleteVoucher(voucher.getId());
		Common.tell(player, Locale.SUCCESS_DELETED.getString().replace("{voucher_id}", voucherId));
	}

	@Subcommand("give")
	@CommandPermission("vouchers.command.give")
	public void give(final Player player, String voucherId, Player target, @Optional @Default("1") int amount) {
		final Voucher voucher = Vouchers.getVoucherManager().findVoucher(voucherId);

		if (voucher == null) {
			Common.tell(player, Locale.ERROR_VOUCHER_DOES_NOT_EXIST.getString().replace("{voucher_id}", voucherId));
			return;
		}

		for (int i = 0; i < amount; i++)
			PlayerUtil.addItems(target.getInventory(), voucher.build());
	}
}
