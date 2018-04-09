package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ReflectStampOutput;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.overtime.AutoCalculationSetService;

@Stateless
public class ResetDailyPerforDomainServiceImpl implements ResetDailyPerforDomainService {

	@Inject
	private ReflectWorkInforDomainService reflectWorkInforDomainService;

	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;

	@Inject
	private AutoCalculationSetService autoCalculationSetService;

	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;

	@Inject
	private ReflectShortWorkingTimeDomainService reflectShortWorkingTimeDomainService;

	@Inject
	private ReflectBreakTimeOfDailyDomainService reflectBreakTimeOfDailyDomainService;

	@Inject
	private ReflectStampDomainService reflectStampDomainService;

	@Inject
	private RegisterDailyPerformanceInfoService registerDailyPerformanceInfoService;

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Override
	public void resetDailyPerformance(String companyID, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr) {
		Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance = this.workInformationRepository
				.find(employeeID, processingDate);

		Optional<AffiliationInforOfDailyPerfor> affiliationInforOfDailyPerfor = this.affiliationInforOfDailyPerforRepository
				.findByKey(employeeID, processingDate);

		if (workInfoOfDailyPerformance.isPresent() && affiliationInforOfDailyPerfor.isPresent()) {
			// 再設定する区分を取得(get data 一部再設定区分, execution log)
			Optional<ExecutionLog> executionLog = this.empCalAndSumExeLogRepository
					.getByExecutionContent(empCalAndSumExecLogID, 0);

			CalAttrOfDailyPerformance calAttrOfDailyPerformance = null;
			AffiliationInforState affiliationInforState = null;
			SpecificDateAttrOfDailyPerfor specificDateAttrOfDailyPerfor = null;
			ShortTimeOfDailyPerformance shortTimeOfDailyPerformance = null;
			BreakTimeOfDailyPerformance breakTimeOfDailyPerformance = null;
			ReflectStampOutput stampOutput = null;
			List<ErrMessageInfo> errMesInfos = new ArrayList<>();
			if (executionLog.isPresent()) {
				if (executionLog.get().getDailyCreationSetInfo().isPresent()) {
					if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().isPresent()) {
						// 計算区分を日別実績に反映する(Reflect 計算区分 in 日別実績)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getCalculationClassificationResetting() == true) {

							calAttrOfDailyPerformance = this.reflectWorkInforDomainService
									.reflectCalAttOfDaiPer(companyID, employeeID, processingDate);

						}
						// 所属情報を反映する(Reflect info 所属情報)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getMasterReconfiguration() == true) {
							affiliationInforState = this.reflectWorkInforDomainService
									.createAffiliationInforOfDailyPerfor(companyID, employeeID, processingDate,
											empCalAndSumExecLogID);
							if (affiliationInforState.getErrMesInfos().isEmpty()) {
								affiliationInforOfDailyPerfor = affiliationInforState
										.getAffiliationInforOfDailyPerfor();
							} else {
								for (ErrMessageInfo errMessageInfo : affiliationInforState.getErrMesInfos()) {
									errMesInfos.add(errMessageInfo);
								}
							}
						}
						// 特定日を日別実績に反映する(Reflect 日別実績)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getSpecificDateClassificationResetting() == true) {
							specificDateAttrOfDailyPerfor = reflectWorkInforDomainService.reflectSpecificDate(companyID,
									employeeID, processingDate, affiliationInforOfDailyPerfor.get().getWplID());
						}
						// 短時間勤務時間帯を反映する(reflect 短時間勤務時間帯)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getResetTimeChildOrNurseCare() == true) {
							shortTimeOfDailyPerformance = reflectShortWorkingTimeDomainService.reflect(companyID,
									processingDate, employeeID, workInfoOfDailyPerformance.get());
						}
						// 休業再設定(reSetting 休業)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getClosedHolidays() == true) {
							// has error
							workInfoOfDailyPerformance = this.reflectWorkInforDomainService
									.reflectHolidayOfDailyPerfor(companyID, employeeID, processingDate);
						}
						// 就業時間帯再設定(reSetting worktime)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getResettingWorkingHours() == true) {
							this.breakTimeOfDailyPerformanceRepository.deleteByBreakType(employeeID, processingDate, 0);
							breakTimeOfDailyPerformance = this.reflectBreakTimeOfDailyDomainService.reflectBreakTime(
									companyID, employeeID, processingDate, empCalAndSumExecLogID, null,
									workInfoOfDailyPerformance.isPresent() ? workInfoOfDailyPerformance.get() : null);
						}
						// 打刻を取得する(get info stamp)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getReflectsTheNumberOfFingerprintChecks() == true) {
							stampOutput = this.reflectStampDomainService.reflectStampInfo(companyID, employeeID,
									processingDate,
									workInfoOfDailyPerformance.isPresent() ? workInfoOfDailyPerformance.get() : null,
									null, empCalAndSumExecLogID, reCreateAttr);
						}
					}
				}
			}

			if (errMesInfos.isEmpty() && breakTimeOfDailyPerformance != null) {
				this.registerDailyPerformanceInfoService.registerDailyPerformanceInfo(companyID, employeeID,
						processingDate, stampOutput, affiliationInforOfDailyPerfor.get(),
						workInfoOfDailyPerformance.get(), specificDateAttrOfDailyPerfor, calAttrOfDailyPerformance,
						null, breakTimeOfDailyPerformance);
			} else {
				errMesInfos.forEach(action -> {
					this.errMessageInfoRepository.add(action);
				});
			}
		}
	}

}
