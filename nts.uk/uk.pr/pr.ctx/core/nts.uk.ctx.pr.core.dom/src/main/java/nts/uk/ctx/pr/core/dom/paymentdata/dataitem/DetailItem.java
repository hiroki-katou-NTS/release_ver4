package nts.uk.ctx.pr.core.dom.paymentdata.dataitem;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.DeductionAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.TaxAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.ColumnPosition;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.DetailItemPosition;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.LinePosition;

/**
 * 明細データ明細
 * 
 * @author vunv
 *
 */
public class DetailItem extends DomainObject {

	/**
	 * 項目コード
	 */
	@Getter
	private ItemCode itemCode;

	/**
	 * 値
	 */
	@Getter
	private Double value;

	/**
	 * 修正フラグ
	 */
	@Getter
	private CorrectFlag correctFlag;

	/**
	 * 社保対象区分
	 */
	@Getter
	private InsuranceAtr socialInsuranceAtr;

	/**
	 * 労保対象区分
	 */
	@Getter
	private InsuranceAtr laborInsuranceAtr;

	@Getter
	private DetailItemPosition itemPosition;

	@Getter
	private CategoryAtr categoryAtr;

	/*
	 * 控除種類
	 */
	@Getter
	private DeductionAtr deductionAtr;

	@Getter
	private ItemAtr itemAtr;

	@Getter
	private int averagePayAtr;

	@Getter
	private int fixPayAtr;

	/**
	 * 課税区分
	 */
	@Getter
	Integer taxAtr;
	
	@Getter
	private int limitAmount;

	@Getter
	private double commuteAllowTaxImpose;

	@Getter
	private double commuteAllowMonth;

	@Getter
	private double commuteAllowFraction;
	
	

	/**
	 * Constructor
	 * 
	 * @param value
	 * @param correctFlag
	 * @param socialInsuranceAtr
	 * @param laborInsuranceAtr
	 */
	public DetailItem(ItemCode itemCode, Double value, CorrectFlag correctFlag, InsuranceAtr socialInsuranceAtr,
			InsuranceAtr laborInsuranceAtr, CategoryAtr categoryAtr, DeductionAtr deductionAtr,
			DetailItemPosition itemPosition) {
		super();
		this.itemCode = itemCode;
		this.value = value;
		this.correctFlag = correctFlag;
		this.socialInsuranceAtr = socialInsuranceAtr;
		this.laborInsuranceAtr = laborInsuranceAtr;
		this.categoryAtr = categoryAtr;
		this.deductionAtr = deductionAtr;
		this.itemPosition = itemPosition;
	}

	public static DetailItem createFromJavaType(String itemCode, Double value, int correctFlag, int socialInsuranceAtr,
			int laborInsuranceAtr, int categoryAtr, int deductionAtr, int linePosition, int columnPosition) {

		return new DetailItem(new ItemCode(itemCode), value, EnumAdaptor.valueOf(correctFlag, CorrectFlag.class),
				EnumAdaptor.valueOf(socialInsuranceAtr, InsuranceAtr.class),
				EnumAdaptor.valueOf(laborInsuranceAtr, InsuranceAtr.class),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class),
				EnumAdaptor.valueOf(deductionAtr, DeductionAtr.class),
				new DetailItemPosition(new LinePosition(linePosition), new ColumnPosition(columnPosition)));
	}

	/**
	 * Create data for detail item (using for create data)
	 * 
	 * @param limitAmount
	 *            limitAmount
	 * @param fixPayAtr
	 *            fixPayAtr
	 * @param averagePayAtr
	 *            averagePayAtr
	 * @param itemAtr
	 *            itemAtr
	 */
	public void additionalInfo(int limitAmount, int fixPayAtr, int averagePayAtr, int itemAtr) {
		this.limitAmount = limitAmount;
		this.fixPayAtr = fixPayAtr;
		this.averagePayAtr = averagePayAtr;
		this.itemAtr = EnumAdaptor.valueOf(itemAtr, ItemAtr.class);
	}

	/**
	 * Create data for detail item (using for create data)
	 * 
	 * @param correctFlag
	 *            correctFlag
	 * @param socialInsuranceAtr
	 *            socialInsuranceAtr
	 * @param laborInsuranceAtr
	 *            laborInsuranceAtr
	 * @param deductionAtr
	 *            deductionAtr
	 */
	public void additionalInfo(CorrectFlag correctFlag, int socialInsuranceAtr, int laborInsuranceAtr,
			DeductionAtr deductionAtr) {
		this.correctFlag = correctFlag;
		this.socialInsuranceAtr = EnumAdaptor.valueOf(socialInsuranceAtr, InsuranceAtr.class);
		this.laborInsuranceAtr = EnumAdaptor.valueOf(laborInsuranceAtr, InsuranceAtr.class);
		this.deductionAtr = deductionAtr;
	}

	/**
	 * Create data for detail item (using for create data)
	 * 
	 * @param linePosition
	 *            linePosition
	 * @param columnPosition
	 *            columnPosition
	 */
	public void additionalInfo(int linePosition, int columnPosition) {
		this.itemPosition = new DetailItemPosition(new LinePosition(linePosition), new ColumnPosition(columnPosition));
	}

	/**
	 * Add info: commute Allow Month
	 * 
	 * @param commuteAllowMonth
	 * @return
	 */
	public void additionalInfo(double commuteAllowMonth) {
		this.commuteAllowMonth = commuteAllowMonth;
	}

	/**
	 * Add info: taxtAtr
	 * 
	 * @param taxAtr
	 * @return
	 */
	public void additionalInfo(TaxAtr taxAtr) {
		this.taxAtr = taxAtr.value;
	}

	
	/**
	 * add comute data
	 * 
	 * @param commuteAllowTaxImpose
	 * @param commuteAllowMonth
	 * @param commuteAllowFraction
	 */
	public void addCommuteData(double commuteAllowTaxImpose, double commuteAllowMonth, double commuteAllowFraction) {
		this.commuteAllowTaxImpose = commuteAllowTaxImpose;
		this.commuteAllowMonth = commuteAllowMonth;
		this.commuteAllowFraction = commuteAllowFraction;
	}

	/**
	 * Create data for detail item (using for create data)
	 * 
	 */
	public static DetailItem createDataDetailItem(ItemCode itemCode, Double value, CategoryAtr categoryAttribute) {
		return new DetailItem(itemCode, value.doubleValue(), CorrectFlag.NO_MODIFY, InsuranceAtr.UN_SUBJECT,
				InsuranceAtr.UN_SUBJECT, categoryAttribute, DeductionAtr.ANY_DEDUCTION, null);
	}
}
