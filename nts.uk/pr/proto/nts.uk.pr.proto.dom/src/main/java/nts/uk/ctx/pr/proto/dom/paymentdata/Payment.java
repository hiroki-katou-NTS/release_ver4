package nts.uk.ctx.pr.proto.dom.paymentdata;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.proto.dom.itemmaster.TaxAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.proto.dom.paymentdata.dataitem.position.PrintPositionCategory;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.AgeContinuationInsureAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.EmploymentInsuranceAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.HealthInsuranceAverageEarn;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.HealthInsuranceGrade;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.InsuredAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.PensionAverageEarn;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.PensionInsuranceGrade;
import nts.uk.ctx.pr.proto.dom.paymentdata.insure.WorkInsuranceCalculateAtr;
import nts.uk.ctx.pr.proto.dom.paymentdata.residence.ResidenceCode;
import nts.uk.ctx.pr.proto.dom.paymentdata.residence.ResidenceName;
import nts.uk.shr.com.primitive.PersonId;

/**
 * 明細データ
 * 
 * @author vunv
 *
 */
public class Payment extends AggregateRoot {
	@Getter
	private final CompanyCode companyCode;

	@Getter
	private final PersonId personId;

	@Getter
	private final ProcessingNo processingNo;

	@Getter
	private final PayBonusAtr payBonusAtr;

	@Getter
	private final YearMonth processingYM;

	@Getter
	private SparePayAtr sparePayAtr;

	@Getter
	private GeneralDate standardDate;

	@Getter
	private SpecificationCode specificationCode;

	@Getter
	private ResidenceCode residenceCode;

	@Getter
	private ResidenceName residenceName;

	@Getter
	private HealthInsuranceGrade healthInsuranceGrade;

	@Getter
	private HealthInsuranceAverageEarn healthInsuranceAverageEarn;

	@Getter
	private AgeContinuationInsureAtr ageContinuationInsureAtr;

	@Getter
	private TenureAtr tenureAtr;

	@Getter
	private TaxAtr taxAtr;

	@Getter
	private PensionInsuranceGrade pensionInsuranceGrade;

	@Getter
	private PensionAverageEarn pensionAverageEarn;

	@Getter
	private EmploymentInsuranceAtr employmentInsuranceAtr;

	@Getter
	private DependentNumber dependentNumber;

	@Getter
	private WorkInsuranceCalculateAtr workInsuranceCalculateAtr;

	@Getter
	private InsuredAtr insuredAtr;

	@Getter
	private BonusTaxRate bonusTaxRate;

	@Getter
	private CalcFlag calcFlag;

	@Getter
	private MakeMethodFlag makeMethodFlag;

	@Getter
	private Comment comment;
	
	@Getter
	private List<DetailItem> detailPaymentItems = new ArrayList<>();

	@Getter
	private List<DetailItem> detailDeductionItems = new ArrayList<>();

	@Getter
	private List<DetailItem> detailPersonalTimeItems = new ArrayList<>();

	@Getter
	private List<DetailItem> detailArticleItems = new ArrayList<>();

	@Getter
	private List<PrintPositionCategory> printCategories = new ArrayList<>();

