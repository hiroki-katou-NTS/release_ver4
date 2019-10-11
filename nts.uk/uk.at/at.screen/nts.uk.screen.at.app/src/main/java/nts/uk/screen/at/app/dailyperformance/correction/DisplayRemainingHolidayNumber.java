package nts.uk.screen.at.app.dailyperformance.correction;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualholidaymanagement.AnnualHolidayManagementAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualholidaymanagement.NextAnnualLeaveGrantImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnLeaveRemainNumberAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.ReNumAnnLeaReferenceDateImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.rsvleamanager.ReserveLeaveManagerApdater;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.rsvleamanager.rsvimport.RsvLeaManagerImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsenceReruitmentMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AbsenceTenProcess;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AnnualHolidaySetOutput;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.LeaveSetOutput;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.SubstitutionHolidayOutput;
import nts.uk.screen.at.app.dailyperformance.correction.dto.Com60HVacationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.CompensLeaveComDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.HolidayRemainNumberDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ReserveLeaveDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.SubstVacationDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.YearHolidaySettingDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT - 休暇残数を表示する
 *
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DisplayRemainingHolidayNumber {

	@Inject
	private AbsenceTenProcess absenceProc;

	@Inject
	private AnnLeaveRemainNumberAdapter annLeaveRemainAdapter;
	
	@Inject
	private ReserveLeaveManagerApdater rsvLeaveRemainAdapter;
	
	@Inject
	private AnnualHolidayManagementAdapter annualHolidayMng;
	
	@Inject
	private BreakDayOffMngInPeriodQuery brDayOffQuery;
	
	@Inject
	private AbsenceReruitmentMngInPeriodQuery absRecQuery;

	public YearHolidaySettingDto getAnnualLeaveSetting(String companyId, String employeeId, GeneralDate date) {
		AnnualHolidaySetOutput output = absenceProc.getSettingForAnnualHoliday(companyId);
		if (output.isYearHolidayManagerFlg()) {
			//RequestList198
			ReNumAnnLeaReferenceDateImport remainNum = annLeaveRemainAdapter
					.getReferDateAnnualLeaveRemainNumber(employeeId, date);
			return new YearHolidaySettingDto(output.isYearHolidayManagerFlg(), output.isSuspensionTimeYearFlg(),
					remainNum.getAnnualLeaveRemainNumberExport() != null
							? remainNum.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantDay() : 0,
					remainNum.getAnnualLeaveRemainNumberExport() != null
							? remainNum.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantTime()
							: 0);
		} else {
			return new YearHolidaySettingDto(false, false, null, null);
		}
	}

	public ReserveLeaveDto getReserveLeaveSetting(String companyId, String employeeId, GeneralDate date) {
		boolean manageAtr = absenceProc.getSetForYearlyReserved(companyId, employeeId, date);
		if (manageAtr) {
			// call requestlist201
			Optional<RsvLeaManagerImport> optOutput = rsvLeaveRemainAdapter.getRsvLeaveManager(employeeId, date);
			return new ReserveLeaveDto(manageAtr, optOutput.isPresent() && optOutput.get().getReserveLeaveInfo() != null
					? optOutput.get().getReserveLeaveInfo().getRemainingDays() : 0);
		} else
			return new ReserveLeaveDto(false, null);
	}
	
	public SubstVacationDto getSubsitutionVacationSetting(String companyId, String employeeId, GeneralDate date) {
		LeaveSetOutput output = absenceProc.getSetForLeave(companyId, employeeId, date);
		if (output.isSubManageFlag()) {
			double remain = absRecQuery.getAbsRecMngRemain(employeeId, date);
			return new SubstVacationDto(output.isSubManageFlag(), remain);
		}
		return new SubstVacationDto(false, null);
	}

	public CompensLeaveComDto getCompensatoryLeaveSetting(String companyId, String employeeId, GeneralDate date) {
		SubstitutionHolidayOutput output = absenceProc.getSettingForSubstituteHoliday(companyId, employeeId, date);
		if (output != null && output.isSubstitutionFlg()) {
			double remain = brDayOffQuery.getBreakDayOffMngRemain(employeeId, date);
			return new CompensLeaveComDto(output.isSubstitutionFlg(), output.isTimeOfPeriodFlg(), remain, 0);
		} else {
			return new CompensLeaveComDto(false, false, null, null);
		}
	}

	public Com60HVacationDto getCom60HVacationSetting(String companyId, String employeeId, GeneralDate date) {
		Com60HVacationDto output = new Com60HVacationDto("", false, null, null);
		return output;
	}
	
	private GeneralDate getNextGrantDate(String companyId, String employeeId, GeneralDate date) {
		List<NextAnnualLeaveGrantImport> lstOutput = this.annualHolidayMng.acquireNextHolidayGrantDate(companyId, employeeId, date);
		return lstOutput.isEmpty() ? null : lstOutput.get(0).grantDate;
	}
	
	public HolidayRemainNumberDto getRemainingHolidayNumber(String employeeId) {
		String companyId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		HolidayRemainNumberDto result = new HolidayRemainNumberDto();
		result.setAnnualLeave(this.getAnnualLeaveSetting(companyId, employeeId, baseDate));
		result.setCompensatoryLeave(this.getCompensatoryLeaveSetting(companyId, employeeId, baseDate));
		result.setReserveLeave(this.getReserveLeaveSetting(companyId, employeeId, baseDate));
		result.setSubstitutionLeave(this.getSubsitutionVacationSetting(companyId, employeeId, baseDate));
		result.setCom60HVacation(this.getCom60HVacationSetting(companyId, employeeId, baseDate));
		if (result.getAnnualLeave().isManageYearOff())
			result.setNextGrantDate(this.getNextGrantDate(companyId, employeeId, baseDate));
		return result;
	}

}
