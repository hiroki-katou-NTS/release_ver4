package nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.cps003;

import java.util.ArrayList;
import java.util.List;

//import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.AddSpecialLeaveGrant9Command;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.SpeLeaveGrantCommandHandler;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;

@Stateless
public class AddSpeLeaGrant9ListCommandHandler
extends CommandHandlerWithResult<List<AddSpecialLeaveGrant9Command>, List<PeregAddCommandResult>>
implements PeregAddListCommandHandler<AddSpecialLeaveGrant9Command> {

		
	@Inject
	private SpeLeaveGrantCommandHandler addSpeLeaveGrantCommandHandler;

	@Override
	public String targetCategoryCd() {
		return "CS00047";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialLeaveGrant9Command.class;
	}

	@Override
	protected List<PeregAddCommandResult> handle(CommandHandlerContext<List<AddSpecialLeaveGrant9Command>> context) {
		List<AddSpecialLeaveGrant9Command> cmd = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<PeregAddCommandResult> result = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> insertLst = new ArrayList<>();
		cmd.parallelStream().forEach(c ->{
			String specialId = IdentifierUtil.randomUniqueId();
			SpecialLeaveGrantRemainingData domain = SpecialLeaveGrantRemainingData.createFromJavaType(specialId, cid,
					c.getSid(), 9,
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
