package nts.uk.ctx.at.request.app.command.application.remainingnumber.checkfunc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.file.export.ExportServiceResult;
import nts.arc.task.data.TaskDataSetter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.SyEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SyEmployeeImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.AnnualBreakManageAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.AnnualBreakManageImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.DailyWorkTypeListImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.NumberOfWorkTypeUsedImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.YearlyHolidaysTimeRemainingImport;
import nts.uk.ctx.at.request.dom.application.remainingnumer.ExcelInforCommand;
import nts.uk.ctx.at.request.dom.application.remainingnumer.PlannedVacationListCommand;
import nts.uk.ctx.at.request.dom.vacation.history.PlanVacationHistory;
import nts.uk.ctx.at.request.dom.vacation.history.VacationHistoryRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

@Stateful
public class SyncCheckFuncDataCommandHandler extends AsyncCommandHandler<CheckFuncDataCommand> {

	private static final String NUMBER_OF_SUCCESS = "NUMBER_OF_SUCCESS";
	private static final String NUMBER_OF_ERROR = "NUMBER_OF_ERROR";
	private static final String MSG_1116 = "Msg_1116";
	private static final String MSG_1316 = "Msg_1316";
	private static final String MSG_1139 = "Msg_1139";
	private static final String ERROR_LIST = "ERROR_LIST";

	@Inject
	private SyEmployeeAdapter employeeRecordAdapter;

	@Inject
	private WorkTypeRepository workTypeRepository;

	@Inject
	private AnnualBreakManageAdapter annualBreakManageAdapter;

	@Inject
	private VacationHistoryRepository vacationHistoryRepository;

	@Inject
	private RemainingNumberExportExcel exportService;

