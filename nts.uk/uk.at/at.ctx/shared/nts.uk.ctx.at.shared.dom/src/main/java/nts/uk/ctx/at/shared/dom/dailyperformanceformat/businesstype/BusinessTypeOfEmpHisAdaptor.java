package nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author sonnh1
 *
 */
public interface BusinessTypeOfEmpHisAdaptor {
	/**
	 * find by base date and employeeId
	 * 
	 * @param baseDate
	 * @param sId
	 * @return BusinessTypeOfEmployeeHistory
	 */
	Optional<BusinessTypeOfEmpHis> findByBaseDateAndSid(GeneralDate baseDate, String sId);

	/**
	 * <<Public>> 社員ID(List)、期間で期間分の勤務種別情報を取得する
	 * find by base date and list employeeId and cid
	 * @param cid
	 * @param sIds
	 * @param datePeriod
	 * @return
	 */
	List<BusinessTypeOfEmpDto> findByCidSidBaseDate(String cid, List<String> sIds, DatePeriod datePeriod);
}
