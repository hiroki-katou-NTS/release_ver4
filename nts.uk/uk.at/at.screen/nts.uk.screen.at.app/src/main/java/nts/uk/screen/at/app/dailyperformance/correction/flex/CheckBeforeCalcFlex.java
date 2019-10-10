package nts.uk.screen.at.app.dailyperformance.correction.flex;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CheckBeforeCalcFlex {

	@Inject
	private ShClosurePub shClosurePub;
	
	@Inject
	private WorkingConditionRepository workingConditionRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	public String getConditionCalcFlex(CalcFlexDto calc) {
		String companyId = AppContexts.user().companyId();
		String timeCheck = "15:00";
		Optional<PresentClosingPeriodExport> periodExportOpt = shClosurePub.find(companyId, calc.getClosureId(), calc.getDate());
		Optional<WorkingCondition> workConditionOpt = workingConditionRepository.getByHistoryId(calc.getHistId());
		if(workConditionOpt.isPresent() && periodExportOpt.isPresent()){
			GeneralDate startDate = periodExportOpt.get().getClosureStartDate();
			GeneralDate endDate = periodExportOpt.get().getClosureEndDate();
			GeneralDate dateCheck = GeneralDate.today();
			Optional<DateHistoryItem> dateHist = workConditionOpt.get().getDateHistoryItem().stream().filter(x -> x.end().afterOrEquals(startDate) && x.end().beforeOrEquals(endDate)).findFirst();
		    if(dateHist.isPresent()){
		    	dateCheck = dateHist.get().end().nextValue(true);
		    	Optional<WorkingConditionItem> workingConditionItemOpt =  workingConditionItemRepository.getBySidAndStandardDate(calc.getEmployeeId(), dateCheck);
		    	if(!workingConditionItemOpt.isPresent() || !workingConditionItemOpt.get().getLaborSystem().equals(WorkingSystem.FLEX_TIME_WORK)){
		    		timeCheck ="0:00";
		    	}
		    }else{
		    	if(calc.isFlag()){
		    		timeCheck ="0:00";
		    	}
		    }
		}
		return timeCheck;
	}

}
