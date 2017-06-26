package nts.uk.ctx.pr.core.app.command.rule.employment.processing.yearmonth;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.PaydayProcessing;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday.StandardDay;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday.SystemDay;

@Getter
public class Qmm005cCommand {
	private int payBonusAtr;

	private int sparePayAtr;

	private int processingNo;

	private String processingName;

	private int dispSet;

	private int currentProcessingYm;

	private int bonusAtr;

	private int bcurrentProcessingYm;

	private int payStdDay;
	
	private int pickupStdMonAtr;

	private int pickupStdDay;

	private int accountDueMonAtr;

	private int accountDueDay;

	private int payslipPrintMonthAtr;

	private int socialInsuLevyMonAtr;

	private int socialInsStdYearAtr;

	private int socialInsStdMon;

	private int socialInsStdDay;

	private int empInsStdMon;

	private int empInsStdDay;

	private int incometaxStdYearAtr;

	private int incometaxStdMon;

	private int incometaxStdDay;

	private List<PayDayInsertCommand> payDays;

	public SystemDay toSystemDayDomain(String companyCode) {
		return SystemDay.createSimpleFromJavaType(companyCode, getProcessingNo(), getSocialInsuLevyMonAtr(), getPickupStdMonAtr(), getPickupStdDay(),
				getPayStdDay(), getAccountDueMonAtr(), getAccountDueDay(), getPayslipPrintMonthAtr());
	}

	public StandardDay toStandardDayDomain(String companyCode) {
		return StandardDay.createSimpleFromJavaType(companyCode, getProcessingNo(), getSocialInsStdYearAtr(),
				getSocialInsStdMon(), getSocialInsStdDay(), getIncometaxStdYearAtr(), getIncometaxStdMon(),
				getIncometaxStdDay(), getEmpInsStdMon(), getEmpInsStdDay());
	}

	public PaydayProcessing toPaydayProcessingDomain(String companyCode) {
		return PaydayProcessing.createSimpleFromJavaType(companyCode, getProcessingNo(), getProcessingName(),
				getDispSet(), getCurrentProcessingYm(), getBonusAtr(), getBcurrentProcessingYm());
	}
}