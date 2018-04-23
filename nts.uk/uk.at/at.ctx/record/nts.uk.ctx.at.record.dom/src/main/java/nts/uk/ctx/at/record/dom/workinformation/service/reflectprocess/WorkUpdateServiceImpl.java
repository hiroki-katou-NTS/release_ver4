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
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
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
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
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
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(para.getEmployeeId(), para.getDateData()); 
		if(!optDailyPerfor.isPresent()) {
			return;
		}
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		WorkInformation workInfor = new WorkInformation(para.getWorkTimeCode(), para.getWorkTypeCode());
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(1);
			lstItem.add(2);
			dailyPerfor.setScheduleInfo(workInfor);
			workRepository.updateByKeyFlush(dailyPerfor);
		} else {
			lstItem.add(28);
			lstItem.add(29);
			dailyPerfor.setRecordInfo(workInfor);
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
	
	/*private List<EditStateOfDailyPerformance> updateEditStateOfDailyPerHoliday(String employeeId, 
			GeneralDate dateData, 
			List<Integer> lstItem, List<EditStateOfDailyPerformance> lstDaily){
		lstItem.stream().forEach(z -> {	
			boolean ischeck = false;
			for(EditStateOfDailyPerformance item : lstDaily) {
				if(item.getAttendanceItemId() == z) {
					EditStateOfDailyPerformance itemTmp = new EditStateOfDailyPerformance(employeeId, 
							z, 
							dateData, 
							EditStateSetting.REFLECT_APPLICATION);
					lstDaily.remove(item);
					lstDaily.add(itemTmp);
					ischeck = true;
					continue;
				}
			}
			if(!ischeck) {
				EditStateOfDailyPerformance insertData = new EditStateOfDailyPerformance(employeeId, z, dateData, EditStateSetting.REFLECT_APPLICATION);
				lstDaily.add(insertData);
			}
		});
		if(!lstDaily.isEmpty()) {
			dailyReposiroty.updateByKeyFlush(lstDaily);
		}
		return lstDaily;
	}*/
	
	@Override
	public void updateScheStartEndTime(TimeReflectPara para) {
		if(para.getStartTime() == null
				|| para.getEndTime() == null) {
			return;
		}
		//日別実績の勤務情報
		Optional<WorkInfoOfDailyPerformance> optDailyPerfor = workRepository.find(para.getEmployeeId(), para.getDateData());
		if(!optDailyPerfor.isPresent()) {
			return;
		}
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = optDailyPerfor.get();
		ScheduleTimeSheet timeSheet;
		if(dailyPerfor.getScheduleTimeSheets().isEmpty()) {
			timeSheet = new ScheduleTimeSheet(1, 
					para.getStartTime(),
					para.getEndTime());
		} else {
			List<ScheduleTimeSheet> lstTimeSheetFrameNo = dailyPerfor.getScheduleTimeSheets().stream()
					.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
			if(lstTimeSheetFrameNo.isEmpty()) {
				return;
			}
			ScheduleTimeSheet timeSheetFrameNo = lstTimeSheetFrameNo.get(0);
			
			timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(), 
					para.isStart() ? para.getStartTime() : timeSheetFrameNo.getAttendance().v(),
					para.isEnd() ? para.getEndTime() : timeSheetFrameNo.getLeaveWork().v());

			dailyPerfor.getScheduleTimeSheets().remove(timeSheetFrameNo);
		}
		
		dailyPerfor.getScheduleTimeSheets().add(timeSheet);
		workRepository.updateByKeyFlush(dailyPerfor);
		
		
		//日別実績の編集状態
		//予定開始時刻の項目ID
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.getFrameNo() == 1) {
			if(para.isStart()) {
				lstItem.add(3);	
			}
			if(para.isEnd()) {
				lstItem.add(4);	
			}
		} else {
			if(para.isStart()) {
				lstItem.add(5);	
			}
			if(para.isEnd()) {
				lstItem.add(6);	
			}
		}
		//TODO add lstItem
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		
	}
	@Override
	public void updateRecordStartEndTime(TimeReflectParameter para) {
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
					timeActualStam, 
					timeLeavingWork.getLeaveStamp().isPresent() ? timeLeavingWork.getLeaveStamp().get() : null);
		} else {
			timeLeavingWorkTmp = new TimeLeavingWork(timeLeavingWork.getWorkNo(), 
					timeLeavingWork.getAttendanceStamp().isPresent() ? timeLeavingWork.getAttendanceStamp().get() : null,
					timeActualStam);
		}
		List<TimeLeavingWork> lstTimeLeavingWorksTmp = new ArrayList<>();
		lstTimeLeavingWorksTmp.add(timeLeavingWorkTmp);
		TimeLeavingOfDailyPerformance tmpData = new TimeLeavingOfDailyPerformance(para.getEmployeeId(), timeLeavingOfDailyData.getWorkTimes(), lstTimeLeavingWorksTmp, para.getDateData());
		timeLeavingOfDaily.updateFlush(tmpData);
		//開始時刻の編集状態を更新する		
		//予定項目ID=出勤の項目ID	
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.isPreCheck()) {
			if(para.getFrameNo() == 1) {
				lstItem.add(31);	
			} else {
				lstItem.add(41);
			}
		} else {
			if(para.getFrameNo() == 1) {
				lstItem.add(34);	
			} else {
				lstItem.add(44);
			}
		}
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		
	}

	@Override
	public void reflectOffOvertime(String employeeId, GeneralDate dateData, Map<Integer, Integer> mapOvertime, boolean isPre) {
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
		if(isPre) {			
			lstOverTimeWorkFrameTime.stream().forEach(x -> {
				if(mapOvertime.containsKey(x.getOverWorkFrameNo().v())) {
					x.setBeforeApplicationTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
				}
			});	
		} else {
			lstOverTimeWorkFrameTime.stream().forEach(x -> {
				if(mapOvertime.containsKey(x.getOverWorkFrameNo().v())) {
					x.getOverTimeWork().setTime(new AttendanceTime(mapOvertime.get(x.getOverWorkFrameNo().v())));
				}
			});
		}
		

		attendanceTime.updateFlush(attendanceTimeData);
		//残業時間の編集状態を更新する
		//日別実績の編集状態  予定項目ID=残業時間(枠番)の項目ID
		List<Integer> lstOverTemp = new ArrayList<>();
		if(isPre) {
			lstOverTemp = this.lstPreOvertimeItem();
			for(int i = 1; i <= 10; i++) {
				if(!mapOvertime.containsKey(i)) {
					Integer item = this.lstPreOvertimeItem().get(i - 1); 
					lstOverTemp.remove(item);
				}
			}	
		} else {
			lstOverTemp = this.lstAfterOvertimeItem();
			for(int i = 1; i <= 10; i++) {
				if(!mapOvertime.containsKey(i)) {
					Integer item = this.lstAfterOvertimeItem().get(i - 1); 
					lstOverTemp.remove(item);
				}
			}	
		}
		
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstOverTemp);
	}
	/**
	 * 予定項目ID=残業時間(枠番)の項目ID: 事前申請
	 * @return
	 */
	private List<Integer> lstPreOvertimeItem(){
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
	/**
	 * 予定項目ID=残業時間(枠番)の項目ID: 事後申請
	 * @return
	 */
	private List<Integer> lstAfterOvertimeItem(){
		List<Integer> lstItem = new ArrayList<Integer>();
		lstItem.add(216);
		lstItem.add(221);
		lstItem.add(226);
		lstItem.add(231);
		lstItem.add(236);
		lstItem.add(241);
		lstItem.add(245);
		lstItem.add(251);
		lstItem.add(256);
		lstItem.add(261);
		return lstItem;		
	}
	
	@Override
	public void updateTimeShiftNight(String employeeId, GeneralDate dateData, Integer timeNight, boolean isPre) {
		if(timeNight < 0) {
			return;
		}
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
		if(isPre) {
			excessOfStatutory.setExcessOfStatutoryMidNightTime(tmp);
		} else {
			excessOfStatutory.getExcessOfStatutoryMidNightTime().getTime().setTime(new AttendanceTime(timeNight));
		}
		attendanceTime.updateFlush(attendanceTimeData);
		//所定外深夜時間の編集状態を更新する
		List<Integer> lstNightItem = new ArrayList<Integer>();//所定外深夜時間の項目ID
		if(isPre) {
			lstNightItem.add(565);
		} else {
			lstNightItem.add(563);
		}
				
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
	public void updateFlexTime(String employeeId, GeneralDate dateData, Integer flexTime, boolean isPre) {
		if(flexTime < 0) {
			return;
		}
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
		if(isPre) {
			workHolidayTime.setFlexTime(temp);	
		} else {
			workHolidayTime.getFlexTime().getFlexTime().setTime(new AttendanceTimeOfExistMinus(flexTime));
		}
				
		attendanceTime.updateFlush(attendanceTimeData);
		//フレックス時間の編集状態を更新する
		//日別実績の編集状態
		List<Integer> lstItem = new ArrayList<Integer>();//フレックス時間の項目ID
		if(isPre) {
			lstItem.add(555);	
		} else {
			lstItem.add(556);
		}
		
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
		
	}
	@Override
	public void updateRecordWorkType(String employeeId, GeneralDate dateData, String workTypeCode, boolean scheUpdate) {
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = workRepository.find(employeeId, dateData).get();
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(1);
			dailyPerfor.setScheduleInfo(new WorkInformation(dailyPerfor.getScheduleInfo().getWorkTimeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTimeCode().v(), workTypeCode));
			workRepository.updateByKeyFlush(dailyPerfor);
		} else {
			lstItem.add(28);
			dailyPerfor.setRecordInfo(new WorkInformation(dailyPerfor.getRecordInfo().getWorkTimeCode() == null ? null : dailyPerfor.getScheduleInfo().getWorkTimeCode().v(), workTypeCode));
			workRepository.updateByKeyFlush(dailyPerfor);
		}
		//日別実績の編集状態
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstItem);
	}
	@Override
	public IntegrationOfDaily updateWorkTimeFrame(String employeeId, GeneralDate dateData, Map<Integer, Integer> worktimeFrame,
			boolean isPre, IntegrationOfDaily dailyData) {
		AttendanceTimeOfDailyPerformance attendanceTimeData = dailyData.getAttendanceTimeOfDailyPerformance().get();
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();		
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		//日別実績の休出時間
		Optional<HolidayWorkTimeOfDaily> optWorkHolidayTime = excessOfStatutory.getWorkHolidayTime();
		if(!optWorkHolidayTime.isPresent()) {
			return dailyData;
		}
		HolidayWorkTimeOfDaily workHolidayTime = optWorkHolidayTime.get();
		List<HolidayWorkFrameTime> lstHolidayWorkFrameTime = workHolidayTime.getHolidayWorkFrameTime();
		if(lstHolidayWorkFrameTime.isEmpty()) {
			return dailyData;
		}
		if(isPre) {			
			lstHolidayWorkFrameTime.stream().forEach(x -> {
				if(worktimeFrame.containsKey(x.getHolidayFrameNo().v())) {
					AttendanceTime worktimeTmp = new AttendanceTime(worktimeFrame.get(x.getHolidayFrameNo().v()));
					x.setBeforeApplicationTime(Finally.of(worktimeTmp));
				}
			});	
		} else {
			lstHolidayWorkFrameTime.stream().forEach(x -> {
				if(worktimeFrame.containsKey(x.getHolidayFrameNo().v())) {
					Finally<TimeDivergenceWithCalculation> holidayWorkTime = x.getHolidayWorkTime();
					if(holidayWorkTime.isPresent()) {
						TimeDivergenceWithCalculation holidayWorkTimeData = holidayWorkTime.get();
						holidayWorkTimeData.setTime(new AttendanceTime(worktimeFrame.get(x.getHolidayFrameNo().v())));
					}
				}
			});
		}
		dailyData.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTimeData));
		attendanceTime.updateFlush(attendanceTimeData);
		List<Integer> lstWorktimeFrameTemp = new ArrayList<>();
		if(isPre) {
			lstWorktimeFrameTemp = this.lstPreWorktimeFrameItem();
			for(int i = 1; i <= 10; i++) {
				if(!worktimeFrame.containsKey(i)) {
					Integer item = this.lstPreWorktimeFrameItem().get(i - 1); 
					lstWorktimeFrameTemp.remove(item);
				}
			}	
		} else {
			lstWorktimeFrameTemp = this.lstAfterWorktimeFrameItem();
			for(int i = 1; i <= 10; i++) {
				if(!worktimeFrame.containsKey(i)) {
					Integer item = this.lstAfterWorktimeFrameItem().get(i - 1); 
					lstWorktimeFrameTemp.remove(item);
				}
			}	
		}
		
		//List<EditStateOfDailyPerformance> lstEdit = this.updateEditStateOfDailyPerHoliday(employeeId, dateData, lstWorktimeFrameTemp, dailyData.getEditState());
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstWorktimeFrameTemp);
		return dailyData;
	}
	
	private List<Integer> lstPreWorktimeFrameItem(){
		List<Integer> lstItem = new ArrayList<>();
		lstItem.add(270);
		lstItem.add(275);
		lstItem.add(280);
		lstItem.add(285);
		lstItem.add(290);
		lstItem.add(295);
		lstItem.add(300);
		lstItem.add(305);
		lstItem.add(310);
		lstItem.add(315);
		return lstItem;
	}
	private List<Integer> lstAfterWorktimeFrameItem(){
		List<Integer> lstItem = new ArrayList<>();
		lstItem.add(266);
		lstItem.add(271);
		lstItem.add(276);
		lstItem.add(281);
		lstItem.add(286);
		lstItem.add(291);
		lstItem.add(296);
		lstItem.add(301);
		lstItem.add(306);
		lstItem.add(311);
		return lstItem;
	}
	@Override
	public IntegrationOfDaily updateWorkTimeTypeHoliwork(ReflectParameter para, boolean scheUpdate,
			IntegrationOfDaily dailyData) {		
		WorkInfoOfDailyPerformance dailyPerfor = dailyData.getWorkInformation();
		WorkInformation workInfor = new WorkInformation(para.getWorkTimeCode(), para.getWorkTypeCode());
		List<Integer> lstItem = new ArrayList<>();
		if(scheUpdate) {
			lstItem.add(1);
			lstItem.add(2);
			dailyPerfor.setScheduleInfo(workInfor);
			dailyData.setWorkInformation(dailyPerfor);
			workRepository.updateByKeyFlush(dailyPerfor);
		} else {
			lstItem.add(28);
			lstItem.add(29);
			dailyPerfor.setRecordInfo(workInfor);
			dailyData.setWorkInformation(dailyPerfor);
			workRepository.updateByKeyFlush(dailyPerfor);
		}
		//日別実績の編集状態
		//this.updateEditStateOfDailyPerHoliday(para.getEmployeeId(), para.getDateData(), lstItem, dailyData.getEditState());		
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyData;
	}
	@Override
	public IntegrationOfDaily updateScheStartEndTimeHoliday(TimeReflectPara para, IntegrationOfDaily dailyData) {
		if(para.getStartTime() == null
				|| para.getEndTime() == null) {
			return dailyData;
		}		
		//日別実績の勤務情報
		WorkInfoOfDailyPerformance dailyPerfor = dailyData.getWorkInformation();
		ScheduleTimeSheet timeSheet;
		if(dailyPerfor.getScheduleTimeSheets().isEmpty()) {
			timeSheet = new ScheduleTimeSheet(1, 
					para.getStartTime(),
					para.getEndTime());
		} else {
			List<ScheduleTimeSheet> lstTimeSheetFrameNo = dailyPerfor.getScheduleTimeSheets().stream()
					.filter(x -> x.getWorkNo().v() == para.getFrameNo()).collect(Collectors.toList());
			if(lstTimeSheetFrameNo.isEmpty()) {
				return dailyData;
			}
			ScheduleTimeSheet timeSheetFrameNo = lstTimeSheetFrameNo.get(0);
			
			timeSheet = new ScheduleTimeSheet(timeSheetFrameNo.getWorkNo().v(), 
					para.isStart() ? para.getStartTime() : timeSheetFrameNo.getAttendance().v(),
					para.isEnd() ? para.getEndTime() : timeSheetFrameNo.getLeaveWork().v());

			dailyPerfor.getScheduleTimeSheets().remove(timeSheetFrameNo);
		}
		
		dailyPerfor.getScheduleTimeSheets().add(timeSheet);
		dailyData.setWorkInformation(dailyPerfor);
		workRepository.updateByKeyFlush(dailyPerfor);
		//日別実績の編集状態
		//予定開始時刻の項目ID
		List<Integer> lstItem = new ArrayList<Integer>();
		if(para.getFrameNo() == 1) {
			if(para.isStart()) {
				lstItem.add(3);	
			}
			if(para.isEnd()) {
				lstItem.add(4);	
			}
		} else {
			if(para.isStart()) {
				lstItem.add(5);	
			}
			if(para.isEnd()) {
				lstItem.add(6);	
			}
		}
		//this.updateEditStateOfDailyPerHoliday(para.getEmployeeId(), para.getDateData(), lstItem, dailyData.getEditState());
		this.updateEditStateOfDailyPerformance(para.getEmployeeId(), para.getDateData(), lstItem);
		return dailyData;
	}
	@Override
	public IntegrationOfDaily updateTimeShiftNightHoliday(String employeeId, GeneralDate dateData, Integer timeNight,
			boolean isPre, IntegrationOfDaily dailyData) {
		if(timeNight < 0) {
			return dailyData;
		}
		// 所定外深夜時間を反映する		
		AttendanceTimeOfDailyPerformance attendanceTimeData = dailyData.getAttendanceTimeOfDailyPerformance().get();		
		ActualWorkingTimeOfDaily actualWorkingTime = attendanceTimeData.getActualWorkingTimeOfDaily();
		TotalWorkingTime totalWorkingTime =  actualWorkingTime.getTotalWorkingTime();
		// ドメインモデル「日別実績の残業時間」を取得する
		ExcessOfStatutoryTimeOfDaily excessOfStatutory = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		ExcessOfStatutoryMidNightTime exMidNightTime = excessOfStatutory.getExcessOfStatutoryMidNightTime();
		ExcessOfStatutoryMidNightTime tmp = new ExcessOfStatutoryMidNightTime(exMidNightTime.getTime(), new AttendanceTime(timeNight));
		if(isPre) {
			excessOfStatutory.setExcessOfStatutoryMidNightTime(tmp);
		} else {
			excessOfStatutory.getExcessOfStatutoryMidNightTime().getTime().setTime(new AttendanceTime(timeNight));
		}
		dailyData.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTimeData));
		attendanceTime.updateFlush(attendanceTimeData);
		//所定外深夜時間の編集状態を更新する
		List<Integer> lstNightItem = new ArrayList<Integer>();//所定外深夜時間の項目ID
		if(isPre) {
			lstNightItem.add(565);
		} else {
			lstNightItem.add(563);
		}
				
		//this.updateEditStateOfDailyPerHoliday(employeeId, dateData, lstNightItem, dailyData.getEditState());
		this.updateEditStateOfDailyPerformance(employeeId, dateData, lstNightItem);
		return dailyData;
	}

}
