package nts.uk.ctx.pr.core.dom.itemmaster;

import java.util.HashMap;

/**控除項目種類*/
public enum DeductionAtr {
	//0:任意控除
	ANY_DEDUCTION(0),
	//1:社保控除
	SOCIAL_INSURANCE_DEDUCTION(1),
	//2:所得税控除
	INCOME_TAX_CREDIT(2),
	//3:住民税控除
	RESIDENT_TAX_DEDUCTION(3);
	
	public final int value;

	private static HashMap<Integer, DeductionAtr> map = new HashMap<>();
	
	static{
		for(DeductionAtr item: DeductionAtr.values()){
			map.put(item.value, item);
		}
	} 	

	/**
	 * Constructor.
	 * 
	 * @param 控除項目種類
	 */
	DeductionAtr(int value) {
		this.value = value;
	}
	/**
	 * convert to enum DeductionAtr by value
	 * @param value
	 * @return
	 */
	public static DeductionAtr valueOf(int value){
		return map.get(value);
	}
	
	
}
