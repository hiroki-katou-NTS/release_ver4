package nts.uk.ctx.basic.app.command.organization.position;

import java.time.LocalDate;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.position.JobHistory;
import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateHistoryCommandHandler extends CommandHandler<UpdateHistoryCommand> {

	@Inject
	private PositionRepository repository;

	@Override 
	protected void handle(CommandHandlerContext<UpdateHistoryCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		UpdateHistoryCommand command = context.getCommand();

		String historyId = command.getHistoryId();
		//check xem lich su dang chon con ton tai hay khong
		Optional<JobHistory> historyInfo = repository.findSingleHistory(companyCode, historyId);
		if(!historyInfo.isPresent()){
			throw new BusinessException("ER026");
		}
		//update start lich su dang chon
		JobHistory history = historyInfo.get();
		GeneralDate newStartDate = GeneralDate.localDate(LocalDate.parse(command.getNewStartDate()));
		history.setStartDate(newStartDate);
		repository.updateHistory(history);
		//Neu co thang lich su duoi no thi update enddate
		GeneralDate oldEndate = GeneralDate.localDate(LocalDate.parse(command.getOldStartDate())).addDays(-1);
		GeneralDate newEndate = newStartDate.addDays(-1);
		Optional<JobHistory> getHistoryByEndDate = repository.getHistoryByEdate(companyCode, oldEndate);
		if(getHistoryByEndDate.isPresent()){
			JobHistory historyPre = getHistoryByEndDate.get();
			historyPre.setEndDate(newEndate);
			repository.updateHistory(historyPre);
		}
	}

}
