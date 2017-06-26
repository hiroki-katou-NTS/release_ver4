package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayRepository;
import nts.uk.shr.find.employment.processing.yearmonth.IPaydayFinderShr;

@Stateless
public class PaydayFinderShr implements IPaydayFinderShr {

	@Inject
	private PaydayRepository paydayRepo;

	/// Get working day of processingNumber
	@Override
	public BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm,
			int sparePayAtr) {
		return paydayRepo.select1(companyCode, processingNo, payBonusAtr, processingYm, sparePayAtr);
	}

}
