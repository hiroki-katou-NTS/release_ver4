package nts.uk.ctx.at.request.app.command.application.overtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateOvertimeCommandHandler extends CommandHandlerWithResult<UpdateOvertimeCommand, List<String>>{

	@Inject
	private OvertimeRepository overtimeRepository;
	
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Override
	protected List<String> handle(CommandHandlerContext<UpdateOvertimeCommand> context) {
		String companyID = AppContexts.user().companyId();
		UpdateOvertimeCommand command = context.getCommand();
		Optional<AppOverTime> opAppOverTime = overtimeRepository.getFullAppOvertime(companyID, command.getAppID());
		if(!opAppOverTime.isPresent()){
			throw new RuntimeException("khong ton tai doi tuong de update");
		}
		AppOverTime appOverTime = opAppOverTime.get();
		List<OverTimeInput> overTimeInputs = new ArrayList<>();
		overTimeInputs.addAll(command.getRestTime().stream().filter(x -> x.getStartTime()!=null||x.getEndTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getOvertimeHours().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getBreakTimes().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		overTimeInputs.addAll(command.getBonusTimes().stream().filter(x -> x.getApplicationTime()!=null).map(x -> x.convertToDomain()).collect(Collectors.toList()));
		String divergenceReason = command.getDivergenceReasonContent().replaceFirst(":", System.lineSeparator());
		String applicationReason = command.getApplicationReason().replaceFirst(":", System.lineSeparator());
		appOverTime.setDivergenceReason(divergenceReason);
		appOverTime.setFlexExessTime(command.getFlexExessTime());
		appOverTime.setOverTimeAtr(EnumAdaptor.valueOf(command.getOvertimeAtr(), OverTimeAtr.class));
		appOverTime.setOverTimeInput(overTimeInputs);
		appOverTime.setOverTimeShiftNight(command.getOverTimeShiftNight());
		appOverTime.setSiftCode(command.getSiftTypeCode() == null ? null : new WorkTimeCode(command.getSiftTypeCode()));
		appOverTime.setWorkClockFrom1(command.getWorkClockFrom1());
		appOverTime.setWorkClockFrom2(command.getWorkClockFrom2());
		appOverTime.setWorkClockTo1(command.getWorkClockTo1());
		appOverTime.setWorkClockTo2(command.getWorkClockTo2());
		appOverTime.setWorkTypeCode(command.getWorkTypeCode() == null? null : new WorkTypeCode(command.getWorkTypeCode()));
		appOverTime.getApplication().setAppReason(new AppReason(applicationReason));
		appOverTime.setVersion(appOverTime.getVersion());
		appOverTime.getApplication().setVersion(command.getVersion());
		
		detailBeforeUpdate.processBeforeDetailScreenRegistration(
				companyID, 
				appOverTime.getApplication().getEmployeeID(), 
				appOverTime.getApplication().getAppDate(), 
				1, 
				appOverTime.getAppID(), 
				appOverTime.getApplication().getPrePostAtr(), command.getVersion());
		overtimeRepository.update(appOverTime);
		applicationRepository.updateWithVersion(appOverTime.getApplication());
		return detailAfterUpdate.processAfterDetailScreenRegistration(appOverTime.getApplication());
	}

}
