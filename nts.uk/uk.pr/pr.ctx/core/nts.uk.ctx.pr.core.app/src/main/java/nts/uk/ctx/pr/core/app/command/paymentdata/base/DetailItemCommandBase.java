package nts.uk.ctx.pr.core.app.command.paymentdata.base;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.TaxAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;

@Getter
@Setter
public class DetailItemCommandBase {

	/**
	 * category atr
	 */
	int categoryAtr;
	
	Integer itemAtr;
	
	/**
	 * 項目コード
	 */
	String itemCode;

	/**
	 * 項目名
	 */
	String itemName;

	/**
	 * 値
	 */
	Double value;

	/**
	 * 修正フラグ
	 */
	int correctFlag;

	/**
	 * 社保対象区分
	 */
	int socialInsuranceAtr;

	/**
	 * 労保対象区分
	 */
	int laborInsuranceAtr;

	int linePosition;

	int columnPosition;

	Integer deductAtr;
	
	Integer displayAtr;
	
	private Double commuteAllowTaxImpose;

	private Double commuteAllowMonth;

	private Double commuteAllowFraction;
	
	boolean created;

	/**
	 * 課税区分
	 */
	Integer taxAtr;
	
	public DetailItem toDomain(Integer linePosition) {
		if (displayAtr == DisplayAtr.NO_DISPLAY.value) {
			this.linePosition = -1;
		}else {
			this.linePosition = linePosition;
		}
		
//		if ("".equals(this.value)) {
//			throw new BusinessException("入力にエラーがあります。");	
//		}
		
		DetailItem item=  DetailItem.createFromJavaType(
				this.itemCode, 
				this.value == null ? 0: this.value, 
				this.correctFlag, 
				this.socialInsuranceAtr, 
				this.laborInsuranceAtr,
				this.categoryAtr,
				this.deductAtr,
				this.linePosition,
				this.columnPosition
				);
		
		item.addCommuteData(this.commuteAllowTaxImpose, this.commuteAllowMonth, this.commuteAllowFraction);
		item.additionalInfo(EnumAdaptor.valueOf(this.taxAtr, TaxAtr.class));
		return item;
	}
}
