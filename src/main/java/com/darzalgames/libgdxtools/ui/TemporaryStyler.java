package com.darzalgames.libgdxtools.ui;

import java.util.*;

import com.badlogic.gdx.graphics.Color;

public class TemporaryStyler {

	public static String make(String string) {
		return "[(label)]" + string + "[ label]";
	}

	public static String makeColored(Color color, String string) {
		return make("[#" + color.toString() + "]" + string);
	}

	private TemporaryStyler() {}

	public static String colorInAllWordVariants(Color color, String initialString, Set<String> variants) {
		List<String> variantsSortedLongestFirst = new ArrayList<>(variants);
		Collections.sort(variantsSortedLongestFirst, Comparator.comparing(String::length).reversed());
		Map<Integer, Integer> variantIndexToColorLength = new HashMap<>();
		variantsSortedLongestFirst.forEach(
				variant -> {
					int indexOfThisVariant = initialString.indexOf(variant);
					while (indexOfThisVariant != -1) {
						if (!variantIndexToColorLength.containsKey(indexOfThisVariant)) {
							variantIndexToColorLength.put(indexOfThisVariant, variant.length());
						}
						indexOfThisVariant = initialString.indexOf(variant, indexOfThisVariant + 1);
					}
				}
		);

		List<Integer> allFoundIndicesToColorLargestToSmallest = new ArrayList<>(variantIndexToColorLength.keySet());
		Collections.sort(allFoundIndicesToColorLargestToSmallest, Comparator.reverseOrder());
		String result = initialString;
		for (int indexToColor : allFoundIndicesToColorLargestToSmallest) {
			String upToVariantWordToColor = result.substring(0, indexToColor);
			int lengthToColor = variantIndexToColorLength.get(indexToColor);
			String restOfTheString = result.substring(indexToColor + lengthToColor);
			String coloredVariantWord = makeColored(color, result.substring(indexToColor, indexToColor + lengthToColor));
			result = upToVariantWordToColor + coloredVariantWord + restOfTheString;
		}

		return result;
	}

}
