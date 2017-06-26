package nts.uk.ctx.pr.core.dom.itemmaster.itemperiod;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.Year;

@Getter
public class ItemPeriod extends AggregateRoot {
	private CompanyCode companyCode;
	private ItemClass itemclass;
	private ItemCode itemCode;
	private DateUseClassification expirationDateUseClassification;
	private Year startYear;
	private Year endYear;
	private UsageClassification cycleUsageClassification;
	private UsageClassification januaryUsageClassification;
	private UsageClassification februaryUsageClassification;
	private UsageClassification marchUsageClassification;
	private UsageClassification aprilUsageClassification;
	private UsageClassification mayUsageClassification;
	private UsageClassification juneUsageClassification;
	private UsageClassification julyUsageClassification;
	private UsageClassification augustUsageClassification;
	private UsageClassification septemberUsageClassification;
	private UsageClassification octoberUsageClassification;
	private UsageClassification novemberUsageClassification;
	private UsageClassification decemberUsageClassification;

	public ItemPeriod(CompanyCode companyCode, ItemClass itemclass, ItemCode itemCode,
			DateUseClassification expirationDateUseClassification, Year startYear, Year endYear,
			UsageClassification cycleUsageClassification, UsageClassification januaryUsageClassification,
			UsageClassification februaryUsageClassification, UsageClassification marchUsageClassification,
			UsageClassification aprilUsageClassification, UsageClassification mayUsageClassification,
			UsageClassification juneUsageClassification, UsageClassification julyUsageClassification,
			UsageClassification augustUsageClassification, UsageClassification septemberUsageClassification,
			UsageClassification octoberUsageClassification, UsageClassification novemberUsageClassification,
			UsageClassification decemberUsageClassification) {
		super();
		this.companyCode = companyCode;
		this.itemclass = itemclass;
		this.itemCode = itemCode;
		this.expirationDateUseClassification = expirationDateUseClassification;
		this.startYear = startYear;
		this.endYear = endYear;
		this.cycleUsageClassification = cycleUsageClassification;
		this.januaryUsageClassification = januaryUsageClassification;
		this.februaryUsageClassification = februaryUsageClassification;
		this.marchUsageClassification = marchUsageClassification;
		this.aprilUsageClassification = aprilUsageClassification;
		this.mayUsageClassification = mayUsageClassification;
		this.juneUsageClassification = juneUsageClassification;
		this.julyUsageClassification = julyUsageClassification;
		this.augustUsageClassification = augustUsageClassification;
		this.septemberUsageClassification = septemberUsageClassification;
		this.octoberUsageClassification = octoberUsageClassification;
		this.novemberUsageClassification = novemberUsageClassification;
		this.decemberUsageClassification = decemberUsageClassification;
	}

	public static ItemPeriod createFromJavaType(String companyCode, int itemclass, String itemcode,
			int expirationDateUseClassification, int startYear, int endYear, int cycleUsageClassification,
			int januaryUsageClassification, int februaryUsageClassification, int marchUsageClassification,
			int aprilUsageClassification, int mayUsageClassification, int juneUsageClassification,
			int julyUsageClassification, int augustUsageClassification, int septemberUsageClassification,
			int octoberUsageClassification, int novemberUsageClassification, int decemberUsageClassification) {

		return new ItemPeriod(new CompanyCode(companyCode), new ItemClass(itemclass), new ItemCode(itemcode),
				EnumAdaptor.valueOf(expirationDateUseClassification, DateUseClassification.class), new Year(startYear),
				new Year(endYear), EnumAdaptor.valueOf(cycleUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(januaryUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(februaryUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(marchUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(aprilUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(mayUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(juneUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(julyUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(augustUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(septemberUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(octoberUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(novemberUsageClassification, UsageClassification.class),
				EnumAdaptor.valueOf(decemberUsageClassification, UsageClassification.class));

	}

}
