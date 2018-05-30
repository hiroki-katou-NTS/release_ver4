/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Interface ClosureService.
 */
public interface ClosureService {

	/**
	 * Gets the closure period.
	 *
	 * @param closureId the closure id
	 * @param processingYm the processing ym
	 * @return the closure period
	 */
	// 当月の期間を算�する
	public DatePeriod getClosurePeriod(int closureId, YearMonth processingYm);
	
	// TODO: Pls review
	public DatePeriod getClosurePeriodNws(int closureId, YearMonth processingYm);
	
	/**
	 * Gets the closure info.
	 *
	 * @return the closure info
	 */
	public List<ClosureInfor> getClosureInfo();
	

	/**
	 * Gets the all closure info.
	 *
	 * @return the all closure info
	 */
	// 2018.4.4 add shuichi_ishida
	public List<ClosureInfo> getAllClosureInfo();
	/**
	 * 社員に対応する処理締めを取得する
	 * @param employeeId
	 * @param baseDate
	 * @return
	 */
	public Closure getClosureDataByEmployee(String employeeId, GeneralDate baseDate);
}
