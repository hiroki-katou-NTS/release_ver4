/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.app.employee;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.auth.dom.role.RoleType;
import nts.uk.query.model.employee.CCG001SystemType;
import nts.uk.query.model.employee.EmployeeAuthAdapter;
import nts.uk.query.model.employee.EmployeeReferenceRange;
import nts.uk.query.model.employee.EmployeeRoleImported;
import nts.uk.query.model.employee.EmployeeRoleRepository;
import nts.uk.query.model.employee.RegulationInfoEmployee;
import nts.uk.query.model.employee.RegulationInfoEmployeeRepository;
import nts.uk.query.model.employee.RoleWorkPlaceAdapter;
import nts.uk.query.model.employee.SearchReferenceRange;
import nts.uk.query.model.employee.history.EmployeeHistoryRepository;
import nts.uk.query.model.employee.mgndata.EmpDataMngInfoAdapter;
import nts.uk.query.model.employement.history.EmploymentHistoryAdapter;
import nts.uk.query.model.person.QueryPersonAdapter;
import nts.uk.query.model.workplace.QueryWorkplaceAdapter;
import nts.uk.query.model.workplace.WorkplaceInfoImport;
import nts.uk.query.model.workrule.closure.QueryClosureEmpAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class RegulationInfoEmployeeFinder.
 */
@Stateless
public class RegulationInfoEmployeeFinder {

	/** The repo. */
	@Inject
	private RegulationInfoEmployeeRepository repo;

	/** The role repo. */
	@Inject
	private EmployeeRoleRepository roleRepo;

	/** The work place adapter. */
	@Inject
	private RoleWorkPlaceAdapter workPlaceAdapter;

	/** The emp auth adapter. */
	@Inject
	private EmployeeAuthAdapter empAuthAdapter;

	/** The emp data mng info adapter. */
	@Inject
	private EmpDataMngInfoAdapter empDataMngInfoAdapter;

	/** The person adapter. */
	@Inject
	private QueryPersonAdapter personAdapter;

	/** The emp his repo. */
	@Inject
	private EmployeeHistoryRepository empHisRepo;

	/** The closure emp adapter. */
	@Inject
	private QueryClosureEmpAdapter closureEmpAdapter;

	@Inject
	private EmploymentHistoryAdapter empHisAdapter;

	@Inject
	private QueryWorkplaceAdapter queryWorkplaceAdapter;

