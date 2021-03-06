package nts.uk.ctx.at.request.app.command.application.holidayshipment.refactor5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.command.application.kdl035.HolidayWorkAssociationStart;
import nts.uk.ctx.at.request.app.command.application.kdl035.Kdl035InputData;
import nts.uk.ctx.at.request.app.command.application.kdl035.Kdl035OutputData;
import nts.uk.ctx.at.request.app.command.application.kdl036.HolidayAssociationStart;
import nts.uk.ctx.at.request.app.command.application.kdl036.Kdl036InputData;
import nts.uk.ctx.at.request.app.command.application.kdl036.Kdl036OutputData;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.refactor5.dto.DisplayInforWhenStarting;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ActualContentDisplay;
import nts.uk.ctx.at.request.dom.application.common.service.setting.CommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.shared.app.find.remainingnumber.paymana.PayoutSubofHDManagementDto;
import nts.uk.ctx.at.shared.app.find.remainingnumber.subhdmana.dto.LeaveComDayOffManaDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutSubofHDManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveComDayOffManagement;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

/**
 * @author thanhpv
 * @URL UKDesign.UniversalK.??????.KAF_??????.KAF011_??????????????????.B?????????????????????????????????.??????????????????.???????????????.??????????????????????????????????????????????????????.??????????????????????????????????????????????????????
 * 
 */
@Stateless
public class PreUpdateErrorCheck {
	
	@Inject
	private PreRegistrationErrorCheck preRegistrationErrorCheck;
	
	@Inject
	private ErrorCheckProcessingBeforeRegistrationKAF011 errorCheckBeforeRegistrationKAF011;
	
	@Inject
	private CommonAlgorithm commonAlgorithm;
	
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
    private HolidayAssociationStart holidayAssociationStart;
	
	@Inject
    private HolidayWorkAssociationStart holidayWorkAssociationStart;
	
	/**
	 * ??????????????????????????????????????????????????????
	 * @param companyId ???????????????ID
	 * @param abs ????????????
	 * @param rec ????????????
	 * @param displayInforWhenStarting ??????????????????????????????????????????
	 */
	public void errorCheck(String companyId, Optional<AbsenceLeaveApp> abs, Optional<RecruitmentApp> rec, DisplayInforWhenStarting displayInforWhenStarting, 
	        List<PayoutSubofHDManagement> payoutSubofHDManagements, List<LeaveComDayOffManagement> leaveComDayOffManagements, 
	        boolean checkFlag, List<WorkType> listWorkTypes) {
		//?????????????????????????????????????????????????????????????????????????????????
		this.preRegistrationErrorCheck.preconditionCheck(abs, rec, 
		        Optional.ofNullable(displayInforWhenStarting.getApplicationForHoliday() == null ? null : displayInforWhenStarting.getApplicationForHoliday().getWorkInformationForApplication()), 
		        Optional.ofNullable(displayInforWhenStarting.getApplicationForWorkingDay() == null ? null : displayInforWhenStarting.getApplicationForWorkingDay().getWorkInformationForApplication()));
		
		//??????????????????????????????
		this.preRegistrationErrorCheck.allDayAndHalfDayContradictionCheck(companyId, abs, rec);
		
		List<GeneralDate> dateLst = new ArrayList<>();
		List<String> workTypeLst = new ArrayList<>();
		if(rec.isPresent()) {
			dateLst.add(rec.get().getAppDate().getApplicationDate());
			workTypeLst.add(rec.get().getWorkInformation().getWorkTypeCode().v());
		}
		if(abs.isPresent()) {
			dateLst.add(abs.get().getAppDate().getApplicationDate());
			workTypeLst.add(abs.get().getWorkInformation().getWorkTypeCode().v());
		}
		//???????????????????????????
		this.commonAlgorithm.appConflictCheck(companyId,
				displayInforWhenStarting.appDispInfoStartup.getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0),
				dateLst, workTypeLst,
				displayInforWhenStarting.appDispInfoStartup.toDomain().getAppDispInfoWithDateOutput()
						.getOpActualContentDisplayLst().orElse(new ArrayList<ActualContentDisplay>()));

		boolean existFlag = false;
		if (rec.isPresent() && abs.isPresent()) {
		    existFlag = true;
		}
		//??????????????????????????????
//		 this.errorCheckBeforeRegistrationKAF011.checkForInsufficientNumberOfHolidays(companyId, rec.isPresent()?rec.get().getEmployeeID():abs.get().getEmployeeID(), abs, rec);
		// INPUT??????????????????NULL???AND???INPUT??????????????????NULL???????????????AND???INPUT.????????????.??????????????????????????????????????????
        if (!rec.isPresent() && abs.isPresent() && checkFlag && payoutSubofHDManagements.isEmpty()) {
            throw new BusinessException("Msg_2223");
        }
	 
