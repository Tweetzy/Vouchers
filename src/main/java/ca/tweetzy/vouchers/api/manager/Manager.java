package ca.tweetzy.vouchers.api.manager;

import lombok.NonNull;

public abstract class Manager {

	protected String name;

	public Manager(@NonNull final String name) {
		this.name = name;
	}

	public abstract void load();
}
