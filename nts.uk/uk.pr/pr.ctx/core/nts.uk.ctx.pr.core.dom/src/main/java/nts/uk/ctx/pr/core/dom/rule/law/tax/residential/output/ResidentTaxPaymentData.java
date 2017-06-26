package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
/**
 * 
 * @author phongtq
 *
 */
@Getter
public class ResidentTaxPaymentData extends AggregateRoot {
	private ResiTaxCode code;
	private ResidentTaxMoney taxPayrollMoney;
	private ResidentTaxMoney taxBonusMoney;
	private ResidentTaxMoney taxOverdueMoney;
	private ResidentTaxMoney taxDemandChargeMoney;
	private ResidentTaxAddress address;
	private GeneralDate dueDate;
	private StaffNo staffNo;
	private ResidentTaxMoney retirementAmount;
	private ResidentTaxMoney cityTaxMoney;
	private ResidentTaxMoney prefectureTaxMoney;
	private YearMonth yearMonth;

	@Override
	public void validate() {
		super.validate();

		if (this.code == null) {
			throw new BusinessException(new RawErrorMessage("対象データがありません。"));
		}
	}

	/**
	 * Convert java type of Resident Tax Payment
	 * @param code
	 * @param taxPayrollMoney
	 * @param taxBonusMoney
	 * @param taxOverdueMoney
	 * @param taxDemandChargeMoney
	 * @param address
	 * @param dueDate
	 * @param staffNo
	 * @param retirementAmount
	 * @param cityTaxMoney
	 * @param prefectureTaxMoney
	 * @param yearMonth
	 * @return
	 */
	public static ResidentTaxPaymentData createFromJavaType(String code, BigDecimal taxPayrollMoney,
			BigDecimal taxBonusMoney, BigDecimal taxOverdueMoney, BigDecimal taxDemandChargeMoney, String address,
			GeneralDate dueDate, BigDecimal staffNo, BigDecimal retirementAmount, BigDecimal cityTaxMoney,
			BigDecimal prefectureTaxMoney, int yearMonth) {
		return new ResidentTaxPaymentData(new ResiTaxCode(code), new ResidentTaxMoney(taxPayrollMoney),
				new ResidentTaxMoney(taxBonusMoney), new ResidentTaxMoney(taxOverdueMoney),
				new ResidentTaxMoney(taxDemandChargeMoney), new ResidentTaxAddress(address), dueDate,
				new StaffNo(staffNo), new ResidentTaxMoney(retirementAmount), new ResidentTaxMoney(cityTaxMoney),
				new ResidentTaxMoney(prefectureTaxMoney), new YearMonth(yearMonth));
	}
	/**
	 * Constructor Resident Tax Payment
	 * @param code
	 * @param taxPayrollMoney
	 * @param taxBonusMoney
	 * @param taxOverdueMoney
	 * @param taxDemandChargeMoney
	 * @param address
	 * @param dueDate
	 * @param staffNo
	 * @param retirementAmount
	 * @param cityTaxMoney
	 * @param prefectureTaxMoney
	 * @param yearMonth
	 */
	public ResidentTaxPaymentData(ResiTaxCode code, ResidentTaxMoney taxPayrollMoney, ResidentTaxMoney taxBonusMoney,
			ResidentTaxMoney taxOverdueMoney, ResidentTaxMoney taxDemandChargeMoney, ResidentTaxAddress address,
			GeneralDate dueDate, StaffNo staffNo, ResidentTaxMoney retirementAmount, ResidentTaxMoney cityTaxMoney,
			ResidentTaxMoney prefectureTaxMoney, YearMonth yearMonth) {
		super();
		this.code = code;
		this.taxPayrollMoney = taxPayrollMoney;
		this.taxBonusMoney = taxBonusMoney;
		this.taxOverdueMoney = taxOverdueMoney;
		this.taxDemandChargeMoney = taxDemandChargeMoney;
		this.address = address;
		this.dueDate = dueDate;
		this.staffNo = staffNo;
		this.retirementAmount = retirementAmount;
		this.cityTaxMoney = cityTaxMoney;
		this.prefectureTaxMoney = prefectureTaxMoney;
		this.yearMonth = yearMonth;
	}
}
