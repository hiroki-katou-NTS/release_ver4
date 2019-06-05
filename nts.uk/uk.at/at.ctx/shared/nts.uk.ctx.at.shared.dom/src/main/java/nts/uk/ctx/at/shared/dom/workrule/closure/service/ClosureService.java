/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
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
	
	public DatePeriod getClosurePeriod(int closureId, YearMonth processingYm, Optional<Closure> closure);
	
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
	/**
	 * 社員に対応する締め期間を取得する
	 * @param employeeId
	 * @param baseDate
	 * @return
	 */
	DatePeriod findClosurePeriod(String employeeId, GeneralDate baseDate);
	/**
	 * 締めを取得する
	 * @param employmentCd 雇用コード
	 * @return
	 */
	public Closure getClosurByEmployment(String employmentCd);
	
	// 当月の期間を算する
	public DatePeriod getClosurePeriod(Closure closure, YearMonth processingYm);
	
	/**
	 * Gets the closure data by employees.
	 *
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the closure data by employees
	 */
	List<Closure> getClosureDataByEmployees(List<String> employeeIds, GeneralDate baseDate);
	
	Map<String, Closure> getClosureByEmployees(List<String> employeeIds, GeneralDate baseDate);
	/**
	 * 指定した年月の期間を算出する
	 * @param baseDate
	 * @return True: 含まれている　を返す, False: 含まれていない　を返す
	 */
	boolean includeDate(GeneralDate baseDate, Closure closure);
}
