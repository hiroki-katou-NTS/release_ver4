package nts.uk.ctx.pr.core.app.find.rule.employment.processing.yearmonth;

import java.math.BigDecimal;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday.Payday;

@Value
public class PaydayDto {

	String companyCode;

	int processingNo;

	int payBonusAtr;

	int processingYm;

	int sparePayAtr;

	GeneralDate payDate;

	GeneralDate stdDate;

	GeneralDate accountingClosing;

	int socialInsLevyMon;

	GeneralDate socialInsStdDate;

	GeneralDate incomeTaxStdDate;

	BigDecimal neededWorkDay;

	GeneralDate empInsStdDate;

	int stmtOutputMon;

	public static PaydayDto fromDomain(Payday domain) {
		return new PaydayDto(domain.getCompanyCode().v(), domain.getProcessingNo().v(), domain.getPayBonusAtr().value,
				domain.getProcessingYm().v(), domain.getSparePayAtr().value, domain.getPayDate(), domain.getStdDate(),
				domain.getAccountingClosing(), domain.getSocialInsLevyMon().v(), domain.getSocialInsStdDate(),
				domain.getIncomeTaxStdDate(), domain.getNeededWorkDay().v(), domain.getEmpInsStdDate(),
				domain.getStmtOutputMon().v());
	}
}
