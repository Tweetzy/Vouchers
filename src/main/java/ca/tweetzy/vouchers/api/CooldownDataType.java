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