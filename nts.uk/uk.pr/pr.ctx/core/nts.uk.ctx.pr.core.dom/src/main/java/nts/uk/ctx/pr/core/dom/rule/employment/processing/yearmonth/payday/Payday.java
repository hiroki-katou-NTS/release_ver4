package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.ProcessingNo;

@Getter
@AllArgsConstructor
public class Payday extends AggregateRoot {

	private CompanyCode companyCode;

	private ProcessingNo processingNo;

	private PayBonusAtr payBonusAtr;

	private YearMonth processingYm;

	private SparePayAtr sparePayAtr;

	private GeneralDate payDate;

	private GeneralDate stdDate;

	private GeneralDate accountingClosing;

	private YearMonth socialInsLevyMon;

	private GeneralDate socialInsStdDate;

	private GeneralDate incomeTaxStdDate;

	private NeededWorkDay neededWorkDay;

	private GeneralDate empInsStdDate;

	private YearMonth stmtOutputMon;

	public static Payday createSimpleFromJavaType(String companyCode, int processingNo, int payBonusAtr,
			int processingYm, int sparePayAtr, GeneralDate payDate, GeneralDate stdDate, GeneralDate accountingClosing,
			int socialInsLevyMon, GeneralDate socialInsStdDate, GeneralDate incomeTaxStdDate, BigDecimal neededWorkDay,
			GeneralDate empInsStdDate, int stmtOutputMon) {

		return new Payday(new CompanyCode(companyCode), new ProcessingNo(processingNo),
				EnumAdaptor.valueOf(payBonusAtr, PayBonusAtr.class), YearMonth.of(processingYm),
				EnumAdaptor.valueOf(sparePayAtr, SparePayAtr.class), payDate, stdDate,
				accountingClosing, YearMonth.of(socialInsLevyMon), socialInsStdDate,
				incomeTaxStdDate, new NeededWorkDay(neededWorkDay), empInsStdDate, YearMonth.of(stmtOutputMon));
	}
}
