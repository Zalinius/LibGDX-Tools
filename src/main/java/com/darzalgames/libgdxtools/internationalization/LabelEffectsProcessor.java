package com.darzalgames.libgdxtools.internationalization;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LabelEffectsProcessor {

	private LabelEffectsProcessor() {}

	private static final Pattern REGEX_PATTERN = Pattern.compile("\\[-(.*?)\\]");
	private static final Function<MatchResult, String> MATCH_REPLACER = matchResult -> "{" + matchResult.group(1) + "}";

	public static String process(String original) {
		Matcher regexMatcher = REGEX_PATTERN.matcher(original);

		return regexMatcher.replaceAll(MATCH_REPLACER);
	}

}
