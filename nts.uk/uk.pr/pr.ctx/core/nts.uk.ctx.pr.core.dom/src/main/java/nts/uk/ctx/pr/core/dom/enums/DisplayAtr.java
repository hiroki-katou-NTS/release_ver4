package nts.uk.ctx.pr.core.dom.enums;

import java.util.HashMap;

/** 表示区分 */
public enum DisplayAtr {
	//0:表示しない
	NO_DISPLAY(0),
	//1:表示する
	DISPLAY(1);
	
	public final int value;
	
	private static HashMap<Integer, DisplayAtr> map = new HashMap<>();
	
	static{
		for(DisplayAtr item: DisplayAtr.values()){
			map.put(item.value, item);
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	DisplayAtr(int value) {
		this.value = value;
	}	
	/**
	 * convert to enum DisplayAtr by value 
	 * @param value
	 * @return
	 */
	public static DisplayAtr valueOf(int value){
		return map.get(value);
	}
}
