package com.darzalgames.libgdxtools.preferences;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @deprecated Next time we want to have a daily challenge, salvage this code to be used as safe file data, not as a preference!
 */
@Deprecated(forRemoval = true)
public class DailyChallengePreferenceManagerSHOULDBESAVEFILEDATA {

	private static final String DAILY_DAY_KEY = "dailyChallengeDay";
	private static final String DAILY_SCORE_KEY = "dailyChallengeScore";

	@Deprecated
	public int getTodaysDailyHighScore() {
		return getDailyHighScore(today());
	}

	@Deprecated
	public long hoursTillReset() {
		Instant tomorrow = Instant.EPOCH.plus(today() + 1, ChronoUnit.DAYS);
		return ChronoUnit.HOURS.between(Instant.now(), tomorrow);
	}

	@Deprecated
	public boolean hasFinishedAGameToday() {
		return hasFinishedAGameForDay(today());
	}

	@Deprecated
	public boolean hasFinishedAGameForDay(final long referenceDay) {

//		if (prefHasSavedKey(DAILY_DAY_KEY) && prefHasSavedKey(DAILY_SCORE_KEY)) {
//			long highScoreDay = getLongPrefValue(DAILY_DAY_KEY);
//			if (highScoreDay == referenceDay) {
//				hasFinishedAGameForDay = true;
//			}
//		}

		return false;
	}

	private long today() {
		Instant instant = Instant.now();
		return ChronoUnit.DAYS.between(Instant.EPOCH, instant);
	}

	@Deprecated
	public int getDailyHighScore(final long referenceDay) {
		int highScore = Integer.MIN_VALUE;

		if (hasFinishedAGameForDay(referenceDay)) {
//			highScore = getIntegerPrefValue(DAILY_SCORE_KEY);
		}

		return highScore;
	}

	@Deprecated
	public void saveDailyHighScore(final long referenceDay, final int newScore) {
		final int currentHighScore = getDailyHighScore(referenceDay);

		if (newScore > currentHighScore) {
//			savePrefValue(DAILY_DAY_KEY, referenceDay);
//			savePrefValue(DAILY_SCORE_KEY, newScore);
		}
	}
}
