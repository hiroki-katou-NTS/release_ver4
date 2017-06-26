package nts.uk.ctx.at.shared.dom.attendance;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AttendanceAtr {
	/* 時間*/
	Time(0),
	/* 時刻*/
	TimeOfDay(1),
	/* 回数*/
	NumberOfTime(2),
	/* 区分*/
	Attribute(3),
	/* ｺｰﾄﾞ*/
	Code(4),
	/* 金額*/
	Money(5);
	public final int value;
}
