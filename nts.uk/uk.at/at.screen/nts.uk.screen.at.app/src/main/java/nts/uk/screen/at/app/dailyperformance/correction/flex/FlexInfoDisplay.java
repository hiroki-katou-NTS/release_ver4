package nts.uk.screen.at.app.dailyperformance.correction.flex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortageFlex;
import nts.uk.ctx.at.shared.app.service.workingcondition.WorkingConditionService;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
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
import nts.uk.screen.at.app.dailyperformance.correction.dto.DateRange;
import nts.uk.screen.at.app.dailyperformance.correction.dto.FlexShortage;
import nts.uk.screen.at.app.dailyperformance.correction.dto.month.DPMonthParent;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyQueryProcessor;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyMultiQuery;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class FlexInfoDisplay {
	
	@Inject
	private WorkingConditionService workingConditionService;
	
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
	
	//<<Public>> フレックス情報を表示する
	public FlexShortage flexInfo(String employeeId, GeneralDate baseDate, String roleId){
		 String companyId = AppContexts.user().companyId();
		 Optional<WorkingConditionItem>  workingConditionItemOpt = workingConditionService.findWorkConditionByEmployee(employeeId, baseDate);
		 if(!workingConditionItemOpt.isPresent()) return new FlexShortage().createShowFlex(false);
		 if(!workingConditionItemOpt.get().getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)) return new FlexShortage().createShowFlex(false);
		 //TODO 対応するドメインモデル「月別実績の勤怠時間」を取得する
		Optional<ClosureEmployment> closureEmploymentOptional = this.closureEmploymentRepository
				.findByEmploymentCD(companyId, getEmploymentCode(new DateRange(null, baseDate), employeeId));
		List<MonthlyModifyResult> results = new ArrayList<>();
		FlexShortage dataMonth = new FlexShortage();
		if (closureEmploymentOptional.isPresent()) {
			Optional<PresentClosingPeriodExport> closingPeriod = shClosurePub.find(companyId,
					closureEmploymentOptional.get().getClosureId(), baseDate);
			if(closingPeriod.isPresent()){
			//set dpMonthParent
			dataMonth.createMonthParent(new DPMonthParent(employeeId, closingPeriod.get().getProcessingYm().v(),
					closureEmploymentOptional.get().getClosureId(),
					new ClosureDateDto(closingPeriod.get().getClosureDate().getClosureDay(),
							closingPeriod.get().getClosureDate().getLastDayOfMonth())));
			results = monthlyModifyQueryProcessor.initScreen(new MonthlyMultiQuery(Arrays.asList(employeeId)),
															Arrays.asList(18, 21, 189, 190, 191), closingPeriod.get().getProcessingYm(),
															ClosureId.valueOf(closureEmploymentOptional.get().getClosureId()),
															new ClosureDate(closingPeriod.get().getClosureDate().getClosureDay(),
																	closingPeriod.get().getClosureDate().getLastDayOfMonth()));
			}
		}
		if (!results.isEmpty()) {
			mapValue(results.get(0).getItems(), dataMonth);
		}else{
			 return new FlexShortage().createShowFlex(false);
		}
		if(!workingConditionItemOpt.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().isPresent()) return null;
		 //TODO 所定時間（1日の時間内訳）を取得する
		 Optional<PredeterminedTimeExport> predertermineOpt = predetemineTimeSettingPub.acquirePredeterminedTime(AppContexts.user().companyId(),  workingConditionItemOpt.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get().v());
		 if(!predertermineOpt.isPresent()) return null;
		 //TODO フレックス不足の相殺が実施できるかチェックする
		 boolean checkFlex = checkShortageFlex.checkShortageFlex(employeeId, baseDate);
		 BreakDownTimeDayExport time = predertermineOpt.get().getPredTime();
		return dataMonth.createCanFlex(checkFlex)
				.createBreakTimeDay(new BreakTimeDay(time.getOneDay(), time.getMorning(), time.getAfternoon()))
				.createShowFlex(showFlex());
	}
    
	//hide or show 
	private boolean showFlex(){
		return true;
	}
	
	private String getEmploymentCode(DateRange dateRange, String sId) {
		AffEmploymentHistoryDto employment = repo.getAffEmploymentHistory(sId, dateRange);
		String employmentCode = employment == null ? "" : employment.getEmploymentCode();
		return employmentCode;
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
