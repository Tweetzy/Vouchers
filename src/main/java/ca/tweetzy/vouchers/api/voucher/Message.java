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

package ca.tweetzy.vouchers.api.voucher;

import ca.tweetzy.vouchers.api.Jsonable;

public interface Message extends Jsonable {

	MessageType getMessageType();

	String getMessage();

	int getFadeInDuration();

	int getStayDuration();

	int getFadeOutDuration();

	void setMessage(String message);

	void setFadeInDuration(int ticks);

	void setStayDuration(int ticks);

	void setFadeOutDuration(int ticks);
}
