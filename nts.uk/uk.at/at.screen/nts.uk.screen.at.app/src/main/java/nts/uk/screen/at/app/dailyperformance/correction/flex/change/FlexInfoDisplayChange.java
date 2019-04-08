package nts.uk.screen.at.app.dailyperformance.correction.flex.change;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthly.flex.CalcFlexChangeDto;
import nts.uk.ctx.at.record.dom.monthly.flex.CheckBeforeCalcFlexChangeService;
import nts.uk.ctx.at.record.dom.monthly.flex.ConditionCalcResult;
import nts.uk.ctx.at.record.dom.monthly.flex.MessageFlex;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortage;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortageFlex;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.screen.at.app.dailyperformance.correction.finddata.IFindData;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 * 
 * <<Public>> フレックス情報を表示する
 */
@Stateless
public class FlexInfoDisplayChange {

//	@Inject
//	private WorkingConditionService workingConditionService;

	@Inject
	private CheckShortageFlex checkShortageFlex;

//	@Inject
//	private PredetemineTimeSettingPub predetemineTimeSettingPub;

//	@Inject
//	private DailyPerformanceScreenRepo repo;

	@Inject
	private IFindData findData;

	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

//	@Inject
//	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;

	@Inject
	private CheckBeforeCalcFlexChangeService checkBeforeCalcFlex;

	// <<Public>> フレックス情報を表示する
	public FlexShortageDto flexInfo(String companyId, String employeeId, GeneralDate baseDate, String roleId,
			Optional<PresentClosingPeriodExport> closingPeriod, List<MonthlyModifyResult> results) {
		return flexInfo(companyId, employeeId, baseDate, roleId, closingPeriod, results, Optional.empty());
	}
	
	// <<Public>> フレックス情報を表示する
	public FlexShortageDto flexInfo(String companyId, String employeeId, GeneralDate baseDate, String roleId,
			Optional<PresentClosingPeriodExport> closingPeriod, List<MonthlyModifyResult> results, 
			Optional<ClosureEmployment> closureEmployment) {
		CalcFlexChangeDto calcFlex = CalcFlexChangeDto.createCalcFlexDto(employeeId, closingPeriod.get().getClosureEndDate());
		// 取得しているドメインモデル「日別実績の修正の機能．フレックス勤務者のフレックス不足情報を表示する」をチェックする
		Optional<DaiPerformanceFun> daiFunOpt = findData.getDailyPerformFun(companyId);
		if (!daiFunOpt.isPresent() || daiFunOpt.get().getFlexDispAtr() == 0) {
			return new FlexShortageDto().createShowFlex(false);
		}
		// 対応するドメインモデル「月別実績の勤怠時間」を取得する
		FlexShortageDto dataMonth = new FlexShortageDto();

		List<WorkingConditionItem> workingConditionItems = workingConditionItemRepository.getBySidAndPeriodOrderByStrD(
				employeeId, new DatePeriod(closingPeriod.get().getClosureStartDate(), closingPeriod.get().getClosureEndDate()));
		List<WorkingConditionItem> workConditions = workingConditionItems.stream()
				.filter(x -> x.getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)).collect(Collectors.toList());
		if (workConditions.isEmpty())
			return new FlexShortageDto().createShowFlex(false);

		if (!results.isEmpty()) {
			mapValue(results.get(0).getItems(), dataMonth);
		} else {
			return new FlexShortageDto().createShowFlex(false);
		}

		// 社員のフレックス繰越上限時間を求める
//			String hist = dailyPerformanceScreenRepo.findWorkConditionLastest(
//					workConditions.stream().map(x -> x.getHistoryId()).collect(Collectors.toList()), employeeId);
//			Optional<WorkingConditionItem> wCItem = workConditions.stream().filter(x -> x.getHistoryId().equals(hist))
//					.findFirst();
		calcFlex.createWCItem(workConditions);
		ConditionCalcResult conditionResult = checkBeforeCalcFlex.getConditionCalcFlex(companyId, calcFlex);
		dataMonth.createRedConditionMessage(conditionResult.getValueResult());
		dataMonth.createNotForward("");

		// TODO フレックス不足の相殺が実施できるかチェックする
		CheckShortage checkShortage = checkShortageFlex.checkShortageFlex(employeeId, baseDate);
		boolean checkFlex = checkShortage.isCheckShortage() && employeeId.equals(AppContexts.user().employeeId());
		//checkShortage.createRetiredFlag(checkShortage.isRetiredFlag());
		//if (condition.equals("0:00") && !checkFlex) {
		dataMonth.createNotForward(messageE22(conditionResult.getMessage()));
		//}
		return dataMonth.createCanFlex(checkFlex).createShowFlex(showFlex()).createCalcFlex(calcFlex);
	}

	// hide or show
	private boolean showFlex() {
		return true;
	}

	private void mapValue(List<ItemValue> items, FlexShortageDto dataMonth) {
		for (ItemValue item : items) {
			setValueMonth(dataMonth, item);
		}
	}

	private void setValueMonth(FlexShortageDto dataMonth, ItemValue item) {
		switch (item.getItemId()) {
		case 18:
			dataMonth.setValue18(item);
			break;
		case 19:
			dataMonth.setValue19(item);
			break;
		case 21:
			dataMonth.setValue21(item);
			break;
		case 189:
			dataMonth.setValue189(item);
			break;
		case 190:
			dataMonth.setValue190(item);
			break;
		case 191:
			dataMonth.setValue191(item);
			break;
		default:
			break;
		}
	}
	
	private String messageE22(MessageFlex messageFlex) {
		switch (messageFlex.value) {
		case 0:
			return "";
		case 1:
			return TextResource.localize("KDW003_114");
		case 2:
			return TextResource.localize("KDW003_127");
		default:
			return "";
		}
	}
}
