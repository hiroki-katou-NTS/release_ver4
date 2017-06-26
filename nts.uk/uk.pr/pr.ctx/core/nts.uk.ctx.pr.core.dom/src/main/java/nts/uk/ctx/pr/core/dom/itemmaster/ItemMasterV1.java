package nts.uk.ctx.pr.core.dom.itemmaster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.util.Range;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.enums.UseOrNot;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.RangeChecker;

/**
 * 
 * 項目マスタ
 *
 */
public class ItemMasterV1 extends AggregateRoot{
	/** 会社コード */
	@Getter
	private CompanyCode companyCode;	
	/** カテゴリ区分 */
	@Getter
	private CategoryAtr categoryAtr;
	/** 項目コード */
	@Getter
	private ItemCode itemCode;
	/** 項目名称 */
	@Getter
	private ItemName itemName;
	/** 略名 */
	@Getter
	private ItemName itemAbName;
	/** 表示区分 */
	@Getter
	private DisplayAtr displayAtr;
	/** 平均賃金対象区分 */
	@Getter
	private WageClassificationAtr avgPaidAtr;
	@Getter
	private List<RangeChecker> alarm;
	@Getter
	private List<RangeChecker> error;
	
	/** 控除種類 */
	@Getter
	private DeductionAtr deductAttribute;
	/** 固定的賃金対象区分 */
	@Getter
	private WageClassificationAtr fixedPaidAtr;
	/** 統合項目コード */
	@Getter
	private IntegratedItemCode integratedItemCode;
	
	/** 項目属性 */
	@Getter
	private ItemAtr itemAtr;	
	/** 項目名表示区分 */
	@Getter
	private ItemNameDisplayAtr itemNameDisplayAtr;
	/** 労働保険対象区分 */
	@Getter
	private WageClassificationAtr laborInsuranceAtr;
	/** 限度金額 */
	@Getter
	private LimitMoney limitMoney;
	/** メモ */
	@Getter
	private Memo memo;
	/** 社会保険対象区分 */
	@Getter
	private WageClassificationAtr socialInsuranceAtr;
	/** 課税区分 */
	@Getter
	private TaxAtr taxAtr;
	/** ゼロ表示区分 */
	@Getter
	private DisplayAtr zeroDisplayAtr;
	
	
	public ItemMasterV1(CompanyCode companyCode, 
			ItemCode itemCode, 
			CategoryAtr categoryAtr, 
			ItemName itemName, 
			ItemName itemAbName, 
			TaxAtr taxAtr, 
			ItemAtr itemAtr) {
		super();
		this.companyCode = companyCode;
		this.itemCode = itemCode;
		this.categoryAtr = categoryAtr;
		this.itemName = itemName;
		this.itemAbName = itemAbName;
		this.taxAtr = taxAtr;
		this.itemAtr = itemAtr;
	}
	/**
	 * Validate
	 */
	@Override
	public void validate() {
		super.validate();
	}	
	
	public static ItemMasterV1 createSimpleFromJavaType(
			String companyCode, 
			String itemCode, 
			int categoryAtr, 
			String itemName,
			String itemAbName,
			int taxAtr,
			int itemAtr
			)
	{
		return new ItemMasterV1(new CompanyCode(companyCode),
				new ItemCode(itemCode),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class),
				new ItemName(itemName), new ItemName(itemAbName), EnumAdaptor.valueOf(taxAtr, TaxAtr.class),
				EnumAdaptor.valueOf(itemAtr, ItemAtr.class)
				);
		
	}
	
	/**
	 * additional Info 
	 * @param limitMoney
	 * @param fixedPaidAtr
	 * @param laborInsuranceAtr
	 * @param socialInsuranceAtr
	 * @return
	 */
	public ItemMasterV1 additionalInfo(int limitMoney, int fixedPaidAtr, int laborInsuranceAtr, int socialInsuranceAtr, int avgPaidAtr, int deductAttribute) {
		this.limitMoney = new LimitMoney(limitMoney);
		this.fixedPaidAtr = EnumAdaptor.valueOf(fixedPaidAtr, WageClassificationAtr.class);
		this.laborInsuranceAtr = EnumAdaptor.valueOf(laborInsuranceAtr, WageClassificationAtr.class);
		this.socialInsuranceAtr = EnumAdaptor.valueOf(socialInsuranceAtr, WageClassificationAtr.class);
		this.avgPaidAtr = EnumAdaptor.valueOf(avgPaidAtr, WageClassificationAtr.class);
		this.deductAttribute = EnumAdaptor.valueOf(deductAttribute, DeductionAtr.class);
		return this;
	}
	
	public ItemMasterV1 additionalErrorAlarm(int isUseHighError, BigDecimal errRangeHigh, int isUseLowError, BigDecimal errRangeLow, int isUseHighAlam, BigDecimal alamRangeHigh, int isUseLowAlam, BigDecimal alamRangeLow){
		this.error = new ArrayList<>();
		this.alarm = new ArrayList<>();
		this.error.add(new RangeChecker(EnumAdaptor.valueOf(isUseHighError, UseOrNot.class),  EnumAdaptor.valueOf(isUseLowError, UseOrNot.class), Range.between(errRangeLow, errRangeHigh)));
		this.alarm.add(new RangeChecker(EnumAdaptor.valueOf(isUseHighAlam, UseOrNot.class),  EnumAdaptor.valueOf(isUseLowAlam, UseOrNot.class), Range.between(alamRangeLow, alamRangeHigh)));
		return this;
	}
	
	/**
	 * Check tax = COMMUTING_COST || COMMUTING_EXPENSE
	 * (using for calculate payment create data)
	 * @return
	 */
	public boolean isTaxCommutingoCostOrCommutingExpense() {
		return this.taxAtr == TaxAtr.COMMUTING_COST || this.taxAtr == TaxAtr.COMMUTING_EXPENSE;
	}
	
	/**
	 * Check tax = TAXATION || TAX_FREE_LIMIT || TAX_FREE_UN_LIMIT
	 * (using for calculate payment create data)
	 * @return
	 */
	public boolean isTaxTaxationOrTaxFreeLimitOrTaxFreeUnLimit() {
		return this.taxAtr ==  TaxAtr.TAXATION || this.taxAtr == TaxAtr.TAX_FREE_LIMIT || this.taxAtr == TaxAtr.TAX_FREE_UN_LIMIT;
	}
}
