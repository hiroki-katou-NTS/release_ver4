/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.executionlog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.PeriodObject;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleExecutionLogDto;
import nts.uk.ctx.at.schedule.app.find.executionlog.dto.ScheduleExecutionLogInfoDto;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmployeeDto;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreatorRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ScheduleExecutionLogFinder.
 */
@Stateless
public class ScheduleExecutionLogFinder {

	/** The schedule execution log repository. */
	@Inject
	private ScheduleExecutionLogRepository scheduleExecutionLogRepository;
	
	/** The schedule creator repository. */
	@Inject
	private ScheduleCreatorRepository scheduleCreatorRepository;
	/** The schedule creator repository. */
	@Inject
	private ScheduleErrorLogRepository scheduleErrorLogRepository;
	
	/** The Constant DEFAULT_NUMBER. */
	public static final int DEFAULT_NUMBER = 0;
	
	/** The Constant NEXT_NUMBER. */
	public static final int NEXT_NUMBER = 1;

	/** The employee adapter. */
	@Inject
	private SCEmployeeAdapter employeeAdapter;
	
	/**
	 * Find by date.
	 *
	 * @param period the period
	 * @return the list
	 */
	public List<ScheduleExecutionLogDto> findByDate(PeriodObject period) {

		// get company id by login
		String companyId = AppContexts.user().companyId();

		// check period is null
		if (period == null) {
			return new ArrayList<>();
		}

		// call repository find by date time
		List<ScheduleExecutionLog> scheduleExecutionLogs = scheduleExecutionLogRepository.findByDateTime(companyId,
				period.getStartDate(), period.getEndDate());

		// check empty data by select
		if (CollectionUtil.isEmpty(scheduleExecutionLogs)) {
			return new ArrayList<>();
		}
		
		// fixbug #88257
		List<String> employeeIds = scheduleExecutionLogs.stream().map(x -> x.getExecutionEmployeeId()).collect(Collectors.toList());
		List<EmployeeDto> employeeList = employeeAdapter.findByEmployeeIds(employeeIds);
		Map<String, EmployeeDto> employeeMap = employeeList.stream()
				.collect(Collectors.toMap(EmployeeDto::getEmployeeId, x->x));
		// return full by map
		return scheduleExecutionLogs.stream().map(item -> {
			ScheduleExecutionLogDto dto = new ScheduleExecutionLogDto();
			item.saveToMemento(dto);
			EmployeeDto employee = employeeMap.get(dto.getExecutionEmployeeId());
			dto.setEmployeeCode(employee.getEmployeeCode());
			dto.setEmployeeName(employee.getEmployeeName());
			return dto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Find by id.
	 *
	 * @param executionId the execution id
	 * @return the schedule execution log dto
	 */
	public ScheduleExecutionLogDto findById(String executionId) {
		
		// get company id
		String companyId = AppContexts.user().companyId();
		
		ScheduleExecutionLogDto dto = new ScheduleExecutionLogDto();
		 
		// call repository find by id
		Optional<ScheduleExecutionLog> optionalScheduleExecutionLog = this.scheduleExecutionLogRepository
				.findById(companyId, executionId);
		
		// check exist data
		if(optionalScheduleExecutionLog.isPresent()){
			optionalScheduleExecutionLog.get().saveToMemento(dto);
		}
		return dto;
	}
	
	/**
	 * Find info by id.
	 *
	 * @param executionId the execution id
	 * @return the schedule execution log info dto
	 */
	public ScheduleExecutionLogInfoDto findInfoById(String executionId){
		
		// default data 
		ScheduleExecutionLogInfoDto dto = new ScheduleExecutionLogInfoDto();
		
		// set total number
		dto.setTotalNumber(this.scheduleCreatorRepository.countdAll(executionId));
		
		// set total number error
		dto.setTotalNumberError(this.scheduleErrorLogRepository.distinctErrorByExecutionIdNew(executionId));
		
		// set total number created
		dto.setTotalNumberCreated(
				this.scheduleCreatorRepository.countByStatus(executionId, ExecutionStatus.CREATED.value));

		return dto;
	}
}
