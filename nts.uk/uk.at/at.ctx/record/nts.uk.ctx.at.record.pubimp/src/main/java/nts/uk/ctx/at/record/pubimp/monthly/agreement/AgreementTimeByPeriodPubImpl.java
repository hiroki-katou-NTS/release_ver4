package nts.uk.ctx.at.record.pubimp.monthly.agreement;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreTimeByPeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeByPeriod;
import nts.uk.ctx.at.record.pub.monthly.agreement.AgreementTimeByPeriodPub;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.monthly.agreement.PeriodAtrOfAgreement;

/**
 * 指定期間36協定時間の取得
 * @author shuichu_ishida
 */
@Stateless
public class AgreementTimeByPeriodPubImpl implements AgreementTimeByPeriodPub {

	/** 指定期間36協定時間の取得 */
	@Inject
	private GetAgreTimeByPeriod getAgreTimeByPeriod;
	
	/** 指定期間36協定時間の取得 */
	@Override
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
}
