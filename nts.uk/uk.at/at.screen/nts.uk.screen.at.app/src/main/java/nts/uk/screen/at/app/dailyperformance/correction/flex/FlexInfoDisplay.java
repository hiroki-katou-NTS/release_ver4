package nts.uk.screen.at.app.dailyperformance.correction.flex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortage;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortageFlex;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFunRepository;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.ctx.at.shared.pub.worktime.predset.BreakDownTimeDayExport;
import nts.uk.ctx.at.shared.pub.worktime.predset.PredetemineTimeSettingPub;
import nts.uk.ctx.at.shared.pub.worktime.predset.PredeterminedTimeExport;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.AffEmploymentHistoryDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.FlexShortage;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthParent;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQueryProcessor;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyMultiQuery;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class FlexInfoDisplay {
	
//	@Inject
//	private WorkingConditionService workingConditionService;
	
	@Inject
	private CheckShortageFlex checkShortageFlex;
	
	@Inject
	private PredetemineTimeSettingPub predetemineTimeSettingPub;
	
	@Inject
	private ClosureEmploymentRepository closureEmploymentRepository;
	
	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private ShClosurePub shClosurePub;
	
	@Inject
	private MonthlyModifyQueryProcessor monthlyModifyQueryProcessor;
	
	@Inject
	private DaiPerformanceFunRepository daiPerformanceFunRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private DailyPerformanceScreenRepo dailyPerformanceScreenRepo;
	
	@Inject
	private CheckBeforeCalcFlex checkBeforeCalcFlex;
	
	//<<Public>> フレックス情報を表示する
	public FlexShortage flexInfo(String employeeId, GeneralDate baseDate, String roleId){
		 String companyId = AppContexts.user().companyId();
		 CalcFlexDto calcFlex = CalcFlexDto.createCalcFlexDto(employeeId, baseDate);
		 Optional<DaiPerformanceFun> daiFunOpt = daiPerformanceFunRepository.getDaiPerformanceFunById(companyId);
		 if(!daiFunOpt.isPresent() || daiFunOpt.get().getFlexDispAtr() == 0){
			 return new FlexShortage().createShowFlex(false);
		 }
		 //TODO 対応するドメインモデル「月別実績の勤怠時間」を取得する
		Optional<ClosureEmployment> closureEmploymentOptional = this.closureEmploymentRepository
				.findByEmploymentCD(companyId, getEmploymentCode(companyId, baseDate, employeeId));
		List<MonthlyModifyResult> results = new ArrayList<>();
		FlexShortage dataMonth = new FlexShortage();
		List<WorkingConditionItem> workConditions = new ArrayList<>();
		if (closureEmploymentOptional.isPresent()) {
			Optional<PresentClosingPeriodExport> closingPeriod = shClosurePub.find(companyId,
					closureEmploymentOptional.get().getClosureId(), baseDate);
			if(closingPeriod.isPresent()){
			//set dpMonthParent
			dataMonth.createMonthParent(new DPMonthParent(employeeId, closingPeriod.get().getProcessingYm().v(),
					closureEmploymentOptional.get().getClosureId(),
					new ClosureDateDto(closingPeriod.get().getClosureDate().getClosureDay(),
							closingPeriod.get().getClosureDate().getLastDayOfMonth())));
			
			List<WorkingConditionItem> workingConditionItems =  workingConditionItemRepository.getBySidAndPeriodOrderByStrD(employeeId, new DatePeriod(closingPeriod.get().getClosureStartDate(), closingPeriod.get().getClosureEndDate()));
			workConditions =  workingConditionItems.stream().filter(x -> x.getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)).collect(Collectors.toList());
			if(workConditions.isEmpty()) return new FlexShortage().createShowFlex(false);
			 //if(!workingConditionItemOpt.get().getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)) return new FlexShortage().createShowFlex(false);
			results = monthlyModifyQueryProcessor.initScreen(new MonthlyMultiQuery(Arrays.asList(employeeId)),
															Arrays.asList(18, 21, 189, 190, 191), closingPeriod.get().getProcessingYm(),
															ClosureId.valueOf(closureEmploymentOptional.get().getClosureId()),
															new ClosureDate(closingPeriod.get().getClosureDate().getClosureDay(),
																	closingPeriod.get().getClosureDate().getLastDayOfMonth()));
			}
		}else{
			return new FlexShortage().createShowFlex(false);
		}
		
		if (!results.isEmpty()) {
			mapValue(results.get(0).getItems(), dataMonth);
			dataMonth.getMonthParent().setVersion(results.get(0).getVersion());
		}else{
			 return new FlexShortage().createShowFlex(false);
		}
		
		String hist = dailyPerformanceScreenRepo.findWorkConditionLastest(workConditions.stream().map(x -> x.getHistoryId()).collect(Collectors.toList()), employeeId);
		Optional<WorkingConditionItem> wCItem = workConditions.stream().filter(x -> x.getHistoryId().equals(hist)).findFirst();
		if(!wCItem.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().isPresent()) return new FlexShortage().createShowFlex(false);
		 //TODO 所定時間（1日の時間内訳）を取得する
		 Optional<PredeterminedTimeExport> predertermineOpt = predetemineTimeSettingPub.acquirePredeterminedTime(AppContexts.user().companyId(),  wCItem.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get().v());
		 if(!predertermineOpt.isPresent()) return null;
		 //TODO フレックス不足の相殺が実施できるかチェックする
		 CheckShortage checkShortage = checkShortageFlex.checkShortageFlex(employeeId, baseDate);
		 boolean checkFlex = checkShortage.isCheckShortage();
		 calcFlex.setFlag(checkShortage.isRetiredFlag());
		 checkShortage.createRetiredFlag(checkShortage.isRetiredFlag());
		 
		 //set closureId
		 calcFlex.setClosureId(closureEmploymentOptional.get().getClosureId());
		 calcFlex.setHistId(workConditions.get(0).getHistoryId());
		 
		 //翌月繰越可能時間をチェックする
		 String condition = checkBeforeCalcFlex.getConditionCalcFlex(calcFlex);
		 dataMonth.createRedConditionMessage(condition);
		 dataMonth.createNotForward("");
		 if(condition.equals("0:00")){
			 dataMonth.createNotForward(TextResource.localize("KDW003_114")); 
		 }
		 BreakDownTimeDayExport time = predertermineOpt.get().getPredTime();
		return dataMonth.createCanFlex(checkFlex)
				.createBreakTimeDay(new BreakTimeDay(time.getOneDay(), time.getMorning(), time.getAfternoon()))
				.createShowFlex(showFlex()).createCalcFlex(calcFlex);
	}
    
	//hide or show 
	private boolean showFlex(){
		return true;
	}
	
	private String getEmploymentCode( String companyId, GeneralDate dateRange, String sId) {
		AffEmploymentHistoryDto employment = repo.getAffEmploymentHistory(companyId, sId, dateRange);
		return employment == null ? "" : employment.getEmploymentCode();
	}
	
	private void mapValue(List<ItemValue> items, FlexShortage dataMonth){
		for(ItemValue item : items){
			setValueMonth(dataMonth, item);
		}
	}
	
	private void setValueMonth(FlexShortage dataMonth, ItemValue item){
		switch (item.getItemId()) {
		case 18:
			dataMonth.setValue18(item);
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
	
	
}