	public Payment(
				CompanyCode companyCode, 
				PersonId personId, 
				ProcessingNo processingNo, 
				PayBonusAtr  payBonusAtr,
				YearMonth processingYM, 
				SparePayAtr sparePayAtr, 
				GeneralDate standardDate,
				SpecificationCode specificationCode, 
				ResidenceCode residenceCode, 
				ResidenceName residenceName,
				HealthInsuranceGrade healthInsuranceGrade, 
				HealthInsuranceAverageEarn healthInsuranceAverageEarn,
				AgeContinuationInsureAtr ageContinuationInsureAtr, 
				TenureAtr tenureAtr, 
				TaxAtr taxAtr,
				PensionInsuranceGrade pensionInsuranceGrade, 
				PensionAverageEarn pensionAverageEarn,
				EmploymentInsuranceAtr employmentInsuranceAtr, 
				DependentNumber dependentNumber,
				WorkInsuranceCalculateAtr workInsuranceCalculateAtr, 
				InsuredAtr insuredAtr, 
				BonusTaxRate bonusTaxRate,
				CalcFlag calcFlag, 
				MakeMethodFlag makeMethodFlag,
				Comment comment) {
		
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.processingNo = processingNo;
		this. payBonusAtr =  payBonusAtr;
		this.processingYM = processingYM;
		this.sparePayAtr = sparePayAtr;
		this.standardDate = standardDate;
		this.specificationCode = specificationCode;
		this.residenceCode = residenceCode;
		this.residenceName = residenceName;
		this.healthInsuranceGrade = healthInsuranceGrade;
		this.healthInsuranceAverageEarn = healthInsuranceAverageEarn;
		this.ageContinuationInsureAtr = ageContinuationInsureAtr;
		this.tenureAtr = tenureAtr;
		this.taxAtr = taxAtr;
		this.pensionInsuranceGrade = pensionInsuranceGrade;
		this.pensionAverageEarn = pensionAverageEarn;
		this.employmentInsuranceAtr = employmentInsuranceAtr;
		this.dependentNumber = dependentNumber;
		this.workInsuranceCalculateAtr = workInsuranceCalculateAtr;
		this.insuredAtr = insuredAtr;
		this.bonusTaxRate = bonusTaxRate;
		this.calcFlag = calcFlag;
		this.makeMethodFlag = makeMethodFlag;
		this.comment = comment;
	}
	
	public static Payment createFromJavaType(
							String companyCode, 
							String personId, 
							int processingNo, 
							int  payBonusAtr,
							int processingYM, 
							int sparePayAtr, 
							LocalDate standardDate,
							String specificationCode, 
							String residenceCode, 
							String residenceName,
							int healthInsuranceGrade, 
							int healthInsuranceAverageEarn,
							int ageContinuationInsureAtr, 
							int tenureAtr, 
							int taxAtr,
							int pensionInsuranceGrade, 
							int pensionAverageEarn,
							int employmentInsuranceAtr, 
							int dependentNumber,
							int workInsuranceCalculateAtr, 
							int insuredAtr, 
							int bonusTaxRate,
							int calcFlag, 
							int makeMethodFlag,
							String comment
			){
		
		return new Payment(
					new CompanyCode(companyCode),
					new PersonId(personId),
					new ProcessingNo(processingNo),
					EnumAdaptor.valueOf( payBonusAtr, PayBonusAtr.class),
					new YearMonth(processingYM),
					EnumAdaptor.valueOf(sparePayAtr, SparePayAtr.class),
					GeneralDate.localDate(standardDate),
					new SpecificationCode(specificationCode),
					new ResidenceCode(residenceCode),
					new ResidenceName(residenceName),
					new HealthInsuranceGrade(healthInsuranceGrade),
					new HealthInsuranceAverageEarn(healthInsuranceAverageEarn),
					EnumAdaptor.valueOf(ageContinuationInsureAtr, AgeContinuationInsureAtr.class),
					EnumAdaptor.valueOf(tenureAtr, TenureAtr.class),
					EnumAdaptor.valueOf(taxAtr, TaxAtr.class),
					new PensionInsuranceGrade(pensionInsuranceGrade),
					new PensionAverageEarn(pensionAverageEarn),
					EnumAdaptor.valueOf(employmentInsuranceAtr, EmploymentInsuranceAtr.class),
					new DependentNumber(dependentNumber),
					EnumAdaptor.valueOf(workInsuranceCalculateAtr, WorkInsuranceCalculateAtr.class),
					EnumAdaptor.valueOf(insuredAtr, InsuredAtr.class),
					new BonusTaxRate(bonusTaxRate),
					EnumAdaptor.valueOf(calcFlag, CalcFlag.class),
					EnumAdaptor.valueOf(makeMethodFlag, MakeMethodFlag.class),
					new Comment(comment));
	}
	
