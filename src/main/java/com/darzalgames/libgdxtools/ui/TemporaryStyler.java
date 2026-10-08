package com.darzalgames.libgdxtools.ui;

import java.util.*;

import com.badlogic.gdx.graphics.Color;

public class TemporaryStyler {

	/**
	 * Marksup the provided String so that any style markup it contains is temporary. Useful when you want to embed the resulting String into
	 * more text, but not have its markup affect the rest of the text that follows.
	 * @param string The String to style temporarily, this should begin with some styling markup (like font or color)
	 * @return A new String that is the same as the provided one but with markup for temporary styling
	 */
	public static String make(String string) {
		return "[(label)]" + string + "[ label]";
	}

	/**
	 * @param color  The Color to apply to the whole string
	 * @param string The String to color in
	 * @return A new String that's fully colored in
	 */
	public static String makeColored(Color color, String string) {
		return make("[#" + color.toString() + "]" + string);
	}

	private TemporaryStyler() {}

	/**
	 * @param color         The color to apply to all the provided variants
	 * @param initialString The starter string which will have all occurrences of the variants colored in
	 * @param variants      All variants of a word to be colored in (e.g. "start", "started", "starting", etc...)
	 * @return A new String that is the same as the initialString but with all occurrences of the variants colored in
	 */
	public static String colorInAllWordVariants(Color color, String initialString, Set<String> variants) {
		List<String> variantsLongestToShortest = new ArrayList<>(variants);
		Collections.sort(variantsLongestToShortest, Comparator.comparing(String::length).reversed());
		Map<Integer, Integer> variantIndexToLengthToColor = new HashMap<>();
		variantsLongestToShortest.forEach(
				variant -> {
					int indexOfThisVariant = initialString.indexOf(variant);
					while (indexOfThisVariant != -1) {
						if (!variantIndexToLengthToColor.containsKey(indexOfThisVariant)) {
							variantIndexToLengthToColor.put(indexOfThisVariant, variant.length());
						}
						indexOfThisVariant = initialString.indexOf(variant, indexOfThisVariant + 1);
					}
				}
		);

		List<Integer> allFoundIndicesToColorLargestToSmallest = new ArrayList<>(variantIndexToLengthToColor.keySet());
		Collections.sort(allFoundIndicesToColorLargestToSmallest, Comparator.reverseOrder());
		String result = initialString;
		for (int indexToColor : allFoundIndicesToColorLargestToSmallest) {
			String upToVariantWordToColor = result.substring(0, indexToColor);
			int lengthToColor = variantIndexToLengthToColor.get(indexToColor);
			String restOfTheString = result.substring(indexToColor + lengthToColor);
			String coloredVariantWord = makeColored(color, result.substring(indexToColor, indexToColor + lengthToColor));
			result = upToVariantWordToColor + coloredVariantWord + restOfTheString;
		}

		return result;
	}

}
