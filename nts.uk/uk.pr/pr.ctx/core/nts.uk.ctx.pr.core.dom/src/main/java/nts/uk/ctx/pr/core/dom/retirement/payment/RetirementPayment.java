package nts.uk.ctx.pr.core.dom.retirement.payment;


import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.shr.com.primitive.Memo;
import nts.uk.shr.com.primitive.PersonId;

/**
 * 退職金明細データ
 * @author Doan Duy Hung
 *
 */

public class RetirementPayment extends AggregateRoot {
	@Getter
	private CompanyCode companyCode;
	
	@Getter
	private PersonId personId;
	
	@Getter
	private GeneralDate payDate;
	
	@Getter
	private TrialPeriodSet trialPeriodSet;
	
	@Getter
	private PaymentYear exclusionYears;
	
	@Getter
	private PaymentYear additionalBoardYears;
	
	@Getter
	private PaymentYear boardYears;
	
	@Getter
	private PaymentMoney totalPaymentMoney;
	
	@Getter
	private PaymentMoney deduction1Money;
	
	@Getter
	private PaymentMoney deduction2Money;
	
	@Getter
	private PaymentMoney deduction3Money;
	
	@Getter
	private RetirementPayOption retirementPayOption;
	
	@Getter
	private TaxCalculationMethod taxCalculationMethod;
	
	@Getter
	private PaymentMoney incomeTaxMoney;
	
	@Getter
	private PaymentMoney cityTaxMoney;
	
	@Getter
	private PaymentMoney prefectureTaxMoney;
	
	@Getter
	private PaymentMoney totalDeclarationMoney;
	
	@Getter
	private PaymentMoney actualRecieveMoney;
	
	@Getter
	private BankTransferOption bankTransferOption1;
	
	@Getter
	private PaymentMoney option1Money;
	
	@Getter
	private BankTransferOption bankTransferOption2;
	
	@Getter
	private PaymentMoney option2Money;
	
	@Getter
	private BankTransferOption bankTransferOption3;
	
	@Getter
	private PaymentMoney option3Money;
	
	@Getter
	private BankTransferOption bankTransferOption4;
	
	@Getter
	private PaymentMoney option4Money;
	
	@Getter
	private BankTransferOption bankTransferOption5;
	
	@Getter
	private PaymentMoney option5Money;
	
	@Getter
	private Memo withholdingMeno;
	
	@Getter
	private Memo statementMemo;
	
	public RetirementPayment(CompanyCode companyCode, PersonId personId, GeneralDate payDate,
			TrialPeriodSet trialPeriodSet, PaymentYear exclusionYears, PaymentYear additionalBoardYears,
			PaymentYear boardYears, PaymentMoney totalPaymentMoney, PaymentMoney deduction1Money,
			PaymentMoney deduction2Money, PaymentMoney deduction3Money, RetirementPayOption retirementPayOption,
			TaxCalculationMethod taxCalculationMethod, PaymentMoney incomeTaxMoney, PaymentMoney cityTaxMoney,
			PaymentMoney prefectureTaxMoney, PaymentMoney totalDeclarationMoney, PaymentMoney actualRecieveMoney,
			BankTransferOption bankTransferOption1, PaymentMoney option1Money, BankTransferOption bankTransferOption2,
			PaymentMoney option2Money, BankTransferOption bankTransferOption3, PaymentMoney option3Money,
			BankTransferOption bankTransferOption4, PaymentMoney option4Money, BankTransferOption bankTransferOption5,
			PaymentMoney option5Money, Memo withholdingMeno, Memo statementMemo) {
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.payDate = payDate;
		this.trialPeriodSet = trialPeriodSet;
		this.exclusionYears = exclusionYears;
		this.additionalBoardYears = additionalBoardYears;
		this.boardYears = boardYears;
		this.totalPaymentMoney = totalPaymentMoney;
		this.deduction1Money = deduction1Money;
		this.deduction2Money = deduction2Money;
		this.deduction3Money = deduction3Money;
		this.retirementPayOption = retirementPayOption;
		this.taxCalculationMethod = taxCalculationMethod;
		this.incomeTaxMoney = incomeTaxMoney;
		this.cityTaxMoney = cityTaxMoney;
		this.prefectureTaxMoney = prefectureTaxMoney;
		this.totalDeclarationMoney = totalDeclarationMoney;
		this.actualRecieveMoney = actualRecieveMoney;
		this.bankTransferOption1 = bankTransferOption1;
		this.option1Money = option1Money;
		this.bankTransferOption2 = bankTransferOption2;
		this.option2Money = option2Money;
		this.bankTransferOption3 = bankTransferOption3;
		this.option3Money = option3Money;
		this.bankTransferOption4 = bankTransferOption4;
		this.option4Money = option4Money;
		this.bankTransferOption5 = bankTransferOption5;
		this.option5Money = option5Money;
		this.withholdingMeno = withholdingMeno;
		this.statementMemo = statementMemo;
		if(this.taxCalculationMethod.value==0){
			RetirementPaymentDomainService.autoCalculate(this);
		} else RetirementPaymentDomainService.manualCalculate(this);
	}
	
	/**
	 * set new value after auto calculate
	 * @param incomeTaxMoney IncomeTaxMoney
	 * @param cityTaxMoney CityTaxMoney
	 * @param prefectureTaxMoney PrefectureTaxMoney
	 * @param totalDeclarationMoney TotalDeclarationMoney
	 * @param actualRecieveMoney ActualRecieveMoney
	 */
	public void autoCalculate(PaymentMoney incomeTaxMoney, PaymentMoney cityTaxMoney, 
			PaymentMoney prefectureTaxMoney, PaymentMoney totalDeclarationMoney, PaymentMoney actualRecieveMoney){
		this.incomeTaxMoney = incomeTaxMoney;
		this.cityTaxMoney = cityTaxMoney;
		this.prefectureTaxMoney = prefectureTaxMoney;
		this.totalDeclarationMoney = totalDeclarationMoney;
		this.actualRecieveMoney = actualRecieveMoney;
	}
	
	/**
	 * set new value after manual calculate
	 * @param totalDeclarationMoney TotalDeclarationMoney
	 * @param actualRecieveMoney ActualRecieveMoney
	 */
	public void manualCalculate(PaymentMoney totalDeclarationMoney, PaymentMoney actualRecieveMoney){
		this.totalDeclarationMoney = totalDeclarationMoney;
		this.actualRecieveMoney = actualRecieveMoney;
	}
}
