package com.darzalgames.libgdxtools.internationalization;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LabelEffectsProcessor {

	private LabelEffectsProcessor() {}

	private static final Pattern regexPattern = Pattern.compile("\\[-(.*?)\\]");
	private static final Function<MatchResult, String> replacer = matchResult -> "{" + matchResult.group(1) + "}";

	public static String process(String original) {
		Matcher regexMatcher = regexPattern.matcher(original);

		return regexMatcher.replaceAll(replacer);
	}

}
