package nts.uk.screen.at.app.dailyperformance.correction.flex.change;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.monthly.flex.CalcFlexChangeDto;
import nts.uk.ctx.at.record.dom.monthly.flex.CheckBeforeCalcFlexChangeService;
import nts.uk.ctx.at.record.dom.monthly.flex.ConditionCalcResult;
import nts.uk.ctx.at.record.dom.monthly.flex.MessageFlex;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.MonthlyFlexStatutoryLaborTime;
import nts.uk.ctx.at.record.dom.statutoryworkinghours.monthly.MonthlyStatutoryWorkingHours;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CheckBeforeCalcFlexChange implements CheckBeforeCalcFlexChangeService {

	@Inject
	private ShClosurePub shClosurePub;

	@Inject
	private WorkingConditionRepository workingConditionRepository;

	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

	@Inject
	private DailyPerformanceCorrectionProcessor processor;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;

	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;

//	@Inject
//	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepository;

	@Inject
	private MonthlyStatutoryWorkingHours monthlyStatutoryWorkingHours;

	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;

//	@Inject
//	private AffWorkplaceHistoryItemRepository affWorkplaceHis;

	@Inject
	private AttendanceTimeRepository attendanceTime;

	private static final String TIME_DEFAULT = "0:00";

	// 社員のフレックス繰越上限時間を求める
	@Override
	public ConditionCalcResult getConditionCalcFlex(String companyId, CalcFlexChangeDto calc) {
		return getConditionCalcFlex(companyId, calc, Optional.empty(), Optional.empty());
	}
	
	// 社員のフレックス繰越上限時間を求める
	public ConditionCalcResult getConditionCalcFlex(String companyId, CalcFlexChangeDto calc, Optional<ClosureEmployment> closureEmployment,
			Optional<PresentClosingPeriodExport> periodExportOpt) {
		
		if(!closureEmployment.isPresent()){
			closureEmployment = this.closureEmploymentRepository
					.findByEmploymentCD(companyId, processor.getEmploymentCode(companyId, calc.getDate(), calc.getEmployeeId()));
		}
		if (!closureEmployment.isPresent())
			return new ConditionCalcResult("", MessageFlex.Normal);
		// 指定した年月日時点の締め期間を取得する
		if(!periodExportOpt.isPresent()){
			periodExportOpt = shClosurePub.find(companyId, closureEmployment.get().getClosureId(), calc.getDate());
		}
		
		if (periodExportOpt.isPresent() && (calc.getDate().after(periodExportOpt.get().getClosureEndDate())
				|| calc.getDate().before(periodExportOpt.get().getClosureStartDate()))) {
			// ドメインモデル「フレックス不足の繰越上限時間」を取得する
			Integer valueLimit = dailyPerformanceScreenRepo.getLimitFexMonth(companyId);
			if (valueLimit == null)
				valueLimit = 0;
			//102745  ・状態：正常 
			return new ConditionCalcResult(converMinutesToHours(valueLimit), MessageFlex.Normal);
		}

		// 社員ID（List）と指定期間から所属会社履歴項目を取得
		DatePeriod datePeriod = new DatePeriod(periodExportOpt.get().getClosureStartDate(), periodExportOpt.get().getClosureEndDate());
		List<String> sIds = Arrays.asList(calc.getEmployeeId());
		List<AffCompanyHistImport> affCompanyHis = syCompanyRecordAdapter.getAffCompanyHistByEmployee(sIds, datePeriod);
		if (!affCompanyHis.isEmpty()) {
			for (AffComHistItemImport his : affCompanyHis.get(0).getLstAffComHistItem()) {
				// 「所属会社履歴項目．退職日」がパラメータ「当月の期間」に含まれているかチェックする
				if (his.getDatePeriod().end() != null && datePeriod.contains(his.getDatePeriod().end())) {
					//102745 ・状態：当月退職
					return new ConditionCalcResult(TIME_DEFAULT, MessageFlex.Retirement);
				}
			}
		}
		
		if (calc.getWCItems().isEmpty()) {
			//102745 ・状態：当月退職
			return new ConditionCalcResult(TIME_DEFAULT, MessageFlex.Retirement);
		}
		
		// ドメインモデル「労働条件」を取得する
		// Slow Perfomnace
//		GeneralDate dateCheck = GeneralDate.today();
//		Optional<WorkingConditionItem> todayWCI = workingConditionItemRepository.getBySidAndStandardDate(calc.getEmployeeId(), dateCheck);
//		List<DateHistoryItem> WCes = workingConditionRepository.getBySidsAndDatePeriod(sIds, datePeriod)
//																.stream().map(c -> c.getDateHistoryItem())
//																.flatMap(List::stream).distinct().collect(Collectors.toList());
//		for (WorkingConditionItem item : calc.getWCItems()) {
////			Optional<WorkingCondition> workConditionOpt = workingConditionRepository.getByHistoryId(item.getHistoryId());
////			if (workConditionOpt.isPresent() && periodExportOpt.isPresent()) {
//				
//			Optional<DateHistoryItem> dateHist = WCes.stream().filter(x -> x.identifier().equals(item.getHistoryId())).findFirst();
//			if (dateHist.isPresent()) {
//				dateCheck = dateHist.get().end().nextValue(true);
//				
//				if (!todayWCI.isPresent() || !todayWCI.get().getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)) {
//					return TIME_DEFAULT;
//				}
//			}
////			}
//		}
		/** TODO: replace below code with above code after confirm design? */
		boolean checkFlex = true;
		for (WorkingConditionItem item : calc.getWCItems()) {
			Optional<WorkingCondition> workConditionOpt = workingConditionRepository
					.getByHistoryId(item.getHistoryId());
			if (workConditionOpt.isPresent() && periodExportOpt.isPresent()) {
				GeneralDate startDate = periodExportOpt.get().getClosureStartDate();
				GeneralDate endDate = periodExportOpt.get().getClosureEndDate();
				GeneralDate dateCheck = GeneralDate.today();
				Optional<DateHistoryItem> dateHist = workConditionOpt.get().getDateHistoryItem().stream()
						.filter(x -> x.end().afterOrEquals(startDate) && x.end().beforeOrEquals(endDate)).findFirst();
				if (dateHist.isPresent()) {
					dateCheck = dateHist.get().end().nextValue(true);
					Optional<WorkingConditionItem> workingConditionItemOpt = workingConditionItemRepository
							.getBySidAndStandardDate(calc.getEmployeeId(), dateCheck);
					if (!workingConditionItemOpt.isPresent()
							|| !workingConditionItemOpt.get().getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)) {
						checkFlex = false;
					}
				}
			}
		}
        
		if(!checkFlex){
			//102745 ・状態：翌月フレックスでない
			return new ConditionCalcResult(TIME_DEFAULT, MessageFlex.Not_Next_Month);
		}
		// フレックス時間勤務の場合(là フレックス時間勤務)
		// 指定した年月日時点の締め期間を取得する
		Optional<PresentClosingPeriodExport> periodExportOptNext = shClosurePub.find(companyId,
				closureEmployment.get().getClosureId(), periodExportOpt.get().getClosureEndDate().addDays(1));
		// ドメインモデル「月別実績の勤怠時間」を取得する 
		// #102745 change EAP
//		Optional<AttendanceTimeOfMonthly> attTiemOpt = attendanceTimeOfMonthlyRepository.find(calc.getEmployeeId(),
//				periodExportOptNext.get().getProcessingYm(),
//				ClosureId.valueOf(closureEmployment.get().getClosureId()),
//				new ClosureDate(periodExportOptNext.get().getClosureDate().getClosureDay(),
//						periodExportOptNext.get().getClosureDate().getLastDayOfMonth()));
//
//		// String timeCarryoverTime = "0:00";
		long timeSum = 0L;
//		if (attTiemOpt.isPresent()) {
//			// 「法定労働時間」-「所定労働時間」をパラメータ「翌月繰越可能時間」にセットする
//			if (attTiemOpt.get().getMonthlyCalculation() == null
//					|| attTiemOpt.get().getMonthlyCalculation().getStatutoryWorkingTime() == null)
//				return TIME_DEFAULT;
//			timeSum = attTiemOpt.get().getMonthlyCalculation().getStatutoryWorkingTime().v()
//					- attTiemOpt.get().getMonthlyCalculation().getAggregateTime().getPrescribedWorkingTime()
//							.getSchedulePrescribedWorkingTime().v();
//			// timeCarryoverTime = String.format("%d:%02d", time);
//		} else {
			// 社員と基準日から雇用履歴項目を取得する
			AffEmploymentHistoryDto afEmpDto = dailyPerformanceScreenRepo.getAffEmploymentHistory(companyId, calc.getEmployeeId(),
														periodExportOptNext.get().getClosureStartDate());
//			List<AffWorkplaceHistoryItem> lstAffWorkplace = affWorkplaceHis
//					.getAffWrkplaHistItemByEmpIdAndDate(datePeriod.start(), calc.getEmployeeId());
			// 週、月の法定労働時間を取得(フレックス用)
			MonthlyFlexStatutoryLaborTime monthFlex = monthlyStatutoryWorkingHours.getFlexMonAndWeekStatutoryTime(
					companyId, afEmpDto.getEmploymentCode(), calc.getEmployeeId(), datePeriod.start(),
					periodExportOptNext.get().getProcessingYm());

			// 締め期間中に「フレックス勤務」だった期間を取得する
			// List<WorkingConditionItem> workingConditionItems =
			// workingConditionItemRepository.getBySidAndPeriodOrderByStrD(calc.getEmployeeId(),
			// new DatePeriod(periodExportOptNext.get().getClosureStartDate(),
			// periodExportOptNext.get().getClosureStartDate()));
			// workingConditionItems = workingConditionItems.stream().filter(x
			// ->
			// x.getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)).collect(Collectors.toList());
			List<DateRange> dates = dailyPerformanceScreenRepo.getWorkConditionFlexDatePeriod(calc.getEmployeeId(),
					new DatePeriod(periodExportOptNext.get().getClosureStartDate(),
							periodExportOptNext.get().getClosureEndDate()));
			List<Integer> listAttTime = attendanceTime.findAtt(calc.getEmployeeId(),
					convertToListDate(dates));
			// 「法定労働時間」-「所定労働時間」をパラメータ「翌月繰越可能時間」にセットする

			timeSum = (monthFlex != null && monthFlex.getStatutorySetting() != null)
					? monthFlex.getStatutorySetting().v() - calcSumTime(listAttTime) : 0 - calcSumTime(listAttTime);
//		}

		if (timeSum <= 0) {
			timeSum = 0;
		}
		
		// ドメインモデル「フレックス不足の繰越上限管理」を取得する
		Integer valueLimit = dailyPerformanceScreenRepo.getLimitFexMonth(companyId);
		if(valueLimit == null) valueLimit = 0;
		if (valueLimit >= timeSum) {
			//102745  ・状態：正常
			return new ConditionCalcResult(converMinutesToHours((int)timeSum), MessageFlex.Normal);
		} else {
			////102745  ・状態：正常
			return new ConditionCalcResult(converMinutesToHours(valueLimit), MessageFlex.Normal);
		}
	}

	private List<GeneralDate> convertToListDate(List<DateRange> dates) {
		List<GeneralDate> listDate = new ArrayList<>();
		dates.forEach(x -> {
			listDate.addAll(x.toListDate());
		});
		return listDate;
	}

	public String converMinutesToHours(int time) {
		int hours = time / 60;
		int minute = Math.abs(time) % 60;
		return (time < 0 && hours == 0) ? "-" + String.format("%d:%02d", hours, minute)
				: String.format("%d:%02d", hours, minute);
	}

	public int convertHourToMinute(String time) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = sdf.parse("8:00");
		cal.setTime(date);
		int mins = cal.get(Calendar.HOUR) * 60 + cal.get(Calendar.MINUTE);
		return mins;
	}

	private long calcSumTime(List<Integer> listAttTime) {
		long sum = 0;
		for (Integer data : listAttTime) {
			if (data != null) {
				sum += data;
			}
		}
		;
		return sum;
	}
}
