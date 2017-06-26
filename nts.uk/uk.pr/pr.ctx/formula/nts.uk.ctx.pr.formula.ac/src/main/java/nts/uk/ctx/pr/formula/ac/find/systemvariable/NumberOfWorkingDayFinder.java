package nts.uk.ctx.pr.formula.ac.find.systemvariable;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth.IPaydayPub;
import nts.uk.ctx.pr.formula.dom.adapter.SystemVariableAdapter;

@Stateless
public class NumberOfWorkingDayFinder implements SystemVariableAdapter{
	
	@Inject
	private IPaydayPub iPaydayPub;

	@Override
	public BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm,
			int sparePayAtr) {
		return iPaydayPub.getNumbersOfWorkingDay(companyCode, processingNo, payBonusAtr, processingYm, sparePayAtr);
	}

}
