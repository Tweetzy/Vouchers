package ca.tweetzy.vouchers.api.manager;

import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class KeyValueManager<K, V> extends Manager {

	protected final Map<K, V> managerContent = new ConcurrentHashMap<>();

	public KeyValueManager(@NonNull String name) {
		super(name);
	}

	public V get(@NonNull final K k) {
		return this.managerContent.getOrDefault(k, null);
	}

	public void add(@NonNull final K k, @NonNull final V v) {
		if (this.managerContent.containsKey(k)) return;
		this.managerContent.put(k, v);
	}

	public void remove(@NonNull final K k) {
		this.managerContent.remove(k);
	}

	public void clear() {
		this.managerContent.clear();
	}

	public Map<K, V> getManagerContent() {
		return Map.copyOf(this.managerContent);
	}

	public List<V> getValues() {
		return this.managerContent.values().stream().toList();
	}
}
