/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.calculation.holiday;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.dom.AggregateRoot;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.AddSetManageWorkHour;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.EmploymentCalcDetailedSetIncludeVacationAmount;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayAddtionRepository;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HourlyPaymentAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.IncludeHolidaysPremiumCalcDetailSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.LeaveSetAdded;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.PremiumCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.PremiumHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.ReferEmployeeInformation;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.ReferWorkRecord;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.ReferenceDestinationAbsenceWorkingHours;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.TimeHolidayAddingMethod;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.TimeHolidayAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.VacationSpecifiedTimeRefer;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkClassOfTimeHolidaySet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkTimeCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.WorkTimeHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.workingcondition.BreakdownTimeDay;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstAddSetManWKHour;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstAddSetManWKHourPK;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstHolidayAdditionSet;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstHolidayAdditionSetPK;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstHourPayAaddSet;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstHourPayAaddSetPK;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstWorkDepLaborSet;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshstWorkDepLaborSetPK;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshmtCalcCAddHdFle;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshmtCalcCAddHdFlePK;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshmtCalcCAddHdReg;
import nts.uk.ctx.at.shared.infra.entity.calculation.holiday.KshmtCalcCAddHdRegPK;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The Class JpaHolidayAddtionRepository.
 */
@Stateless
public class JpaHolidayAddtionRepository extends JpaRepository implements HolidayAddtionRepository {

