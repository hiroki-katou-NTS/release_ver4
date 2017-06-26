package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail;
import lombok.AllArgsConstructor;
/**
 * 
 * 計算方法
 *
 */
@AllArgsConstructor
public enum CalculationMethod {
	/** 0:手入力*/
	MANUAL_ENTRY(0),
	/** 1:個人情報 */
	PERSONAL_INFORMATION(1),
	/** 2:計算式*/
	FORMULA(2),
	/** 3:賃金テーブル*/
	WAGE_TABLE(3),
	/** 4: 共通金額*/
	COMMON_AMOUNT_MONEY(4),
	/** 5:支給相殺*/
	PAYMENT_CANCELED(5),
	/**　9:システム計算	 */
	SYSTEM_CALCULATION(9);
	/**
     * value.
     */
    public final int value;    
}
