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
		Vouchers.getVoucherManager().getAll().forEach(Voucher::exportVoucher);
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
		return "<voucher id>";
	}

	@Override
	public String getDescription() {
		return "Used to export voucher(s) into a editable file";
	}
}
