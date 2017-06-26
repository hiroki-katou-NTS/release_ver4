package nts.uk.ctx.basic.app.command.organization.workplace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.organization.department.DepartmentRepository;
import nts.uk.ctx.basic.dom.organization.workplace.WorkPlaceRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateStartDateAndEndDateByHistoryIdWkPCommandHandler
		extends CommandHandler<UpdateStartDateandEndDateWKPHistoryCommand> {

	@Inject
	private WorkPlaceRepository workPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateStartDateandEndDateWKPHistoryCommand> context) {
		// update StartDate of History item When click edit history button
		// and update endDate of History
		String companyCode = AppContexts.user().companyCode();
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		if (!workPlaceRepository.isExistHistory(companyCode, context.getCommand().getHistoryId1())) {
			throw new BusinessException("ER06");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			startDate1 = formatter.parse(context.getCommand().getNewStartDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GeneralDate startDate = GeneralDate.legacyDate(startDate1);
		workPlaceRepository.updateStartdate(companyCode, context.getCommand().getHistoryId1(), startDate);

		if (context.getCommand().getHistoryId2() != null) {
			if (!workPlaceRepository.isExistHistory(companyCode, context.getCommand().getHistoryId2())) {
				throw new BusinessException("ER06");
			}
			try {
				endDate1 = formatter.parse(context.getCommand().getNewEndDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);
			workPlaceRepository.updateEnddate(companyCode, context.getCommand().getHistoryId2(), endDate);
		}
	}

}
