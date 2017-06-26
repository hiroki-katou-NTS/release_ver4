package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday.Payday;

@Getter
public class PayDayUpdateCommand {

	private int processingNo;

	private int payBonusAtr;

	private int processingYm;

	private int sparePayAtr;

	private GeneralDate payDate;

	private GeneralDate stdDate;

	private GeneralDate accountingClosing;

	private int socialInsLevyMon;

	private GeneralDate socialInsStdDate;

	private GeneralDate incomeTaxStdDate;

	private BigDecimal neededWorkDay;

	private GeneralDate empInsStdDate;

	private int stmtOutputMon;

	public Payday toDomain(String companyCode) {
		return Payday.createSimpleFromJavaType(companyCode, processingNo, payBonusAtr, processingYm, sparePayAtr,
				payDate, stdDate, accountingClosing, socialInsLevyMon, socialInsStdDate, incomeTaxStdDate,
				neededWorkDay, empInsStdDate, stmtOutputMon);
	}
}
