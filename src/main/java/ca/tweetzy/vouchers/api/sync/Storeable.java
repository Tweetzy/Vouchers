package ca.tweetzy.vouchers.api.sync;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface Storeable<T> {

	void store(@NonNull final Consumer<T> stored);

	default void unStore(@Nullable final Consumer<SynchronizeResult> syncResult) {
		if (syncResult != null)
			syncResult.accept(SynchronizeResult.SUCCESS);
	}
}
