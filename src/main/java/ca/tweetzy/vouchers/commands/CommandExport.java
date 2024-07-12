package ca.tweetzy.vouchers.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.vouchers.Vouchers;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class CommandExport extends Command {

	public CommandExport() {
		super(AllowedExecutor.BOTH, "export");
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 1) {
			tellNoPrefix(sender, "<center>%pl_name%");
			tellNoPrefix(sender, "<center>&cYou are about to use the file export!, Please use &e/vouchers confirm &cif you accept the terms below.");
			tellNoPrefix(sender, "");
			tellNoPrefix(sender, "&7By doing this you acknowledge that you are taking full responsibility when it comes to editing the voucher file, ");
			tellNoPrefix(sender, "&7You can expect no support while directly editing the voucher file as the GUI editor the intended way!");
			tellNoPrefix(sender, "&7If you are not confident editing a JSON file, then I suggest you don't use the export command.");
			tellNoPrefix(sender, "");
			return ReturnType.FAIL;
		}

		final boolean wasConfirmed = args.length == 1 && args[0].equalsIgnoreCase("confirm");
		if (!wasConfirmed) return ReturnType.FAIL;

		tellNoPrefix(sender, "<center>%pl_name%");
		tellNoPrefix(sender, "<center>&eBeginning Voucher Export");
		Vouchers.getVoucherManager().getAll().forEach(voucher -> {
			tellNoPrefix(sender, "&f+ &eExported voucher&F: &a" + voucher.getId());
			voucher.exportVoucher();
		});

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> tab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "vouchers.command.export";
	}

	@Override
	public String getSyntax() {
		return "confirm";
	}

	@Override
	public String getDescription() {
		return "Used to export voucher(s) into a editable file";
	}
}
