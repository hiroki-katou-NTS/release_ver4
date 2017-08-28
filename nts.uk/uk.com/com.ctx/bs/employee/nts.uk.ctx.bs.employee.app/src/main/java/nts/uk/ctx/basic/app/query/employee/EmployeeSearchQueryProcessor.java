/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.basic.app.query.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.basic.dom.company.organization.employee.Employee;
import nts.uk.ctx.basic.dom.company.organization.employee.EmployeeRepository;
import nts.uk.ctx.basic.dom.company.organization.employee.classification.AffClassHistory;
import nts.uk.ctx.basic.dom.company.organization.employee.classification.AffClassHistoryRepository;
import nts.uk.ctx.basic.dom.company.organization.employee.employment.AffEmploymentHistory;
import nts.uk.ctx.basic.dom.company.organization.employee.employment.AffEmploymentHistoryRepository;
import nts.uk.ctx.basic.dom.company.organization.employee.jobtile.AffJobTitleHistory;
import nts.uk.ctx.basic.dom.company.organization.employee.jobtile.AffJobTitleHistoryRepository;
import nts.uk.ctx.basic.dom.company.organization.employee.workplace.AffWorkplaceHistory;
import nts.uk.ctx.basic.dom.company.organization.employee.workplace.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.access.jobtitle.JobTitleAdapter;
import nts.uk.ctx.bs.employee.dom.access.jobtitle.dto.AcJobTitleDto;
import nts.uk.ctx.bs.employee.dom.access.person.PersonAdapter;
import nts.uk.ctx.bs.employee.dom.access.person.dto.AcPersonDto;
import nts.uk.ctx.bs.employee.dom.access.workplace.WorkplaceAdapter;
import nts.uk.ctx.bs.employee.dom.access.workplace.dto.AcWorkplaceDto;
import nts.uk.ctx.bs.employee.dom.access.workplace.dto.AcWorkplaceHierarchyDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class EmployeeQueryProcessor.
 */
@Stateless
public class EmployeeSearchQueryProcessor {
	/** The person repository. */
	@Inject
	private PersonAdapter personAdapter;

	/** The workplace repository. */
	@Inject
	private WorkplaceAdapter workplaceAdapter;

	/** The job title repository. */
	@Inject
	private JobTitleAdapter jobTitleAdapter;

	/** The employee repository. */
	@Inject
	private EmployeeRepository employeeRepository;

	/** The employment history repository. */
	@Inject
	private AffEmploymentHistoryRepository employmentHistoryRepository;

	/** The classification history repository. */
	@Inject
	private AffClassHistoryRepository classificationHistoryRepository;

	/** The job title history repository. */
	@Inject
	private AffJobTitleHistoryRepository jobTitleHistoryRepository;

	/** The workplace history repository. */
	@Inject
	private AffWorkplaceHistoryRepository workplaceHistoryRepository;

	/**
	 * To employee.
	 *
	 * @param baseDate
	 *            the base date
	 * @param employeeIds
	 *            the employee ids
	 * @param companyId
	 *            the company id
	 * @return the list
	 */

	public List<EmployeeSearchData> toEmployee(GeneralDate baseDate, List<String> employeeIds,
			String companyId) {

		// get employee list
		List<Employee> employees = this.employeeRepository.findByListEmployeeId(companyId,
				employeeIds);

		// check exist employee
		if (CollectionUtil.isEmpty(employees)) {
			throw new BusinessException("Msg_317");
		}

		// get work place history by employee
		List<AffWorkplaceHistory> workplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceOfCompanyId(employeeIds, baseDate);

		// check exist data work place
		if (CollectionUtil.isEmpty(workplaceHistory)) {
			throw new BusinessException("Msg_177");
		}

		// get all work place of company
		List<AcWorkplaceDto> workplaces = this.workplaceAdapter.findAllWorkplaceOfCompany(companyId,
				baseDate);

		// to map work place
		Map<String, AcWorkplaceDto> workplaceMap = workplaces.stream()
				.collect(Collectors.toMap((workplace) -> {
					return workplace.getWorkplaceId();
				}, Function.identity()));

		// to map work place history
		Map<String, AffWorkplaceHistory> workplaceHistoryMap = workplaceHistory.stream()
				.collect(Collectors.toMap((workplace) -> {
					return workplace.getEmployeeId();
				}, Function.identity()));

		// get person name
		List<AcPersonDto> persons = this.personAdapter.findByPersonIds(
				employees.stream().map(employee -> employee.getPId()).collect(Collectors.toList()));

		// to map person (person id)
		Map<String, AcPersonDto> personMap = persons.stream().collect(Collectors.toMap((person) -> {
			return person.getPersonId();
		}, Function.identity()));

		List<EmployeeSearchData> employeeSearchData = new ArrayList<>();

		employees.forEach(employee -> {
			// check exist data
			if (workplaceHistoryMap.containsKey(employee.getSId())
					&& personMap.containsKey(employee.getPId()) && workplaceMap.containsKey(
							workplaceHistoryMap.get(employee.getSId()).getWorkplaceId().v())) {

				// add to dto
				EmployeeSearchData dto = new EmployeeSearchData();
				dto.setEmployeeId(employee.getSId());
				dto.setEmployeeCode(employee.getSCd().v());
				dto.setEmployeeName(personMap.get(employee.getPId()).getPersonName());
				dto.setWorkplaceId(workplaceHistoryMap.get(employee.getSId()).getWorkplaceId().v());

				dto.setWorkplaceCode(workplaceMap.get(dto.getWorkplaceId()).getWorkplaceCode());
				dto.setWorkplaceName(workplaceMap.get(dto.getWorkplaceId()).getWorkplaceName());
				employeeSearchData.add(dto);
			}
		});

		// check exist data employee search
		if (CollectionUtil.isEmpty(employeeSearchData)) {
			throw new BusinessException("Msg_176");
		}

		return employeeSearchData;
	}

