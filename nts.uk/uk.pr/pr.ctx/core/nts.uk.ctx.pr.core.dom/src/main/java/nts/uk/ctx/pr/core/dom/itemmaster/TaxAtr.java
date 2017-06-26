package nts.uk.ctx.pr.core.dom.itemmaster;

import lombok.AllArgsConstructor;

/** 課税区分 */
@AllArgsConstructor
public enum TaxAtr {
	/**0:課税 */
	TAXATION(0),
	/**1:非課税（限度あり） */
	TAX_FREE_LIMIT(1),
	/**2:非課税（限度無し） */
	TAX_FREE_UN_LIMIT(2),
	/**3:通勤費（手入力） */
	COMMUTING_COST(3),
	/**4:通勤費（定期券利用） */
	COMMUTING_EXPENSE(4);
	
	public final int value;
}
