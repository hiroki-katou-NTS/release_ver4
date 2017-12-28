package nts.uk.ctx.at.record.pubimp.workinformation;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPub;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPubExport;

@Stateless
public class RecordWorkInfoPubImpl implements RecordWorkInfoPub {

	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	/**
	 * Request List 5
	 */
	@Override
	public RecordWorkInfoPubExport getRecordWorkInfo(String employeeId, GeneralDate ymd) {
		Optional<WorkInfoOfDailyPerformance> opWorkInfoOfDailyPerformance = this.workInformationRepository.find(employeeId, ymd);
		if(!opWorkInfoOfDailyPerformance.isPresent()) {
			return new RecordWorkInfoPubExport("", "", -1, -1, -1, -1, -1, -1, -1, -1, -1);
		}
		
		// 日別実績の勤務情報
		WorkInfoOfDailyPerformance workInfoOfDailyPerformance = opWorkInfoOfDailyPerformance.get();
		
		//日別実績の出退勤
		Optional<TimeLeavingOfDailyPerformance> opTimeLeavingOfDailyPerformance = this.timeLeavingOfDailyPerformanceRepository.findByKey(employeeId, ymd);
		if(!opTimeLeavingOfDailyPerformance.isPresent()) {
			return new RecordWorkInfoPubExport("", "", -1, -1, -1, -1, -1, -1, -1, -1, -1);
		}
		TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = opTimeLeavingOfDailyPerformance.get();
		
		Integer attendanceStampTimeFirst = -1;
		Integer leaveStampTimeFirst = -1;
		Integer attendanceStampTimeSecond = -1;
		Integer leaveStampTimeSecond = -1; 
		if(timeLeavingOfDailyPerformance.getTimeLeavingWorks().size()>1){
			attendanceStampTimeSecond = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getAttendanceStamp().getStamp().get().getTimeWithDay().v();
			leaveStampTimeSecond = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(1).getLeaveStamp().getStamp().get().getTimeWithDay().v();
		}
		if(timeLeavingOfDailyPerformance.getTimeLeavingWorks().size()>0){
			attendanceStampTimeFirst = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getAttendanceStamp().getStamp().get().getTimeWithDay().v();
			leaveStampTimeFirst = timeLeavingOfDailyPerformance.getTimeLeavingWorks().get(0).getLeaveStamp().getStamp().get().getTimeWithDay().v();
		}
		
		RecordWorkInfoPubExport recordWorkInfoPubExport = new RecordWorkInfoPubExport(
				workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTypeCode().v(),
				workInfoOfDailyPerformance.getRecordWorkInformation().getWorkTimeCode().v(),
				attendanceStampTimeFirst,
				leaveStampTimeFirst,
				attendanceStampTimeSecond,
				leaveStampTimeSecond,
				-1, -1, -1, -1, -1);
		return recordWorkInfoPubExport;
	}

}
