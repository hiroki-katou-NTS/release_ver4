package nts.uk.ctx.at.record.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.RecordRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.AppTimeType;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationTimeInfor;
@Stateless
public class RemainNumberCreateInformationImpl implements RemainNumberCreateInformation{

	@Override
	public List<RecordRemainCreateInfor> createRemainInfor(List<AttendanceTimeOfDailyPerformance> lstAttendanceTimeData,
			List<WorkInfoOfDailyPerformance> lstWorkInfor) {
		List<RecordRemainCreateInfor> lstOutputData = new ArrayList<>();
		//残数作成元情報を作成する
		lstWorkInfor.stream().forEach(workInfor -> {
			List<AttendanceTimeOfDailyPerformance> lstAttendance = lstAttendanceTimeData.stream()
					.filter(x -> x.getEmployeeId().equals(workInfor.getEmployeeId()) && x.getYmd().equals(workInfor.getYmd()))
					.collect(Collectors.toList());
			if(!lstAttendance.isEmpty()) {
				AttendanceTimeOfDailyPerformance attendanceInfor = lstAttendance.get(0);
				RecordRemainCreateInfor outPutData = this.remainDataFromRecord(workInfor, attendanceInfor);
				lstOutputData.add(outPutData);
			}
		});		
		return lstOutputData;
	}
	@Override
	public RecordRemainCreateInfor remainDataFromRecord(WorkInfoOfDailyPerformance workInfor, AttendanceTimeOfDailyPerformance attendanceInfor) {
		RecordRemainCreateInfor outputInfor = new RecordRemainCreateInfor();
		//残業振替時間の合計を算出する
		Optional<OverTimeOfDaily> overTimeWork = attendanceInfor.getActualWorkingTimeOfDaily()
				.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork();
		Integer overTimes = 0;
		if(overTimeWork.isPresent()) {
			List<OverTimeFrameTime> overTimeWorkFrameTime = overTimeWork.get().getOverTimeWorkFrameTime();
			for (OverTimeFrameTime overTimeFrameTime : overTimeWorkFrameTime) {
				overTimes += overTimeFrameTime.getTransferTime().getTime().v();
			}
		} 
		outputInfor.setTransferOvertimesTotal(overTimes);
		//休出振替時間の合計を算出する
		Optional<HolidayWorkTimeOfDaily> workHolidayTime = attendanceInfor.getActualWorkingTimeOfDaily()
				.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime();
		Integer transferTotal = 0;
		if(workHolidayTime.isPresent()) {
			List<HolidayWorkFrameTime> holidayWorkFrameTime = workHolidayTime.get().getHolidayWorkFrameTime();			
			for (HolidayWorkFrameTime holidayWork : holidayWorkFrameTime) {
				transferTotal += holidayWork.getTransferTime().isPresent() ? holidayWork.getTransferTime().get().getTime().v() : 0;
			}
		}
		outputInfor.setTransferTotal(transferTotal);
		//時間休暇使用情報を作成する
		outputInfor.setLstVacationTimeInfor(this.getLstVacationTimeInfor());
		outputInfor.setSid(workInfor.getEmployeeId());
		outputInfor.setYmd(workInfor.getYmd());
		outputInfor.setWorkTypeCode(workInfor.getRecordInfo().getWorkTypeCode() == null ? "" 
				: workInfor.getRecordInfo().getWorkTypeCode().v());
		outputInfor.setWorkTimeCode(Optional.of(workInfor.getRecordInfo().getSiftCode() == null ? "" 
				: workInfor.getRecordInfo().getSiftCode().v()));
		return outputInfor;
	}
	/**
	 * データ移送元不明瞭とりあえず作成するだけ
	 * @return
	 */
	private List<VacationTimeInfor> getLstVacationTimeInfor(){
		List<VacationTimeInfor> lstOutput = new ArrayList<>();
		VacationTimeInfor timeInfor = new VacationTimeInfor(0, AppTimeType.ATWORK, 0, 0);
		lstOutput.add(timeInfor);
		timeInfor = new VacationTimeInfor(0, AppTimeType.ATWORK2, 0, 0);
		lstOutput.add(timeInfor);
		timeInfor = new VacationTimeInfor(0, AppTimeType.OFFWORK, 0, 0);
		lstOutput.add(timeInfor);
		timeInfor = new VacationTimeInfor(0, AppTimeType.OFFWORK2, 0, 0);
		lstOutput.add(timeInfor);
		timeInfor = new VacationTimeInfor(0, AppTimeType.PRIVATE, 0, 0);
		lstOutput.add(timeInfor);
		timeInfor = new VacationTimeInfor(0, AppTimeType.UNION, 0, 0);
		lstOutput.add(timeInfor);
		return lstOutput;
	}

}
