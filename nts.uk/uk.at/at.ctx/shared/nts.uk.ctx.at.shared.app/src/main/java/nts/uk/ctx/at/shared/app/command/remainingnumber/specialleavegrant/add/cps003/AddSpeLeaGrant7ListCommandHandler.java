package nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.cps003;

import java.util.ArrayList;
import java.util.List;

//import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.AddSpecialLeaveGrant7Command;
import nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant.add.SpeLeaveGrantCommandHandler;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.MyCustomizeException;
import nts.uk.shr.pereg.app.command.PeregAddListCommandHandler;

@Stateless
public class AddSpeLeaGrant7ListCommandHandler
extends CommandHandlerWithResult<List<AddSpecialLeaveGrant7Command>, List<MyCustomizeException>>
implements PeregAddListCommandHandler<AddSpecialLeaveGrant7Command>  {

		
	@Inject
	private SpeLeaveGrantCommandHandler addSpeLeaveGrantCommandHandler;

	@Override
	public String targetCategoryCd() {
		return "CS00045";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialLeaveGrant7Command.class;
	}

	@Override
	protected List<MyCustomizeException> handle(CommandHandlerContext<List<AddSpecialLeaveGrant7Command>> context) {
		List<AddSpecialLeaveGrant7Command> cmd = context.getCommand();
		String cid = AppContexts.user().companyId();
		List<MyCustomizeException> result = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> insertLst = new ArrayList<>();
		cmd.stream().forEach(c ->{
			String specialId = IdentifierUtil.randomUniqueId();
			SpecialLeaveGrantRemainingData domain = SpecialLeaveGrantRemainingData.createFromJavaType(specialId, cid,
					c.getSid(), 7,
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
