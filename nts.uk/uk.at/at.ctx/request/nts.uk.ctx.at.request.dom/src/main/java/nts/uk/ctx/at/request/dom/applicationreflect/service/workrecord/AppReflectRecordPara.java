package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.WorkChangeCommonReflectPara;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;

@AllArgsConstructor
@Setter
@Getter
public class AppReflectRecordPara {
	/**
	 * 
	 */
	private ReflectRecordInfor recordInfor;
	/**
	 * 直行直帰申請
	 */
	private GobackReflectPara gobackInfor;
	/**
	 * 残業申請
	 */
	private OvertimeReflectPara overtimeInfor;
	/**
	 * 勤務変更
	 */
	private WorkChangeCommonReflectPara workchangeInfor;
	/**
	 * 休日出勤申請
	 */
	private HolidayWorkReflectPara holidayworkInfor;
	/**
	 * 休暇申請
	 */
	private WorkChangeCommonReflectPara absenceInfor;
	/**
	 * 振休申請
	 */
	private CommonReflectPara absenceLeaveAppInfor;
	/**
	 * 振出申請
	 */
	private CommonReflectPara recruitmentInfor;
	/**
	 * 実行種別
	 */
	private ExecutionTypeExImport execuTionType;

}
