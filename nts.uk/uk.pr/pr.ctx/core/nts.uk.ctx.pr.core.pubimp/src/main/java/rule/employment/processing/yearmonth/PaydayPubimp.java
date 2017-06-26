package rule.employment.processing.yearmonth;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;
import nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth.IPaydayPub;

@Stateless
public class PaydayPubimp implements IPaydayPub {

	@Inject
	private PaydayRepository paydayRepo;

	@Override
	public BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm,
			int sparePayAtr) {
		return paydayRepo.select1(companyCode, processingNo, payBonusAtr, processingYm, sparePayAtr);
	}
}
