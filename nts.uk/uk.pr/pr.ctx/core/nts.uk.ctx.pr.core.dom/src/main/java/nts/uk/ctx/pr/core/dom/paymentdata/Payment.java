package nts.uk.ctx.pr.core.dom.paymentdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.TaxAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPositionCategory;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.AgeContinuationInsureAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.EmploymentInsuranceAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.HealthInsuranceAverageEarn;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.HealthInsuranceGrade;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.InsuredAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.PensionAverageEarn;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.PensionInsuranceGrade;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.WorkInsuranceCalculateAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.residence.ResidenceName;
import nts.uk.shr.com.primitive.PersonId;
import nts.uk.shr.com.primitive.sample.ProcessingNo;
import nts.uk.shr.com.primitive.sample.ResidenceCode;

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
	private PersonName personName;
	
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
	private SpecificationName specificationName;

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
	private PaymentRemarks remarks;
	
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

	@Getter
	private String departmentCode;
	
	@Getter
	private String departmentName;
	
	@Getter
	private String companyName;
	
	public void additionInfo(String departmentCode, String departmentName, String companyName) {
		this.departmentCode = departmentCode;
		this.departmentName = departmentName;
		this.companyName = companyName;
	}
	
	public Payment(
				CompanyCode companyCode, 
				PersonId personId, 
				PersonName personName,
				ProcessingNo processingNo, 
				PayBonusAtr  payBonusAtr,
				YearMonth processingYM, 
				SparePayAtr sparePayAtr, 
				GeneralDate standardDate,
				SpecificationCode specificationCode, 
				SpecificationName specificationName,
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
		this.personName = personName;
		this.processingNo = processingNo;
		this. payBonusAtr =  payBonusAtr;
		this.processingYM = processingYM;
		this.sparePayAtr = sparePayAtr;
		this.standardDate = standardDate;
		this.specificationCode = specificationCode;
		this.specificationName = specificationName;
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
							String personName,
							int processingNo, 
							int  payBonusAtr,
							int processingYM, 
							int sparePayAtr, 
							GeneralDate standardDate,
							String specificationCode, 
							String specificationName,
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
					new PersonName(personName),
					new ProcessingNo(processingNo),
					EnumAdaptor.valueOf( payBonusAtr, PayBonusAtr.class),
					new YearMonth(processingYM),
					EnumAdaptor.valueOf(sparePayAtr, SparePayAtr.class),
					standardDate,
					new SpecificationCode(specificationCode),
					new SpecificationName(specificationName),
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
	
	public void setPositionCategoryItems(List<PrintPositionCategory> items) {
		this.printCategories.clear();
		this.printCategories.addAll(items);
	}
	
	public void setStandardDate(GeneralDate standardDate) {
		this.standardDate = standardDate;
	}
	
	/**
	 * Calculate total payment
	 * @return
	 */
	public static double calculateTotalPayment(List<DetailItem> detailPaymentList) {
		if (detailPaymentList == null) {
			return 0.0;
		}
		
		return detailPaymentList.stream()
				.filter(x -> !itemCodeSpecialList().contains(x.getItemCode().v()))
				.collect(Collectors.summingDouble(x -> x.getValue()));
	}
	
	/**
	 * Calculate deduction total payment
	 * @return
	 */
	public static double calculateDeductionTotalPayment(List<DetailItem> detailDeductionList) {
		if (detailDeductionList == null) {
			return 0.0;
		}
		
		return detailDeductionList.stream()
				.filter(x -> !itemCodeSpecialList().contains(x.getItemCode().v()))
				.collect(Collectors.summingDouble(x -> x.getValue()));
	}
	
	/**
	 * calculate amount of payment
	 * @return
	 */
	public static double amountOfPay(double totalPayment, double deductionTotalPayment) {
		return totalPayment - deductionTotalPayment;
	}
	
	/**
	 * Check duplicate detail payment item
	 * @param cateAtr
	 * @param itemCode
	 * @return
	 */
	public boolean existsDetailPaymentItem(CategoryAtr cateAtr, ItemCode itemCode) {
		return this.detailPaymentItems.stream().anyMatch(x->x.getCategoryAtr() == cateAtr && x.getItemCode().equals(itemCode));
	}
	
	/**
	 * Check duplicate detail deduction item
	 * @param cateAtr
	 * @param itemCode
	 * @return
	 */
	public boolean existsDetailDeductionItem(CategoryAtr cateAtr, ItemCode itemCode) {
		return this.detailDeductionItems.stream().anyMatch(x->x.getCategoryAtr() == cateAtr && x.getItemCode().equals(itemCode));
	}

	/**
	 * Check duplicate detail article item
	 * @param cateAtr
	 * @param itemCode
	 * @return
	 */
	public boolean existsDetailArticleItem(CategoryAtr cateAtr, ItemCode itemCode) {
		return this.detailArticleItems.stream().anyMatch(x->x.getCategoryAtr() == cateAtr && x.getItemCode().equals(itemCode));
	}
	
	/**
	 * Item code list include(TotalPayment, DeductionTotalPayment, amountOfPay)
	 * @return
	 */
	private static List<String> itemCodeSpecialList() {
		return Arrays.asList("F003", "F114", "F309");
	}
}
