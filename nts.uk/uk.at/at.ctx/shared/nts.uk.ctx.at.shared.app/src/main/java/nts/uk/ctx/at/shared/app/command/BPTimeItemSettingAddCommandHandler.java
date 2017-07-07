package nts.uk.ctx.at.shared.app.command;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemSettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BPTimeItemSetting;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class BPTimeItemSettingAddCommandHandler extends CommandHandler<List<BPTimeItemSettingAddCommand>> {
	@Inject
	private BPTimeItemSettingRepository bpTimeItemSettingRepository;

	@Override
	public void handle(CommandHandlerContext<List<BPTimeItemSettingAddCommand>> context) {
		String companyId = AppContexts.user().companyId();
		List<BPTimeItemSettingAddCommand> bpTimeItemSettingAddCommand = context.getCommand();
		bpTimeItemSettingRepository.addListSetting(bpTimeItemSettingAddCommand.stream()
				.map(c -> toBPTimeItemSettingDomain(c,companyId)).collect(Collectors.toList()));
	}

	private BPTimeItemSetting toBPTimeItemSettingDomain(BPTimeItemSettingAddCommand bpTimeItemSettingAddCommand,String companyId) {
		return BPTimeItemSetting.createFromJavaType(companyId,
				bpTimeItemSettingAddCommand.getTimeItemId(), bpTimeItemSettingAddCommand.holidayCalSettingAtr,
				bpTimeItemSettingAddCommand.overtimeCalSettingAtr, bpTimeItemSettingAddCommand.worktimeCalSettingAtr);
	}

}
