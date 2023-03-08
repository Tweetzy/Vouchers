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

import ca.tweetzy.flight.comp.enums.CompSound;
import ca.tweetzy.vouchers.api.voucher.Message;
import ca.tweetzy.vouchers.api.voucher.MessageType;
import ca.tweetzy.vouchers.api.voucher.VoucherOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class VoucherSettings implements VoucherOptions {

	private int maxUses;
	private int cooldown;
	private boolean glowing;
	private boolean askConfirm;
	private boolean removeOnUse;
	private boolean requiresPermission;
	private CompSound sound;
	private String permission;
	private List<Message> messages;

	public VoucherSettings() {
		this(-1, -1, true, true, true, true, CompSound.ENTITY_EXPERIENCE_ORB_PICKUP, "vouchers.usevoucher", new ArrayList<>());
	}

	@Override
	public String toJsonString() {
		final JsonObject object = new JsonObject();

		object.addProperty("maxUses", this.maxUses);
		object.addProperty("cooldown", this.cooldown);
		object.addProperty("glowing", this.glowing);
		object.addProperty("askConfirm", this.askConfirm);
		object.addProperty("removeOnUse", this.removeOnUse);
		object.addProperty("requiresPermission", this.requiresPermission);
		object.addProperty("permission", this.permission);
		object.addProperty("sound", this.sound.name());

		final JsonArray messages = new JsonArray();
		this.messages.forEach(message -> {

			final JsonObject messageObject = new JsonObject();
			messageObject.addProperty("type", message.getMessageType().name());

			messageObject.addProperty("message", message.getMessage());
			messageObject.addProperty("fadeIn", message.getFadeInDuration());
			messageObject.addProperty("stay", message.getStayDuration());
			messageObject.addProperty("fadeOut", message.getFadeOutDuration());

			messages.add(messageObject);
		});

		object.add("messages", messages);
		return object.toString();
	}

	public static VoucherOptions decode(@NonNull final String json) {
		final JsonObject object = JsonParser.parseString(json).getAsJsonObject();

		final List<Message> messages = new ArrayList<>();
		final JsonArray msgArray = object.get("messages").getAsJsonArray();

		msgArray.forEach(message -> {
			final JsonObject messageObject = message.getAsJsonObject();

			messages.add(new VoucherMessage(
					MessageType.valueOf(messageObject.get("type").getAsString().toUpperCase()),
					messageObject.get("message").getAsString(),
					messageObject.get("fadeIn").getAsInt(),
					messageObject.get("stay").getAsInt(),
					messageObject.get("fadeOut").getAsInt()
			));
		});

		CompSound sound = object.has("sound") ? CompSound.matchCompSound(object.get("sound").getAsString()).orElse(CompSound.ENTITY_EXPERIENCE_ORB_PICKUP) : CompSound.ENTITY_EXPERIENCE_ORB_PICKUP;

		return new VoucherSettings(
				object.get("maxUses").getAsInt(),
				object.get("cooldown").getAsInt(),
				object.get("glowing").getAsBoolean(),
				object.get("askConfirm").getAsBoolean(),
				object.get("removeOnUse").getAsBoolean(),
				object.get("requiresPermission").getAsBoolean(),
				sound,
				object.get("permission").getAsString(),
				messages
		);
	}

	@Override
	public int getMaxUses() {
		return this.maxUses;
	}

	@Override
	public int getCooldown() {
		return this.cooldown;
	}

	@Override
	public boolean isGlowing() {
		return this.glowing;
	}

	@Override
	public boolean isAskConfirm() {
		return this.askConfirm;
	}

	@Override
	public boolean isRemoveOnUse() {
		return this.removeOnUse;
	}

	@Override
	public boolean isRequiresPermission() {
		return this.requiresPermission;
	}

	@Override
	public String getPermission() {
		return this.permission;
	}

	@Override
	public List<Message> getMessages() {
		return this.messages;
	}

	@Override
	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}

	@Override
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	@Override
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public void setGlowing(boolean glowing) {
		this.glowing = glowing;
	}

	@Override
	public void setAskConfirm(boolean askConfirm) {
		this.askConfirm = askConfirm;
	}

	@Override
	public void setRemoveOnUse(boolean removeOnUse) {
		this.removeOnUse = removeOnUse;
	}

	@Override
	public void setRequiresPermission(boolean requiresPermission) {
		this.requiresPermission = requiresPermission;
	}

	@Override
	public CompSound getSound() {
		return this.sound;
	}

	@Override
	public void setSound(CompSound sound) {
		this.sound = sound;
	}
}
