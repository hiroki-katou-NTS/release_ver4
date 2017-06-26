package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday;

import java.util.HashMap;

public enum PickupStdMonAtr {
	PREVIEW_MONTH(-1), CURRENT_MONTH(0);
	public final int value;

	private static HashMap<Integer, PickupStdMonAtr> map = new HashMap<>();

	static {
		for (PickupStdMonAtr item : PickupStdMonAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	PickupStdMonAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum PickupStdMonAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static PickupStdMonAtr valueOf(int value) {
		return map.get(value);
	}

}
