package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalHolidaySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfLeaveEarlySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfOverTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.LeaveAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SpecificSalaryCalAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.BaseAutoCalSetting;
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

	@Override
	public void resetDailyPerformance(String companyID, String employeeID, GeneralDate processingDate,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr) {
		Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance = this.workInformationRepository
				.find(employeeID, processingDate);

		if (workInfoOfDailyPerformance.isPresent()) {
			// 再設定する区分を取得(get data 一部再設定区分, execution log)
			Optional<ExecutionLog> executionLog = this.empCalAndSumExeLogRepository
					.getByExecutionContent(empCalAndSumExecLogID, 0);

			if (executionLog.isPresent()) {
				if (executionLog.get().getDailyCreationSetInfo().isPresent()) {
					if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().isPresent()) {
						//計算区分を日別実績に反映する(Reflect 計算区分 in 日別実績)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getCalculationClassificationResetting() == true) {

							BaseAutoCalSetting baseAutoCalSetting = this.autoCalculationSetService
									.getAutoCalculationSetting(companyID, employeeID, processingDate);

							AutoCalculationSetting flexExcessTime = new AutoCalculationSetting(
									baseAutoCalSetting.getFlexOTTime().getFlexOtTime().getCalAtr(),
									baseAutoCalSetting.getFlexOTTime().getFlexOtTime().getUpLimitORtSet());
							
							AutoCalRaisingSalarySetting autoCalRaisingSalarySetting = new AutoCalRaisingSalarySetting(SalaryCalAttr.NOT_USE, SpecificSalaryCalAttr.NOT_USE);

							// number 3
							AutoCalHolidaySetting holidayTimeSetting = new AutoCalHolidaySetting(
									new AutoCalculationSetting(
											baseAutoCalSetting.getRestTime().getRestTime().getCalAtr(),
											baseAutoCalSetting.getRestTime().getRestTime().getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getRestTime().getLateNightTime().getCalAtr(),
											baseAutoCalSetting.getRestTime().getLateNightTime().getUpLimitORtSet()));

							// number 4
							AutoCalOfOverTime overtimeSetting = new AutoCalOfOverTime(
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getEarlyOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getEarlyOtTime().getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getEarlyMidOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getEarlyMidOtTime()
													.getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getNormalOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getNormalOtTime().getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getNormalMidOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getNormalMidOtTime()
													.getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getLegalOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getLegalOtTime().getUpLimitORtSet()),
									new AutoCalculationSetting(
											baseAutoCalSetting.getNormalOTTime().getLegalMidOtTime().getCalAtr(),
											baseAutoCalSetting.getNormalOTTime().getLegalMidOtTime()
													.getUpLimitORtSet()));
							
							AutoCalOfLeaveEarlySetting autoCalOfLeaveEarlySetting = new AutoCalOfLeaveEarlySetting(LeaveAttr.NOT_USE, LeaveAttr.NOT_USE);
							
							AutoCalcSetOfDivergenceTime autoCalcSetOfDivergenceTime = new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.NOT_USE);

							CalAttrOfDailyPerformance calAttrOfDailyPerformance = new CalAttrOfDailyPerformance(
									employeeID, processingDate, flexExcessTime, autoCalRaisingSalarySetting, holidayTimeSetting, overtimeSetting, autoCalOfLeaveEarlySetting,
									autoCalcSetOfDivergenceTime);

						}
						// 特定日を日別実績に反映する(Reflect 日別実績)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getSpecificDateClassificationResetting() == true) {
//							 reflectWorkInforDomainService.reflectSpecificDate(companyID, employeeID, processingDate, workPlaceID);
						}
						// 所属情報を反映する(Reflect info 所属情報)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getMasterReconfiguration() == true) {
							AffiliationInforState affiliationInforState = this.reflectWorkInforDomainService
									.createAffiliationInforOfDailyPerfor(companyID, employeeID, processingDate,
											empCalAndSumExecLogID);
						}
						// 短時間勤務時間帯を反映する(reflect 短時間勤務時間帯)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getResetTimeChildOrNurseCare() == true) {
							// TODO
							// Dung code
						}
						// 休業再設定(reSetting 休業)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getClosedHolidays() == true) {
							// TODO
						}
						// 就業時間帯再設定(reSetting worktime)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getResettingWorkingHours() == true) {
							// TODO
							// Dung code
						}
						// 打刻を取得する(get info stamp)
						if (executionLog.get().getDailyCreationSetInfo().get().getPartResetClassification().get()
								.getReflectsTheNumberOfFingerprintChecks() == true) {
							// TODO
							// dung code
						}
					}
				}
			}
		}
	}

}
