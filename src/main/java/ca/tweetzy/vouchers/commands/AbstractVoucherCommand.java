package ca.tweetzy.vouchers.commands;

import ca.tweetzy.tweety.command.SimpleSubCommand;

/**
 * The current file has been created by Kiran Hart
 * Date Created: January 09 2022
 * Time Created: 4:58 p.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
public abstract class AbstractVoucherCommand extends SimpleSubCommand {

	public AbstractVoucherCommand(String sublabel) {
		super(VouchersCommand.getInstance(), sublabel);
	}
}