	private static final String SELECT_BY_CID;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KshstHolidayAdditionSet e");
		builderString.append(" WHERE e.kshstHolidayAddtimeSetPK.companyId = :companyId");
		SELECT_BY_CID = builderString.toString();
	}
	
	protected HolidayAddtionSet convertToDomainHolidayAdditionSet(KshstHolidayAdditionSet holidayAddtimeSet) {
		List<TimeHolidayAdditionSet> lstTimeHDAddSet = new ArrayList<>();
		TimeHolidayAdditionSet timeHolidayAdditionSet = TimeHolidayAdditionSet.builder()
															.addingMethod(TimeHolidayAddingMethod.valueOf(holidayAddtimeSet.addingMethod1))
															.workClass(WorkClassOfTimeHolidaySet.valueOf(holidayAddtimeSet.workClass1))
															.build();
		lstTimeHDAddSet.add(timeHolidayAdditionSet);
		
		timeHolidayAdditionSet = TimeHolidayAdditionSet.builder()
				.addingMethod(TimeHolidayAddingMethod.valueOf(holidayAddtimeSet.addingMethod2))
				.workClass(WorkClassOfTimeHolidaySet.valueOf(holidayAddtimeSet.workClass2))
				.build();
		lstTimeHDAddSet.add(timeHolidayAdditionSet);
		
		ReferenceDestinationAbsenceWorkingHours workingHours = ReferenceDestinationAbsenceWorkingHours.valueOf(holidayAddtimeSet.referComHolidayTime);
		
		BreakdownTimeDay breakdownTimeDay = new BreakdownTimeDay(new AttendanceTime(holidayAddtimeSet.oneDay.intValue()), 
																	new AttendanceTime(holidayAddtimeSet.morning.intValue()), 
																	new AttendanceTime(holidayAddtimeSet.afternoon.intValue()));
		
		ReferWorkRecord referWorkRecord = new ReferWorkRecord(workingHours, breakdownTimeDay);
		
		VacationSpecifiedTimeRefer vacationSpecifiedTimeRefer = VacationSpecifiedTimeRefer.valueOf(holidayAddtimeSet.notReferringAch);
		ReferEmployeeInformation referEmployeeInformation = new ReferEmployeeInformation(vacationSpecifiedTimeRefer);
		
		NotUseAtr referActualWorkHours = NotUseAtr.valueOf(holidayAddtimeSet.referActualWorkHours);
		
		LeaveSetAdded additionVacationSet = new LeaveSetAdded(NotUseAtr.valueOf(holidayAddtimeSet.annualHoliday), 
																NotUseAtr.valueOf(holidayAddtimeSet.yearlyReserved), 
																NotUseAtr.valueOf(holidayAddtimeSet.specialHoliday));
		
		HolidayAddtionSet addtime = HolidayAddtionSet.createFromJavaType(holidayAddtimeSet.kshstHolidayAddtimeSetPK.companyId, 
				referWorkRecord,
				referActualWorkHours, 
				referEmployeeInformation, 
				additionVacationSet,
				lstTimeHDAddSet);
		return addtime;
	}
	
	/**
	 * Convert to domain.
	 *
	 * @param holidayAddtimeSet the holiday addtime set
	 * @return the map
	 */
	private Map<String, AggregateRoot> convertToDomain(KshstHolidayAdditionSet holidayAddtimeSet){
		Map<String, AggregateRoot> mapAggre = new HashMap<>();
		
		WorkRegularAdditionSet regularWork = convertToDomainRegularWork(holidayAddtimeSet.regularWorkSet);
		WorkFlexAdditionSet flexWork = convertToDomainFlexWork(holidayAddtimeSet.flexWorkSet);
		WorkDeformedLaborAdditionSet irregularWork = convertToDomainIrregularWork(holidayAddtimeSet.irregularWorkSet);
		HourlyPaymentAdditionSet hourlyPaymentAdditionSet = convertToDomainHourlyPaymentAddSet(holidayAddtimeSet.hourPayAaddSet);
		AddSetManageWorkHour addSetManageWorkHour = convertToDomainAddSetManageWorkHour(holidayAddtimeSet.addSetManWKHour);
		
		HolidayAddtionSet addtime = convertToDomainHolidayAdditionSet(holidayAddtimeSet);
		
		mapAggre.put("holidayAddtionSet", addtime);
		mapAggre.put("regularWork", regularWork);
		mapAggre.put("flexWork", flexWork);
		mapAggre.put("irregularWork", irregularWork);
		mapAggre.put("hourlyPaymentAdditionSet", hourlyPaymentAdditionSet);
		mapAggre.put("addSetManageWorkHour", addSetManageWorkHour);
		
		return mapAggre;
	}

	/**
	 * Convert to Database Regular Work
	 * @param regularWork
	 * @return
	 */
	protected KshmtCalcCAddHdReg convertToDbTypeRegularWork(WorkRegularAdditionSet regularWork) {
		KshmtCalcCAddHdRegPK kshstRegularWorkSetPK = new KshmtCalcCAddHdRegPK(regularWork.getCompanyId());
		KshmtCalcCAddHdReg kshstRegularWorkSet;
		Optional<KshmtCalcCAddHdReg> optKshmtCalcCAddHdReg = this.queryProxy().find(kshstRegularWorkSetPK,KshmtCalcCAddHdReg.class);
		if (optKshmtCalcCAddHdReg.isPresent()) {
			kshstRegularWorkSet = optKshmtCalcCAddHdReg.get();
		} else {
			kshstRegularWorkSet = new KshmtCalcCAddHdReg();
		}
		
		PremiumHolidayCalcMethod premiumHolidayCalcMethod = regularWork.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday();
		PremiumCalcMethodDetailOfHoliday advanceSetPre = premiumHolidayCalcMethod.getAdvanceSet().get();
		DeductLeaveEarly deductLeaveEarly = advanceSetPre.getNotDeductLateLeaveEarly();
		IncludeHolidaysPremiumCalcDetailSet includeHolidaysPremiumCalcDetailSet = advanceSetPre.getIncludeVacationSet();
		
		WorkTimeHolidayCalcMethod workTimeHolidayCalcMethod = regularWork.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday();
		WorkTimeCalcMethodDetailOfHoliday advanceSetWork = workTimeHolidayCalcMethod.getAdvancedSet().get();
		EmploymentCalcDetailedSetIncludeVacationAmount includeVacationSet = advanceSetWork.getIncludeVacationSet();
		
		kshstRegularWorkSet.calcActualOperation1 = premiumHolidayCalcMethod.getCalculateActualOperation().value;
		kshstRegularWorkSet.exemptTaxTime1 = advanceSetPre.getCalculateIncludIntervalExemptionTime().value;
		kshstRegularWorkSet.incChildNursingCare1 = advanceSetPre.getCalculateIncludCareTime().value;
		kshstRegularWorkSet.additionTime1  = includeHolidaysPremiumCalcDetailSet.getAddition().value;
		kshstRegularWorkSet.notDeductLateleave1 = deductLeaveEarly.isDeduct() == true ? 1 : 0;
		kshstRegularWorkSet.deformatExcValue1 = includeHolidaysPremiumCalcDetailSet.getDeformationExceedsPredeterminedValue().get().value;
		kshstRegularWorkSet.exemptTaxTime2 = advanceSetWork.getCalculateIncludIntervalExemptionTime().value;
		kshstRegularWorkSet.calcActualOperation2 = workTimeHolidayCalcMethod.getCalculateActualOperation().value;
		kshstRegularWorkSet.incChildNursingCare2 = advanceSetWork.getCalculateIncludCareTime().value;
		kshstRegularWorkSet.notDeductLateleave2 = advanceSetWork.getNotDeductLateLeaveEarly().isDeduct() == true ? 1 : 0;
		kshstRegularWorkSet.additionTime2 = includeVacationSet.getAddition().value;
		kshstRegularWorkSet.enableSetPerWorkHour1 = deductLeaveEarly.isEnableSetPerWorkHour() == true ? 1 : 0;
		kshstRegularWorkSet.enableSetPerWorkHour2 = kshstRegularWorkSet.enableSetPerWorkHour1;
		kshstRegularWorkSet.kshstRegularWorkSetPK = kshstRegularWorkSetPK;
		return kshstRegularWorkSet;
	}

	/**
	 * Convert to Domain Irregular Work
	 * @param irregularWorkSet
	 * @return
	 */
	protected WorkDeformedLaborAdditionSet convertToDomainIrregularWork(KshstWorkDepLaborSet irregularWorkSet) {
		if (irregularWorkSet != null) {
			WorkDeformedLaborAdditionSet irregularWork = WorkDeformedLaborAdditionSet.createFromJavaType(irregularWorkSet.kshstWorkDepLaborSetPK.companyId, 
					irregularWorkSet.calcActualOperation1, 
					irregularWorkSet.exemptTaxTime1, 
					irregularWorkSet.incChildNursingCare1, 
					irregularWorkSet.additionTime1, 
					irregularWorkSet.notDeductLateleave1,
					irregularWorkSet.deformatExcValue,
					irregularWorkSet.exemptTaxTime2, 
					irregularWorkSet.minusAbsenceTime2, 
					irregularWorkSet.calcActualOperation2, 
					irregularWorkSet.incChildNursingCare2, 
					irregularWorkSet.notDeductLateleave2,
					irregularWorkSet.additionTime2,
					irregularWorkSet.enableSetPerWorkHour1);
			return irregularWork;
		}
		return null;
	}

	/**
	 * Convert to Domain Flex Work
	 * @param flexWorkSet
	 * @return
	 */
	protected WorkFlexAdditionSet convertToDomainFlexWork(KshmtCalcCAddHdFle flexWorkSet) {
		if (flexWorkSet != null) {
			WorkFlexAdditionSet flexWork = WorkFlexAdditionSet.createFromJavaType(flexWorkSet.kshstFlexWorkSetPK.companyId, 
					flexWorkSet.calcActualOperation1, 
					flexWorkSet.exemptTaxTime1, 
					flexWorkSet.incChildNursingCare1, 
					flexWorkSet.predeterminedOvertime1, 
					flexWorkSet.additionTime1, 
					flexWorkSet.notDeductLateleave1, 
					flexWorkSet.exemptTaxTime2, 
					flexWorkSet.minusAbsenceTime2, 
					flexWorkSet.calcActualOperation2, 
					flexWorkSet.incChildNursingCare2, 
					flexWorkSet.notDeductLateleave2, 
					flexWorkSet.predeterminDeficiency2, 
					flexWorkSet.additionTime2,
					flexWorkSet.enableSetPerWorkHour1,
					flexWorkSet.additionWithinMonthlyStatutory);
			return flexWork;
		}
		return null;
	}

	/**
	 * Convert to Domain Regular Work
	 * @param regularWorkSet
	 * @return
	 */
	protected WorkRegularAdditionSet convertToDomainRegularWork(KshmtCalcCAddHdReg regularWorkSet) {
		if (regularWorkSet != null) {
			WorkRegularAdditionSet regularWork = WorkRegularAdditionSet.createFromJavaType(regularWorkSet.kshstRegularWorkSetPK.companyId, 
					regularWorkSet.calcActualOperation1, 
					regularWorkSet.exemptTaxTime1, 
					regularWorkSet.incChildNursingCare1, 
					regularWorkSet.additionTime1, 
					regularWorkSet.notDeductLateleave1, 
					regularWorkSet.deformatExcValue1, 
					regularWorkSet.exemptTaxTime2, 
					regularWorkSet.calcActualOperation2, 
					regularWorkSet.incChildNursingCare2, 
					regularWorkSet.notDeductLateleave2, 
					regularWorkSet.additionTime2,
					regularWorkSet.enableSetPerWorkHour1);
			return regularWork;
		}
		return null;
	}
	
	/**
	 * Convert to domain hourly payment add set.
	 *
	 * @param hourPayAaddSet the hour pay aadd set
	 * @return the hourly payment addition set
	 */
	protected HourlyPaymentAdditionSet convertToDomainHourlyPaymentAddSet(KshstHourPayAaddSet hourPayAaddSet) {
		if (hourPayAaddSet != null) {
			HourlyPaymentAdditionSet hourlyPaymentAdditionSet = HourlyPaymentAdditionSet.createFromJavaType(hourPayAaddSet.kshstHourPayAaddSetPK.companyId, 
					hourPayAaddSet.calcPremiumVacation, 
					hourPayAaddSet.addition1, 
					hourPayAaddSet.deformatExcValue, 
					hourPayAaddSet.incChildNursingCare, 
					hourPayAaddSet.deduct, 
					hourPayAaddSet.calculateIncludeIntervalExemptionTime1, 
					hourPayAaddSet.calcWorkHourVacation, 
					hourPayAaddSet.addition2, 
					hourPayAaddSet.calculateIncludCareTime, 
					hourPayAaddSet.notDeductLateLeaveEarly, 
					hourPayAaddSet.calculateIncludeIntervalExemptionTime2, 
					hourPayAaddSet.enableSetPerWorkHour1);
			return hourlyPaymentAdditionSet;
		}
		return null;
		
	}
	
	/**
	 * Convert to domain add set manage work hour.
	 *
	 * @param kshstAddSetManWKHour the kshst add set man WK hour
	 * @return the adds the set manage work hour
	 */
	protected AddSetManageWorkHour convertToDomainAddSetManageWorkHour(KshstAddSetManWKHour kshstAddSetManWKHour) {
		if (kshstAddSetManWKHour != null) {
			AddSetManageWorkHour addSetManageWorkHour = AddSetManageWorkHour.builder()
					.companyId(kshstAddSetManWKHour.kshstAddSetManWKHourPK.companyId)
					.additionSettingOfOvertime(NotUseAtr.valueOf(kshstAddSetManWKHour.addSetOT))
					.build();
			return addSetManageWorkHour;
		}
		return null;
	}


	/**
	 * Convert to db type irregular work.
	 *
	 * @param irregularWork the irregular work
	 * @return the kshst work dep labor set
	 */
	private KshstWorkDepLaborSet convertToDbTypeIrregularWork(WorkDeformedLaborAdditionSet irregularWork) {
			KshstWorkDepLaborSetPK kshstWorkDepLaborSetPK = new KshstWorkDepLaborSetPK(irregularWork.getCompanyId());
			KshstWorkDepLaborSet kshstWorkDepLaborSet;
			Optional<KshstWorkDepLaborSet> optKshstWorkDepLaborSet = this.queryProxy().find(kshstWorkDepLaborSetPK,KshstWorkDepLaborSet.class);
			if (optKshstWorkDepLaborSet.isPresent()) {
				kshstWorkDepLaborSet = optKshstWorkDepLaborSet.get();
			} else {
				kshstWorkDepLaborSet = new KshstWorkDepLaborSet();
			}
			
			PremiumHolidayCalcMethod premiumHolidayCalcMethod = irregularWork.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday();
			PremiumCalcMethodDetailOfHoliday advanceSetPre = premiumHolidayCalcMethod.getAdvanceSet().get();
			DeductLeaveEarly deductLeaveEarly = advanceSetPre.getNotDeductLateLeaveEarly();
			IncludeHolidaysPremiumCalcDetailSet includeHolidaysPremiumCalcDetailSet = advanceSetPre.getIncludeVacationSet();
			
			WorkTimeHolidayCalcMethod workTimeHolidayCalcMethod = irregularWork.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday();
			WorkTimeCalcMethodDetailOfHoliday advanceSetWork = workTimeHolidayCalcMethod.getAdvancedSet().get();
			EmploymentCalcDetailedSetIncludeVacationAmount includeVacationSet = advanceSetWork.getIncludeVacationSet();
			
			kshstWorkDepLaborSet.calcActualOperation1 = premiumHolidayCalcMethod.getCalculateActualOperation().value;
			kshstWorkDepLaborSet.exemptTaxTime1 = advanceSetPre.getCalculateIncludIntervalExemptionTime().value;
			kshstWorkDepLaborSet.incChildNursingCare1 = advanceSetPre.getCalculateIncludCareTime().value;
			kshstWorkDepLaborSet.additionTime1 = includeHolidaysPremiumCalcDetailSet.getAddition().value;
			kshstWorkDepLaborSet.notDeductLateleave1 = deductLeaveEarly.isDeduct() == true ? 1 : 0;
			kshstWorkDepLaborSet.deformatExcValue = includeHolidaysPremiumCalcDetailSet.getDeformationExceedsPredeterminedValue().get().value;
			kshstWorkDepLaborSet.exemptTaxTime2 = advanceSetWork.getCalculateIncludIntervalExemptionTime().value;
			kshstWorkDepLaborSet.minusAbsenceTime2 = advanceSetWork.getMinusAbsenceTime().get().value;
			kshstWorkDepLaborSet.calcActualOperation2 = workTimeHolidayCalcMethod.getCalculateActualOperation().value;
			kshstWorkDepLaborSet.incChildNursingCare2 = advanceSetWork.getCalculateIncludCareTime().value;
			kshstWorkDepLaborSet.notDeductLateleave2 = advanceSetWork.getNotDeductLateLeaveEarly().isDeduct() == true ? 1 : 0;
			kshstWorkDepLaborSet.additionTime2 = includeVacationSet.getAddition().value;
			kshstWorkDepLaborSet.enableSetPerWorkHour1 = deductLeaveEarly.isEnableSetPerWorkHour() == true ? 1 : 0;
			kshstWorkDepLaborSet.enableSetPerWorkHour2 = kshstWorkDepLaborSet.enableSetPerWorkHour1;
			
			kshstWorkDepLaborSet.kshstWorkDepLaborSetPK = kshstWorkDepLaborSetPK;
		return kshstWorkDepLaborSet;
	}
	
	/**
	 * Convert to db type hour pay aadd set.
	 *
	 * @param hourlyPaymentAdditionSet the hourly payment addition set
	 * @return the kshst hour pay aadd set
	 */
	private KshstHourPayAaddSet convertToDbTypeHourPayAaddSet(HourlyPaymentAdditionSet hourlyPaymentAdditionSet) {
		KshstHourPayAaddSetPK kshstHourPayAaddSetPK = new KshstHourPayAaddSetPK(hourlyPaymentAdditionSet.getCompanyId());
		KshstHourPayAaddSet kshstHourPayAaddSet;
		Optional<KshstHourPayAaddSet> optKshstHourPayAaddSet = this.queryProxy().find(kshstHourPayAaddSetPK,KshstHourPayAaddSet.class);
		if (optKshstHourPayAaddSet.isPresent()) {
			kshstHourPayAaddSet = optKshstHourPayAaddSet.get();
		} else {
			kshstHourPayAaddSet = new KshstHourPayAaddSet();
		}
		
		PremiumHolidayCalcMethod premiumHolidayCalcMethod = hourlyPaymentAdditionSet.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday();
		PremiumCalcMethodDetailOfHoliday advanceSetPre = premiumHolidayCalcMethod.getAdvanceSet().get();
		DeductLeaveEarly deductLeaveEarly = advanceSetPre.getNotDeductLateLeaveEarly();
		IncludeHolidaysPremiumCalcDetailSet includeHolidaysPremiumCalcDetailSet = advanceSetPre.getIncludeVacationSet();
		
		WorkTimeHolidayCalcMethod workTimeHolidayCalcMethod = hourlyPaymentAdditionSet.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday();
		WorkTimeCalcMethodDetailOfHoliday advanceSetWork = workTimeHolidayCalcMethod.getAdvancedSet().get();
		EmploymentCalcDetailedSetIncludeVacationAmount includeVacationSet = advanceSetWork.getIncludeVacationSet();
		
		kshstHourPayAaddSet.calcPremiumVacation = premiumHolidayCalcMethod.getCalculateActualOperation().value;
		kshstHourPayAaddSet.addition1 = includeHolidaysPremiumCalcDetailSet.getAddition().value;
		kshstHourPayAaddSet.deformatExcValue = includeHolidaysPremiumCalcDetailSet.getDeformationExceedsPredeterminedValue().get().value;
		kshstHourPayAaddSet.incChildNursingCare = advanceSetPre.getCalculateIncludCareTime().value;
		kshstHourPayAaddSet.deduct = deductLeaveEarly.isDeduct() == true ? 1 : 0;
		kshstHourPayAaddSet.calculateIncludeIntervalExemptionTime1 = advanceSetPre.getCalculateIncludIntervalExemptionTime().value;
		kshstHourPayAaddSet.calcWorkHourVacation = workTimeHolidayCalcMethod.getCalculateActualOperation().value;
		kshstHourPayAaddSet.addition2 = includeVacationSet.getAddition().value;
		kshstHourPayAaddSet.calculateIncludCareTime = advanceSetWork.getCalculateIncludCareTime().value;
		kshstHourPayAaddSet.notDeductLateLeaveEarly = advanceSetWork.getNotDeductLateLeaveEarly().isDeduct() == true ? 1 : 0;
		kshstHourPayAaddSet.calculateIncludeIntervalExemptionTime2 = advanceSetWork.getCalculateIncludIntervalExemptionTime().value;
		kshstHourPayAaddSet.enableSetPerWorkHour1 = deductLeaveEarly.isEnableSetPerWorkHour() == true ? 1 : 0;
		kshstHourPayAaddSet.enableSetPerWorkHour2 = kshstHourPayAaddSet.enableSetPerWorkHour1;
		kshstHourPayAaddSet.kshstHourPayAaddSetPK = kshstHourPayAaddSetPK;
		return kshstHourPayAaddSet;
}

	/**
	 * Convert to db type flex work.
	 *
	 * @param flexWork the flex work
	 * @return the kshst work flex set
	 */
	private KshmtCalcCAddHdFle convertToDbTypeFlexWork(WorkFlexAdditionSet flexWork) {
		KshmtCalcCAddHdFlePK kshstFlexWorkSetPK = new KshmtCalcCAddHdFlePK(flexWork.getCompanyId());
		KshmtCalcCAddHdFle kshstFlexWorkSet;
		Optional<KshmtCalcCAddHdFle> optKshmtCalcCAddHdFle = this.queryProxy().find(kshstFlexWorkSetPK,KshmtCalcCAddHdFle.class);
		if (optKshmtCalcCAddHdFle.isPresent()) {
			kshstFlexWorkSet = optKshmtCalcCAddHdFle.get();
		} else {
			kshstFlexWorkSet = new KshmtCalcCAddHdFle();
		}
			
		PremiumHolidayCalcMethod premiumHolidayCalcMethod = flexWork.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday();
		PremiumCalcMethodDetailOfHoliday advanceSetPre = premiumHolidayCalcMethod.getAdvanceSet().get();
		DeductLeaveEarly deductLeaveEarly = advanceSetPre.getNotDeductLateLeaveEarly();
		IncludeHolidaysPremiumCalcDetailSet includeHolidaysPremiumCalcDetailSet = advanceSetPre.getIncludeVacationSet();
		
		WorkTimeHolidayCalcMethod workTimeHolidayCalcMethod = flexWork.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday();
		WorkTimeCalcMethodDetailOfHoliday advanceSetWork = workTimeHolidayCalcMethod.getAdvancedSet().get();
		EmploymentCalcDetailedSetIncludeVacationAmount includeVacationSet = advanceSetWork.getIncludeVacationSet();
		
		kshstFlexWorkSet.calcActualOperation1 = premiumHolidayCalcMethod.getCalculateActualOperation().value;
		kshstFlexWorkSet.exemptTaxTime1 = advanceSetPre.getCalculateIncludIntervalExemptionTime().value;
		kshstFlexWorkSet.incChildNursingCare1 = advanceSetPre.getCalculateIncludCareTime().value;
		kshstFlexWorkSet.predeterminedOvertime1 = includeHolidaysPremiumCalcDetailSet.getPredeterminedExcessTimeOfFlex().get().value;
		kshstFlexWorkSet.additionTime1  = includeHolidaysPremiumCalcDetailSet.getAddition().value;
		kshstFlexWorkSet.notDeductLateleave1 = deductLeaveEarly.isDeduct() == true ? 1 : 0;
		kshstFlexWorkSet.exemptTaxTime2 = advanceSetWork.getCalculateIncludIntervalExemptionTime().value;
		kshstFlexWorkSet.minusAbsenceTime2 = advanceSetWork.getMinusAbsenceTime().get().value;
		kshstFlexWorkSet.calcActualOperation2 = workTimeHolidayCalcMethod.getCalculateActualOperation().value;
		kshstFlexWorkSet.incChildNursingCare2 = advanceSetWork.getCalculateIncludCareTime().value;
		kshstFlexWorkSet.notDeductLateleave2 = advanceSetWork.getNotDeductLateLeaveEarly().isDeduct() == true ? 1 : 0;
		kshstFlexWorkSet.predeterminDeficiency2 = includeVacationSet.getPredeterminedDeficiencyOfFlex().get().value;
		kshstFlexWorkSet.additionTime2 = includeVacationSet.getAddition().value;
		kshstFlexWorkSet.enableSetPerWorkHour1 = deductLeaveEarly.isEnableSetPerWorkHour() == true ? 1 : 0;
		kshstFlexWorkSet.enableSetPerWorkHour2 = kshstFlexWorkSet.enableSetPerWorkHour1;
		kshstFlexWorkSet.additionWithinMonthlyStatutory = includeVacationSet.getAdditionWithinMonthlyStatutory().get().value;
		
		kshstFlexWorkSet.kshstFlexWorkSetPK = kshstFlexWorkSetPK;
		return kshstFlexWorkSet;
	}
	
	private KshstAddSetManWKHour convertToDbTypeAddSetManWKHour(AddSetManageWorkHour addSetManageWorkHour) {
		
		KshstAddSetManWKHourPK kshstAddSetManWKHourPK = new KshstAddSetManWKHourPK(addSetManageWorkHour.getCompanyId());
		KshstAddSetManWKHour kshstAddSetManWKHour;
		Optional<KshstAddSetManWKHour> optKshstAddSetManWKHour = this.queryProxy().find(kshstAddSetManWKHourPK, KshstAddSetManWKHour.class);
		if (optKshstAddSetManWKHour.isPresent()) {
			kshstAddSetManWKHour = optKshstAddSetManWKHour.get();
		} else {
			kshstAddSetManWKHour = new KshstAddSetManWKHour(); 
		}
		kshstAddSetManWKHour.addSetOT = addSetManageWorkHour.getAdditionSettingOfOvertime().value;
		kshstAddSetManWKHour.kshstAddSetManWKHourPK = kshstAddSetManWKHourPK;
		
		return kshstAddSetManWKHour;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository#findByCompanyId(java.lang.String)
	 */
	@Override
	public Map<String, AggregateRoot> findByCompanyId(String companyId) {
		Optional<KshstHolidayAdditionSet> entity = this.queryProxy().query(SELECT_BY_CID, KshstHolidayAdditionSet.class)
																		.setParameter("companyId", companyId)
																		.getSingle();
		if (entity.isPresent()) {
			return convertToDomain(entity.get());
		}
		return new HashMap<>();
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository#findByCId(java.lang.String)
	 */
	@Override
	public Optional<HolidayAddtionSet> findByCId(String companyId) {
		return this.queryProxy().find(new KshstHolidayAdditionSetPK(companyId),KshstHolidayAdditionSet.class)
				.map(c->convertToDomainHolidayAdditionSet(c));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository#add(nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.AddSetManageWorkHour, nts.uk.ctx.at.shared.dom.calculation.holiday.HourlyPaymentAdditionSet)
	 */
	@Override
	public void add(HolidayAddtionSet holidayAddtime, WorkRegularAdditionSet regularAdditionSet,
						WorkFlexAdditionSet flexAdditionSet, WorkDeformedLaborAdditionSet deformedLaborAdditionSet,
						AddSetManageWorkHour addSetManageWorkHour, HourlyPaymentAdditionSet hourlyPaymentAdditionSet) {

		KshstHolidayAdditionSetPK kshstHolidayAddtimeSetPK = new KshstHolidayAdditionSetPK(holidayAddtime.getCompanyId());
		Optional<KshstHolidayAdditionSet> optKshstHolidayAdditionSet = this.queryProxy().find(kshstHolidayAddtimeSetPK,KshstHolidayAdditionSet.class);
		KshstHolidayAdditionSet kshstHolidayAddtimeSet;
		if (optKshstHolidayAdditionSet.isPresent()) {
			kshstHolidayAddtimeSet = optKshstHolidayAdditionSet.get();
		} else {
			kshstHolidayAddtimeSet = new KshstHolidayAdditionSet();
		}
		kshstHolidayAddtimeSet.kshstHolidayAddtimeSetPK = kshstHolidayAddtimeSetPK;
	
		kshstHolidayAddtimeSet.referComHolidayTime = holidayAddtime.getWorkRecord().get().getTimeReferenceDestination().get().value;
		kshstHolidayAddtimeSet.oneDay = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getOneDay().v());
		kshstHolidayAddtimeSet.morning = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getMorning().v());
		kshstHolidayAddtimeSet.afternoon = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getAfternoon().v());
		kshstHolidayAddtimeSet.referActualWorkHours = holidayAddtime.getReferActualWorkHours().value;
		kshstHolidayAddtimeSet.notReferringAch = holidayAddtime.getEmployeeInformation().get().getTimeReferenceDestination().value;
		kshstHolidayAddtimeSet.annualHoliday = holidayAddtime.getAdditionVacationSet().getAnnualHoliday().value;
		kshstHolidayAddtimeSet.specialHoliday = holidayAddtime.getAdditionVacationSet().getSpecialHoliday().value;
		kshstHolidayAddtimeSet.yearlyReserved = holidayAddtime.getAdditionVacationSet().getYearlyReserved().value;
		
		kshstHolidayAddtimeSet.regularWorkSet = convertToDbTypeRegularWork(regularAdditionSet);
		kshstHolidayAddtimeSet.flexWorkSet = convertToDbTypeFlexWork(flexAdditionSet);
		kshstHolidayAddtimeSet.irregularWorkSet = convertToDbTypeIrregularWork(deformedLaborAdditionSet);
		kshstHolidayAddtimeSet.hourPayAaddSet = convertToDbTypeHourPayAaddSet(hourlyPaymentAdditionSet);
		kshstHolidayAddtimeSet.addSetManWKHour = convertToDbTypeAddSetManWKHour(addSetManageWorkHour);
		
		kshstHolidayAddtimeSet.addingMethod1 = holidayAddtime.getTimeHolidayAddition().get(0).getAddingMethod().value;
		kshstHolidayAddtimeSet.workClass1 = holidayAddtime.getTimeHolidayAddition().get(0).getWorkClass().value;
		kshstHolidayAddtimeSet.addingMethod2 = holidayAddtime.getTimeHolidayAddition().get(1).getAddingMethod().value;
		kshstHolidayAddtimeSet.workClass2 = holidayAddtime.getTimeHolidayAddition().get(1).getWorkClass().value;
		
		this.commandProxy().insert(kshstHolidayAddtimeSet);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionRepository#update(nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet, nts.uk.ctx.at.shared.dom.calculation.holiday.AddSetManageWorkHour, nts.uk.ctx.at.shared.dom.calculation.holiday.HourlyPaymentAdditionSet)
	 */
	@Override
	public void update(HolidayAddtionSet holidayAddtime, WorkRegularAdditionSet regularAdditionSet,
			WorkFlexAdditionSet flexAdditionSet, WorkDeformedLaborAdditionSet deformedLaborAdditionSet,
			AddSetManageWorkHour addSetManageWorkHour, HourlyPaymentAdditionSet hourlyPaymentAdditionSet) {
		
		KshstHolidayAdditionSetPK primaryKey = new KshstHolidayAdditionSetPK(holidayAddtime.getCompanyId());
		KshstHolidayAdditionSet entity = this.queryProxy().find(primaryKey, KshstHolidayAdditionSet.class).get();
			entity.referComHolidayTime = holidayAddtime.getWorkRecord().get().getTimeReferenceDestination().get().value;
			entity.oneDay = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getOneDay().v());
			entity.morning = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getMorning().v());
			entity.afternoon = new BigDecimal(holidayAddtime.getWorkRecord().get().getAdditionTimeCompany().get().getAfternoon().v());
			entity.referActualWorkHours = holidayAddtime.getReferActualWorkHours().value;
			entity.notReferringAch = holidayAddtime.getEmployeeInformation().get().getTimeReferenceDestination().value;
			entity.annualHoliday = holidayAddtime.getAdditionVacationSet().getAnnualHoliday().value;
			entity.specialHoliday = holidayAddtime.getAdditionVacationSet().getSpecialHoliday().value;
			entity.yearlyReserved = holidayAddtime.getAdditionVacationSet().getYearlyReserved().value;;
			
			entity.regularWorkSet = convertToDbTypeRegularWork(regularAdditionSet);
			entity.flexWorkSet = convertToDbTypeFlexWork(flexAdditionSet);
			entity.irregularWorkSet = convertToDbTypeIrregularWork(deformedLaborAdditionSet);
			entity.hourPayAaddSet = convertToDbTypeHourPayAaddSet(hourlyPaymentAdditionSet);
			entity.addSetManWKHour = convertToDbTypeAddSetManWKHour(addSetManageWorkHour);
			
			entity.addingMethod1 = holidayAddtime.getTimeHolidayAddition().get(0).getAddingMethod().value;
			entity.workClass1 = holidayAddtime.getTimeHolidayAddition().get(0).getWorkClass().value;
			entity.addingMethod2 = holidayAddtime.getTimeHolidayAddition().get(1).getAddingMethod().value;
			entity.workClass2 = holidayAddtime.getTimeHolidayAddition().get(1).getWorkClass().value;
			entity.kshstHolidayAddtimeSetPK = primaryKey;
	this.commandProxy().update(entity);
	}
}
