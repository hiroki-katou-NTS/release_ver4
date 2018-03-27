package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceNameAdapter;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceNameDto;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceTypeAdapter;
import nts.uk.ctx.at.record.dom.divergence.time.service.attendance.DivergenceAttendanceTypeDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceAttendanceItemFinder.
 */
@Stateless
public class DivergenceAttendanceItemFinder {

	/** The at type. */
	@Inject
	private DivergenceAttendanceTypeAdapter atType;
	
	/** The at name. */
	// user contexts
	@Inject
	private DivergenceAttendanceNameAdapter atName;

	/**
	 * Gets the all at type.
	 *
	 * @param screenUseAtr the screen use atr
	 * @return the all at type
	 */
	public List<DivergenceAttendanceTypeDto> getAllAtType(int screenUseAtr) {
		String companyId = AppContexts.user().companyId();
		List<DivergenceAttendanceTypeDto> data = atType.getItemByScreenUseAtr(companyId, screenUseAtr);
		return data;
	}

	/**
	 * Gets the at name.
	 *
	 * @param dailyAttendanceItemIds the daily attendance item ids
	 * @return the at name
	 */
	public List<DivergenceAttendanceNameDto> getAtName(List<Integer> dailyAttendanceItemIds) {
		List<DivergenceAttendanceNameDto> data = atName.getDailyAttendanceItemName(dailyAttendanceItemIds);
		return data;
	}
}
