package nts.uk.ctx.basic.app.command.organization.workplace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateEndDateWorkPalceByHistoryIdCommandHandler extends CommandHandler<String> {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<String> context) {
		// update enddate of History when insert 1 history.
		String companyCode = AppContexts.user().companyCode();
		Date endDate1 = new Date();
		// check isExistHistory
		if (!workPlaceRepository.isExistHistory(companyCode, context.getCommand().toString())) {
			throw new BusinessException("ER06");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			endDate1 = formatter.parse("9999/12/31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GeneralDate endDate = GeneralDate.legacyDate(endDate1);
		workPlaceRepository.updateEnddate(companyCode, context.getCommand().toString(), endDate);

	}

}
