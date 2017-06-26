package nts.uk.shr.find.employment.processing.yearmonth;

import java.math.BigDecimal;

public interface IPaydayFinderShr {
	BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm, int sparePayAtr);
}
