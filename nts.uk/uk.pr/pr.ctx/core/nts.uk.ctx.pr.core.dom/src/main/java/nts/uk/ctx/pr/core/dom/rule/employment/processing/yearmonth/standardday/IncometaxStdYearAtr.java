package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday;

import java.util.HashMap;

public enum IncometaxStdYearAtr {
	PREVIEW_YEAR(-1), CURRENT_YEAR(0), NEXT_YEAR(1), NEXT2_YEAR(2);
	
	public final int value;

	private static HashMap<Integer, IncometaxStdYearAtr> map = new HashMap<>();

	static {
		for (IncometaxStdYearAtr item : IncometaxStdYearAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	IncometaxStdYearAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum IncometaxStdYearAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static IncometaxStdYearAtr valueOf(int value) {
		return map.get(value);
	}

}
