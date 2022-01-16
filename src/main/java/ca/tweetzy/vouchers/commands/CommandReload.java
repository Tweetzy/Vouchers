package ca.tweetzy.vouchers.commands;

import ca.tweetzy.vouchers.Vouchers;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 4:57 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public final class CommandReload extends AbstractVoucherCommand {

	public CommandReload() {
		super("reload|rl");
	}

	@Override
	protected void onCommand() {
		if (args.length == 0)
			returnTell("&cPlease use the command&f: &4/vouchers reload confirm. &CIf you made changes to the data.db file manually, reloading will not keep those changes, you must restart to apply them.");

		if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
			Vouchers.getInstance().reload();
			tell("&aSuccessfully reloaded files.");
		}
	}
}
