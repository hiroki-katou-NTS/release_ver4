package nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth;

import java.math.BigDecimal;

public interface IPaydayPub {
	BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm, int sparePayAtr);
}
