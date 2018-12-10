package nts.uk.ctx.at.record.pub.remainnumber.annualleave;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
//import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.CalYearOffWorkAttendRateExport;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.ReNumAnnLeaReferenceDateExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AnnLeaveRemainNumberPub {
	
	/**
	 * RequestList #No.265
	 * @param employeeId
	 * @return
	 */
	AnnLeaveOfThisMonth getAnnLeaveOfThisMonth(String employeeId);

	/**
	 * RequestList #No.363
	 * @param employeeId
	 * @param datePeriod
	 * @return
	 */
	List<AggrResultOfAnnualLeaveEachMonth> getAnnLeaveRemainAfterThisMonth(String employeeId, DatePeriod datePeriod);
	
	/**
	 * RequestList #No.369
	 * @param employeeId
	 * @return
	 */
	NextHolidayGrantDate getNextHolidayGrantDate(String companyId, String employeeId);	

	/**
	 * RequestList198
	 * 基準日時点の年休残数を取得する
	 * @param employeeID
	 * @param date
	 * @return ReNumAnnLeaReferenceDateExport
	 */
	ReNumAnnLeaReferenceDateExport getReferDateAnnualLeaveRemainNumber(String employeeID,GeneralDate date);
	
	/**
	 * @author hoatt
	 * KDR001
	 * RequestList #No.369 - ver2
	 * @param employeeId
	 * @param closureDate
	 * @return
	 */
	public NextHolidayGrantDate getNextHdGrantDateVer2(String companyId, String employeeId, Optional<GeneralDate> closureDate);	
}
