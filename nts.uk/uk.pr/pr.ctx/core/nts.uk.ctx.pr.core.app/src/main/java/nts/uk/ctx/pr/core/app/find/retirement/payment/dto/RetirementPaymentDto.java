package nts.uk.ctx.pr.core.app.find.retirement.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@AllArgsConstructor
public class RetirementPaymentDto {
	private String companyCode;
	private String personId;
	private GeneralDate payDate;
	private int trialPeriodSet;
	private int exclusionYears;
	private int additionalBoardYears;
	private int boardYears;
	private int totalPaymentMoney;
	private int deduction1Money;
	private int deduction2Money;
	private int deduction3Money;
	private int retirementPayOption;
	private int taxCalculationMethod;
	private int incomeTaxMoney;
	private int cityTaxMoney;
	private int prefectureTaxMoney;
	private int totalDeclarationMoney;
	private int actualRecieveMoney;
	private int bankTransferOption1;
	private int option1Money;
	private int bankTransferOption2;
	private int option2Money;
	private int bankTransferOption3;
	private int option3Money;
	private int bankTransferOption4;
	private int option4Money;
	private int bankTransferOption5;
	private int option5Money;
	private String withholdingMeno;
	private String statementMemo;
}
