package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.ProcessingNo;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.SocialInsLevyMonAtr;

@Getter
@AllArgsConstructor
public class SystemDay extends AggregateRoot {

	private CompanyCode companyCode;

	private ProcessingNo processingNo;

	private SocialInsLevyMonAtr socialInsLevyMonAtr;

	private PickupStdMonAtr pickupStdMonAtr;

	private PickupStdDay pickupStdDay;

	private PayStdDay payStdDay;

	private AccountDueMonAtr accountDueMonAtr;

	private AccountDueDay accountDueDay;

	private PayslipPrintMonthAtr payslipPrintMonthAtr;

	public static SystemDay createSimpleFromJavaType(String companyCode, int processingNo, int socialInsLevyMonAtr, int pickupStdMonAtr, int pickupStdDay,
			int payStdDay, int accountDueMonAtr, int accountDueDay, int payslipPrintMonthAtr) {

		return new SystemDay(new CompanyCode(companyCode), new ProcessingNo(processingNo),
				EnumAdaptor.valueOf(socialInsLevyMonAtr, SocialInsLevyMonAtr.class),
				EnumAdaptor.valueOf(pickupStdMonAtr, PickupStdMonAtr.class), new PickupStdDay(pickupStdDay),
				new PayStdDay(payStdDay), EnumAdaptor.valueOf(accountDueMonAtr, AccountDueMonAtr.class),
				new AccountDueDay(accountDueDay), EnumAdaptor.valueOf(payslipPrintMonthAtr, PayslipPrintMonthAtr.class));
	}
}
