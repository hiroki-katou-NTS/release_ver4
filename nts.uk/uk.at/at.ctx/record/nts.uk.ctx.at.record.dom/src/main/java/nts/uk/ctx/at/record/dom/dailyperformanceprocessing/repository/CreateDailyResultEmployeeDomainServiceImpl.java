package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.EmployeeAndClosureOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.organization.EmploymentHistoryImported;
import nts.uk.ctx.at.record.dom.organization.adapter.EmploymentAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLock;
import nts.uk.ctx.at.record.dom.workrecord.actuallock.ActualLockRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureGetMonthDay;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CreateDailyResultEmployeeDomainServiceImpl implements CreateDailyResultEmployeeDomainService {

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ActualLockRepository actualLockRepository;

	@Inject
	private EmploymentAdapter employmentAdapter;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private ReflectWorkInforDomainService reflectWorkInforDomainService;
	
	@Inject
	private ResetDailyPerforDomainService resetDailyPerforDomainService;

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;

	@Override
	public ProcessState createDailyResultEmployee(AsyncCommandHandlerContext asyncContext, String employeeId,
			DatePeriod periodTime, String companyId, String empCalAndSumExecLogID, Optional<ExecutionLog> executionLog, boolean reCreateWorkType) {
				
		// 正常終了 : 0
		// 中断 : 1
		ProcessState status = ProcessState.SUCCESS;

		GeneralDate processingDate = periodTime.start();

		// lits day between startDate and endDate
		List<GeneralDate> listDayBetween = this.getDaysBetween(periodTime.start(), periodTime.end());

		// Imported（就業）「所属雇用履歴」を取得する
		Optional<EmploymentHistoryImported> employmentHisOptional = this.employmentAdapter.getEmpHistBySid(companyId, employeeId, processingDate);
		if (!employmentHisOptional.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("010"), EnumAdaptor.valueOf(0, ExecutionContent.class), processingDate,
					new ErrMessageContent(TextResource.localize("Msg_426")));
			this.errMessageInfoRepository.add(employmentErrMes);
		} else {
			String employmentCode = employmentHisOptional.get().getEmploymentCode();

			for (GeneralDate day : listDayBetween) {
				
				// 締めIDを取得する
				Optional<ClosureEmployment> closureEmploymentOptional = this.closureEmploymentRepository
						.findByEmploymentCD(companyId, employmentCode);

				if (day.afterOrEquals(employmentHisOptional.get().getPeriod().end())
						&& day.beforeOrEquals(employmentHisOptional.get().getPeriod().start())) {
					status = ProcessState.SUCCESS;
				} else {
					EmployeeAndClosureOutput employeeAndClosureDto = new EmployeeAndClosureOutput();
					if (employmentHisOptional.get().getEmploymentCode().equals(closureEmploymentOptional.get()
							.getEmploymentCD())) {
						employeeAndClosureDto.setClosureId(closureEmploymentOptional.get().getClosureId());
						employeeAndClosureDto.setEmployeeId(employeeId);
						employeeAndClosureDto.setPeriod(employmentHisOptional.get().getPeriod());
					}

					// アルゴリズム「実績ロックされているか判定する」を実行する
					EmployeeAndClosureOutput employeeAndClosure = this.determineActualLocked(companyId, employeeAndClosureDto,
							day);
					
					if (employeeAndClosure.getLock() == 0) {
						ExecutionType reCreateAttr = executionLog.get().getDailyCreationSetInfo().get().getExecutionType();
//						
//						if (reCreateAttr == ExecutionType.RERUN) {
//							DailyRecreateClassification creationType = executionLog.get().getDailyCreationSetInfo().get().getCreationType();
//							if (creationType == DailyRecreateClassification.PARTLY_MODIFIED) {
//								// 再設定
////								this.resetDailyPerforDomainService.resetDailyPerformance(companyId, employeeId, day, empCalAndSumExecLogID, reCreateAttr);
//							} else {
//								this.reflectWorkInforDomainService.reflectWorkInformation(companyId, employeeId, day,
//										empCalAndSumExecLogID, reCreateAttr, reCreateWorkType);
//							}
//						} else{
							this.reflectWorkInforDomainService.reflectWorkInformation(companyId, employeeId, day,
									empCalAndSumExecLogID, reCreateAttr, reCreateWorkType);
//						}
					} 
					if (asyncContext.hasBeenRequestedToCancel()) {
						asyncContext.finishedAsCancelled();
						status = ProcessState.INTERRUPTION;
						break;
					}
				}
			};
		}

		return status;
	}

	/**
	 * 実績ロックされているか判定する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param processingDate
	 * @param closureId
	 * @return
	 */
	private EmployeeAndClosureOutput determineActualLocked(String companyId, EmployeeAndClosureOutput employeeAndClosure,
			GeneralDate day) {

		/**
		 * ロック : 1 , アンロック : 0
		 */

		/**
		 * アルゴリズム「当月の実績ロックの取得」を実行する
		 */

		Integer closureId = employeeAndClosure.getClosureId();
		// 全てのドメインモデル「当月の実績ロック」を取得する
		// List<ActualLock> actualLockLists =
		// this.actualLockRepository.findByListId(companyId, closureIds);

		Optional<ActualLock> actualLock = this.actualLockRepository.findById(companyId, closureId);
		
		if (!actualLock.isPresent()) {
			employeeAndClosure.setLock(0);
		} else {
			DatePeriod period = new DatePeriod(GeneralDate.min(), GeneralDate.min());
			if (actualLock.get().getDailyLockState().value == 0) {
				employeeAndClosure.setLock(0);
			} else {
				Optional<Closure> closure = this.closureRepository.findById(companyId, closureId);
				
				List<ClosureHistory> closureHistories = this.closureRepository.findByClosureId(companyId,
						closureId);
				
				// exist data
				if (closure.isPresent()) {
					
					// to data
					closure.get().setClosureHistories(closureHistories);
					// 基準日が当月に含まれているかチェックする
					Optional<ClosureHistory> closureHisory = this.closureRepository.findBySelectedYearMonth(
							companyId, closureId, closure.get().getClosureMonth().getProcessingYm().v());
					
					ClosureGetMonthDay closureGetMonthDay = new ClosureGetMonthDay();
					period = closureGetMonthDay.getDayMonth(closureHisory.get().getClosureDate(),
							closure.get().getClosureMonth().getProcessingYm().v());
				}
				if (day.afterOrEquals(period.start())
						&& day.beforeOrEquals(period.end())) {
					employeeAndClosure.setLock(1);
				} else {
					employeeAndClosure.setLock(0);
				}
			}
		}

		return employeeAndClosure;
	}

	private List<GeneralDate> getDaysBetween(GeneralDate startDate, GeneralDate endDate) {
		List<GeneralDate> daysBetween = new ArrayList<>();

		while (startDate.beforeOrEquals(endDate)) {
			daysBetween.add(startDate);
			GeneralDate temp = startDate.addDays(1);
			startDate = temp;
		}

		return daysBetween;
	}

}