	/**
	 * Find.
	 *
	 * @param queryDto the query dto
	 * @return the list
	 */
	public List<RegulationInfoEmployeeDto> find(RegulationInfoEmpQueryDto queryDto) {
		// Do not change workplace when system type = admin or searchReferenceRange = DO_NOT_CONSIDER_REFERENCE_RANGE
		if (queryDto.getSystemType() == CCG001SystemType.ADMINISTRATOR.value
				|| queryDto.getReferenceRange() == SearchReferenceRange.DO_NOT_CONSIDER_REFERENCE_RANGE.value) {
			return this.findEmployeesInfo(queryDto);
		}

		EmployeeRoleImported role = this.getRole(queryDto.getSystemType());
		if (role != null && role.getEmployeeReferenceRange() == EmployeeReferenceRange.ONLY_MYSELF && queryDto.getSystemType() != CCG001SystemType.EMPLOYMENT.value) {
//			LoginEmployeeQuery query = new LoginEmployeeQuery(GeneralDateTime.fromString(queryDto.getBaseDate() + RegulationInfoEmpQueryDto.TIME_DAY_START,
//					RegulationInfoEmpQueryDto.DATE_TIME_FORMAT), queryDto.getSystemType());
//			return Arrays.asList(this.findCurrentLoginEmployeeInfo(query));
			return Arrays.asList(this.findCurrentLoginEmployeeInfo(
					GeneralDateTime.fromString(queryDto.getBaseDate() + RegulationInfoEmpQueryDto.TIME_DAY_START,
					RegulationInfoEmpQueryDto.DATE_TIME_FORMAT)));
		}

		// Algorithm: 検索条件の職場一覧を参照範囲に基いて変更する
		if (role != null && role.getEmployeeReferenceRange() == EmployeeReferenceRange.ONLY_MYSELF) {
			queryDto.setReferenceRange(EmployeeReferenceRange.ONLY_MYSELF.value);
			this.changeListWorkplaces(queryDto);
		} else if (role != null) {
			this.changeWorkplaceListByRole(queryDto, role);
//			Optional<OperationRuleImport> optOperationRuleImport = operationRuleAdapter.getOperationRuleByCompanyId(AppContexts.user().companyId());
            // Default synchronization = true;
//            if (optOperationRuleImport.isPresent() && !optOperationRuleImport.get().isSynchronization()) {
//                this.changeDepartmentListByRole(queryDto, role);
//            }
		}
		List<RegulationInfoEmployeeDto> result = this.findEmployeesInfo(queryDto);
		
		//ThanhPV hiện tại đoạn này đang được thêm vào. khi resole conflict 
		// đang lấy theo đoạn 
		if (queryDto.getSystemType() == CCG001SystemType.EMPLOYMENT.value) {
			List<String> narrowedSids = empAuthAdapter.narrowEmpListByReferenceRange(
											result.stream().map(c -> c.getEmployeeId()).collect(Collectors.toList()), 3,
											GeneralDate.fromString(queryDto.getBaseDate() + RegulationInfoEmpQueryDto.TIME_DAY_START, RegulationInfoEmpQueryDto.DATE_TIME_FORMAT));
			
			result.removeIf(c -> !narrowedSids.contains(c.getEmployeeId()));
		}
		
		return result;
	}

//    private void changeDepartmentListByRole(RegulationInfoEmpQueryDto queryDto, EmployeeRoleImported role) {
//        EmployeeReferenceRange employeeReferenceRange = role.getEmployeeReferenceRange(); // employee's reference authority
//        SearchReferenceRange searchReferenceRange = SearchReferenceRange.valueOf(queryDto.getReferenceRange());
//
//        // An employee's search reference range depends on his reference authority
//        switch (searchReferenceRange) {
//            case ALL_REFERENCE_RANGE:
//                if (employeeReferenceRange == EmployeeReferenceRange.ALL_EMPLOYEE) {
//                    // not change departmentCodes
//                    break;
//                } else {
//                    queryDto.setReferenceRange(employeeReferenceRange.value);
//                    this.changeListDepartment(queryDto);
//                }
//                break;
//            case AFFILIATION_ONLY:
//                // Get list String Department
//                this.changeListDepartment(queryDto);
//                break;
//            case AFFILIATION_AND_ALL_SUBORDINATES:
//                if (employeeReferenceRange == EmployeeReferenceRange.ALL_EMPLOYEE ||
//					employeeReferenceRange == EmployeeReferenceRange.DEPARTMENT_AND_CHILD) {
//                    // Get list String Department
//                    this.changeListDepartment(queryDto);
//                    break;
//                } else {
//                    // Get list String Department
//                    queryDto.setReferenceRange(EmployeeReferenceRange.DEPARTMENT_ONLY.value);
//                    this.changeListDepartment(queryDto);
//                }
//                break;
//            default:
//                throw new RuntimeException("Invalid enum value");
//        }
//    }

//    private void changeListDepartment(RegulationInfoEmpQueryDto queryParam) {
//        // Get list department
//        GeneralDate date = GeneralDate.fromString(queryParam.getBaseDate(), "yyyy-MM-dd");
//        List<String> depList = new ArrayList<>(); // Processing is not prepared yet
//
//        // Check param filterByDepartment
//        if (queryParam.getFilterByDepartment() != null && queryParam.getFilterByDepartment()) {
//            // Merge list department
//            queryParam.setDepartmentCodes(queryParam.getDepartmentCodes().stream().filter(depList::contains).collect(Collectors.toList()));
//        } else {
//            // Set list department
//            queryParam.setDepartmentCodes(depList);
//            queryParam.setFilterByDepartment(true);
//        }
//    }

    /**
	 * Find by employee code.
	 *
	 * @param query the query
	 * @return the list
	 */
	public List<RegulationInfoEmployeeDto> findByEmployeeCode(SearchEmployeeQuery query) {
		List<String> sIds = searchByEmployeeCode(query.getCode(), query.getSystemType(), query.getReferenceDate());

		// filter by closure id
		sIds = this.filterByClosure(query, sIds);

		return getEmployeeInfo(sIds, query.getReferenceDate());
	}

