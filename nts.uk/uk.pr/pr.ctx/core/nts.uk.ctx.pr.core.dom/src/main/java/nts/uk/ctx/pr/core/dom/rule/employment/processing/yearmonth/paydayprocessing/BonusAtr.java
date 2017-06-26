package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing;

import java.util.HashMap;

public enum BonusAtr {
	NO(0), YES(1);

	public final int value;

	private static HashMap<Integer, BonusAtr> map = new HashMap<>();

	static {
		for (BonusAtr item : BonusAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	BonusAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum BonusAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static BonusAtr valueOf(int value) {
		return map.get(value);
	}
}
