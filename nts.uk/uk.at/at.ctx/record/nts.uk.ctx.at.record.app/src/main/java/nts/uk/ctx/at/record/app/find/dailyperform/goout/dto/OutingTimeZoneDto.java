package nts.uk.ctx.at.record.app.find.dailyperform.goout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutingTimeZoneDto implements ItemConst{

	private Integer no;

	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = GO_OUT)
	private WithActualTimeStampDto outing;

	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = BACK)
	private WithActualTimeStampDto comeBack;

	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = REASON)
	@AttendanceItemValue(type = ValueType.INTEGER)
	private int reason;
	
	private int outTimeCalc;
	
	private int outTIme;
}
