package nts.uk.ctx.pr.core.dom.itemmaster;

import lombok.AllArgsConstructor;

/** 項目名表示区分 */
@AllArgsConstructor
public enum ItemNameDisplayAtr {
	/**0:ゼロ円の場合明細書から項目名も出さない */
	NOT_PUT_IF_ZERO(0),
	/** 1:出す*/
	PUT_ALWAYS(1);
	
	public final int value;
}
