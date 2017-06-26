package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.ProcessingNo;

@Setter
@Getter
@AllArgsConstructor
public class PaydayProcessing extends AggregateRoot {

	private CompanyCode companyCode;

	private ProcessingNo processingNo;

	private ProcessingName processingName;

	private DisplayAtr dispSet;

	private YearMonth currentProcessingYm;

	private BonusAtr bonusAtr;

	private YearMonth bCurrentProcessingYm;

	public static PaydayProcessing createSimpleFromJavaType(String ccd, int processingNo, String processingName,
			int displayAtr, int currentProcessingYm, int bonusAtr, int bCurrentProcessingYm) {

		return new PaydayProcessing(new CompanyCode(ccd), new ProcessingNo(processingNo),
				new ProcessingName(processingName), EnumAdaptor.valueOf(displayAtr, DisplayAtr.class),
				YearMonth.of(currentProcessingYm), EnumAdaptor.valueOf(bonusAtr, BonusAtr.class),
				YearMonth.of(bCurrentProcessingYm));
	}
}
