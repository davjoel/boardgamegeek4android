package com.boardgamegeek.filterer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.boardgamegeek.R;
import com.boardgamegeek.provider.BggContract.Collection;
import com.boardgamegeek.util.MathUtils;
import com.boardgamegeek.util.StringUtils;

public class MyRatingFilterer extends CollectionFilterer {
	public static final double MIN_RANGE = 1.0;
	public static final double MAX_RANGE = 10.0;

	private double min;
	private double max;
	private boolean includeUnrated;

	public MyRatingFilterer(Context context) {
		super(context);
	}

	public MyRatingFilterer(@NonNull Context context, double min, double max, boolean includeUnrated) {
		super(context);
		this.min = min;
		this.max = max;
		this.includeUnrated = includeUnrated;
	}

	@Override
	public void setData(@NonNull String data) {
		String[] d = data.split(DELIMITER);
		min = d.length > 0 ? MathUtils.constrain(StringUtils.parseDouble(d[0], MIN_RANGE), MIN_RANGE, MAX_RANGE) : MIN_RANGE;
		max = d.length > 1 ? MathUtils.constrain(StringUtils.parseDouble(d[1], MAX_RANGE), MIN_RANGE, MAX_RANGE) : MAX_RANGE;
		includeUnrated = d.length > 2 ? (d[2].equals("1")) : (Double.valueOf(d[0]) < 1.0);
	}

	@Override
	public int getTypeResourceId() {
		return R.string.collection_filter_type_my_rating;
	}

	@NonNull
	@Override
	public String flatten() {
		return String.valueOf(min) + DELIMITER + String.valueOf(max) + DELIMITER + (includeUnrated ? "1" : "0");
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public boolean includeUnrated() {
		return includeUnrated;
	}

	@Override
	public String getDisplayText() {
		String minText = String.valueOf(min);
		String maxText = String.valueOf(max);

		String text;
		if (min == max) {
			text = maxText;
		} else {
			text = minText + "-" + maxText;
		}
		if (includeUnrated) {
			text += " (+" + context.getString(R.string.unrated) + ")";
		}

		return context.getString(R.string.my_rating) + " " + text;
	}

	@Override
	public String getSelection() {
		String selection;
		if (min == max) {
			selection = Collection.RATING + "=?";
		} else {
			selection = "(" + Collection.RATING + ">=? AND " + Collection.RATING + "<=?)";
		}
		if (includeUnrated) {
			selection += " OR " + Collection.RATING + "=0 OR " + Collection.RATING + " IS NULL";
		}
		return selection;
	}

	@Override
	public String[] getSelectionArgs() {
		if (min == max) {
			return new String[] { String.valueOf(min) };
		} else {
			return new String[] { String.valueOf(min), String.valueOf(max) };
		}
	}
}
