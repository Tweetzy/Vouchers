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

package ca.tweetzy.vouchers.api;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.*;
import java.util.HashMap;

public class CooldownDataType implements PersistentDataType<byte[], HashMap<String, Long>> {

	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

	@Override
	public Class<HashMap<String, Long>> getComplexType() {
		return (Class<HashMap<String, Long>>) (Class<?>) HashMap.class;
	}

	@Override
	public byte[] toPrimitive(HashMap<String, Long> complex, PersistentDataAdapterContext context) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(complex);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public HashMap<String, Long> fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(primitive);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (HashMap<String, Long>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}