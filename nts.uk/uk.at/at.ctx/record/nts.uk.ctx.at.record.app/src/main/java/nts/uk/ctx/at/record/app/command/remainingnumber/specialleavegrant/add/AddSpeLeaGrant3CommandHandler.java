package nts.uk.ctx.at.record.app.command.remainingnumber.specialleavegrant.add;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddSpeLeaGrant3CommandHandler
		extends CommandHandlerWithResult<AddSpecialLeaveGrant3Command, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddSpecialLeaveGrant3Command> {

	@Inject
	private SpeLeaveGrantCommandHandler addSpeLeaveGrantCommandHandler;

	@Override
	public String targetCategoryCd() {
		return "CS00041";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialLeaveGrant3Command.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddSpecialLeaveGrant3Command> context) {
		val command = context.getCommand();
		String specialId = IdentifierUtil.randomUniqueId();
		String cid = AppContexts.user().companyId();

		SpecialLeaveGrantRemainingData domain = SpecialLeaveGrantRemainingData.createFromJavaType(specialId, cid,
				command.getSid(), 3,
				command.getGrantDate(),command.getDeadlineDate(), 
				command.getExpStatus().intValue(),
				GrantRemainRegisterType.MANUAL.value, 
				command.getNumberDayGrant().doubleValue(), 
				command.getTimeGrant() != null ? command.getTimeGrant().intValue() : null ,
				command.getNumberDayUse().doubleValue(), 
				command.getTimeUse() != null ? command.getTimeUse().intValue() : null, 
				0.0,
				command.getNumberDaysOver().doubleValue(),
				command.getTimeOver() != null ? command.getTimeOver().intValue() : null,
				command.getNumberDayRemain().doubleValue(),
				command.getTimeRemain() != null ? command.getTimeRemain().intValue() : null);

		return new PeregAddCommandResult(addSpeLeaveGrantCommandHandler.addHandler(domain));
	}
}
