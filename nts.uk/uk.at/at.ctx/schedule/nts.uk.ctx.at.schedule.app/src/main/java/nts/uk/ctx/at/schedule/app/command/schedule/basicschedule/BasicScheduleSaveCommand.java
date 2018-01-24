/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.schedule.basicschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleGetMemento;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak.WorkScheduleBreak;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.shared.dom.worktime.predset.PrescribedTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;

/**
 * The Class BasicScheduleSaveCommand.
 */

@Getter
@Setter
public class BasicScheduleSaveCommand {

	/** The employee id. */
	private String employeeId;
	
	/** The ymd. */
	private GeneralDate ymd;
	
	/** The confirmed atr. */
	private int confirmedAtr;
	
	/** The worktype code. */
	private String worktypeCode;
	
	/** The worktime code. */
	private String worktimeCode;
	
	/** The work schedule time zones. */
	private List<WorkScheduleTimeZoneSaveCommand> workScheduleTimeZones;
	
	/** The work schedule breaks. */
	private List<WorkScheduleBreakSaveCommand> workScheduleBreaks;
	
	/** The child care schedules. */
	private List<ChildCareScheduleSaveCommand> childCareSchedules;
	
	
	/**
	 * Update work schedule time zones.
	 *
	 * @param workTimeSet the work time set
	 */
	public void updateWorkScheduleTimeZones(PrescribedTimezoneSetting workTimeSet) {
		this.workScheduleTimeZones = workTimeSet.getLstTimezone().stream().filter(timezone -> timezone.isUsed())
				.map(timezone -> this.convertTimeZoneToScheduleTimeZone(timezone)).collect(Collectors.toList());
	}
	
	/**
	 * Convert time zone to schedule time zone.
	 *
	 * @param timezone the timezone
	 * @return the work schedule time zone save command
	 */
	// 勤務予定時間帯
	private WorkScheduleTimeZoneSaveCommand convertTimeZoneToScheduleTimeZone(TimezoneUse timezone) {
		WorkScheduleTimeZoneSaveCommand command = new WorkScheduleTimeZoneSaveCommand();

		// 予定勤務回数 = 取得した勤務予定時間帯. 勤務NO
		command.setScheduleCnt(timezone.getWorkNo());

		// 予定開始時刻 = 取得した勤務予定時間帯. 開始
		command.setScheduleStartClock(timezone.getStart().valueAsMinutes());

		// 予定終了時刻 = 取得した勤務予定時間帯. 終了
		command.setScheduleEndClock(timezone.getEnd().valueAsMinutes());

		return command;
	}
	/**
	 * To domain.
	 *
	 * @return the basic schedule
	 */
	public BasicSchedule toDomain(){
		return new BasicSchedule(new BasicScheduleSaveCommandGetMementoImpl());
	}
	
	/**
	 * The Class BasicScheduleSaveCommandGetMementoImpl.
	 */
	class BasicScheduleSaveCommandGetMementoImpl implements BasicScheduleGetMemento{

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getEmployeeId()
		 */
		@Override
		public String getEmployeeId() {
			return employeeId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getDate()
		 */
		@Override
		public GeneralDate getDate() {
			return ymd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkTypeCode()
		 */
		@Override
		public String getWorkTypeCode() {
			return worktypeCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkTimeCode()
		 */
		@Override
		public String getWorkTimeCode() {
			return worktimeCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getConfirmedAtr()
		 */
		@Override
		public ConfirmedAtr getConfirmedAtr() {
			return ConfirmedAtr.valueOf(confirmedAtr);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkScheduleTimeZones()
		 */
		@Override
		public List<WorkScheduleTimeZone> getWorkScheduleTimeZones() {
			if (CollectionUtil.isEmpty(workScheduleTimeZones)) {
				return new ArrayList<>();
			}
			return workScheduleTimeZones.stream().map(command -> new WorkScheduleTimeZone(command))
					.collect(Collectors.toList());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkScheduleBreaks()
		 */
		@Override
		public List<WorkScheduleBreak> getWorkScheduleBreaks() {
			if (CollectionUtil.isEmpty(workScheduleBreaks)) {
				return new ArrayList<>();
			}
			return workScheduleBreaks.stream().map(command -> new WorkScheduleBreak(command))
					.collect(Collectors.toList());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkScheduleTime()
		 */
		@Override
		public Optional<WorkScheduleTime> getWorkScheduleTime() {
			return Optional.empty();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getWorkSchedulePersonFees()
		 */
		@Override
		public List<WorkSchedulePersonFee> getWorkSchedulePersonFees() {
			return new ArrayList<>();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see nts.uk.ctx.at.schedule.dom.schedule.basicschedule.
		 * BasicScheduleGetMemento#getChildCareSchedules()
		 */
		@Override
		public List<ChildCareSchedule> getChildCareSchedules() {
			if (CollectionUtil.isEmpty(childCareSchedules)) {
				return new ArrayList<>();
			}
			return childCareSchedules.stream().map(command -> new ChildCareSchedule(command))
					.collect(Collectors.toList());
		}

	}
}
