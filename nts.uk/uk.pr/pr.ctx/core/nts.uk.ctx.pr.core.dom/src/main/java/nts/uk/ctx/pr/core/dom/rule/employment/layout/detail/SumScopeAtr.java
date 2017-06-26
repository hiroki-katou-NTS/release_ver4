package nts.uk.ctx.pr.core.dom.rule.employment.layout.detail;
import lombok.AllArgsConstructor;

/**
 * 
 * 合計対象区分
 *
 */
@AllArgsConstructor
public enum SumScopeAtr {
	/** 0:対象外*/
	EXCLUDED(0),
	/** 1:対象内*/
	INCLUDED(1),
	/**2:対象外（現物）	 */
	EXCLUDED_ACTUAL(2),
	/**3:対象内（現物）	 */
	INCLUDED_ACTUAL(3);
	
	public final int value;	
}
