package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;

@Stateless
public class AfterOvertimeReflectServiceImpl implements AfterOvertimeReflectService {
	@Inject
	private AfterOvertimeReflectProcess afterOverTimeReflect;
	@Inject
	private PreOvertimeReflectProcess preAfterOvertimeReflectProcess;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkUpdateService scheWorkUpdate;
	@Inject
	private AttendanceTimeRepository attendanceTime;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public void reflectAfterOvertime(OvertimeParameter overtimePara) {
			/*WorkInfoOfDailyPerformance dailyInfor = workRepository.find(overtimePara.getEmployeeId(), overtimePara.getDateInfo()).get();
			//予定勤種・就時の反映
			dailyInfor = afterOverTimeReflect.checkScheReflect(overtimePara, dailyInfor);
			//勤種・就時の反映
			AppReflectRecordWork isWorkReflect = preAfterOvertimeReflectProcess.changeFlg(overtimePara, dailyInfor);
			//予定勤種・就時反映後の予定勤種・就時を取得する
			//勤種・就時反映後の予定勤種・就時を取得する
			dailyInfor = isWorkReflect.getDailyInfo();
			//予定開始終了時刻の反映
			WorkTimeTypeOutput workTimeTypeScheData = new WorkTimeTypeOutput(dailyInfor.getScheduleInfo().getWorkTimeCode().v(),
					dailyInfor.getScheduleInfo().getWorkTypeCode().v());		
			dailyInfor = afterOverTimeReflect.checkScheWorkStarEndReflect(overtimePara, isWorkReflect.chkReflect, workTimeTypeScheData, dailyInfor);
			//日別実績の勤務情報  変更
			workRepository.updateByKeyFlush(dailyInfor);
			
			//開始終了時刻の反映
			WorkTimeTypeOutput workTimeTypeRecordData = new WorkTimeTypeOutput(dailyInfor.getRecordInfo().getWorkTimeCode().v(), 
					dailyInfor.getRecordInfo().getWorkTypeCode().v());
			afterOverTimeReflect.recordStartEndReflect(overtimePara, workTimeTypeRecordData);
		
			Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = attendanceTime.find(overtimePara.getEmployeeId(), overtimePara.getDateInfo());
			if(optAttendanceTime.isPresent()) {
				AttendanceTimeOfDailyPerformance attendanceTimeData = optAttendanceTime.get();
				//残業時間の反映
				attendanceTimeData = afterOverTimeReflect.reflectOvertimeFrame(overtimePara, attendanceTimeData);
				//所定外深夜時間の反映
				attendanceTimeData = afterOverTimeReflect.reflectTimeShiftNight(overtimePara.getEmployeeId(), overtimePara.getDateInfo(), 
						overtimePara.getOvertimePara().getOverTimeShiftNight(), attendanceTimeData);
				//フレックス時間の反映
				//INPUT．フレックス時間をチェックする
				if(overtimePara.getOvertimePara().getFlexExessTime() > 0) {
					attendanceTimeData = scheWorkUpdate.updateFlexTime(overtimePara.getEmployeeId(), overtimePara.getDateInfo(), 
							overtimePara.getOvertimePara().getFlexExessTime(), false, attendanceTimeData);
				}
				attendanceTime.updateFlush(attendanceTimeData);
			}			
			
			//日別実績の修正からの計算
			//○日別実績を置き換える Replace daily performance		
			commonService.calculateOfAppReflect(null, overtimePara.getEmployeeId(), overtimePara.getDateInfo(), false);
		*/
	}
}
