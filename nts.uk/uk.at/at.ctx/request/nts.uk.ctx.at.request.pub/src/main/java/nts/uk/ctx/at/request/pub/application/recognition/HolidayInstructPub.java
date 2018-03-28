package nts.uk.ctx.at.request.pub.application.recognition;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface HolidayInstructPub {
	/**
	 * For request list No.231
	 * @param sId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<GeneralDate> acquireBreakIndication(String sId, GeneralDate startDate, GeneralDate endDate);
}
