package nts.uk.ctx.at.record.app.command.remainingnumber.rervleagrtremnum;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.RervLeaGrantRemDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;

@Stateless
public class UpdateResvLeaCommandHandler extends AsyncCommandHandler<UpdateResvLeaRemainCommand> {

	@Inject
	private RervLeaGrantRemDataRepository resvLeaRepo;

	@Override
	protected void handle(CommandHandlerContext<UpdateResvLeaRemainCommand> context) {
		UpdateResvLeaRemainCommand c = context.getCommand();
		ReserveLeaveGrantRemainingData data = ReserveLeaveGrantRemainingData.createFromJavaType(c.getRvsLeaId(),
				c.getEmployeeId(), c.getGrantDate(), c.getDeadline(), c.getExpirationStatus().value,
				GrantRemainRegisterType.MANUAL.value, c.getGrantDays(), c.getUseDays(), c.getOverLimitDays(),
				c.getRemainingDays());
		resvLeaRepo.update(data);
	}

}
