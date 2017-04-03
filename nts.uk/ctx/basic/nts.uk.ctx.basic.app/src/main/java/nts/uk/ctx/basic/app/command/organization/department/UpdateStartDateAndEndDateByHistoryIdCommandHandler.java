package nts.uk.ctx.basic.app.command.organization.department;

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
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UpdateStartDateAndEndDateByHistoryIdCommandHandler
		extends CommandHandler<UpdateStartDateandEndDateHistoryCommand> {

	@Inject
	private DepartmentRepository departmentRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateStartDateandEndDateHistoryCommand> context) {
		// TODO Auto-generated method stub
		String companyCode = AppContexts.user().companyCode();
		Date startDate1 = new Date();
		Date endDate1 = new Date();
		if (!departmentRepository.isExistHistory(companyCode, context.getCommand().getHistoryId1())) {
			throw new BusinessException("ER06");
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			startDate1 = formatter.parse(context.getCommand().getNewStartDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GeneralDate startDate = GeneralDate.legacyDate(startDate1);
		departmentRepository.updateStartdate(companyCode, context.getCommand().getHistoryId1(), startDate);

		if (context.getCommand().getHistoryId2() != null) {
			if (!departmentRepository.isExistHistory(companyCode, context.getCommand().getHistoryId2())) {
				throw new BusinessException("ER06");
			}
			try {
				endDate1 = formatter.parse(context.getCommand().getNewEndDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GeneralDate endDate = GeneralDate.legacyDate(endDate1);
			departmentRepository.updateEnddate(companyCode, context.getCommand().getHistoryId2(), endDate);
		}

	}

}
