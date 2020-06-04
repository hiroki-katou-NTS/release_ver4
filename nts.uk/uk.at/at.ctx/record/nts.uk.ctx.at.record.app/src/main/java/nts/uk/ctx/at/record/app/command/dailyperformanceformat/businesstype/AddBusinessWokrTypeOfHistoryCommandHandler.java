package nts.uk.ctx.at.record.app.command.dailyperformanceformat.businesstype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfHistoryGeneralRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.pereg.app.command.PeregAddCommandHandler;
import nts.uk.shr.pereg.app.command.PeregAddCommandResult;

@Stateless
public class AddBusinessWokrTypeOfHistoryCommandHandler
		extends CommandHandlerWithResult<AddBusinessWokrTypeOfHistoryCommand, PeregAddCommandResult>
		implements PeregAddCommandHandler<AddBusinessWokrTypeOfHistoryCommand> {
	@Inject
	private BusinessTypeOfEmployeeRepository typeOfEmployeeRepos;

	@Inject
	private BusinessTypeEmpOfHistoryRepository typeEmployeeOfHistoryRepos;

	@Inject
	private BusinessTypeOfHistoryGeneralRepository typeOfHistoryGeneralRepos;

	@Override
	public String targetCategoryCd() {
		return "CS00021";
	}

	@Override
	public Class<?> commandClass() {
		return AddBusinessWokrTypeOfHistoryCommand.class;
	}

	@Override
	protected PeregAddCommandResult handle(CommandHandlerContext<AddBusinessWokrTypeOfHistoryCommand> context) {
		String companyId = AppContexts.user().companyId();
		String historyId = IdentifierUtil.randomUniqueId();

		AddBusinessWokrTypeOfHistoryCommand command = context.getCommand();
		String employeeId = command.getEmployeeId();
		String businessTypeCode = command.getBusinessTypeCode();
		// Hop.NT update in case of startDate is null set to minDate
		GeneralDate startDate = command.getStartDate() != null ? command.getStartDate() : GeneralDate.min();
		// Hop.NT update in case of endDate is null set to maxDate
		GeneralDate endDate = command.getEndDate() != null ? command.getEndDate() : GeneralDate.max();
		Optional<BusinessTypeOfEmployeeHistory> optional = typeEmployeeOfHistoryRepos.findByEmployee(companyId,employeeId);
		
		List<DateHistoryItem> history = new ArrayList<DateHistoryItem>();
		BusinessTypeOfEmployeeHistory bEmployeeHistory = new BusinessTypeOfEmployeeHistory(companyId, history, employeeId);
		
		if (optional.isPresent()) {
			bEmployeeHistory = optional.get();
		}
		DateHistoryItem newDate = new DateHistoryItem(historyId, new DatePeriod(startDate, endDate));
		bEmployeeHistory.add(newDate);
		// insert typeof employee
		BusinessTypeOfEmployee bEmployee = BusinessTypeOfEmployee.createFromJavaType(businessTypeCode, historyId,
				employeeId);
		typeOfHistoryGeneralRepos.addBusinessTypeEmpOfHistory(bEmployeeHistory, bEmployee);
		
		return new PeregAddCommandResult(historyId);
	}

}
