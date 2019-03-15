package nts.uk.ctx.at.shared.app.command.remainingnumber.empinfo.basicinfo.add;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.shared.app.command.remainingnumber.empinfo.basicinfo.SpLeaInfoCommandHandler;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.SpecialLeaveCode;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;
@Stateless
public class AddSpLea4InfoListCommandHandler extends CommandHandlerWithResult<List<AddSpecialleave4informationCommand>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddSpecialleave4informationCommand>{
	@Inject
	private SpLeaInfoCommandHandler addSpLeaInfoCommandHandler;
	@Override
	public String targetCategoryCd() {
		return "CS00028";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialleave4informationCommand.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(
			CommandHandlerContext<List<AddSpecialleave4informationCommand>> context) {
		String cid = AppContexts.user().companyId();
		List<AddSpecialleave4informationCommand> cmd = context.getCommand();
		List<SpecialLeaveBasicInfo> domains = cmd.parallelStream().map(c ->{return new SpecialLeaveBasicInfo(cid, c.getSID(), SpecialLeaveCode.CS00028.value,
				c.getUseAtr(), c.getAppSet(), c.getGrantDate(),
				c.getGrantDays() != null ? c.getGrantDays().intValue() : null, c.getGrantTable());}).collect(Collectors.toList());
		return addSpLeaInfoCommandHandler.addAllHandler(domains);
	}

}
