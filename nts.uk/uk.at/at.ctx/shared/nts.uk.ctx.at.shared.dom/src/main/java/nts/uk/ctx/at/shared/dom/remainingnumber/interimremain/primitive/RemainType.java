package nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive;

import lombok.AllArgsConstructor;

/**
 * 残数種類
 * @author do_dt
 *
 */
@AllArgsConstructor
public enum RemainType {
	/**	年休 */
	ANNUAL(0,"年休"),
	/**	積立年休 */
	FUNDINGANNUAL(1,"積立年休"),
	/**	特休 */
	SPECIAL(2,"特休"),
	/**	振休 */
	PAUSE(3,"振休"),
	/**	代休 */
	SUBHOLIDAY(4,"代休"),
	/**	時間年休 */
	TIMEANNUAL(5, "時間年休"),
	/**
	 * 振出
	 */
	PICKINGUP(6,"振出"),
	
	/** 休出. */	
	BREAK(7,"休出"),

	/** 60H超休 */
	SIXTY_OVER_BREAK(8, "60H超休"),

	/** 公休 */
	CLOSED(9, "公休"),

	/** The nursing child. */
	NURSING_CHILD(10, "子の看護"),

	/** The nursing. */
	NURSING(11, "介護");
	
	public final Integer value;
	
	public final String name;

}
