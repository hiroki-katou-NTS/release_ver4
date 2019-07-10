package nts.uk.ctx.at.record.pubimp.monthly.agreement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgeementTimeCommonSetting;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgeementTimeCommonSettingService;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreTimeByPeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeByEmpExport;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeByPeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeByPeriodPub;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 指定期間36協定時間の取得
 * @author shuichu_ishida
 */
@Stateless
public class AgreementTimeByPeriodPubImpl implements AgreementTimeByPeriodPub {

	/** 指定期間36協定時間の取得 */
	@Inject
	private GetAgreTimeByPeriod getAgreTimeByPeriod;
	
	@Inject
	public AgeementTimeCommonSettingService settingService;
	
	/** 指定期間36協定時間の取得 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr) {

		return this.getAgreTimeByPeriod.algorithm(companyId, employeeId, criteria, startMonth, year, periodAtr)
				.stream().map(c -> toPub(c)).collect(Collectors.toList());
	}

	private AgreementTimeByPeriod toPub(
			nts.uk.ctx.at.record.dom.monthly.agreement.export.AgreementTimeByPeriod domain){
		
		return AgreementTimeByPeriod.of(
				domain.getStartMonth(),
				domain.getEndMonth(),
				domain.getAgreementTime(),
				domain.getLimitErrorTime(),
				domain.getLimitAlarmTime(),
				domain.getExceptionLimitErrorTime(),
				domain.getExceptionLimitAlarmTime(),
				domain.getStatus());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AgreementTimeByPeriod> algorithm(String companyId, String employeeId, GeneralDate criteria,
			Month startMonth, Year year, PeriodAtrOfAgreement periodAtr, Object basicSetGetter) {
		
		return this.getAgreTimeByPeriod.algorithm(companyId, employeeId, criteria, startMonth, year, periodAtr, (AgeementTimeCommonSetting) basicSetGetter)
				.stream().map(c -> toPub(c)).collect(Collectors.toList());
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Object getCommonSetting(String companyId, List<String> employeeIds, DatePeriod criteria) {
		
		return this.settingService.getCommonService(companyId, employeeIds, criteria);
		
	}
	
	@Override
	public List<AgreementTimeByEmpExport> algorithmImprove(String companyId, List<String> employeeIds, GeneralDate criteria,
													Month startMonth, Year year, List<PeriodAtrOfAgreement> periodAtrs,  Map<String, YearMonthPeriod> periodWorking) {
		return this.getAgreTimeByPeriod.algorithmImprove(companyId, employeeIds, criteria, startMonth, year, periodAtrs,   periodWorking)
				.stream().map(AgreementTimeByEmpExport::fromDomain).collect(Collectors.toList());
	}
}
