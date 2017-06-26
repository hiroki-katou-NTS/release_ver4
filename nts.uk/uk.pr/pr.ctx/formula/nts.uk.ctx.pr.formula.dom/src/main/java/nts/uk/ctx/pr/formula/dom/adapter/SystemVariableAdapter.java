package nts.uk.ctx.pr.formula.dom.adapter;

import java.math.BigDecimal;

public interface SystemVariableAdapter {
	 BigDecimal getNumbersOfWorkingDay(String companyCode, int processingNo, int payBonusAtr, int processingYm, int sparePayAtr);
}
