package nts.uk.ctx.at.record.pub.monthly.agreement;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 指定期間36協定時間の取得
 * @author shuichu_ishida
 */
public interface AgreementTimeByPeriodPub {

	/**
	 * 指定期間36協定時間の取得
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param criteria 基準日
	 * @param startMonth 起算月
	 * @param year 年度
	 * @param periodAtr 期間区分
	 * @return 指定期間36協定時間リスト
	 */
	// RequestList453
	List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr);
	
	List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr, Object basicSetGetter);
	
	Object getCommonSetting(String companyId, List<String> employeeId, DatePeriod criteria);

	// RequestList453
	List<AgreementTimeByEmpExport> algorithmImprove(String companyId, List<String> employeeIds, GeneralDate criteria,
									   Month startMonth, Year year, List<PeriodAtrOfAgreement> periodAtrs, Map<String, YearMonthPeriod> periodWorking);
}
