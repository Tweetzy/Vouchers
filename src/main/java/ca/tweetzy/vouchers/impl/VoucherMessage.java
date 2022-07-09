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

package ca.tweetzy.vouchers.impl;

import ca.tweetzy.feather.comp.ActionBar;
import ca.tweetzy.feather.comp.Titles;
import ca.tweetzy.feather.utils.Common;
import ca.tweetzy.feather.utils.Replacer;
import ca.tweetzy.vouchers.api.voucher.Message;
import ca.tweetzy.vouchers.api.voucher.MessageType;
import ca.tweetzy.vouchers.api.voucher.Voucher;
import ca.tweetzy.vouchers.settings.Settings;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.entity.Player;

@AllArgsConstructor
public final class VoucherMessage implements Message {

	private final MessageType type;
	private String message;
	private int fadeInDuration;
	private int stayDuration;
	private int fadeOutDuration;

	@Override
	public MessageType getMessageType() {
		return this.type;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public int getFadeInDuration() {
		return this.fadeInDuration;
	}

	@Override
	public int getStayDuration() {
		return this.stayDuration;
	}

	@Override
	public int getFadeOutDuration() {
		return this.fadeOutDuration;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void setFadeInDuration(int ticks) {
		this.fadeInDuration = ticks;
	}

	@Override
	public void setStayDuration(int ticks) {
		this.stayDuration = ticks;
	}

	@Override
	public void setFadeOutDuration(int ticks) {
		this.fadeOutDuration = ticks;
	}

	@Override
	public void send(Player player, Voucher voucher) {
		switch (this.type) {
			case BROADCAST -> Common.broadcast(null, false, getColouredAndReplaced(player, voucher));
			case CHAT -> Common.tell(player, false, getColouredAndReplaced(player, voucher));
			case ACTION_BAR -> ActionBar.sendActionBar(player, getColouredAndReplaced(player, voucher));
			case TITLE -> Titles.sendTitle(player, this.fadeInDuration, this.stayDuration, this.fadeOutDuration, getColouredAndReplaced(player, voucher), "");
			case SUBTITLE -> Titles.sendTitle(player, this.fadeInDuration, this.stayDuration, this.fadeOutDuration, "", getColouredAndReplaced(player, voucher));
		}
	}

	@Override
	public String getColouredAndReplaced(@NonNull final Player player, @NonNull final Voucher voucher) {
		return Common.colorize(Replacer.replaceVariables(this.message,
				"player", player.getName(),
				"voucher_id", voucher.getId(),
				"voucher_name", voucher.getName(),
				"pl_prefix", Settings.PREFIX.getString()
		));
	}

	@Override
	public String toJsonString() {
		final JsonObject object = new JsonObject();

		object.addProperty("type", this.type.name());
		object.addProperty("fadeIn", this.fadeInDuration);
		object.addProperty("stay", this.stayDuration);
		object.addProperty("fadeOut", this.fadeOutDuration);
		object.addProperty("message", this.message);

		return object.toString();
	}
}
