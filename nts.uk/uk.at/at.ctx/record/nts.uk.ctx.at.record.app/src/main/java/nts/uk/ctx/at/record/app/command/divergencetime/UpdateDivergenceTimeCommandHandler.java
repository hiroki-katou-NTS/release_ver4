package nts.uk.ctx.at.record.app.command.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItemSet;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.ctx.at.record.dom.divergencetime.service.DivergenceReasonService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateDivergenceTimeCommandHandler extends CommandHandler<UpdateDivergenceTimeCommand>{

	@Inject
	private DivergenceTimeRepository divTimeRepo;
	@Inject
	public DivergenceReasonService check;

	@Override
	protected void handle(CommandHandlerContext<UpdateDivergenceTimeCommand> context) {
		String companyId = AppContexts.user().companyId();
		DivergenceTime divTime = DivergenceTime.createSimpleFromJavaType(companyId,
									context.getCommand().getDivTime().getDivTimeId(),
									context.getCommand().getDivTime().getDivTimeName(),
									context.getCommand().getDivTime().getDivTimeUseSet(),
									context.getCommand().getDivTime().getAlarmTime(),
									context.getCommand().getDivTime().getErrTime(),
									context.getCommand().getDivTime().getSelectSet().getSelectUseSet(), 
									context.getCommand().getDivTime().getSelectSet().getCancelErrSelReason(),
									context.getCommand().getDivTime().getInputSet().getSelectUseSet(),
									context.getCommand().getDivTime().getInputSet().getCancelErrSelReason());
		Boolean checkTime = DivergenceTime.checkAlarmErrTime(context.getCommand().getDivTime().getAlarmTime(), context.getCommand().getDivTime().getErrTime());
		if(checkTime == true){
			if(check.isExit(context.getCommand().getDivTime().getSelectSet().getSelectUseSet(),context.getCommand().getDivTime().getDivTimeId())){
				if(context.getCommand().getTimeItem().isEmpty()){
					divTimeRepo.updateDivTime(divTime);
				}else{
					List<DivergenceItemSet> listUpdate = context.getCommand().getTimeItem().stream().map(c -> {
						return new DivergenceItemSet(companyId, c.getDivTimeId(), c.getAttendanceId());
					}).collect(Collectors.toList());
					if (listUpdate == null) {
						return;
					}
					divTimeRepo.deleteItemId(companyId, context.getCommand().getDivTime().getDivTimeId());
					divTimeRepo.addItemId(listUpdate);
					divTimeRepo.updateDivTime(divTime);
				}
			}else{
				throw new BusinessException("Msg_32");
			}
			
		}else{
			throw new BusinessException("Msg_82");
		}
		
	}
	
}
