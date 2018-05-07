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
public class AddSpeLeaGrant20CommandHandler
		extends CommandHandlerWithResult<AddSpecialLeaveGrant20Command, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddSpecialLeaveGrant20Command> {

	@Inject
	private SpeLeaveGrantCommandHandler addSpeLeaveGrantCommandHandler;

	@Override
	public String targetCategoryCd() {
		return "CS00068";
	}

	@Override
	public Class<?> commandClass() {
		return AddSpecialLeaveGrant20Command.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddSpecialLeaveGrant20Command> context) {
		val command = context.getCommand();
		String specialId = IdentifierUtil.randomUniqueId();
		String cid = AppContexts.user().companyId();

		// 付与日＞使用期限の場合はエラー #Msg_1023
		if (command.getGrantDate().compareTo(command.getDeadlineDate()) > 0) {
			throw new BusinessException("Msg_1023");
		}

		SpecialLeaveGrantRemainingData domain = SpecialLeaveGrantRemainingData.createFromJavaType(specialId, cid,
				command.getSid(),20,
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
