package ca.tweetzy.vouchers.model;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The current file has been created by Kiran Hart
 * Date Created: February 05 2022
 * Time Created: 2:29 a.m.
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise
 */
@UtilityClass
public final class InventoryBorder {

	public List<Integer> getBorders(final int rows) {
		final List<Integer> borders = new ArrayList<>();

		for (int index = 0; index < rows * 9; index++) {
			int row = index / 9;
			int column = (index % 9) + 1;

			if (row == 0 || row == rows - 1 || column == 1 || column == 9)
				borders.add(index);
		}

		return borders;
	}

	public List<Integer> getInsideBorders(final int rows) {
		final List<Integer> inner = new ArrayList<>();

		for (int index = 0; index < rows * 9; index++) {
			int row = index / 9;
			int column = (index % 9) + 1;

			if (row == 0 || row == rows - 1 || column == 1 || column == 9)
				continue;

			inner.add(index);
		}

		return inner;
	}
}
