package nts.uk.ctx.at.shared.app.command.remainingnumber.specialleavegrant;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateSpecialLeaCommandHandler extends CommandHandler<SpecialLeaveRemainCommand> {

	@Inject
	private SpecialLeaveGrantRepository repo;

	@Override
	protected void handle(CommandHandlerContext<SpecialLeaveRemainCommand> context) {
		SpecialLeaveRemainCommand command = context.getCommand();
		GeneralDate grantDate = GeneralDate.fromString(command.getGrantDate(), "yyyy/MM/dd");
		boolean isHasData = this.repo.isHasData(command.getSid(), command.getSpecialid(), grantDate, command.getSpecialLeaCode());

		// 付与日＞使用期限の場合はエラー #Msg_1023
		if (command.getGrantDate().compareTo(command.getDeadlineDate()) > 0) {
			throw new BusinessException("Msg_1023");
		}

		//update No.2845 theo mã bug 102061 - ユニーク制約, 社員ID、付与日 #Msg_1456
		if(isHasData == true) {
			throw new BusinessException("Msg_1456");
		}


		SpecialLeaveGrantRemainingData data = SpecialLeaveGrantRemainingData.createFromJavaType(
				command.getSpecialid(),
				command.getSid(),
				grantDate,
				GeneralDate.fromString(command.getDeadlineDate(), "yyyy/MM/dd"),
				command.getExpStatus(),
				GrantRemainRegisterType.MANUAL.value,
				command.getNumberDayGrant().doubleValue(),
				command.getTimeGrant(),
				command.getNumberDayUse().doubleValue(),
				command.getTimeUse(),
				null,
				command.getNumberDayRemain().doubleValue(),
				command.getTimeRemain(),
				0.0,
				command.getSpecialLeaCode());

		repo.update(data);
	}

}
