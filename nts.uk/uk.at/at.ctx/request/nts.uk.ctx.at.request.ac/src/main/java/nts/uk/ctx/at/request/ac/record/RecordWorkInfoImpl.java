package nts.uk.ctx.at.request.ac.record;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.workinformation.CommonTimeSheet;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPub;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPubExport;
import nts.uk.ctx.at.request.dom.application.common.adapter.frame.OvertimeInputCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class RecordWorkInfoImpl implements RecordWorkInfoAdapter {
	
	@Inject
	private RecordWorkInfoPub recordWorkInfoPub;
	
	@Override
	public RecordWorkInfoImport getRecordWorkInfo(String employeeId, GeneralDate ymd) {
		List<OvertimeInputCaculation> headCaculation = new ArrayList<>();
		List<OvertimeInputCaculation> transfer = new ArrayList<>();
		RecordWorkInfoPubExport recordWorkInfo = recordWorkInfoPub.getRecordWorkInfo(employeeId, ymd);
		List<OvertimeInputCaculation> overtimeCaculations = this.converTimeCaculation(recordWorkInfo.getOvertimes(), AttendanceType.NORMALOVERTIME.value);
		List<OvertimeInputCaculation> holiday = this.converTimeCaculation(recordWorkInfo.getHolidayWorks(), AttendanceType.BREAKTIME.value);
		return new RecordWorkInfoImport(
				recordWorkInfo.getWorkTypeCode(), 
				recordWorkInfo.getWorkTimeCode(), 
				recordWorkInfo.getAttendanceStampTimeFirst(), 
				recordWorkInfo.getLeaveStampTimeFirst(), 
				recordWorkInfo.getAttendanceStampTimeSecond(), 
				recordWorkInfo.getLeaveStampTimeSecond(), 
				recordWorkInfo.getLateTime1(), 
				recordWorkInfo.getLeaveEarlyTime1(), 
				recordWorkInfo.getLateTime2(), 
				recordWorkInfo.getLeaveEarlyTime2(), 
				recordWorkInfo.getChildCareTime(),
				recordWorkInfo.getFlexTime(),
				overtimeCaculations,
				headCaculation,
				holiday,
				transfer,
				0,
				0);
	}
	private List<OvertimeInputCaculation> converTimeCaculation(List<CommonTimeSheet> lstTime, int attendanceType){
		List<OvertimeInputCaculation> result = new ArrayList<>();
		for (CommonTimeSheet time : lstTime) {
			result.add(new OvertimeInputCaculation(attendanceType, time.getNo(), time.getTime()));
		}
		return result;
	}
	
}
