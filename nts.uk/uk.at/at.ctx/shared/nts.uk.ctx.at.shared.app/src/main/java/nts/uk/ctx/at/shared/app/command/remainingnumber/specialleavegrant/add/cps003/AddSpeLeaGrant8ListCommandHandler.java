package nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.cps003;

import java.util.ArrayList;
import java.util.List;

//import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.AddSpecialLeaveGrant8Command;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.SpeLeaveGrantCommandHandler;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;

@Stateless
public class AddSpeLeaGrant8ListCommandHandler
extends CommandHandlerWithResult<List<AddSpecialLeaveGrant8Command>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddSpecialLeaveGrant8Command> {

		
	@Inject
	private SpeLeaveGrantCommandHandler addSpeLeaveGrantCommandHandler;

	@Override
	public String targetCategoryCd() {
		return "CS00046";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialLeaveGrant8Command.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(CommandHandlerContext<List<AddSpecialLeaveGrant8Command>> context) {
		List<AddSpecialLeaveGrant8Command> cmd = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<PeregAddCommandResult> result = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> insertLst = new ArrayList<>();
		cmd.parallelStream().forEach(c ->{
			String specialId = IdentifierUtil.randomUniqueId();
			SpecialLeaveGrantRemainingData domain = SpecialLeaveGrantRemainingData.createFromJavaType(specialId, cid,
					c.getSid(), 8,
					c.getGrantDate(),c.getDeadlineDate(), 
					c.getExpStatus().intValue(),
					GrantRemainRegisterType.MANUAL.value, 
					c.getNumberDayGrant(), 
					c.getTimeGrant() != null ? c.getTimeGrant().intValue() : null ,
					c.getNumberDayUse(), 
					c.getTimeUse() != null ? c.getTimeUse().intValue() : null, 
					null,
					c.getNumberDaysOver(),
					c.getTimeOver() != null ? c.getTimeOver().intValue() : null,
					c.getNumberDayRemain(),
					c.getTimeRemain() != null ? c.getTimeRemain().intValue() : null,
					c.grantDateItemName, c.deadlineDateItemName);
			insertLst.add(domain);
		});
		if(!insertLst.isEmpty()) {
			result.addAll(addSpeLeaveGrantCommandHandler.addHandler(insertLst));
		}
		
		return result;
	}
}