	/**
	 * Find by employee name.
	 *
	 * @param query the query
	 * @return the list
	 */
	public List<RegulationInfoEmployeeDto> findByEmployeeName(SearchEmployeeQuery query) {
		List<String> sIds = searchByEmployeeName(query.getName(), query.getSystemType(), query.getReferenceDate());

		// filter by closure id
		sIds = this.filterByClosure(query, sIds);

		return getEmployeeInfo(sIds, query.getReferenceDate());
	}

	/**
	 * Find by employee entry date.
	 *
	 * @param query the query
	 * @return the list
	 */
	public List<RegulationInfoEmployeeDto> findByEmployeeEntryDate(SearchEmployeeQuery query) {
		List<String> sIds = searchByEntryDate(query.getDatePeriod(), query.getSystemType(), query.getReferenceDate());

		// filter by closure id
		sIds = this.filterByClosure(query, sIds);

		return getEmployeeInfo(sIds, query.getReferenceDate());
	}

	/**
	 * Find by employee retirement date.
	 *
	 * @param query the query
	 * @return the list
	 */
	public List<RegulationInfoEmployeeDto> findByEmployeeRetirementDate(SearchEmployeeQuery query) {
		List<String> sIds = searchByRetirementDate(query.getDatePeriod(), query.getSystemType(), query.getReferenceDate());

		// filter by closure id
		sIds = this.filterByClosure(query, sIds);

		return getEmployeeInfo(sIds, query.getReferenceDate());
	}