	/**
	 * Search all employee.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<EmployeeSearchData> searchAllEmployee(GeneralDate baseDate) {

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get all employee of company id
		List<Employee> employees = this.employeeRepository.findAll(companyId);

		return toEmployee(baseDate,
				employees.stream().map(employee -> employee.getSId()).collect(Collectors.toList()),
				companyId);
	}

	/**
	 * Search all employee.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<EmployeeSearchData> searchEmployeeByLogin(GeneralDate baseDate) {

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get employee id
		String employeeId = loginUserContext.employeeId();

		// add employeeId
		List<String> employeeIds = new ArrayList<>();

		employeeIds.add(employeeId);

		// get data
		return toEmployee(baseDate, employeeIds, companyId);
	}

	/**
	 * Search mode employee.
	 *
	 * @param input
	 *            the input
	 * @return the list
	 */
	public List<EmployeeSearchData> searchModeEmployee(EmployeeSearchQuery input) {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// find by employment
		List<AffEmploymentHistory> employmentHistory = this.employmentHistoryRepository
				.searchEmployee(input.getBaseDate(), input.getEmploymentCodes());

		// find by classification
		List<AffClassHistory> classificationHistorys = this.classificationHistoryRepository
				.searchClassification(
						employmentHistory.stream().map(employment -> employment.getEmployeeId())
								.collect(Collectors.toList()),
						input.getBaseDate(), input.getClassificationCodes());

		// find by job title
		List<AffJobTitleHistory> jobTitleHistory = this.jobTitleHistoryRepository
				.searchJobTitleHistory(
						classificationHistorys.stream()
								.map(classification -> classification.getEmployeeId())
								.collect(Collectors.toList()),
						input.getBaseDate(), input.getJobTitleCodes());
		// find by work place
		List<AffWorkplaceHistory> workplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceHistory(
						jobTitleHistory.stream().map(jobtitle -> jobtitle.getEmployeeId())
								.collect(Collectors.toList()),
						input.getBaseDate(), input.getWorkplaceCodes());
		// to employees
		List<Employee> employees = this.employeeRepository.findByListEmployeeId(companyId,
				workplaceHistory.stream().map(workplace -> workplace.getEmployeeId())
						.collect(Collectors.toList()));

		// to person info
		return this.toEmployee(input.getBaseDate(),
				employees.stream().map(employee -> employee.getSId()).collect(Collectors.toList()),
				companyId);
	}

	/**
	 * Search of workplace.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<EmployeeSearchData> searchOfWorkplace(GeneralDate baseDate) {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get employee id
		String employeeId = loginUserContext.employeeId();

		List<AffWorkplaceHistory> workplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		// check exist data
		if (CollectionUtil.isEmpty(workplaceHistory)) {
			throw new BusinessException("Msg_177");
		}

		// return data
		return this.toEmployee(baseDate, workplaceHistory.stream()
				.map(workplace -> workplace.getEmployeeId()).collect(Collectors.toList()),
				companyId);
	}

	/**
	 * Search workplace child.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<EmployeeSearchData> searchWorkplaceChild(GeneralDate baseDate) {

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get employee id
		String employeeId = loginUserContext.employeeId();

		// get data work place history
		List<AffWorkplaceHistory> workplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		// check exist data
		if (CollectionUtil.isEmpty(workplaceHistory)) {
			throw new BusinessException("Msg_177");
		}

		// get data child
		List<AcWorkplaceHierarchyDto> workPlaceHierarchies = new ArrayList<>();
		workplaceHistory.forEach(work -> {
			workPlaceHierarchies.addAll(this.workplaceAdapter.findAllHierarchyChild(companyId,
					work.getWorkplaceId().v()));
		});

		workplaceHistory = this.workplaceHistoryRepository.searchWorkplaceHistory(baseDate,
				workPlaceHierarchies.stream().map(workplace -> workplace.getWorkplaceId())
						.collect(Collectors.toList()));
		// return data
		return this.toEmployee(baseDate, workplaceHistory.stream()
				.map(workplace -> workplace.getEmployeeId()).collect(Collectors.toList()),
				companyId);
	}

	/**
	 * Search workplace of employee.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<String> searchWorkplaceOfEmployee(GeneralDate baseDate) {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get employee id
		String employeeId = loginUserContext.employeeId();

		// get data work place history
		List<AffWorkplaceHistory> workplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceHistoryByEmployee(employeeId, baseDate);

		// return data
		return workplaceHistory.stream().map(workplace -> workplace.getWorkplaceId().v())
				.collect(Collectors.toList());
	}

	/**
	 * Gets the of selected employee.
	 *
	 * @param input
	 *            the input
	 * @return the of selected employee
	 */
	public List<EmployeeSearchData> getOfSelectedEmployee(EmployeeSearchListQuery input) {
		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		return this.toEmployee(input.getBaseDate(), input.getEmployeeIds(), companyId);
	}