		 if(rec.isPresent()) {

			 //????????????????????????????????????????????????????????????????????????
			 this.detailBeforeUpdate.processBeforeDetailScreenRegistration(companyId, 
					 rec.get().getEmployeeID(), 
					 rec.get().getAppDate().getApplicationDate(), 
					 EmploymentRootAtr.APPLICATION.value, 
					 rec.get().getAppID(), 
					 rec.get().getPrePostAtr(), 
					 displayInforWhenStarting.appDispInfoStartup.getAppDetailScreenInfo().getApplication().getVersion(), 
					 null,
					 null,
					 displayInforWhenStarting.appDispInfoStartup.toDomain(), 
					 Arrays.asList(rec.get().getWorkInformation().getWorkTypeCode().v()), 
					 Optional.empty(), 
					 existFlag, 
					 Optional.of(rec.get().getWorkInformation().getWorkTypeCode().v()), 
					 rec.get().getWorkInformation().getWorkTimeCodeNotNull().map(WorkTimeCode::v));
		 }
		 if(abs.isPresent()) {
			 //????????????????????????????????????????????????????????????????????????
			 this.detailBeforeUpdate.processBeforeDetailScreenRegistration(companyId, 
					 abs.get().getEmployeeID(), 
					 abs.get().getAppDate().getApplicationDate(), 
					 EmploymentRootAtr.APPLICATION.value, 
					 abs.get().getAppID(), 
					 abs.get().getPrePostAtr(), 
					 displayInforWhenStarting.appDispInfoStartup.getAppDetailScreenInfo().getApplication().getVersion(), 
					 null,
					 null,
					 displayInforWhenStarting.appDispInfoStartup.toDomain(), 
					 Arrays.asList(abs.get().getWorkInformation().getWorkTypeCode().v()), 
					 Optional.empty(), 
					 existFlag, 
					 Optional.of(abs.get().getWorkInformation().getWorkTypeCode().v()), 
					 abs.get().getWorkInformation().getWorkTimeCodeNotNull().map(WorkTimeCode::v));
			 
			 Optional<WorkType> workType = listWorkTypes.stream().filter(x -> x.getWorkTypeCode().v().equals(abs.get().getWorkInformation().getWorkTypeCode().v())).findFirst();
			 if (workType.isPresent() && isHolidayWorkType(workType.get()) && !rec.isPresent()) {
                 // ?????????????????????????????????????????????
                 Kdl036OutputData output = holidayAssociationStart.init(new Kdl036InputData(
                         abs.get().getEmployeeID(), 
                         abs.get().getAppDate().getApplicationDate(), 
                         abs.get().getAppDate().getApplicationDate(), 
                         workType.get().getDailyWork().getWorkTypeUnit().equals(WorkTypeUnit.OneDay) ? 1 : 0, 
                         1,
                         displayInforWhenStarting.getAppDispInfoStartup().getAppDispInfoWithDateOutput().getOpActualContentDisplayLst(), 
                         new ArrayList<LeaveComDayOffManaDto>()));
                 
                 // ?????????????????????AND???INPUT.????????????.??????????????????????????????Empty 
                 if (!output.getHolidayWorkInfoList().isEmpty() && leaveComDayOffManagements.isEmpty()) {
                     throw new BusinessException("Msg_3255");
                 }
             }
			 
			 if (workType.isPresent() && isPauseWorkType(workType.get()) && !rec.isPresent()) {
			     // ?????????????????????????????????????????????
			     Kdl035OutputData kdl035output = holidayWorkAssociationStart.init(new Kdl035InputData(
			             abs.get().getEmployeeID(), 
			             abs.get().getAppDate().getApplicationDate(), 
			             abs.get().getAppDate().getApplicationDate(), 
			             workType.get().getDailyWork().getWorkTypeUnit().equals(WorkTypeUnit.OneDay) ? 1 : 0, 
			                     1,
			                     displayInforWhenStarting.getAppDispInfoStartup().getAppDispInfoWithDateOutput().getOpActualContentDisplayLst(), 
			                     new ArrayList<PayoutSubofHDManagementDto>()));
			     
			     if (!kdl035output.getSubstituteWorkInfoList().isEmpty() && payoutSubofHDManagements.isEmpty()) {
			         throw new BusinessException("Msg_2223");
			     }
			 }
		 }
		
	}
	
	public boolean isHolidayWorkType(WorkType workType) {
        WorkTypeUnit workTypeUnit = workType.getDailyWork().getWorkTypeUnit();
        if (workTypeUnit.equals(WorkTypeUnit.MonringAndAfternoon)) {
            return workType.getDailyWork().getMorning().equals(WorkTypeClassification.SubstituteHoliday) || workType.getDailyWork().getAfternoon().equals(WorkTypeClassification.SubstituteHoliday);
        }
        return false;
    }
	
	public boolean isPauseWorkType(WorkType workType) {
        WorkTypeUnit workTypeUnit = workType.getDailyWork().getWorkTypeUnit();
        if (workTypeUnit.equals(WorkTypeUnit.OneDay)) {
            return workType.getDailyWork().getOneDay().equals(WorkTypeClassification.Pause);
        } else {
            return workType.getDailyWork().getMorning().equals(WorkTypeClassification.Pause) || workType.getDailyWork().getAfternoon().equals(WorkTypeClassification.Pause);
        }
    }
	
}