	// A：社員情報取得処理
	public List<RegulationInfoEmployeeDto> getEmployeeInfo(List<String> sIds, GeneralDate referenceDate) {
		return this.repo.findInfoBySIds(sIds, referenceDate).stream().map(item -> this.toDto(item))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the role.
	 *
	 * @param systemType the system type
	 * @return the role
	 */
	private EmployeeRoleImported getRole(int systemType) {
		if (systemType == CCG001SystemType.ADMINISTRATOR.value) {
			return null;
		}
		String roleId = this.workPlaceAdapter.findRoleIdBySystemType(systemType);

		// Find Role by roleId
		return roleId == null ? null : this.roleRepo.findRoleById(roleId);
	}

	/**
	 * Change workplace list by role.
	 *
	 * @param queryDto the query dto
	 * @return the list
	 */
	// 検索条件の職場一覧を参照範囲に基いて変更する
	private void changeWorkplaceListByRole(RegulationInfoEmpQueryDto queryDto, EmployeeRoleImported role) {
		EmployeeReferenceRange employeeReferenceRange = role.getEmployeeReferenceRange(); // employee's reference authority
		SearchReferenceRange searchReferenceRange = SearchReferenceRange.valueOf(queryDto.getReferenceRange());

		// An employee's search reference range depends on his reference authority
		switch (searchReferenceRange) {
		case ALL_REFERENCE_RANGE:
			if (employeeReferenceRange == EmployeeReferenceRange.ALL_EMPLOYEE) {
				// not change workplaceCodes
				break;
			} else {
				queryDto.setReferenceRange(employeeReferenceRange.value);
				this.changeListWorkplaces(queryDto);
			}
			break;
		case AFFILIATION_ONLY:
			// Get list String Workplace
			this.changeListWorkplaces(queryDto);
			break;
		case AFFILIATION_AND_ALL_SUBORDINATES:
			if (employeeReferenceRange == EmployeeReferenceRange.ALL_EMPLOYEE
					|| employeeReferenceRange == EmployeeReferenceRange.DEPARTMENT_AND_CHILD) {
				// Get list String Workplace
				this.changeListWorkplaces(queryDto);
				break;
			} else {
				// Get list String Workplace
				queryDto.setReferenceRange(EmployeeReferenceRange.DEPARTMENT_ONLY.value);
				this.changeListWorkplaces(queryDto);
			}
			break;
		default:
			throw new RuntimeException("Invalid enum value");
		}
	}

	/**
	 * Change list workplaces.
	 *
	 * @param queryParam the query param
	 * @return the list
	 */
	//change list Workplace [指定条件から参照可能な職場リストを取得する]
	private void changeListWorkplaces(RegulationInfoEmpQueryDto queryParam) {
		// get List Workplace
		GeneralDate date = GeneralDate.fromString(queryParam.getBaseDate(), "yyyy-MM-dd");
		List<String> wkplist = this.workPlaceAdapter.getWorkPlaceIdByEmployeeReferenceRange(date,
				queryParam.getReferenceRange());

		// check param filterByWorkplace
		if (queryParam.getFilterByWorkplace()) {
			// merge list workplaces
			queryParam.setWorkplaceCodes(queryParam.getWorkplaceCodes().stream().filter(wkplist::contains)
					.collect(Collectors.toList()));
		} else {
			// Set list Workplace
			queryParam.setWorkplaceCodes(wkplist);
			queryParam.setFilterByWorkplace(true);
		}

	}

	/**
	 * Search by employee code.
	 *
	 * @param sCd the s cd
	 * @param systemType the system type
	 * @return the list
	 */
	public List<String> searchByEmployeeCode(String sCd, Integer systemType, GeneralDate referenceDate) {
		List<String> sIds = this.empDataMngInfoAdapter.findNotDeletedBySCode(AppContexts.user().companyId(),
				sCd);

		return this.narrowEmpListByReferenceRange(sIds, systemType, referenceDate);
	}

	/**
	 * Search by employee name.
	 *
	 * @param sName the s name
	 * @param systemType the system type
	 * @return the list
	 */
	public List<String> searchByEmployeeName(String sName, Integer systemType, GeneralDate referenceDate) {
		List<String> pIds = this.personAdapter.findPersonIdsByName(sName);

		List<String> sIds = this.empDataMngInfoAdapter.findByListPersonId(AppContexts.user().companyId(), pIds);

		return this.narrowEmpListByReferenceRange(sIds, systemType, referenceDate);
	}

	/**
	 * Search by entry date.
	 *
	 * @param period the period
	 * @param systemType the system type
	 * @return the list
	 */
	public List<String> searchByEntryDate(DatePeriod period, Integer systemType, GeneralDate referenceDate) {
		List<String> sIds = this.empHisRepo.findEmployeeByEntryDate(AppContexts.user().companyId(), period);

		return this.narrowEmpListByReferenceRange(sIds, systemType, referenceDate);
	}

	/**
	 * Search by retirement date.
	 *
	 * @param period the period
	 * @param systemType the system type
	 * @return the list
	 */
	public List<String> searchByRetirementDate(DatePeriod period, Integer systemType, GeneralDate referenceDate) {
		List<String> sIds = this.empHisRepo.findEmployeeByRetirementDate(AppContexts.user().companyId(), period);

		return this.narrowEmpListByReferenceRange(sIds, systemType, referenceDate);
	}

	/**
	 * Filter by closure.
	 *
	 * @param query the query
	 * @param sIds the s ids
	 * @return the list
	 */
	private List<String> filterByClosure(SearchEmployeeQuery query, List<String> sIds) {
		if (query.isUseClosure() && !query.isAllClosure()) {
			List<String> empCds = this.closureEmpAdapter.findListEmpCdByClosureId(AppContexts.user().companyId(),
					query.getClosureId());
			List<String> filteredSids = this.empHisAdapter.findSIdsByEmpCdsAndPeriod(empCds,
					query.getReferenceDatePeriod());

			// filter list employee ids
			return sIds.stream().filter(filteredSids::contains).collect(Collectors.toList());
		}
		return sIds;
	}

	/**
	 * To dto.
	 *
	 * @param model the model
	 * @return the regulation info employee dto
	 */
	private RegulationInfoEmployeeDto toDto(RegulationInfoEmployee model) {
		return RegulationInfoEmployeeDto.builder()
				.employeeCode(model.getEmployeeCode())
				.employeeId(model.getEmployeeID())
				.employeeName(model.getName().orElse(""))
				.workplaceId(model.getWorkplaceId().orElse(""))
				.workplaceCode(model.getWorkplaceCode().orElse(""))
				.workplaceName(model.getWorkplaceName().orElse(""))
//				.affiliationId(model.getWorkplaceId().orElse(""))
//				.affiliationCode(model.getWorkplaceCode().orElse(""))
//				.affiliationName(model.getWorkplaceName().orElse(""))
				.build();
	}

	/**
	 * Find current login employee info.
	 *
	 * @return the list
	 */
	public RegulationInfoEmployeeDto findCurrentLoginEmployeeInfo(GeneralDateTime baseDate) {
		String loginEmployeeId = AppContexts.user().employeeId();
		String companyId = AppContexts.user().companyId();
		RegulationInfoEmployee loginEmployee = this.repo.findBySid(companyId, loginEmployeeId, baseDate);
//		RegulationInfoEmployee loginEmployee = this.repo.findBySid(companyId, loginEmployeeId, query.getBaseDate(), query.getSystemType());
//		switch(EnumAdaptor.valueOf(query.getSystemType(), CCG001SystemType.class)) {
//			case SALARY:
//				if (loginEmployee == null || !loginEmployee.getDepartmentId().isPresent()) {
//					throw new BusinessException("Msg_317");
//				}
//				List<DepartmentInfoImport> departmentInfoImports = departmentAdapter.getDepartmentInfoByDepIds(companyId, Arrays.asList(loginEmployee.getDepartmentId().get()), query.getBaseDate().toDate());
//				return RegulationInfoEmployeeDto.builder()
//						.employeeCode(loginEmployee.getEmployeeCode())
//						.employeeId(loginEmployee.getEmployeeID())
//						.employeeName(loginEmployee.getName().orElse(""))
//						.workplaceId(loginEmployee.getDepartmentId().orElse(""))
//						.workplaceCode(loginEmployee.getDepartmentCode().orElse(""))
//						.workplaceName(departmentInfoImports.get(0).getDepartmentName())
////						.affiliationId(loginEmployee.getDepartmentId().orElse(""))
////						.affiliationCode(loginEmployee.getDepartmentCode().orElse(""))
////						.affiliationName(departmentInfoImports.get(0).getDepartmentName())
//						.build();
//			default:
				if (loginEmployee == null || !loginEmployee.getWorkplaceId().isPresent()) {
					throw new BusinessException("Msg_317");
				}
				List<WorkplaceInfoImport> workplaceInfoImports = queryWorkplaceAdapter.getWkpInfoByWkpIds_OLD(companyId, Arrays.asList(loginEmployee.getWorkplaceId().get()), baseDate.toDate());
				return RegulationInfoEmployeeDto.builder()
					.employeeCode(loginEmployee.getEmployeeCode())
					.employeeId(loginEmployee.getEmployeeID())
					.employeeName(loginEmployee.getName().orElse(""))
					.workplaceId(loginEmployee.getWorkplaceId().orElse(""))
					.workplaceCode(loginEmployee.getWorkplaceCode().orElse(""))
                    .workplaceName(workplaceInfoImports.get(0).getWorkplaceName())
//					.affiliationId(loginEmployee.getWorkplaceId().orElse(""))
//					.affiliationCode(loginEmployee.getWorkplaceCode().orElse(""))
//					.affiliationName(workplaceInfoImports.get(0).getWorkplaceName())
					.build();
//		}
	}

	/**
	 * Find employees info.
	 *
	 * @param queryDto the query dto
	 * @return the list
	 */
	private List<RegulationInfoEmployeeDto> findEmployeesInfo(RegulationInfoEmpQueryDto queryDto) {
		String companyId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.fromString(queryDto.getBaseDate(), "yyyy-MM-dd");

		// return data
		List<RegulationInfoEmployeeDto> empDtos;

		// get list employee
		List<RegulationInfoEmployee> regulationInfoEmployees = this.repo.find(AppContexts.user().companyId(), queryDto.toQueryModel());

		// check system type
//		if (queryDto.getSystemType() == CCG001SystemType.SALARY.value) {
//			// filter present department config span by reference date
//			regulationInfoEmployees = regulationInfoEmployees.stream().
//					filter(e -> e.getDepartmentId().isPresent())
//					.collect(Collectors.toList());
//
//			// get data for list department with no data
//			List<String> noDataDepIds = regulationInfoEmployees.stream()
//					.filter(e -> !e.getDepartmentCode().isPresent() || !e.getDepartmentDeleteFlag().isPresent() || e.getDepartmentDeleteFlag().get())
//					.map(e -> e.getDepartmentId().get())
//					.distinct()
//					.collect(Collectors.toList());
//			// Request list 563
//			Map<String, DepartmentInfoImport> depInfoImports = departmentAdapter.getDepartmentInfoByDepIds(companyId, noDataDepIds, baseDate)
//					.stream().collect(Collectors.toMap(DepartmentInfoImport::getDepartmentId, Function.identity()));
//
//			// Set return data
//			empDtos = regulationInfoEmployees
//						.stream()
//						.map(e -> RegulationInfoEmployeeDto.builder()
//                                .employeeCode(e.getEmployeeCode())
//                                .employeeId(e.getEmployeeID())
//                                .employeeName(e.getName().orElse(""))
//                                .workplaceId(e.getDepartmentId().orElse(""))
//                                .workplaceCode(e.getDepartmentCode().orElse(""))
//                                .workplaceName(depInfoImports.containsKey(e.getDepartmentId().get()) ? depInfoImports.get(e.getDepartmentId().get()).getDepartmentName() : e.getDepartmentName().get())
////                                .affiliationId(e.getDepartmentId().orElse(""))
////                                .affiliationCode(e.getDepartmentCode().orElse(""))
////                                .affiliationName(depInfoImports.containsKey(e.getDepartmentId().get()) ? depInfoImports.get(e.getDepartmentId().get()).getDepartmentName() : e.getDepartmentName().get())
//                                .build())
//						.collect(Collectors.toList());
//		} else {
			// filter present workplace config span by reference date
			regulationInfoEmployees = regulationInfoEmployees.stream().
					filter(e -> e.getWorkplaceId().isPresent())
					.collect(Collectors.toList());

			// get data for list workplace with no data
			List<String> noDataWkpIds = regulationInfoEmployees.stream()
					.filter(e -> !e.getWorkplaceCode().isPresent())
					.map(e -> e.getWorkplaceId().get())
					.distinct()
					.collect(Collectors.toList());
			
            // Request list 560
			Map<String, WorkplaceInfoImport> wkpInfoImports = queryWorkplaceAdapter.getWkpInfoByWkpIds_OLD(companyId, noDataWkpIds, baseDate)
					.stream().collect(Collectors.toMap(WorkplaceInfoImport::getWorkplaceId, Function.identity()));

			// Set return data
			empDtos = regulationInfoEmployees
					.stream()
					.map(e -> RegulationInfoEmployeeDto.builder()
							.employeeCode(e.getEmployeeCode())
							.employeeId(e.getEmployeeID())
							.employeeName(e.getName().orElse(""))
							.workplaceId(e.getWorkplaceId().orElse(""))
							.workplaceCode(e.getWorkplaceCode().orElse(""))
							.workplaceName(wkpInfoImports.containsKey(e.getWorkplaceId().get()) ? wkpInfoImports.get(e.getWorkplaceId().get()).getWorkplaceName() : e.getWorkplaceName().get())
//							.affiliationId(e.getWorkplaceId().orElse(""))
//							.affiliationCode(e.getWorkplaceCode().orElse(""))
//							.affiliationName(wkpInfoImports.containsKey(e.getWorkplaceId().get()) ? wkpInfoImports.get(e.getWorkplaceId().get()).getWorkplaceName() : e.getWorkplaceName().get())
							.build())
					.collect(Collectors.toList());
//		}
		return empDtos;
	}

	/**
	 * Narrow emp list by reference range.
	 *
	 * @param sIds the s ids
	 * @param systemType the system type
	 * @return the list
	 */
	private List<String> narrowEmpListByReferenceRange(List<String> sIds, Integer systemType, GeneralDate referenceDate) {
		// Do not narrowEmpListByReferenceRange when system type = admin
		if (systemType == CCG001SystemType.ADMINISTRATOR.value) {
			return sIds;
		}
		return this.empAuthAdapter.narrowEmpListByReferenceRange(sIds,
				this.roleTypeFrom(CCG001SystemType.valueOf(systemType)).value, referenceDate);
	}

	// ・ロール種類：
	// ※パラメータ「システム区分」＝「個人情報」の場合
	// →「ロール種類」＝「個人情報」
	// ※パラメータ「システム区分」＝「就業」の場合
	// →「ロール種類」＝「就業」
	// ※パラメータ「システム区分」＝「給与」の場合
	// →「ロール種類」＝「給与」
	// ※パラメータ「システム区分」＝「人事」の場合
	// →「ロール種類」＝「人事」
	/**
	 * Convert from.
	 *
	 * @param systemType the system type
	 * @return the role type
	 */
	private RoleType roleTypeFrom(CCG001SystemType systemType) {
		switch (systemType) {
		case PERSONAL_INFORMATION:
			return RoleType.PERSONAL_INFO;
		case EMPLOYMENT:
			return RoleType.EMPLOYMENT;
		case SALARY:
			return RoleType.SALARY;
		case HUMAN_RESOURCES:
			return RoleType.HUMAN_RESOURCE;
		default:
			throw new RuntimeException();
		}
	}
}
