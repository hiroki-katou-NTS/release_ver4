package nts.uk.ctx.pr.core.app.command.paymentdata.base;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;

/**
 * 
 * @author vunv
 *
 */
@Getter
@Setter
public class PaymentHeaderCommandBase {

	private String personId;

	private String personName;

	private int processingNo = 1;

	private int payBonusAtr = 0;

	private int processingYM;

	private int sparePayAtr = 0;

	private GeneralDate standardDate;

	private String specificationCode;

	private String specificationName;

	private String residenceCode = "000001";

	private String residenceName = "住民税納付先";

	private int healthInsuranceGrade = 5;

	private int healthInsuranceAverageEarn = 98000;

	private int ageContinuationInsureAtr = 0;

	private int tenureAtr = 0;

	private int taxAtr = 0;

	private int pensionInsuranceGrade = 1;

	private int pensionAverageEarn = 98000;

	private int employmentInsuranceAtr = 0;

	private int dependentNumber = 0;

	private int workInsuranceCalculateAtr = 0;

	private int insuredAtr = 0;

	private int bonusTaxRate = 0;

	private int calcFlag = 1;

	private int makeMethodFlag = 1;

	private String comment;

	private List<PrintPositionCategoryBase> printPositionCategories;

	/**
	 * Convert to domain object.
	 * 
	 * @return domain
	 */
	public Payment toDomain(String companyCode) {
		Payment payment = Payment.createFromJavaType(companyCode, this.personId, this.personName, this.processingNo,
				this.payBonusAtr, this.processingYM, this.sparePayAtr, this.standardDate, this.specificationCode,
				this.specificationName, this.residenceCode, this.residenceName, this.healthInsuranceGrade,
				this.healthInsuranceAverageEarn, this.ageContinuationInsureAtr, this.tenureAtr, this.taxAtr,
				this.pensionInsuranceGrade, this.pensionAverageEarn, this.employmentInsuranceAtr, this.dependentNumber,
				this.workInsuranceCalculateAtr, this.insuredAtr, this.bonusTaxRate, this.calcFlag, this.makeMethodFlag,
				this.comment);

		payment.setPositionCategoryItems(printPositionCategories.stream()
				.map(x -> x.toDomain(x.getCategoryAtr(), x.getLines())).collect(Collectors.toList()));
		return payment;
	}
}
