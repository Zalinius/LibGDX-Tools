package com.zalinius.libgdxtools.preferencemanagers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DailyChallengePreferenceManager extends PreferenceManager {

	private static final String DAILY_DAY_KEY =   "dailyChallengeDay";
	private static final String DAILY_SCORE_KEY = "dailyChallengeScore";

	public static int getTodaysDailyHighScore() {
		return getDailyHighScore(today());
	}

	public static long hoursTillReset() {
		Instant tomorrow = Instant.EPOCH.plus(today() + 1, ChronoUnit.DAYS);
		return ChronoUnit.HOURS.between(Instant.now(), tomorrow);
	}

	public static boolean hasFinishedAGameToday() {
		return hasFinishedAGameForDay(today());
	}

	public static boolean hasFinishedAGameForDay(final long referenceDay) {
		boolean hasFinishedAGameForDay = false;

		if(prefHasSavedKey(DAILY_DAY_KEY) && prefHasSavedKey(DAILY_SCORE_KEY)) {
			long highScoreDay = getLongPrefValue(DAILY_DAY_KEY);
			if(highScoreDay == referenceDay) {
				hasFinishedAGameForDay = true;
			}
		}

		return hasFinishedAGameForDay;
	}

	private static long today() {
		Instant instant = Instant.now();
		return ChronoUnit.DAYS.between(Instant.EPOCH, instant);
	}

	public static int getDailyHighScore(final long referenceDay) {
		int highScore = Integer.MIN_VALUE;

		if(hasFinishedAGameForDay(referenceDay)) {
			highScore = getIntegerPrefValue(DAILY_SCORE_KEY);
		}

		return highScore;
	}

	public static void saveDailyHighScore(final long referenceDay, final int newScore) {
		final int currentHighScore = getDailyHighScore(referenceDay);

		if(newScore > currentHighScore) {
			savePrefValue(DAILY_DAY_KEY, referenceDay);
			savePrefValue(DAILY_SCORE_KEY, newScore);
		}
	}
}
