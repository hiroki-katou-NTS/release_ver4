package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.algorithmdailyper;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;

@Getter
@Setter
@NoArgsConstructor
public class CheckAttendanceHolidayOutPut {
	
	/**
	 * 出勤扱い : true
	 * 休日扱い : false
	 */
	private boolean isAtWork;
	
	private List<ErrMessageInfo> errMesInfos;

}
