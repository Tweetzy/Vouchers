/*
 * Vouchers
 * Copyright 2025 Kiran Hart
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

package ca.tweetzy.vouchers.impl.message;

import ca.tweetzy.flight.utils.Common;
import ca.tweetzy.flight.utils.Replacer;
import ca.tweetzy.flight.utils.messages.Titles;
import ca.tweetzy.vouchers.api.voucher.message.BaseMessage;
import ca.tweetzy.vouchers.api.voucher.message.MessageType;
import ca.tweetzy.vouchers.hook.PAPIHook;
import ca.tweetzy.vouchers.model.VoucherHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public final class VoucherTitleMessage extends BaseMessage {

	private String secondaryContent;
	private int fadeInTime;
	private int stayTime;
	private int fadeOutTime;

	public VoucherTitleMessage(final String primaryContent, final String secondaryContent, final int fadeInTime, final int stayTime, final int fadeOutTime) {
		super(MessageType.TITLE, primaryContent);
		this.secondaryContent = secondaryContent;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
	}

	@Override
	public void send(Player player, String[] variables) {
		String formattedTitle = VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, Common.colorize(this.getPrimaryContent())), variables);
		String formattedSubtitle = VoucherHelper.dynamicVariablesReplace(PAPIHook.tryReplace(player, Common.colorize(this.getSecondaryContent())), variables);

		formattedTitle = formattedTitle.replace("%player%", player.getName());
		formattedSubtitle = formattedSubtitle.replace("%player%", player.getName());

		Titles.sendTitle(player, this.fadeInTime, this.stayTime, this.fadeOutTime, formattedTitle, formattedSubtitle);
	}
}
