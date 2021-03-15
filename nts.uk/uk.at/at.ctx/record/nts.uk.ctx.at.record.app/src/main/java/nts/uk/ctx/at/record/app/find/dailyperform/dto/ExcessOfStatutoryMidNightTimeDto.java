package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;

/** 所定外深夜時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcessOfStatutoryMidNightTimeDto implements ItemConst {

	/** 時間: 計算付き時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME)
	private CalcAttachTimeDto time;

	/** 事前申請時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = BEFOR_APPLICATION)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer beforeApplicationTime;
	
	@Override
	public ExcessOfStatutoryMidNightTimeDto clone() {
		return new ExcessOfStatutoryMidNightTimeDto(time == null ? null : time.clone(), beforeApplicationTime);
	}
	
	public static ExcessOfStatutoryMidNightTime defaultDomain() {
		return new ExcessOfStatutoryMidNightTime(TimeDivergenceWithCalculation.defaultValue(), AttendanceTime.ZERO);
	}
}
