package com.darzalgames.libgdxtools.preferences;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.badlogic.gdx.Preferences;

/**
 * @deprecated Next time we want to have a daily challenge, salvage this code to be used as safe file data, not as a preference!
 */
@Deprecated(forRemoval=true)
public class DailyChallengePreferenceManagerSHOULDBESAVEFILEDATA extends PreferenceManager {

	private static final String DAILY_DAY_KEY =   "dailyChallengeDay";
	private static final String DAILY_SCORE_KEY = "dailyChallengeScore";

	/**
	 * @param preferencePrefix A name for the preferences file, which should be in a package format corresponding to the game, e.g. com.darzalgames.cultivar
	 */
	protected DailyChallengePreferenceManagerSHOULDBESAVEFILEDATA(final String preferencePrefix) {
		super(preferencePrefix);
	}
	public DailyChallengePreferenceManagerSHOULDBESAVEFILEDATA(final Preferences preferencesFile) {
		super(preferencesFile);
	}

	public int getTodaysDailyHighScore() {
		return getDailyHighScore(today());
	}

	public long hoursTillReset() {
		Instant tomorrow = Instant.EPOCH.plus(today() + 1, ChronoUnit.DAYS);
		return ChronoUnit.HOURS.between(Instant.now(), tomorrow);
	}

	public boolean hasFinishedAGameToday() {
		return hasFinishedAGameForDay(today());
	}

	public boolean hasFinishedAGameForDay(final long referenceDay) {
		boolean hasFinishedAGameForDay = false;

		if(prefHasSavedKey(DAILY_DAY_KEY) && prefHasSavedKey(DAILY_SCORE_KEY)) {
			long highScoreDay = getLongPrefValue(DAILY_DAY_KEY);
			if(highScoreDay == referenceDay) {
				hasFinishedAGameForDay = true;
			}
		}

		return hasFinishedAGameForDay;
	}

	private long today() {
		Instant instant = Instant.now();
		return ChronoUnit.DAYS.between(Instant.EPOCH, instant);
	}

	public int getDailyHighScore(final long referenceDay) {
		int highScore = Integer.MIN_VALUE;

		if(hasFinishedAGameForDay(referenceDay)) {
			highScore = getIntegerPrefValue(DAILY_SCORE_KEY);
		}

		return highScore;
	}

	public void saveDailyHighScore(final long referenceDay, final int newScore) {
		final int currentHighScore = getDailyHighScore(referenceDay);

		if(newScore > currentHighScore) {
			savePrefValue(DAILY_DAY_KEY, referenceDay);
			savePrefValue(DAILY_SCORE_KEY, newScore);
		}
	}
}
