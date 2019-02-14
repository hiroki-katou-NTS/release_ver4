package nts.uk.screen.at.app.dailymodify.command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSttMonthEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.EmpPerformMonthParamImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.ParamRegisterConfirmMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.RegisterConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.algorithm.SelfConfirm;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class PersonalTightCommandFacade {
	@Inject
	private ShClosurePub shClosurePub;

	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private RegisterConfirmationMonth registerConfirmationMonth;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	public void insertPersonalTight(String employeeId, GeneralDate date) {
		String companyId = AppContexts.user().companyId();
		Optional<ClosureEmployment> closureEmploymentOptional = this.closureEmploymentRepository
				.findByEmploymentCD(companyId, getEmploymentCode(companyId, date, employeeId));
		if (closureEmploymentOptional.isPresent()) {
			Optional<PresentClosingPeriodExport> closingPeriod = shClosurePub.find(companyId,
					closureEmploymentOptional.get().getClosureId(), date);
			if (closingPeriod.isPresent() && (closingPeriod.get().getClosureStartDate().beforeOrEquals(date) && closingPeriod.get().getClosureEndDate().afterOrEquals(date))) {
				registerConfirmationMonth.registerConfirmationMonth(new ParamRegisterConfirmMonth(
						closingPeriod.get().getProcessingYm(), Arrays.asList(new SelfConfirm(employeeId, true)),
						closureEmploymentOptional.get().getClosureId(), new ClosureDate(closingPeriod.get().getClosureDate().getClosureDay(),
								closingPeriod.get().getClosureDate().getLastDayOfMonth()),
						GeneralDate.today()));
			}
		}
	}
	
	public String releasePersonalTight(String employeeId, GeneralDate date) {
		String companyId = AppContexts.user().companyId();
		Optional<ClosureEmployment> closureEmploymentOptional = this.closureEmploymentRepository
				.findByEmploymentCD(companyId, getEmploymentCode(companyId, date, employeeId));
		if (closureEmploymentOptional.isPresent()) {
			Optional<PresentClosingPeriodExport> closingPeriod = shClosurePub.find(companyId,
					closureEmploymentOptional.get().getClosureId(), date);
			 List<AppRootSttMonthEmpImport> lstAppStt =  approvalStatusAdapter
					.getAppRootStatusByEmpsMonth(
							Arrays.asList(new EmpPerformMonthParamImport(closingPeriod.get().getProcessingYm(),
									closureEmploymentOptional.get().getClosureId(),
									new ClosureDate(closingPeriod.get().getClosureDate().getClosureDay(),
											closingPeriod.get().getClosureDate().getLastDayOfMonth()),
									date, employeeId)));
			if(lstAppStt.isEmpty() ||  lstAppStt.get(0).getApprovalStatus() != ApprovalStatusForEmployee.UNAPPROVED) return "Msg_1501";
			if (closingPeriod.isPresent() && (closingPeriod.get().getClosureStartDate().beforeOrEquals(date)
					&& closingPeriod.get().getClosureEndDate().afterOrEquals(date))) {
				registerConfirmationMonth
						.registerConfirmationMonth(new ParamRegisterConfirmMonth(closingPeriod.get().getProcessingYm(),
								Arrays.asList(new SelfConfirm(employeeId, false)),
								closureEmploymentOptional.get().getClosureId(),
								new ClosureDate(closingPeriod.get().getClosureDate().getClosureDay(),
										closingPeriod.get().getClosureDate().getLastDayOfMonth()),
								GeneralDate.today()));
				return "";
			}
		}
		return "";
	}
	
	private String getEmploymentCode(String companyId, GeneralDate date, String sId) {
		AffEmploymentHistoryDto employment = repo.getAffEmploymentHistory(companyId, sId, date);
		return employment == null ? "" : employment.getEmploymentCode();
	}
}
