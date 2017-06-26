package nts.uk.ctx.at.shared.dom.attendance;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AttendanceItemType {
	/* 人件費*/
	Cost(0),
	/* 乖離時間*/
	DivergenceTime(1);
	public final int value;
}
