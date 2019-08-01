package nts.uk.ctx.at.record.app.command.dailyperform.workinfo;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class WorkInformationOfDailyPerformCommandUpdateHandler extends CommandFacade<WorkInformationOfDailyPerformCommand> {

	@Inject
	private WorkInformationRepository repo;
	
	@Inject
	private WorkTypeRepository workTypeRepo;

	@Override
	protected void handle(CommandHandlerContext<WorkInformationOfDailyPerformCommand> context) {
		WorkInfoOfDailyPerformance domain = context.getCommand().toDomain();
		/** check worktype*/
		checkWorkType(domain);
		
		/** update domain */
		repo.updateByKey(domain);
		
		/** fire changed event */
		if(context.getCommand().isTriggerEvent()){
			domain.workInfoChanged();
		}
	}
	
	private void checkWorkType(WorkInfoOfDailyPerformance domain) {
		String comId = AppContexts.user().companyId();
		if(domain.getRecordInfo().getWorkTypeCode() != null && domain.getScheduleInfo().getWorkTypeCode() != null && 
				domain.getRecordInfo().getWorkTypeCode().equals(domain.getScheduleInfo().getWorkTypeCode())){
			checkTogether(domain, comId);
			return;
		}
		checkSeperate(domain, comId);
	}
	
	private void checkTogether(WorkInfoOfDailyPerformance domain, String comId) {
		workTypeRepo.findByPK(comId, domain.getRecordInfo().getWorkTypeCode().v()).ifPresent(wt -> {
			if(wt.isNoneWorkTimeType()){
				domain.getRecordInfo().removeWorkTimeInHolydayWorkType();
				domain.getScheduleInfo().removeWorkTimeInHolydayWorkType();
			}
		});
	}

	private void checkSeperate(WorkInfoOfDailyPerformance domain, String comId) {
		workTypeRepo.findByPK(comId, domain.getRecordInfo().getWorkTypeCode().v()).ifPresent(wt -> {
			if(wt.isNoneWorkTimeType()){
				domain.getRecordInfo().removeWorkTimeInHolydayWorkType();
			}
		});
		workTypeRepo.findByPK(comId, domain.getScheduleInfo().getWorkTypeCode().v()).ifPresent(wt -> {
			if(wt.isNoneWorkTimeType()){
				domain.getScheduleInfo().removeWorkTimeInHolydayWorkType();
			}
		});
	}

}
