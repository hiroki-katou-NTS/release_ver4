package nts.uk.ctx.at.record.pubimp.workinformation;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.stamp.GoOutReason;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.pub.workinformation.CommonTimeSheet;
import nts.uk.ctx.at.record.pub.workinformation.InfoCheckNotRegisterPubExport;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPub;
import nts.uk.ctx.at.record.pub.workinformation.RecordWorkInfoPubExport;

@Stateless
public class RecordWorkInfoPubImpl implements RecordWorkInfoPub {

	@Inject
	private WorkInformationRepository workInformationRepository;
	
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;
	
	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;

	/**
	 * RequestList5
	 */
	@Override
	public RecordWorkInfoPubExport getRecordWorkInfo(String employeeId, GeneralDate ymd) {
		Optional<WorkInfoOfDailyPerformance> workInfo = this.workInformationRepository.find(employeeId, ymd);
		if(!workInfo.isPresent()) {
			return new RecordWorkInfoPubExport("", "");
		}
		String workTimeCode = workInfo.get().getRecordWorkInformation().getWorkTimeCode() == null 
				? null : workInfo.get().getRecordWorkInformation().getWorkTimeCode().v();
		String workTypeCode = workInfo.get().getRecordWorkInformation().getWorkTypeCode().v();
		RecordWorkInfoPubExport record = new RecordWorkInfoPubExport(
				workTypeCode,
				workTimeCode);
		
		//日別実績の出退勤
		Optional<TimeLeavingOfDailyPerformance> timeLeaving = this.timeLeavingOfDailyPerformanceRepository.findByKey(employeeId, ymd);
		Optional<AttendanceTimeOfDailyPerformance> attenTime = this.attendanceTimeRepo.find(employeeId, ymd);
		
		timeLeaving.ifPresent(tl -> {
			tl.getAttendanceLeavingWork(1).ifPresent(al -> {
				// nampt : check null case
				al.getAttendanceStamp().ifPresent(stamp -> {
					stamp.getStamp().ifPresent(s -> {
						record.setAttendanceStampTimeFirst(s.getTimeWithDay() == null ? null : s.getTimeWithDay().valueAsMinutes());
					});
				});
				al.getLeaveStamp().ifPresent(stamp -> {
					stamp.getStamp().ifPresent(s -> {
						record.setLeaveStampTimeFirst(s.getTimeWithDay() == null ? null : s.getTimeWithDay().valueAsMinutes());
					});
				});
			});
			tl.getAttendanceLeavingWork(2).ifPresent(al -> {
				// nampt : check null case
				al.getAttendanceStamp().ifPresent(stamp -> {
					stamp.getStamp().ifPresent(s -> {
						record.setAttendanceStampTimeSecond(s.getTimeWithDay() == null ? null : s.getTimeWithDay().valueAsMinutes());
					});
				});
				al.getLeaveStamp().ifPresent(stamp -> {
					stamp.getStamp().ifPresent(s -> {
						record.setLeaveStampTimeSecond(s.getTimeWithDay() == null ? null : s.getTimeWithDay().valueAsMinutes());
					});
				});
			});
		});
		
		attenTime.ifPresent(at -> {
			TotalWorkingTime totalWT = at.getActualWorkingTimeOfDaily().getTotalWorkingTime();
			if(totalWT == null){
				return;
			}
			totalWT.getLateTimeNo(1).ifPresent(lt -> {
				Integer time = lt.getLateTime().getTime() == null ? null : lt.getLateTime().getTime().valueAsMinutes();
				record.setLateTime1(time);
			});
			totalWT.getLateTimeNo(2).ifPresent(lt -> {
				Integer time = lt.getLateTime().getTime() == null ? null : lt.getLateTime().getTime().valueAsMinutes();
				record.setLateTime2(time);
			});
			totalWT.getLeaveEarlyTimeNo(1).ifPresent(lt -> {
				Integer time = lt.getLeaveEarlyTime().getTime() == null ? null : lt.getLeaveEarlyTime().getTime().valueAsMinutes();
				record.setLeaveEarlyTime1(time);
			});
			totalWT.getLeaveEarlyTimeNo(2).ifPresent(lt -> {
				Integer time = lt.getLeaveEarlyTime().getTime() == null ? null : lt.getLeaveEarlyTime().getTime().valueAsMinutes();
				record.setLeaveEarlyTime2(time);
			});
			if(totalWT.getShotrTimeOfDaily() != null && 
					totalWT.getShotrTimeOfDaily().getTotalTime() != null
					&& totalWT.getShotrTimeOfDaily().getTotalTime().getTotalTime() != null){
				record.setChildCareTime(totalWT.getShotrTimeOfDaily().getTotalTime().getTotalTime().getTime().valueAsMinutes());
			}
			totalWT.getOutingTimeByReason(GoOutReason.OFFICAL).ifPresent(ot -> {
				if(ot.getRecordTotalTime() != null && ot.getRecordTotalTime().getTotalTime() != null){
					record.setOutingTimePrivate(ot.getRecordTotalTime().getTotalTime().getTime().valueAsMinutes());
				}
			});
			totalWT.getOutingTimeByReason(GoOutReason.SUPPORT).ifPresent(ot -> {
				if(ot.getRecordTotalTime() != null && ot.getRecordTotalTime().getTotalTime() != null){
					record.setOutingTimePrivate(ot.getRecordTotalTime().getTotalTime().getTime().valueAsMinutes());
				}
			});
			totalWT.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().ifPresent(ovt -> {
				if(ovt.getFlexTime() != null && ovt.getFlexTime().getFlexTime() != null){
					record.setFlexTime(ovt.getFlexTime().getFlexTime().getCalcTime().valueAsMinutes());
				}
				record.setOvertimes(ovt.getOverTimeWorkFrameTime().stream().map(c -> 
																	new CommonTimeSheet(c.getOverWorkFrameNo().v(), 
																			getCalcTime(c.getOverTimeWork()), 
																			getCalcTime(c.getTransferTime())))
																.collect(Collectors.toList()));
			});
			totalWT.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().ifPresent(holW -> {
				record.setHolidayWorks(holW.getHolidayWorkFrameTime().stream().map(c -> 
																	new CommonTimeSheet(c.getHolidayFrameNo().v(), 
																			getCalcTime(c.getHolidayWorkTime()), 
																			getCalcTime(c.getTransferTime())))
																.collect(Collectors.toList()));
			});
		});
		
		return record;
	}

	@Override
	public Optional<InfoCheckNotRegisterPubExport> getInfoCheckNotRegister(String employeeId, GeneralDate ymd) {
		 Optional<InfoCheckNotRegisterPubExport> data = workInformationRepository.find(employeeId, ymd).map(c->convertToExport(c));
		if(data.isPresent()) {
			return data;
		}
		return Optional.empty();
	}
	
	private InfoCheckNotRegisterPubExport convertToExport(WorkInfoOfDailyPerformance domain) {
		return new InfoCheckNotRegisterPubExport(
				domain.getEmployeeId(),
				domain.getRecordWorkInformation().getWorkTimeCode()==null?"":domain.getRecordWorkInformation().getWorkTimeCode().v(),
				domain.getRecordWorkInformation().getWorkTypeCode().v()
				);
	}
	
	private Integer getCalcTime(TimeWithCalculation calc){
		return calc == null || calc.getCalcTime() == null ? null : calc.getCalcTime().valueAsMinutes();
	}

	private Integer getCalcTime(Finally<TimeWithCalculation> calc){
		return !calc.isPresent() || calc == null || calc.get().getCalcTime() == null ? null : calc.get().getCalcTime().valueAsMinutes();
	}
}
