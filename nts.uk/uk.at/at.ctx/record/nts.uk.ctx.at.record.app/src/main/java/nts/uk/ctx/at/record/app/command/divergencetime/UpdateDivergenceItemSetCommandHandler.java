package nts.uk.ctx.at.record.app.command.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItemSet;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class UpdateDivergenceItemSetCommandHandler extends CommandHandler<List<UpdateDivergenceItemSetCommand>> {
	@Inject
	private DivergenceTimeRepository divTimeRepo;

	@Override
	protected void handle(CommandHandlerContext<List<UpdateDivergenceItemSetCommand>> context) {
		String companyId = AppContexts.user().companyId();
		List<DivergenceItemSet> listUpdate = context.getCommand().stream().map(c -> {
			return new DivergenceItemSet(companyId, c.getDivTimeId(), c.getAttendanceId());
		}).collect(Collectors.toList());
		if (listUpdate == null) {
			return;
		}
		divTimeRepo.deleteItemId(companyId, context.getCommand().get(0).getDivTimeId());
		divTimeRepo.addItemId(listUpdate);
	}

}
