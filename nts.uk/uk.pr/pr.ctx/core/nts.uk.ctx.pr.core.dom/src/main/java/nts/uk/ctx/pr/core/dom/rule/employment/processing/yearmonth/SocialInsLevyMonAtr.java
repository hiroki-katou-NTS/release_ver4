package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth;

import java.util.HashMap;

public enum SocialInsLevyMonAtr {
	PREVIEW2_MONTH(-2), PREVIEW_MONTH(-1), CURRENT_MONTH(0), NEXT_MONTH(1), NEXT2_MONTH(2);
	public final int value;

	private static HashMap<Integer, SocialInsLevyMonAtr> map = new HashMap<>();

	static {
		for (SocialInsLevyMonAtr item : SocialInsLevyMonAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	SocialInsLevyMonAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum SocialInsLevyMonAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static SocialInsLevyMonAtr valueOf(int value) {
		return map.get(value);
	}

}
