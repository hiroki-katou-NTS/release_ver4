package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday;

import java.util.HashMap;

public enum PayslipPrintMonthAtr {
	PREVIEW_MONTH(-1), CURRENT_MONTH(0);

	public final int value;

	private static HashMap<Integer, PayslipPrintMonthAtr> map = new HashMap<>();

	static {
		for (PayslipPrintMonthAtr item : PayslipPrintMonthAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	PayslipPrintMonthAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum PayslipPrintMonthAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static PayslipPrintMonthAtr valueOf(int value) {
		return map.get(value);
	}

}
