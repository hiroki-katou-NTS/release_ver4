/**
 * 
 */
package nts.uk.file.pr.app.export.retirementpayment.data;

import java.math.BigDecimal;

import lombok.Value;

/**
 * @author hungnm
 *
 */
@Value
public class RetirementPaymentDto {
	
	private String personId;

	private String trialPeriodSet;

	private String exclusionYears;

	private BigDecimal totalPaymentMoney;

	private BigDecimal incomeTaxMoney;

	private BigDecimal cityTaxMoney;

	private BigDecimal prefectureTaxMoney;

	private BigDecimal deductionMoney1;

	private BigDecimal deductionMoney2;

	private BigDecimal deductionMoney3;

	private BigDecimal totalDeductionMoney;

	private BigDecimal actualRecieveMoney;

	private String statementMemo;

	public RetirementPaymentDto(String personId, BigDecimal trialPeriodSet, BigDecimal exclusionYears, BigDecimal totalPaymentMoney,
			BigDecimal incomeTaxMoney, BigDecimal cityTaxMoney, BigDecimal prefectureTaxMoney,
			BigDecimal deductionMoney1, BigDecimal deductionMoney2, BigDecimal deductionMoney3,
			BigDecimal totalDeductionMoney, BigDecimal actualRecieveMoney, String statementMemo) {
		super();
		this.personId = personId;
		if (trialPeriodSet.intValue() == 0) {
			this.trialPeriodSet = "試用期間なし";
		} else {
			this.trialPeriodSet = "試用期間あり";
		}
		this.exclusionYears = exclusionYears + "年0ヶ 月";
		this.totalPaymentMoney = totalPaymentMoney;
		this.incomeTaxMoney = incomeTaxMoney;
		this.cityTaxMoney = cityTaxMoney;
		this.prefectureTaxMoney = prefectureTaxMoney;
		this.deductionMoney1 = deductionMoney1;
		this.deductionMoney2 = deductionMoney2;
		this.deductionMoney3 = deductionMoney3;
		this.totalDeductionMoney = totalDeductionMoney;
		this.actualRecieveMoney = actualRecieveMoney;
		this.statementMemo = statementMemo;
	}

}
