package com.boardgamegeek.util;

import java.util.HashMap;
import java.util.Locale;

import android.text.TextUtils;

public class ColorUtils {
	public static final int BLACK = 0xFF000000;
	public static final int DKGRAY = 0xFF444444;
	public static final int GRAY = 0xFF888888;
	public static final int LTGRAY = 0xFFCCCCCC;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int RED = 0xFFFF0000;
	public static final int GREEN = 0xFF4CC417;
	public static final int BLUE = 0xFF0000FF;
	public static final int YELLOW = 0xFFFFFF00;
	public static final int CYAN = 0xFF00FFFF;
	public static final int MAGENTA = 0xFFFF00FF;
	public static final int TRANSPARENT = 0;
	public static final int PURPLE = 0xFF800080;
	public static final int ORANGE = 0xFFFFA500;
	public static final int BROWN = 0xFFA52A2A;
	public static final int NATURAL = 0xFFDB9370;

	public static int parseColor(String colorString) {
		if (TextUtils.isEmpty(colorString)) {
			return TRANSPARENT;
		}
		if (colorString.charAt(0) == '#') {
			// Use a long to avoid rollovers on #ffXXXXXX
			long color = Long.parseLong(colorString.substring(1), 16);
			if (colorString.length() == 7) {
				// Set the alpha value
				color |= 0x00000000ff000000;
			} else if (colorString.length() != 9) {
				return TRANSPARENT;
			}
			return (int) color;
		} else {
			String key = colorString.toLowerCase(Locale.US);
			Integer color = sColorNameMap.get(key);
			if (color != null) {
				return color;
			}
		}
		return TRANSPARENT;
	}

	private static final HashMap<String, Integer> sColorNameMap;

	static {
		sColorNameMap = new HashMap<String, Integer>();
		sColorNameMap.put("black", BLACK);
		sColorNameMap.put("darkgray", DKGRAY);
		sColorNameMap.put("gray", GRAY);
		sColorNameMap.put("lightgray", LTGRAY);
		sColorNameMap.put("white", WHITE);
		sColorNameMap.put("red", RED);
		sColorNameMap.put("green", GREEN);
		sColorNameMap.put("blue", BLUE);
		sColorNameMap.put("yellow", YELLOW);
		sColorNameMap.put("cyan", CYAN);
		sColorNameMap.put("magenta", MAGENTA);
		sColorNameMap.put("purple", PURPLE);
		sColorNameMap.put("orange", ORANGE);
		sColorNameMap.put("brown", BROWN);
		sColorNameMap.put("natural", NATURAL);
	}
}
