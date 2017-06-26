package nts.uk.ctx.pr.core.dom.itemmaster;
import lombok.AllArgsConstructor;

/** 項目属性 */
@AllArgsConstructor
public enum ItemAtr {
	//0:時間
	HOURS(0),
	//1:回数
	TIMES(1),	
	//2:金額（小数点無し）
	AMOUNT_NO_DECIMAL(2),
	//3:金額（小数点あり）
	AMOUNT_WITH_DECIMAL(3);
//	//4:文字
//	CHARACTERS(4);
	
	public final int value;	
}
