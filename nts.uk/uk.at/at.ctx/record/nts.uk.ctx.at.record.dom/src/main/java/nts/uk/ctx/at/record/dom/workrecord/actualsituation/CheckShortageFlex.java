package nts.uk.ctx.at.record.dom.workrecord.actualsituation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.approvaldaily.CheckApprovalDayComplete;
//import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.approvaldaily.CheckApprovalTargetMonth;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export.CheckIndentityDayConfirm;
//import nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export.CheckIndentityMonthConfirm;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.param.ApprovalDayComplete;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.CurrentMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 * フレックス不足の相殺が実施できるかチェックする
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CheckShortageFlex {
	
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	
	@Inject
	private CheckApprovalDayComplete checkApprovalDayComplete;
	
	@Inject
	private CheckIndentityDayConfirm checkIndentityDayConfirm;
	
//	@Inject
//	private CheckApprovalTargetMonth checkApprovalTargetMonth;
//	
//	@Inject
//	private CheckIndentityMonthConfirm checkIndentityMonthConfirm;
	
	/** The employment adapter. */
	@Inject
	private ShareEmploymentAdapter employmentAdapter;
	
	/** The closure employment repository. */
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	/** The closure repository. */
	@Inject
	private ClosureRepository closureRepository;
	
	/** The closure service. */
	@Inject
	private ClosureService closureService;
	
	public CheckShortage checkShortageFlex(String employeeId, GeneralDate date){

		CheckShortage resultCheck = CheckShortage.defaultShortage(false);
		// TODO 社員に対応する処理締めを取得する
		DatePeriod datePeriod = findClosurePeriod(employeeId, date);
		if(datePeriod == null) return resultCheck;
		// パラメータ「基準日」がパラメータ「当月の期間」に含まれているかチェックする
		if (date.before(datePeriod.start()) || date.after(datePeriod.end()))
			return resultCheck;
		List<AffCompanyHistImport> affCompanyHis = syCompanyRecordAdapter
				.getAffCompanyHistByEmployee(Arrays.asList(employeeId), datePeriod);
		if (!affCompanyHis.isEmpty()) {
			for (AffComHistItemImport his : affCompanyHis.get(0).getLstAffComHistItem()) {
				DatePeriod datePeriodHis = his.getDatePeriod();
				GeneralDate endDate = datePeriodHis.end();
				//「所属会社履歴項目．退職日」がパラメータ「当月の期間」に含まれているかチェックする
				if (endDate != null
						&& (endDate.afterOrEquals(datePeriod.start()) && endDate.beforeOrEquals(datePeriod.end()))) {
					datePeriod = new DatePeriod(datePeriod.start(), endDate);
					resultCheck.createRetiredFlag(true);
				}
			}
		}
		//bug 107966 disable edit flex
		resultCheck.createPeriodCheck(datePeriod);
		// TODO 対象期間の日の承認が済んでいるかチェックする
		Optional<ApprovalDayComplete> approvalOpt = checkApprovalDayComplete.checkApprovalDayComplete(employeeId, datePeriod);
		if (approvalOpt.isPresent() && !approvalOpt.get().isApproved()) {
			/// TODO 対象日の本人確認が済んでいるかチェックする
			return resultCheck.createCheckShortage(checkIndentityDayConfirm.checkIndentityDay(employeeId, approvalOpt.get().getDate()));
		}
		// TODO 対象月の承認が済んでいるかチェックする
		//boolean checkMonth = checkApprovalTargetMonth.checkApprovalTargetMonth(employeeId, date);
		//if (checkMonth)
		return resultCheck.createCheckShortage(true);
		//return resultCheck.createCheckShortage(checkIndentityMonthConfirm.checkIndentityMonth(employeeId, date));
	}
	
	/**
	 * Find closure period.
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the date period
	 */
	// 社員に対応する締め期間を取得する.
	public DatePeriod findClosurePeriod(String employeeId, GeneralDate baseDate) {
		// 社員に対応する処理締めを取得する.
		Optional<Closure> closure = this.findClosureByEmployee(employeeId, baseDate);
		if(!closure.isPresent()) {
			return null;
		}
		CurrentMonth currentMonth = closure.get().getClosureMonth();
		
		// 当月の期間を算出する.
		return this.closureService.getClosurePeriod(closure.get(), currentMonth.getProcessingYm());
	}
	
	/**
	 * Find employment closure.
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	//社員に対応する処理締めを取得する
	public Optional<Closure> findClosureByEmployee(String employeeId, GeneralDate baseDate) {
		String companyId = AppContexts.user().companyId();
		
		// Find Employment History by employeeId and base date.
		Optional<BsEmploymentHistoryImport> empHistOpt = this.employmentAdapter
				.findEmploymentHistory(companyId, employeeId, baseDate);
		
		if (!empHistOpt.isPresent()) {
			return Optional.empty();
		}
		
		// Find closure employment by emp code.
		Optional<ClosureEmployment> closureEmpOpt = this.closureEmploymentRepository
				.findByEmploymentCD(companyId, empHistOpt.get().getEmploymentCode());
		if(!closureEmpOpt.isPresent()) return Optional.empty();
		// Find closure.
		return this.closureRepository.findById(companyId, closureEmpOpt.get().getClosureId());
	}
}
