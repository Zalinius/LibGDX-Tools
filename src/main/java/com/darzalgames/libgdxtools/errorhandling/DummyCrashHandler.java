package com.darzalgames.libgdxtools.errorhandling;

import java.util.List;

/**
 * A dummy implementation of the crash-handler
 * Suitable for injection in unit tests
 */
public class DummyCrashHandler extends CrashHandler {

	@Override
	protected List<ReportStatus> reportCrash(CrashReport crashReport) {
		return List.of();
	}

	@Override
	protected void logCrashReportStatus(List<ReportStatus> statuses) {
		//do nothing
	}

}
