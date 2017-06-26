package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

/**
 * 単価種類
 * 	0:時間単価
 * 	1:日額
 * 	2:その他
 * @author sonnh
 *
 */
public enum UnitpriceAtr {
	/**0:時間単価**/
	HOUR_UNIT_PRICE(0),
	/**1:日額**/
	DAILY_AMOUNT(1),
	/**2:その他**/
	OTHER(2);
	
	public final int value;
	 
	UnitpriceAtr(int value) {
		 this.value = value;
	 }
}