	/**
	 * Search employees.
	 *
	 * @param query
	 *            the query
	 * @return the list
	 */
	public List<EmployeeSearchListData> searchEmployees(EmployeeSearchListQuery query) {

		// check exist data
		if (CollectionUtil.isEmpty(query.getEmployeeIds())) {
			return new ArrayList<>();
		}

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get employee list
		List<Employee> employees = this.employeeRepository.findByListEmployeeId(companyId,
				query.getEmployeeIds());

		// get map person
		Map<String, AcPersonDto> personMap = this.personAdapter
				.findByPersonIds(employees.stream().map(employee -> employee.getPId())
						.collect(Collectors.toList()))
				.stream().collect(Collectors.toMap((person) -> {
					return person.getPersonId();
				}, Function.identity()));

		// get map work place history
		Map<String, AffWorkplaceHistory> mapWorkplaceHistory = this.workplaceHistoryRepository
				.searchWorkplaceOfCompanyId(query.getEmployeeIds(), query.getBaseDate()).stream()
				.collect(Collectors.toMap((workplace) -> {
					return workplace.getEmployeeId();
				}, Function.identity()));

		// get map work place
		Map<String, AcWorkplaceDto> mapWorkplace = this.workplaceAdapter
				.findAllWorkplaceOfCompany(companyId, query.getBaseDate()).stream()
				.collect(Collectors.toMap((workplace) -> {
					return workplace.getWorkplaceId();
				}, Function.identity()));

		// get map job title history
		Map<String, AffJobTitleHistory> mapJobTitleHistory = this.jobTitleHistoryRepository
				.findAllJobTitleHistory(query.getBaseDate(), query.getEmployeeIds()).stream()
				.collect(Collectors.toMap((jobtitle) -> {
					return jobtitle.getEmployeeId();
				}, Function.identity()));

		// get map job title
		Map<String, AcJobTitleDto> mapJobTitle = this.jobTitleAdapter
				.findAll(companyId, query.getBaseDate()).stream()
				.collect(Collectors.toMap((jobtitle) -> {
					return jobtitle.getPositionId();
				}, Function.identity()));
		List<EmployeeSearchListData> dataRes = new ArrayList<>();

		for (Employee employee : employees) {
			EmployeeSearchListData data = new EmployeeSearchListData();
			data.setEmployeeId(employee.getSId());
			data.setEmployeeCode(employee.getSCd().v());

			// check exist person data
			if (personMap.containsKey(employee.getPId())) {
				data.setEmployeeName(personMap.get(employee.getPId()).getPersonName());
			}

			// check exist work place history
			if (mapWorkplaceHistory.containsKey(employee.getSId()) && mapWorkplace.containsKey(
					(mapWorkplaceHistory.get(employee.getSId()).getWorkplaceId().v()))) {
				AffWorkplaceHistory workplaceHistory = mapWorkplaceHistory.get(employee.getSId());
				AcWorkplaceDto workplace = mapWorkplace.get(workplaceHistory.getWorkplaceId().v());
				data.setWorkplaceId(workplace.getWorkplaceId());
				data.setWorkplaceCode(workplace.getWorkplaceCode());
				data.setWorkplaceName(workplace.getWorkplaceName());
			}

			// check exist job title history
			if (mapJobTitleHistory.containsKey(employee.getSId()) && mapJobTitle
					.containsKey(mapJobTitleHistory.get(employee.getSId()).getJobTitleId().v())) {
				AffJobTitleHistory jobTitleHistory = mapJobTitleHistory.get(employee.getSId());
				AcJobTitleDto jobtitle = mapJobTitle.get(jobTitleHistory.getJobTitleId().v());
				data.setJobTitleId(jobtitle.getPositionId());
				data.setJobTitleCode(jobtitle.getPositionCode());
				data.setJobTitleName(jobtitle.getPositionName());
			}
			dataRes.add(data);
		}
		return dataRes;
	}

}