	@Override
	protected void handle(CommandHandlerContext<CheckFuncDataCommand> context) {
		val asyncTask = context.asAsync();
		TaskDataSetter setter = asyncTask.getDataSetter();

		// employee export list, error export list
		List<EmployeeSearchCommand> employeeListResult = new ArrayList<>();
		List<OutputErrorInfoCommand> outputErrorInfoCommand = new ArrayList<>();

		// get data from client to server
		Integer countEmployee = new Integer(0);
		CheckFuncDataCommand command = context.getCommand();
		List<EmployeeSearchCommand> employeeSearchCommand = command.getEmployeeList();
		setter.setData(NUMBER_OF_SUCCESS, command.getPass());
		setter.setData(NUMBER_OF_ERROR, command.getError());
		if (asyncTask.hasBeenRequestedToCancel()) {
			return;
		}
		List<String> employeeList = employeeSearchCommand.stream().map(f -> f.getEmployeeId())
				.collect(Collectors.toList());
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return;
		}
		List<PlannedVacationListCommand> plannedVacationList = new ArrayList<>();
		// ?????????????????????????????????
		plannedVacationList = getPlannedVacationList(asyncTask, command.getDate(), outputErrorInfoCommand,
				countEmployee, setter);
		if (outputErrorInfoCommand.size() > 0) {
			JsonObject value = Json.createObjectBuilder()
					.add("employeeCode", outputErrorInfoCommand.get(0).getEmployeeCode())
					.add("employeeName", outputErrorInfoCommand.get(0).getEmployeeName())
					.add("errorMessage", outputErrorInfoCommand.get(0).getErrorMessage()).build();
			setter.setData(ERROR_LIST, value);
			return;
		}
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return;
		}
		// ???????????????????????????ID????????????????????????????????????????????????????????????????????????????????????????????????
		// RequestList ??????No304.
		List<AnnualBreakManageImport> employeeIdRq304 = annualBreakManageAdapter.getEmployeeId(employeeList,
				command.getStartTime(), command.getEndTime());
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return;
		}
		// ???????????????.??????List??????????????????????????????????????????ID(List)???????????????
		checkEmployeeListId(asyncTask, employeeSearchCommand, employeeIdRq304, employeeListResult,
				outputErrorInfoCommand, countEmployee, setter);
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return;
		}
		countEmployee = employeeSearchCommand.size() - employeeListResult.size();
		setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
		if (employeeListResult.size() > 0) {
			// ??????????????????????????????

			// ?????????????????????Excel???????????????????????????????????????
			List<ExcelInforCommand> excelInforList = new ArrayList<>();
			List<String> empResultIds = employeeListResult.stream().map(i -> i.getEmployeeId())
					.collect(Collectors.toList());
			Map<String, SyEmployeeImport> employeeRecordImportMap = employeeRecordAdapter.getPersonInfor(empResultIds)
					.stream().collect(Collectors.toMap(i -> i.getEmployeeId(), i -> i));
			for (int i = 0; i < employeeListResult.size(); i++) {
				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					break;
				}
				// Imported(??????)???????????????????????????
				SyEmployeeImport employeeRecordImport = employeeRecordImportMap
						.get(employeeListResult.get(i).getEmployeeId());

				if (employeeRecordImport == null) {
					// ????????????
					// ???????????????.?????????????????????????????????
					++countEmployee;
					setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
					continue;
				}
				// ????????????
				// RequestList ??????No327 - ????????????????????????????????????????????????????????????????????????????????????
				// (Th???c hi???n thu???t to??n ????????????????????????????????????????????????-l???y s??? ph??p c??n l???i
				// t???i th???i ??i???m x??c ?????nh???)
				List<YearlyHolidaysTimeRemainingImport> yearlyHolidaysTimeRemainingImport = annualBreakManageAdapter
						.getYearHolidayTimeAnnualRemaining(employeeListResult.get(i).getEmployeeId(), command.getDate(),
								command.getStartTime(), command.getEndTime());
				if (yearlyHolidaysTimeRemainingImport.isEmpty()) {
					// ????????????
					// ???????????????.?????????????????????????????????
					++countEmployee;
					setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
					continue;
				}
				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					break;
				}

				// ???????????????.?????????????????????????????????
				// (Check s??? ph??p c??n l???i trong param -???????????????.????????????)
				if (!checkMaxDayEmployeeList(asyncTask, employeeListResult.get(i), command.getMaxDay(),
						outputErrorInfoCommand, yearlyHolidaysTimeRemainingImport)) {
					countEmployee++;
					setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
					continue;
				}
				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					break;
				}
				// ????????????
				// RequestList ??????No328
				List<String> workTypeCode = plannedVacationList.stream().map(x -> x.getWorkTypeCode())
						.collect(Collectors.toList());
				Optional<DailyWorkTypeListImport> dailyWorkTypeListImport = this.annualBreakManageAdapter
						.getDailyWorkTypeUsed(employeeListResult.get(i).getEmployeeId(), workTypeCode,
								command.getStartTime(), command.getEndTime());
				if (!dailyWorkTypeListImport.isPresent()) {
					// ????????????
					// ???????????????.?????????????????????????????????
					++countEmployee;
					setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
					continue;
				}
				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					break;
				}
				// ??????????????????????????????Excel ????????????List???????????????
				ExcelInforCommand excelInforCommand = new ExcelInforCommand();
				excelInforCommand.setEmployeeCode(employeeRecordImport.getEmployeeCode());
				excelInforCommand.setName(employeeRecordImport.getBusinessName());
				excelInforCommand.setDateStart(employeeRecordImport.getEntryDate().toString());
				excelInforCommand.setDateEnd("9999/12/31".equals(employeeRecordImport.getRetiredDate().toString()) ? ""
						: employeeRecordImport.getRetiredDate().toString());
				excelInforCommand
						.setDateOffYear(yearlyHolidaysTimeRemainingImport.get(0).getAnnualHolidayGrantDay().toString());
				excelInforCommand.setDateTargetRemaining(command.getDate().toString());
				excelInforCommand.setDateAnnualRetirement(
						yearlyHolidaysTimeRemainingImport.get(0).getAnnualRemainingGrantTime());
				excelInforCommand.setDateAnnualRest(yearlyHolidaysTimeRemainingImport.get(0).getAnnualRemaining());
				++countEmployee;
				setter.updateData(NUMBER_OF_SUCCESS, countEmployee);

				List<NumberOfWorkTypeUsedImport> listNumberOfWorkTypeUsedImport = dailyWorkTypeListImport.get()
						.getNumberOfWorkTypeUsedExports().stream().collect(Collectors.toList());
				List<PlannedVacationListCommand> listPlannedVacationListCommand = plannedVacationList.stream()
						.collect(Collectors.toList());
				excelInforCommand.setNumberOfWorkTypeUsedImport(listNumberOfWorkTypeUsedImport);
				excelInforCommand.setPlannedVacationListCommand(listPlannedVacationListCommand);
				excelInforList.add(excelInforCommand);
			}
			setter.setData("EXPORT_SIZE", excelInforList.size());
			ExportServiceResult exportResult = exportService.start(excelInforList);
			setter.setData("EXPORT_TASK_ID", exportResult.getTaskId());

			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				return;
			}
		}
		// push list err
		if (outputErrorInfoCommand.size() > 0) {
			// ???????????????????????????
			for (int i = 0; i < outputErrorInfoCommand.size(); i++) {
				JsonObject value = Json.createObjectBuilder()
						.add("employeeCode", outputErrorInfoCommand.get(i).getEmployeeCode())
						.add("employeeName", outputErrorInfoCommand.get(i).getEmployeeName())
						.add("errorMessage", outputErrorInfoCommand.get(i).getErrorMessage()).build();
				setter.setData(ERROR_LIST + i, value);
				setter.updateData(NUMBER_OF_ERROR, i + 1);

				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					return;
				}
			}
		}
		// delay a moment.
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param outputErrorInfoCommand
	 * @param maxDay
	 * @return
	 */
	private List<PlannedVacationListCommand> getPlannedVacationList(
			AsyncCommandHandlerContext<CheckFuncDataCommand> asyncTask, GeneralDate confirmDate,
			List<OutputErrorInfoCommand> outputErrorInfoCommand, Integer countEmployee, TaskDataSetter setter) {
		List<PlannedVacationListCommand> plannedVacationListCommand = new ArrayList<>();
		LoginUserContext loginUserContext = AppContexts.user();
		String companyId = AppContexts.user().companyId();
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return plannedVacationListCommand;
		}
		// ??????????????????????????????????????????????????????????????????????????? (L???y domain ???????????????????????????????????????)
		List<PlanVacationHistory> planVacationHistory = vacationHistoryRepository.findHistoryByBaseDate(companyId,
				confirmDate);
		// iVactionHistoryRulesService
		// .getPlanVacationHistoryByDate(companyId, date);
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return plannedVacationListCommand;
		}
		if (planVacationHistory.isEmpty()) {
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				return plannedVacationListCommand;
			}
			// ????????????????????????????????????
			OutputErrorInfoCommand outputErrorInfo = new OutputErrorInfoCommand();
			outputErrorInfo.setEmployeeCode("");
			outputErrorInfo.setEmployeeName("");
			outputErrorInfo.setErrorMessage(MSG_1139);
			setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
			outputErrorInfoCommand.add(outputErrorInfo);
			return plannedVacationListCommand;
		}
		for (PlanVacationHistory element : planVacationHistory) {
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				return plannedVacationListCommand;
			}
			// ???????????????????????????????????????????????????????????????????????????????????? (L???y domain ????????????????????????????????????????????????)
			List<PlanVacationHistory> planVacationHistoryMaxDay = vacationHistoryRepository.findHistory(companyId,
					element.identifier());
			// ?????????????????????????????????????????????????????? (l???y domain ??????????????????)
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				return plannedVacationListCommand;
			}
			Optional<WorkType> workType = workTypeRepository.findByDeprecated(loginUserContext.companyId(),
					element.getWorkTypeCode());
			if (workType.isPresent()) {
				if (asyncTask.hasBeenRequestedToCancel()) {
					asyncTask.finishedAsCancelled();
					return plannedVacationListCommand;
				}
				PlannedVacationListCommand plannedVacation = new PlannedVacationListCommand();
				plannedVacation.setWorkTypeCode(workType.get().getWorkTypeCode().toString());
				plannedVacation.setWorkTypeName(workType.get().getName().toString());
				plannedVacation.setMaxNumberDays(planVacationHistoryMaxDay.get(0).getMaxDay().v());
				plannedVacationListCommand.add(plannedVacation);
			}
		}

		return plannedVacationListCommand;
	}

	/**
	 * @param setter
	 * @param employeeIdRq304
	 * @param employeeListResult
	 * @param outputErrorInfoCommand
	 * @param employeeSearchCommand
	 */
	private void checkEmployeeListId(AsyncCommandHandlerContext<CheckFuncDataCommand> asyncTask,
			List<EmployeeSearchCommand> employeeSearchCommand, List<AnnualBreakManageImport> employeeIdRq304,
			List<EmployeeSearchCommand> employeeListResult, List<OutputErrorInfoCommand> outputErrorInfoCommand,
			Integer countEmployee, TaskDataSetter setter) {
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			return;
		}
		for (EmployeeSearchCommand employee : employeeSearchCommand) {
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				return;
			}
			Optional<AnnualBreakManageImport> findEmployeeById = employeeIdRq304.stream()
					.filter(x -> employee.getEmployeeId().equals(x.getEmployeeId())).findFirst();
			if (findEmployeeById.isPresent()) {
				// ????????????????????????????????????
				employeeListResult.add(employee);

			} else {
				// ???????????????.??????List????????????????????????????????????
				OutputErrorInfoCommand outputErrorInfo = new OutputErrorInfoCommand();
				outputErrorInfo.setEmployeeCode(employee.getEmployeeCode());
				outputErrorInfo.setEmployeeName(employee.getEmployeeName());
				outputErrorInfo.setErrorMessage(MSG_1116);
				countEmployee++;
				setter.updateData(NUMBER_OF_SUCCESS, countEmployee);
				outputErrorInfoCommand.add(outputErrorInfo);
			}
		}
	}

	/**
	 * @param setter
	 * @param employeeCheckMaxDay
	 * @param employeeListResult
	 */
	private boolean checkMaxDayEmployeeList(AsyncCommandHandlerContext<CheckFuncDataCommand> asyncTask,
			EmployeeSearchCommand employee, Double maxDay, List<OutputErrorInfoCommand> outputErrorInfoCommand,
			List<YearlyHolidaysTimeRemainingImport> yearlyHolidaysTimeRemainingImport) {
		if (maxDay != null) {
			if (yearlyHolidaysTimeRemainingImport.get(0).getAnnualRemaining().compareTo(maxDay) >= 0) {
				// ???????????????????????? ??? ???????????????.????????????
				// ???????????????.?????????????????????????????????
				OutputErrorInfoCommand outputErrorInfo = new OutputErrorInfoCommand();
				outputErrorInfo.setEmployeeCode(employee.getEmployeeCode());
				outputErrorInfo.setEmployeeName(employee.getEmployeeName());
				outputErrorInfo.setErrorMessage(MSG_1316);

				outputErrorInfoCommand.add(outputErrorInfo);
				return false;
			}
		}
		return true;
	}

}