	public static Payment AddOrUpdateFromJavaType(String companyCode, 
							String personId, 
							int processingNo, 
							int  payBonusAtr,
							int processingYM, 
							int sparePayAtr, 
							LocalDate standardDate,
							String specificationCode, 
							String residenceCode, 
							String residenceName,
							int healthInsuranceGrade, 
							int healthInsuranceAverageEarn,
							int ageContinuationInsureAtr, 
							int tenureAtr, 
							int taxAtr,
							int pensionInsuranceGrade, 
							int pensionAverageEarn,
							int employmentInsuranceAtr, 
							int dependentNumber,
							int workInsuranceCalculateAtr, 
							int insuredAtr, 
							int bonusTaxRate,
							int calcFlag, 
							int makeMethodFlag,
							String comment,
							List<DetailItem> detailPaymentItems,
							List<DetailItem> detailDeductionItems,
							List<DetailItem> detailPersonalTimeItems,
							List<DetailItem> detailArticleItems
							) {
		
		Payment payment =  new Payment(
				new CompanyCode(companyCode),
				new PersonId(personId),
				new ProcessingNo(processingNo),
				EnumAdaptor.valueOf( payBonusAtr, PayBonusAtr.class),
				new YearMonth(processingYM),
				EnumAdaptor.valueOf(sparePayAtr, SparePayAtr.class),
				GeneralDate.localDate(standardDate),
				new SpecificationCode(specificationCode),
				new ResidenceCode(residenceCode),
				new ResidenceName(residenceName),
				new HealthInsuranceGrade(healthInsuranceGrade),
				new HealthInsuranceAverageEarn(healthInsuranceAverageEarn),
				EnumAdaptor.valueOf(ageContinuationInsureAtr, AgeContinuationInsureAtr.class),
				EnumAdaptor.valueOf(tenureAtr, TenureAtr.class),
				EnumAdaptor.valueOf(taxAtr, TaxAtr.class),
				new PensionInsuranceGrade(pensionInsuranceGrade),
				new PensionAverageEarn(pensionAverageEarn),
				EnumAdaptor.valueOf(employmentInsuranceAtr, EmploymentInsuranceAtr.class),
				new DependentNumber(dependentNumber),
				EnumAdaptor.valueOf(workInsuranceCalculateAtr, WorkInsuranceCalculateAtr.class),
				EnumAdaptor.valueOf(insuredAtr, InsuredAtr.class),
				new BonusTaxRate(bonusTaxRate),
				EnumAdaptor.valueOf(calcFlag, CalcFlag.class),
				EnumAdaptor.valueOf(makeMethodFlag, MakeMethodFlag.class),
				new Comment(comment));
		
		payment.setDetailPaymentItems(detailPaymentItems);
		payment.setDetailDeductionItems(detailDeductionItems);
		payment.setDetailPersonalTimeItems(detailPersonalTimeItems);
		payment.setDetailArticleItems(detailArticleItems);
		
		return payment;
	}
	
	public void setDetailPaymentItems(List<DetailItem> items) {
		this.detailPaymentItems.clear();
		this.detailPaymentItems.addAll(items);
	}
	
	public void setDetailDeductionItems(List<DetailItem> items) {
		this.detailDeductionItems.clear();
		this.detailDeductionItems.addAll(items);
	}
	
	public void setDetailPersonalTimeItems(List<DetailItem> items) {
		this.detailPersonalTimeItems.clear();
		this.detailPersonalTimeItems.addAll(items);
	}
	
	public void setDetailArticleItems(List<DetailItem> items) {
		this.detailArticleItems.clear();
		this.detailArticleItems.addAll(items);
	}
	
	/**
	 * Calculate total payment
	 * @return
	 */
	public double calculateTotalPayment() {
		if (this.detailPaymentItems == null) {
			return 0.0;
		}
		
		return this.detailPaymentItems.stream()
				.collect(Collectors.summingDouble(x -> x.getValue()));
	}
	
	/**
	 * Calculate deduction total payment
	 * @return
	 */
	public double calculateDeductionTotalPayment() {
		if (this.detailDeductionItems == null) {
			return 0.0;
		}
		
		return this.detailDeductionItems.stream()
				.collect(Collectors.summingDouble(x -> x.getValue()));
	}
	
	/**
	 * calculate amount of payment
	 * @return
	 */
	public double amountOfPay() {
		return this.calculateTotalPayment() - this.calculateDeductionTotalPayment();
	}
}
