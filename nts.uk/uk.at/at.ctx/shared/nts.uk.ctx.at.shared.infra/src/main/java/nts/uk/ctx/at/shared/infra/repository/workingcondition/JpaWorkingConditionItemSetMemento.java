/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.workingcondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.workingcondition.BonusPaySettingCode;
import nts.uk.ctx.at.shared.dom.workingcondition.BreakdownTimeDay;
import nts.uk.ctx.at.shared.dom.workingcondition.HourlyPaymentAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.LaborContractTime;
import nts.uk.ctx.at.shared.dom.workingcondition.ManageAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.MonthlyPatternCode;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalDayOfWeek;
import nts.uk.ctx.at.shared.dom.workingcondition.PersonalWorkCategory;
import nts.uk.ctx.at.shared.dom.workingcondition.ScheduleMethod;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPerWorkCat;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtPersonalDayOfWeek;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtScheduleMethod;
import nts.uk.ctx.at.shared.infra.entity.workingcondition.KshmtWorkingCondItem;

/**
 * The Class JpaWorkingConditionItemSetMemento.
 */
public class JpaWorkingConditionItemSetMemento implements WorkingConditionItemSetMemento {

	/** The entity. */
	private KshmtWorkingCondItem entity;

	/**
	 * Instantiates a new jpa working condition item set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWorkingConditionItemSetMemento(KshmtWorkingCondItem entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.entity.setHistoryId(historyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setScheduleManagementAtr(nts.uk.ctx.at.shared.dom.workingcondition.
	 * NotUseAtr)
	 */
	@Override
	public void setScheduleManagementAtr(ManageAtr scheduleManagementAtr) {
		this.entity.setScheManagementAtr(scheduleManagementAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setVacationAddedTimeAtr(nts.uk.ctx.at.shared.dom.workingcondition.
	 * NotUseAtr)
	 */
	@Override
	public void setVacationAddedTimeAtr(NotUseAtr vacationAddedTimeAtr) {
		this.entity.setVacationAddTimeAtr(vacationAddedTimeAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setLaborSystem(nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem)
	 */
	@Override
	public void setLaborSystem(WorkingSystem laborSystem) {
		this.entity.setLaborSys(laborSystem.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setWorkCategory(nts.uk.ctx.at.shared.dom.workingcondition.
	 * PersonalWorkCategory)
	 */
	@Override
	public void setWorkCategory(PersonalWorkCategory workCategory) {
		List<KshmtPerWorkCat> kshmtPerWorkCats = new ArrayList<>();
		if(this.entity.getKshmtPerWorkCats() != null){
			kshmtPerWorkCats = this.entity.getKshmtPerWorkCats();
		}
		workCategory.saveToMemento(
				new JpaPerWorkCatSetMemento(this.entity.getHistoryId(), kshmtPerWorkCats));
		this.entity.setKshmtPerWorkCats(kshmtPerWorkCats);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setContractTime(nts.uk.ctx.at.shared.dom.workingcondition.
	 * LaborContractTime)
	 */
	@Override
	public void setContractTime(LaborContractTime contractTime) {
		this.entity.setContractTime(contractTime.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setAutoIntervalSetAtr(nts.uk.ctx.at.shared.dom.workingcondition.
	 * NotUseAtr)
	 */
	@Override
	public void setAutoIntervalSetAtr(NotUseAtr autoIntervalSetAtr) {
		this.entity.setAutoIntervalSetAtr(autoIntervalSetAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setWorkDayOfWeek(nts.uk.ctx.at.shared.dom.workingcondition.
	 * PersonalDayOfWeek)
	 */
	@Override
	public void setWorkDayOfWeek(PersonalDayOfWeek workDayOfWeek) {
		List<KshmtPersonalDayOfWeek> kshmtPersonalDayOfWeeks = new ArrayList<>();
		if(this.entity.getKshmtPersonalDayOfWeeks() != null){
			kshmtPersonalDayOfWeeks = this.entity.getKshmtPersonalDayOfWeeks();
		}
		workDayOfWeek.saveToMemento(
				new JpaPerDayOfWeekSetMemento(this.entity.getHistoryId(), kshmtPersonalDayOfWeeks));
		this.entity.setKshmtPersonalDayOfWeeks(kshmtPersonalDayOfWeeks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setEmployeeId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String employeeId) {
		this.entity.setSid(employeeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setAutoStampSetAtr(nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr)
	 */
	@Override
	public void setAutoStampSetAtr(NotUseAtr autoStampSetAtr) {
		this.entity.setAutoStampSetAtr(autoStampSetAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setScheduleMethod(nts.uk.ctx.at.shared.dom.workingcondition.
	 * ScheduleMethod)
	 */
	@Override
	public void setScheduleMethod(Optional<ScheduleMethod> scheduleMethod) {
		// Check exist
		if (!scheduleMethod.isPresent()) {
			this.entity.setKshmtScheduleMethod(null);
			return;
		}

		KshmtScheduleMethod kshmtScheduleMethod = this.entity.getKshmtScheduleMethod();

		if (kshmtScheduleMethod == null) {
			kshmtScheduleMethod = new KshmtScheduleMethod();
		}

		scheduleMethod.get().saveToMemento(
				new JpaScheduleMethodSetMemento(this.entity.getHistoryId(), kshmtScheduleMethod));

		this.entity.setKshmtScheduleMethod(kshmtScheduleMethod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setHolidayAddTimeSet(nts.uk.ctx.at.shared.dom.workingcondition.
	 * BreakdownTimeDay)
	 */
	@Override
	public void setHolidayAddTimeSet(Optional<BreakdownTimeDay> holidayAddTimeSet) {
		// Check exist
		if (!holidayAddTimeSet.isPresent()) {
			this.entity.setHdAddTimeMorning(null);
			this.entity.setHdAddTimeAfternoon(null);
			this.entity.setHdAddTimeOneDay(null);
			return;
		}
		if (holidayAddTimeSet.get().getMorning() != null){
			this.entity.setHdAddTimeMorning(holidayAddTimeSet.get().getMorning().v());
		} else {
			this.entity.setHdAddTimeMorning(null);
		}
		if (holidayAddTimeSet.get().getAfternoon() != null){
			this.entity.setHdAddTimeAfternoon(holidayAddTimeSet.get().getAfternoon().v());
		} else {
			this.entity.setHdAddTimeAfternoon(null);
		}
		if (holidayAddTimeSet.get().getOneDay() != null){
			this.entity.setHdAddTimeOneDay(holidayAddTimeSet.get().getOneDay().v());
		} else {
				this.entity.setHdAddTimeOneDay(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setHourlyPaymentAtr(nts.uk.ctx.at.shared.dom.workingcondition.
	 * HourlyPaymentAtr)
	 */
	@Override
	public void setHourlyPaymentAtr(HourlyPaymentAtr hourlyPaymentAtr) {
		this.entity.setHourlyPayAtr(hourlyPaymentAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setTimeApply(java.util.Optional)
	 */
	@Override
	public void setTimeApply(Optional<BonusPaySettingCode> timeApply) {
		if (timeApply.isPresent()){
			this.entity.setTimeApply(timeApply.get().v());
		} else {
			this.entity.setTimeApply(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemSetMemento#
	 * setMonthlyPattern(java.util.Optional)
	 */
	@Override
	public void setMonthlyPattern(Optional<MonthlyPatternCode> monthlyPattern) {
		if (monthlyPattern.isPresent()){
			this.entity.setMonthlyPattern(monthlyPattern.get().v());
		} else {
			this.entity.setMonthlyPattern(null);
		}
	}

}
