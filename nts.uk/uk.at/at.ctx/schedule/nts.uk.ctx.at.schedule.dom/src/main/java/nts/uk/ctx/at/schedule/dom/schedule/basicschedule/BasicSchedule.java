/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.schedule.basicschedule;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.WorkSchedulePersonFee;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak.WorkScheduleBreak;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime.WorkScheduleTime;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone.WorkScheduleTimeZone;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkdayDivision;

/**
 * The Class BasicSchedule.
 */
// 勤務予定基本情報
@Getter
public class BasicSchedule extends AggregateRoot {

	/** The employee id. */
	// 社員ID
	private String employeeId;

	/** The date. */
	// 年月日
	private GeneralDate date;

	/** The work type code. */
	// 勤務種類
	private String workTypeCode;

	/** The work time code. */
	// 就業時間帯
	private String workTimeCode;

	/** The confirmed atr. */
	// 確定区分
	private ConfirmedAtr confirmedAtr;

	/** The work day atr. */
	// 稼働日区分
	private WorkdayDivision workDayAtr;

	/** The work schedule time zones. */
	// 勤務予定時間帯
	private List<WorkScheduleTimeZone> workScheduleTimeZones;

	/** The work schedule breaks. */
	// 勤務予定休憩
	private List<WorkScheduleBreak> workScheduleBreaks;

	/** The work schedule time. */
	// 勤務予定時間
	private Optional<WorkScheduleTime> workScheduleTime;

	/** The work schedule person fees. */
	// 勤務予定人件費
	private List<WorkSchedulePersonFee> workSchedulePersonFees;
	
	/** The child care schedules. */
	// 勤務予定育児介護時間帯
	private List<ChildCareSchedule> childCareSchedules;

	/**
	 * Instantiates a new basic schedule.
	 *
	 * @param memento
	 */
	public BasicSchedule(BasicScheduleGetMemento memento) {
		this.employeeId = memento.getEmployeeId();
		this.date = memento.getDate();
		this.workTypeCode = memento.getWorkTypeCode();
		this.workTimeCode = memento.getWorkTimeCode();
		this.confirmedAtr = memento.getConfirmedAtr();
		this.workDayAtr = memento.getWorkDayAtr();
		this.workScheduleTimeZones = memento.getWorkScheduleTimeZones();
		this.workScheduleBreaks = memento.getWorkScheduleBreaks();
		this.workScheduleTime = memento.getWorkScheduleTime();
		this.workSchedulePersonFees = memento.getWorkSchedulePersonFees();
		this.childCareSchedules = memento.getChildCareSchedules();
	}

	/**
	 * Constructor custom
	 * 
	 * @param employeeId
	 * @param date
	 * @param workTypeCode
	 * @param workTimeCode
	 * @param confirmedAtr
	 * @param workDayAtr
	 */
	public BasicSchedule(String employeeId, GeneralDate date, String workTypeCode, String workTimeCode,
			ConfirmedAtr confirmedAtr, WorkdayDivision workDayAtr) {
		this.employeeId = employeeId;
		this.date = date;
		this.workTypeCode = workTypeCode;
		this.workTimeCode = workTimeCode;
		this.confirmedAtr = confirmedAtr;
		this.workDayAtr = workDayAtr;
	}

	/**
	 * Creates the from java type.
	 *
	 * @param sId the s id
	 * @param date the date
	 * @param workTypeCode the work type code
	 * @param workTimeCode the work time code
	 * @param confirmedAtr the confirmed atr
	 * @param workDayAtr the work day atr
	 * @return the basic schedule
	 */
	public static BasicSchedule createFromJavaType(String sId, GeneralDate date, String workTypeCode,
			String workTimeCode, int confirmedAtr, int workDayAtr) {
		return new BasicSchedule(sId, date, workTypeCode, workTimeCode,
				EnumAdaptor.valueOf(confirmedAtr, ConfirmedAtr.class),
				EnumAdaptor.valueOf(workDayAtr, WorkdayDivision.class));
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(BasicScheduleSetMemento memento) {
		memento.setEmployeeId(this.employeeId);
		memento.setDate(this.date);
		memento.setWorkTypeCode(this.workTypeCode);
		memento.setWorkTimeCode(this.workTimeCode);
		memento.setConfirmedAtr(this.confirmedAtr);
		memento.setWorkDayAtr(this.workDayAtr);
		memento.setWorkScheduleTimeZones(this.workScheduleTimeZones);
		memento.setWorkScheduleBreaks(this.workScheduleBreaks);
		memento.setWorkScheduleTime(this.workScheduleTime);
		memento.setWorkSchedulePersonFees(this.workSchedulePersonFees);
		memento.setChildCareSchedules(this.childCareSchedules);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((childCareSchedules == null) ? 0 : childCareSchedules.hashCode());
		result = prime * result + ((confirmedAtr == null) ? 0 : confirmedAtr.hashCode());
		result = prime * result + ((workDayAtr == null) ? 0 : workDayAtr.hashCode());
		result = prime * result + ((workScheduleBreaks == null) ? 0 : workScheduleBreaks.hashCode());
		result = prime * result + ((workSchedulePersonFees == null) ? 0 : workSchedulePersonFees.hashCode());
		result = prime * result + ((workScheduleTime == null) ? 0 : workScheduleTime.hashCode());
		result = prime * result + ((workScheduleTimeZones == null) ? 0 : workScheduleTimeZones.hashCode());
		result = prime * result + ((workTimeCode == null) ? 0 : workTimeCode.hashCode());
		result = prime * result + ((workTypeCode == null) ? 0 : workTypeCode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicSchedule other = (BasicSchedule) obj;
		if (childCareSchedules == null) {
			if (other.childCareSchedules != null)
				return false;
		} else if (!childCareSchedules.equals(other.childCareSchedules))
			return false;
		if (confirmedAtr != other.confirmedAtr)
			return false;
		if (workDayAtr != other.workDayAtr)
			return false;
		if (workScheduleBreaks == null) {
			if (other.workScheduleBreaks != null)
				return false;
		} else if (!workScheduleBreaks.equals(other.workScheduleBreaks))
			return false;
		if (workSchedulePersonFees == null) {
			if (other.workSchedulePersonFees != null)
				return false;
		} else if (!workSchedulePersonFees.equals(other.workSchedulePersonFees))
			return false;
		if (workScheduleTime == null) {
			if (other.workScheduleTime != null)
				return false;
		} else if (!workScheduleTime.equals(other.workScheduleTime))
			return false;
		if (workScheduleTimeZones == null) {
			if (other.workScheduleTimeZones != null)
				return false;
		} else if (!workScheduleTimeZones.equals(other.workScheduleTimeZones))
			return false;
		if (workTimeCode == null) {
			if (other.workTimeCode != null)
				return false;
		} else if (!workTimeCode.equals(other.workTimeCode))
			return false;
		if (workTypeCode == null) {
			if (other.workTypeCode != null)
				return false;
		} else if (!workTypeCode.equals(other.workTypeCode))
			return false;
		return true;
	}
	
	
}
