package nts.uk.ctx.at.shared.dom.specialholiday;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AgeBaseYearAtr {
	/* 翌月 */
	NEXT_MONTH(0),
	/* 当月 */
	THIS_MONTH(1);

	public final int value;
}
