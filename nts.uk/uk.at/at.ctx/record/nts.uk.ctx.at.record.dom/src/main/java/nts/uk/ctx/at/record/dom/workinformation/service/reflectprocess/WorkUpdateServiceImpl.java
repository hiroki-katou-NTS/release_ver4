package nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.enums.EditStateSetting;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class WorkUpdateServiceImpl implements ScheWorkUpdateService{
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private EditStateOfDailyPerformanceRepository dailyReposiroty;
	@Inject
	private AttendanceTimeRepository attendanceTime;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDaily;
	@Override
	public void updateWorkTimeType(ReflectParameter para, boolean scheUpdate) {
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = workRepository.find(para.getEmployeeId(), para.getDateData()).get();
		WorkInformation workInfor = new WorkInformation(para.getWorkTimeCode(), para.getWorkTypeCode());
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(1);
			lstItem.add(2);
			dailyPerfor.setScheduleWorkInformation(workInfor);
			workRepository.updateByKeyFlush(dailyPerfor);
		} else {
			lstItem.add(28);
			lstItem.add(29);
			dailyPerfor.setRecordWorkInformation(workInfor);
			workRepository.updateByKeyFlush(dailyPerfor);
		}
		
		//日別実績の編集状態
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		
	}
	/**
	 * 日別実績の編集状態
	 * @param employeeId
	 * @param dateData
	 * @param lstItem
	 */
	private void updateEditStateOfDailyPerformance(String employeeId, GeneralDate dateData, List<Integer> lstItem) {
		List<EditStateOfDailyPerformance> lstDaily = new ArrayList<>();
		lstItem.stream().forEach(z -> {
			Optional<EditStateOfDailyPerformance> optItemData = dailyReposiroty.findByKeyId(employeeId, dateData, z);
			if(optItemData.isPresent()) {
				EditStateOfDailyPerformance itemData = optItemData.get();
				EditStateOfDailyPerformance data = new EditStateOfDailyPerformance(itemData.getEmployeeId(), 
						itemData.getAttendanceItemId(), itemData.getYmd(), 
						EditStateSetting.REFLECT_APPLICATION);
				lstDaily.add(data);
			}else {
				EditStateOfDailyPerformance insertData = new EditStateOfDailyPerformance(employeeId, z, dateData, EditStateSetting.REFLECT_APPLICATION);
				lstDaily.add(insertData);
			}
		});
		
		if(!lstDaily.isEmpty()) {
			dailyReposiroty.updateByKeyFlush(lstDaily);
		}
	}
	
	
	@Override
	public void updateStartTimeOfReflect(TimeReflectParameter para) {
		
		//日別実績の勤務情報		
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(para.getEmployeeId(), para.getDateData());
		if(!optDailyPerfor.isPresent()) {
			return;
		}
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		if(dailyPerfor.getScheduleTimeSheets().isEmpty()) {
			return;
		}
		List<ScheduleTimeSheet> lstTimeSheetFrameNo = dailyPerfor.getScheduleTimeSheets().stream()
				.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
		if(lstTimeSheetFrameNo.isEmpty()) {
			return;
		}
		ScheduleTimeSheet timeSheetFrameNo = lstTimeSheetFrameNo.get(0);
		ScheduleTimeSheet timeSheet;
		if(para.isPreCheck()) {
			timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(), 
					para.getTime(), 
					timeSheetFrameNo.getLeaveWork().v());
		} else {
			timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(),
					timeSheetFrameNo.getAttendance().v(),
					para.getTime());
		}
		
		lstTimeSheetFrameNo.add(para.getFrameNo(), timeSheet); //can xac nhan lai
		dailyPerfor.setScheduleTimeSheets(lstTimeSheetFrameNo);
		workRepository.updateByKeyFlush(dailyPerfor);
		
		
		//日別実績の編集状態
		//予定開始時刻の項目ID
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.isPreCheck()) {
			lstItem.add(3);
			lstItem.add(5);	
		} else {
			lstItem.add(4);
			lstItem.add(6);
		}
		
		//TODO add lstItem
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		
	}
	@Override
	public void updateReflectStartEndTime(TimeReflectParameter para) {
		//開始時刻を反映する 
		Optional<TimeLeavingOfDailyPerformance> optTimeLeavingOfDaily = timeLeavingOfDaily.findByKey(para.getEmployeeId(), para.getDateData());
		if(!optTimeLeavingOfDaily.isPresent()) {
			return;
		}
		TimeLeavingOfDailyPerformance timeLeavingOfDailyData = optTimeLeavingOfDaily.get();
		List<TimeLeavingWork> lstTimeLeavingWorks = timeLeavingOfDailyData.getTimeLeavingWorks().stream()
				.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
		if(lstTimeLeavingWorks.isEmpty()) {
			return;
		}
		TimeLeavingWork timeLeavingWork = lstTimeLeavingWorks.get(0);
		Optional<TimeActualStamp> optTimeAttendance;
		if(para.isPreCheck()) {
			optTimeAttendance = timeLeavingWork.getAttendanceStamp();	
		}else {
			optTimeAttendance = timeLeavingWork.getLeaveStamp();
		}
		
		if(!optTimeAttendance.isPresent()) {
			return;
		}
		TimeActualStamp timeAttendance = optTimeAttendance.get();
		Optional<WorkStamp> optStamp = timeAttendance.getStamp();
		if(!optStamp.isPresent()) {
			return;
		}
		WorkStamp stamp = optStamp.get();
		WorkStamp stampTmp = new WorkStamp(stamp.getAfterRoundingTime(), 
				new TimeWithDayAttr(para.getTime()),
				stamp.getLocationCode().isPresent() ? stamp.getLocationCode().get() : null,
				stamp.getStampSourceInfo());
		TimeActualStamp timeActualStam = new TimeActualStamp(timeAttendance.getActualStamp().isPresent() ? timeAttendance.getActualStamp().get() : null,
				stampTmp,
				timeAttendance.getNumberOfReflectionStamp());
		TimeLeavingWork timeLeavingWorkTmp;
		if(para.isPreCheck()) {
			timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(), 
					Optional.of(timeActualStam), 
					timeLeavingWork.getLeaveStamp());
		} else {
			timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(), 
					timeLeavingWork.getAttendanceStamp(),
					Optional.of(timeActualStam));
		}
		List<TimeLeavingWork> lstTimeLeavingWorksTmp = new ArrayList<>();
		lstTimeLeavingWorksTmp.add(timeLeavingWorkTmp);
		TimeLeavingOfDailyPerformance tmpData = new TimeLeavingOfDailyPerformance(para.getEmployeeId(), timeLeavingOfDailyData.getWorkTimes(), lstTimeLeavingWorksTmp, para.getDateData());
		timeLeavingOfDaily.updateFlush(tmpData);
		//開始時刻の編集状態を更新する		
		//予定項目ID=出勤の項目ID	
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.isPreCheck()) {
			lstItem.add(31);
			//lstItem.add(34);	
		} else {
			lstItem.add(34);
			//lstItem.add(44);
		}
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		
	}
	/**
	 * 予定項目ID=出勤の項目ID
	 * @return
	 */
	private List<Integer> lstWorkOfItem(){
		List<Integer> lstItem = new ArrayList<Integer>();
		lstItem.add(3);
		lstItem.add(4);
		lstItem.add(5);
		lstItem.add(6);
		return lstItem;
	}
	@Override
	public void reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime) {
		//残業時間を反映する
		//残業枠時間
		Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = attendanceTime.find(employeeId, dateData);
		if(!optAttendanceTime.isPresent()) {
			return;
		}
		AttendanceTimeOfDailyPerformance attendanceTimeData = optAttendanceTime.get();
		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		
		Optional<OverTimeOfDaily> optOverTimeOfDaily = excessOfStatutory.getOverTimeWork();
		if(!optOverTimeOfDaily.isPresent()) {
			return;
		}
		OverTimeOfDaily overTimeOfDaily = optOverTimeOfDaily.get();
		List<OverTimeFrameTime> lstOverTimeWorkFrameTime = overTimeOfDaily.getOverTimeWorkFrameTime();
		if(lstOverTimeWorkFrameTime.isEmpty()) {
			return;
		}
		lstOverTimeWorkFrameTime.stream().forEach(x -> {
			if(mapOvertime.containsKey(x.getOverWorkFrameNo().v())) {				
				//x.getBeforeApplicationTime().setTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
				x.setBeforeApplicationTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
			}
		});

		attendanceTime.updateFlush(attendanceTimeData);		
		//残業時間の編集状態を更新する
		//日別実績の編集状態  予定項目ID=残業時間(枠番)の項目ID
		List<Integer> lstOverTemp = this.lstOvertimeItem();
		for(int i = 1; i <= 10; i++) {
			if(!mapOvertime.containsKey(i)) {
				Integer item = this.lstOvertimeItem().get(i - 1); 
				lstOverTemp.remove(item);
			}
		}
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstOverTemp);
	}
	/**
	 * 予定項目ID=残業時間(枠番)の項目ID
	 * @return
	 */
	private List<Integer> lstOvertimeItem(){
		List<Integer> lstItem = new ArrayList<Integer>();
		lstItem.add(220);
		lstItem.add(225);
		lstItem.add(230);
		lstItem.add(235);
		lstItem.add(240);
		lstItem.add(245);
		lstItem.add(250);
		lstItem.add(255);
		lstItem.add(260);
		lstItem.add(265);
		return lstItem;		
	}
	
	@Override
	public void updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight) {
		// 所定外深夜時間を反映する
		Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = attendanceTime.find(employeeId, dateData);
		if(!optAttendanceTime.isPresent()) {
			return;
		}
		AttendanceTimeOfDailyPerformance attendanceTimeData = optAttendanceTime.get();
		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		ExcessOfStatutoryMidNightTime exMidNightTime = excessOfStatutory.getExcessOfStatutoryMidNightTime();
		ExcessOfStatutoryMidNightTime tmp = new ExcessOfStatutoryMidNightTime(exMidNightTime.getTime(), new AttendanceTime(timeNight));
		excessOfStatutory.setExcessOfStatutoryMidNightTime(tmp);
		attendanceTime.updateFlush(attendanceTimeData);
		//所定外深夜時間の編集状態を更新する
		List<Integer> lstNightItem = new ArrayList<Integer>();//所定外深夜時間の項目ID
		lstNightItem.add(565);		
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstNightItem);
		
		//休出時間(深夜)の反映
		//this.updateBreakNight(employeeId, dateData);
	}
	@Override
	public void updateBreakNight(String employeeId, GeneralDate dateData) {
		// 所定外深夜時間を反映する
		Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = attendanceTime.find(employeeId, dateData);
		if(!optAttendanceTime.isPresent()) {
			return;
		}
		AttendanceTimeOfDailyPerformance attendanceTimeData = optAttendanceTime.get();
		ActualWorkingTimeOfDaily actualWorkingTimeOfDaily = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();		
		ExcessOfStatutoryTimeOfDaily excessOfStatutoryTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		Optional<HolidayWorkTimeOfDaily> optWorkHolidayTime = excessOfStatutoryTimeOfDaily.getWorkHolidayTime();
		if(!optWorkHolidayTime.isPresent()) {
			return;
		}
		HolidayWorkTimeOfDaily workHolidayTime = optWorkHolidayTime.get();
		Finally<HolidayMidnightWork> holidayMidNightWork = workHolidayTime.getHolidayMidNightWork();
		if(!holidayMidNightWork.isPresent()) {
			return;
		}
		HolidayMidnightWork holidayWorkMidNightTime = holidayMidNightWork.get();
		List<HolidayWorkMidNightTime> lstHolidayWorkMidNightTime = holidayWorkMidNightTime.getHolidayWorkMidNightTime();
		if(lstHolidayWorkMidNightTime.isEmpty()) {
			return;
		}
		lstHolidayWorkMidNightTime.stream().forEach(x -> {
			x.getTime().setTime(new AttendanceTime(0));
		});
		
		attendanceTime.updateFlush(attendanceTimeData);
		//休出時間(深夜)(法内)の編集状態を更新する
		List<Integer> lstItem = new ArrayList<Integer>();
		//(法定区分=法定外休出)の時間の項目ID ???
		//(法定区分=祝日休出)の時間の項目ID
		//(法定区分=法定外休出)の時間の項目ID
		lstItem.add(570);
		lstItem.add(567);
		lstItem.add(572);
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);		
	}
	@Override
	public void updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime) {
		Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = attendanceTime.find(employeeId, dateData);
		if(!optAttendanceTime.isPresent()) {
			return;
		}
		AttendanceTimeOfDailyPerformance attendanceTimeData = optAttendanceTime.get();
		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();		
		//ドメインモデル「日別実績の所定外時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		// ドメインモデル「日別実績の残業時間」を取得する
		Optional<OverTimeOfDaily> optOverTimeOfDaily = excessOfStatutory.getOverTimeWork();
		if(!optOverTimeOfDaily.isPresent()) {
			return;
		}
		OverTimeOfDaily workHolidayTime = optOverTimeOfDaily.get();
		FlexTime flexTimeData = workHolidayTime.getFlexTime();
		FlexTime temp = new FlexTime(flexTimeData.getFlexTime(), new AttendanceTime(flexTime));
		workHolidayTime.setFlexTime(temp);		
		attendanceTime.updateFlush(attendanceTimeData);
		//フレックス時間の編集状態を更新する
		//日別実績の編集状態
		List<Integer> lstItem = new ArrayList<Integer>();//フレックス時間の項目ID
		lstItem.add(555);
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		
	}

}
