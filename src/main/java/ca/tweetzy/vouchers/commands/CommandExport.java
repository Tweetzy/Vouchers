package ca.tweetzy.vouchers.commands;

import ca.tweetzy.flight.command.AllowedExecutor;
import ca.tweetzy.flight.command.Command;
import ca.tweetzy.flight.command.ReturnType;
import ca.tweetzy.vouchers.Vouchers;
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
			tellNoPrefix(sender, "<center>&cYou are about to use the file export!");
			tellNoPrefix(sender, "");
			tellNoPrefix(sender, "<center>&7By doing this you acknowledge that you are taking full responsibility when it comes to editing the voucher file, You can expect no support while directly editing the voucher file as the GUI editor the intended way! If you are not confident editing a JSON file, then I suggest you don't use the export command.");
			tellNoPrefix(sender, "");
			tellNoPrefix(sender, "<center>&e/vouchers export confirm");
			tellNoPrefix(sender, "");
			return ReturnType.FAIL;
		}

		final boolean wasConfirmed = args.length == 1 && args[0].equalsIgnoreCase("confirm");
		if (!wasConfirmed) return ReturnType.FAIL;

		tellNoPrefix(sender, "<center>%pl_name%");
		tellNoPrefix(sender, "<center>&eBeginning Voucher Export");
		Vouchers.getVoucherManager().getManagerContent().values().forEach(voucher -> {
			tellNoPrefix(sender, "<center>&f+ &eExported voucher&F: &a" + voucher.getId());
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
