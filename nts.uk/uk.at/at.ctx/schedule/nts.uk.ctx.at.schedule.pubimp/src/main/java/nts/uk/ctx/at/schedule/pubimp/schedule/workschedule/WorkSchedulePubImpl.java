package nts.uk.ctx.at.schedule.pubimp.schedule.workschedule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkScheduleRepository;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.BreakTimeOfDailyAttdExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.BreakTimeSheetExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.ReasonTimeChangeExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.TimeActualStampExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.TimeLeavingOfDailyAttdExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.TimeLeavingWorkExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.WorkScheduleBasicInforExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.WorkScheduleExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.WorkSchedulePub;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.WorkStampExport;
import nts.uk.ctx.at.schedule.pub.schedule.workschedule.WorkTimeInformationExport;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;

/**
 * 
 * @author tutk
 *
 */
@Stateless
public class WorkSchedulePubImpl implements WorkSchedulePub {

	@Inject
	private WorkScheduleRepository workScheduleRepository;

	@Override
	public Optional<WorkScheduleExport> get(String employeeID, GeneralDate ymd) {
		Optional<WorkSchedule> data = workScheduleRepository.get(employeeID, ymd);
		if (data.isPresent()) {
			return Optional.of(convertToWorkSchedule(data.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<WorkScheduleExport> getList(List<String> sids, DatePeriod period) {

		List<WorkSchedule> data = workScheduleRepository.getList(sids, period);
		return data.stream().map(this::convertToWorkSchedule).collect(Collectors.toList());

	}

	private WorkScheduleExport convertToWorkSchedule(WorkSchedule data) {
		
		TimeLeavingOfDailyAttdExport timeLeavingOfDailyAttd = null;
		if (data.getOptTimeLeaving().isPresent()) {
			List<TimeLeavingWorkExport> timeLeavingWorks = data.getOptTimeLeaving().get().getTimeLeavingWorks().stream()
					.map(c -> convertToTimeLeavingWork(c)).collect(Collectors.toList());
			timeLeavingOfDailyAttd = new TimeLeavingOfDailyAttdExport(timeLeavingWorks,
					data.getOptTimeLeaving().get().getWorkTimes() ==null?0:data.getOptTimeLeaving().get().getWorkTimes().v());
		}
		Optional<BreakTimeOfDailyAttdExport> listBreakTimeOfDaily = data.getLstBreakTime().map(c -> new BreakTimeOfDailyAttdExport(
						c.getBreakTimeSheets().stream().map(x -> new BreakTimeSheetExport(x.getBreakFrameNo().v(),
								x.getStartTime().v(), x.getEndTime().v(), x.getBreakTime().v()))
								.collect(Collectors.toList())
				));

		WorkScheduleExport workScheduleExport = new WorkScheduleExport(
				data.getEmployeeID(),
				data.getConfirmedATR().value,
				data.getWorkInfo().getRecordInfo().getWorkTypeCode().v(),
				data.getWorkInfo().getRecordInfo().getWorkTimeCode() == null ? null
						: data.getWorkInfo().getRecordInfo().getWorkTimeCode().v(),
				data.getWorkInfo().getGoStraightAtr().value, data.getWorkInfo().getBackStraightAtr().value,
				timeLeavingOfDailyAttd,listBreakTimeOfDaily);
		return workScheduleExport;

	}

	private TimeLeavingWorkExport convertToTimeLeavingWork(TimeLeavingWork domain) {
		return new TimeLeavingWorkExport(domain.getWorkNo().v(),
				!domain.getAttendanceStamp().isPresent() ? null
						: convertToTimeActualStamp(domain.getAttendanceStamp().get()),
				!domain.getLeaveStamp().isPresent() ? null : convertToTimeActualStamp(domain.getLeaveStamp().get()));
	}

	private TimeActualStampExport convertToTimeActualStamp(TimeActualStamp domain) {
		return new TimeActualStampExport(
				domain.getActualStamp().isPresent() ? convertToWorkStamp(domain.getActualStamp().get()) : null,
				domain.getStamp().isPresent() ? convertToWorkStamp(domain.getStamp().get()) : null,
				domain.getNumberOfReflectionStamp());
	}

	private WorkStampExport convertToWorkStamp(WorkStamp domain) {
		return new WorkStampExport( new WorkTimeInformationExport(
				new ReasonTimeChangeExport(domain.getTimeDay().getReasonTimeChange().getTimeChangeMeans().value,
						domain.getTimeDay().getReasonTimeChange().getEngravingMethod().isPresent()
								? domain.getTimeDay().getReasonTimeChange().getEngravingMethod().get().value
								: null),
				domain.getTimeDay().getTimeWithDay().isPresent() ? domain.getTimeDay().getTimeWithDay().get().v()
						: null),
				domain.getLocationCode().isPresent() ? domain.getLocationCode().get().v() : null);
	}

	@Override
	public List<WorkScheduleBasicInforExport> get(List<String> lstSid, DatePeriod ymdPeriod) {
		List<WorkSchedule> getList = workScheduleRepository.getList(lstSid, ymdPeriod);
		List<WorkScheduleBasicInforExport> lstResult = getList.stream()
				.map(x -> new WorkScheduleBasicInforExport(x.getEmployeeID(),
						x.getYmd(),
						x.getWorkInfo().getRecordInfo().getWorkTypeCode().v(),
						x.getWorkInfo().getRecordInfo().getWorkTimeCodeNotNull().isPresent() ? 
								Optional.ofNullable(x.getWorkInfo().getRecordInfo().getWorkTimeCodeNotNull().get().v()): Optional.empty()))
				.collect(Collectors.toList());
		return lstResult;
	}
}
