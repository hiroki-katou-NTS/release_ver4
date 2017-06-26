package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.ProcessingNo;

@Getter
@AllArgsConstructor
public class StandardDay extends AggregateRoot {

	private CompanyCode companyCode;

	private ProcessingNo processingNo;

	private SocialInsStdYearAtr socialInsStdYearAtr;

	private SocialInsStdMon socialInsStdMon;

	private SocialInsStdDay socialInsStdDay;

	private IncometaxStdYearAtr incometaxStdYearAtr;

	private IncometaxStdMon incometaxStdMon;

	private IncometaxStdDay incometaxStdDay;

	private EmpInsStdMon empInsStdMon;

	private EmpInsStdDay empInsStdDay;

	public static StandardDay createSimpleFromJavaType(String companyCode, int processingNo, int socialInsStdYearAtr,
			int socialInsStdMon, int socialInsStdDay, int incometaxStdYearAtr, int incometaxStdMon, int incometaxStdDay,
			int empInsStdMon, int empInsStdDay) {

		return new StandardDay(new CompanyCode(companyCode), new ProcessingNo(processingNo),
				EnumAdaptor.valueOf(socialInsStdYearAtr, SocialInsStdYearAtr.class),
				new SocialInsStdMon(socialInsStdMon), new SocialInsStdDay(socialInsStdDay),
				EnumAdaptor.valueOf(incometaxStdYearAtr, IncometaxStdYearAtr.class),
				new IncometaxStdMon(incometaxStdMon), new IncometaxStdDay(incometaxStdDay),
				new EmpInsStdMon(empInsStdMon), new EmpInsStdDay(empInsStdDay));
	}
}
