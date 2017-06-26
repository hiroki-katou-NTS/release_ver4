package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday;

import java.util.HashMap;

public enum AccountDueMonAtr {
	PREVIEW_MONTH(-1), CURRENT_MONTH(0);

	public final int value;

	private static HashMap<Integer, AccountDueMonAtr> map = new HashMap<>();

	static {
		for (AccountDueMonAtr item : AccountDueMonAtr.values()) {
			map.put(item.value, item);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param 賃金対象区分の値
	 */
	AccountDueMonAtr(int value) {
		this.value = value;
	}

	/**
	 * convert to enum AccountDueMonAtr by value
	 * 
	 * @param value
	 * @return
	 */
	public static AccountDueMonAtr valueOf(int value) {
		return map.get(value);
	}
}
