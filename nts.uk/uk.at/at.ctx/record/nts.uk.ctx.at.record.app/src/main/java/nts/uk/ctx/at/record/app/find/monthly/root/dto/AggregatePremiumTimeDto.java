package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.verticaltotal.worktime.premiumtime.AggregatePremiumTime;

@Data
/** 集計割増時間 */
@NoArgsConstructor
@AllArgsConstructor
public class AggregatePremiumTimeDto implements ItemConst {

	/** 割増時間項目NO: 割増時間項目NO */
	private int no;

	/** 時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = TIME, layout = LAYOUT_A)
	private int time;
	
	public static AggregatePremiumTimeDto from (AggregatePremiumTime domain) {
		AggregatePremiumTimeDto dto = new AggregatePremiumTimeDto();
		if(domain != null) {
			dto.setNo(domain.getPremiumTimeItemNo());
			dto.setTime(domain.getTime() == null ? 0 : domain.getTime().valueAsMinutes());
		}
		return dto;
	}

	public AggregatePremiumTime toDomain(){
		return AggregatePremiumTime.of(no, new AttendanceTimeMonth(time));
	}
}
