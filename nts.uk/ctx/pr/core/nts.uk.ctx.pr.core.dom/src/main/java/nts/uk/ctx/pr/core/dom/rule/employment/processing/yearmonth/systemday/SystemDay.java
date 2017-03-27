package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;

@Getter
@AllArgsConstructor
public class SystemDay extends AggregateRoot {

	private CompanyCode companyCode;

	private ProcessingNo processingNo;

	private SocialInsLevyMonAtr socialInsLevyMonAtr;

	private ResitaxStdMon resitaxStdMon;

	private ResitaxStdDay resitaxStdDay;

	private ResitaxBeginMon resitaxBeginMon;

	private PickupStdMonAtr pickupStdMonAtr;

	private PickupStdDay pickupStdDay;

	private PayStdDay payStdDay;

	private AccountDueMonAtr accountDueMonAtr;

	private AccountDueDay accountDueDay;

	private PayslipPrintMonth payslipPrintMonth;

	public static SystemDay createSimpleFromJavaType(String companyCode, int processingNo, int socialInsLevyMonAtr,
			int resitaxStdMon, int resitaxStdDay, int resitaxBeginMon, int pickupStdMonAtr, int pickupStdDay,
			int payStdDay, int accountDueMonAtr, int accountDueDay, int payslipPrintMonth) {

		return new SystemDay(new CompanyCode(companyCode), new ProcessingNo(processingNo),
				EnumAdaptor.valueOf(socialInsLevyMonAtr, SocialInsLevyMonAtr.class), new ResitaxStdMon(resitaxStdMon),
				new ResitaxStdDay(resitaxStdDay), new ResitaxBeginMon(resitaxBeginMon),
				EnumAdaptor.valueOf(pickupStdMonAtr, PickupStdMonAtr.class), new PickupStdDay(pickupStdDay),
				new PayStdDay(payStdDay), EnumAdaptor.valueOf(accountDueMonAtr, AccountDueMonAtr.class),
				new AccountDueDay(accountDueDay), EnumAdaptor.valueOf(payslipPrintMonth, PayslipPrintMonth.class));
	}
}
