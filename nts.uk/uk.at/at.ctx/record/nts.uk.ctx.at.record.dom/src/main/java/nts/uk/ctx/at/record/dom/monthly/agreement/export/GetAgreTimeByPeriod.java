package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementDomainServiceImpl.BasicAgreementSettingsGetter;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;

/**
 * 指定期間36協定時間の取得
 * @author shuichu_ishida
 */
public interface GetAgreTimeByPeriod {

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
	List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr);
	
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
	List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr, AgeementTimeCommonSetting basicSetGetter);
}
