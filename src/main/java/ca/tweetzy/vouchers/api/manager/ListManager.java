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

package ca.tweetzy.vouchers.api.manager;

import lombok.NonNull;

import java.util.*;

public abstract class ListManager<T> extends Manager {

	protected final List<T> managerContent = Collections.synchronizedList(new ArrayList<>());

	public ListManager(@NonNull String name) {
		super(name);
	}

	public Optional<T> get(@NonNull final T t) {
		synchronized (this.managerContent) {
			return this.managerContent.stream().filter(contents -> contents == t).findFirst();
		}
	}

	public void add(@NonNull final T t) {
		synchronized (this.managerContent) {
			if (this.managerContent.contains(t)) return;
			this.managerContent.add(t);
		}
	}

	public void addAll(@NonNull final Collection<T> t) {
		synchronized (this.managerContent) {
			this.managerContent.addAll(t);
		}
	}

	public void remove(@NonNull final T t) {
		synchronized (this.managerContent) {
			this.managerContent.remove(t);
		}
	}

	public void clear() {
		synchronized (this.managerContent) {
			this.managerContent.clear();
		}
	}

	public List<T> getManagerContent() {
		synchronized (this.managerContent) {
			return List.copyOf(this.managerContent);
		}
	}
}
