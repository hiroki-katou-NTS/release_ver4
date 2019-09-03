/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.ac.employee.service;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHist;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistByEmployee;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistItem;
import nts.uk.ctx.bs.employee.dom.employee.history.AffCompanyHistRepository;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.employee.dom.employee.service.SearchEmployeeService;
import nts.uk.ctx.bs.employee.dom.employee.service.dto.EmployeeSearchData;
import nts.uk.ctx.bs.employee.dom.employee.service.dto.EmployeeSearchDto;
import nts.uk.ctx.bs.employee.dom.employee.service.dto.PersonalBasicInfo;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItem;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.workplace.affiliate.AffWorkplaceHistoryRepository;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.query.pub.employee.SearchEmployeePub;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SearchEmployeeServiceImpl.
 */
@Stateless
public class SearchEmployeeServiceImpl implements SearchEmployeeService {

	/** The emp data mng repo. */
	@Inject
	private EmployeeDataMngInfoRepository empDataMngRepo;

	/** The aff com hist repo. */
	@Inject
	private AffCompanyHistRepository affComHistRepo;

	/** The person repo. */
	@Inject
	private PersonRepository personRepo;

	/** The aff workplace history repository. */
	@Inject
	private AffWorkplaceHistoryRepository affWorkplaceHistoryRepository;

	/** The aff workplace history item repository. */
	@Inject
	private AffWorkplaceHistoryItemRepository affWorkplaceHistoryItemRepository;

	/** The workplace info repo. */
	@Inject
	private WorkplaceInfoRepository workplaceInfoRepo;

	@Inject
	private SearchEmployeePub searchEmployeePub;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.employee.service.SearchEmployeeService#
	 * searchByCode(nts.uk.ctx.bs.employee.dom.employee.service.dto.
	 * EmployeeSearchDto)
	 */
	@Override
	public EmployeeSearchData searchByCode(EmployeeSearchDto dto) {
		String cid = AppContexts.user().companyId();
		GeneralDate baseDate = dto.getBaseDate() != null ? dto.getBaseDate() : GeneralDate.today();
		// アルゴリズム「社員コードから社員を取得する」を実行する
		EmployeeSearchData result = this.getEmployeeFromEmployeeCode(cid, baseDate, dto);
		if(result == null){
			// エラーメッセージ（#Msg_7）を表示する
            throw new BusinessException("Msg_7");
		}
		// phan con lai lam duoi UI
		return result;
	}

	/**
	 * アルゴリズム「社員コードから社員を取得する」を実行する
	 * 
	 * Gets the employee from employee code
	 * 
	 * @param cid
	 * @param baseDate
	 * @param dto
	 * @return
	 */
	private EmployeeSearchData getEmployeeFromEmployeeCode(String cid, GeneralDate baseDate, EmployeeSearchDto dto) {
		// 社員コードで検索する
		List<String> lstEmpId = this.searchEmployeePub.searchByEmployeeCode(dto.getEmployeeCode(),
				Integer.valueOf(dto.getSystem()),
				baseDate);
		if (lstEmpId.isEmpty()) {
			return null;
		}

		// アルゴリズム「社員IDから個人社員基本情報を取得」を実行する
		PersonalBasicInfo perInfo = this.getPersonalInfo(lstEmpId.get(0));
		// アルゴリズム「社員所属職場履歴を取得」を実行する - RQ30
		Optional<WorkplaceInfo> wkpInfo = this.getWplBelongEmployee(perInfo.getEmployeeId(), baseDate);
		return EmployeeSearchData.builder().companyId(cid).employeeId(perInfo.getEmployeeId())
				.employeeCode(dto.getEmployeeCode()).personalId(perInfo.getPid())
				.businessName(perInfo.getBusinessName()).deptDisplayName("")
				.wkpDisplayName(wkpInfo.isPresent() ? wkpInfo.get().getWkpDisplayName().v() : "").build();
	}

	/**
	 * アルゴリズム「社員IDから個人社員基本情報を取得」を実行する
	 * 
	 * Gets the personal info.
	 *
	 * @param employeeId
	 *            the employee id
	 * @return the personal info
	 */
	private PersonalBasicInfo getPersonalInfo(String employeeId) {
		Optional<EmployeeDataMngInfo> empOpt = empDataMngRepo.findByEmpId(employeeId);
		PersonalBasicInfo perResult = null;

		if (empOpt.isPresent()) {
			EmployeeDataMngInfo empData = empOpt.get();
			perResult = new PersonalBasicInfo();

			setEmployeeInfo(empData, perResult);

			setPersonInfo(empData.getPersonId(), perResult);

		}
		return perResult;
	}

	/**
	 * Sets the person info.
	 *
	 * @param pId
	 *            the id
	 * @param perResult
	 *            the per result
	 */
	private void setPersonInfo(String pId, PersonalBasicInfo perResult) {
		Optional<Person> _person = personRepo.getByPersonId(pId);
		if (_person.isPresent()) {
			Person person = _person.get();
			perResult.setBirthDay(person.getBirthDate());
			perResult.setPid(person.getPersonId());
			perResult.setGender(person.getGender().value);
			perResult.setBusinessName(person.getPersonNameGroup().getBusinessName() == null ? ""
					: person.getPersonNameGroup().getBusinessName().v());
		}
	}

	/**
	 * Sets the employee info.
	 *
	 * @param employee
	 *            the employee
	 * @param perResult
	 *            the per result
	 */
	private void setEmployeeInfo(EmployeeDataMngInfo employee, PersonalBasicInfo perResult) {
		perResult.setEmployeeId(employee.getEmployeeId());
		perResult.setEmployeeCode(employee.getEmployeeCode() == null ? "" : employee.getEmployeeCode().v());

		AffCompanyHist affComHist = affComHistRepo.getAffCompanyHistoryOfEmployeeDesc(employee.getCompanyId(),
				employee.getEmployeeId());

		AffCompanyHistByEmployee affComHistByEmp = affComHist.getAffCompanyHistByEmployee(employee.getEmployeeId());

		if (affComHistByEmp != null) {

			AffCompanyHistItem affComHistItem = new AffCompanyHistItem();

			Optional<AffCompanyHistItem> history = affComHistByEmp.getHistory();

			if (history.isPresent()) {
				affComHistItem = history.get();
				perResult.setEntryDate(affComHistItem.start());
				perResult.setRetiredDate(affComHistItem.end());

			}
		}
	}

	// imported（権限管理）「所属職場履歴」を取得する
	/**
	 * Gets the wpl belong employee.
	 *
	 * @param sid
	 *            the sid
	 * @param baseDate
	 *            the base date
	 * @return the wpl belong employee
	 */
	// No.30
	// アルゴリズム「社員所属職場履歴を取得」を実行する
	private Optional<WorkplaceInfo> getWplBelongEmployee(String sid, GeneralDate baseDate) {
		// get AffWorkplaceHistory
		Optional<AffWorkplaceHistory> affWrkPlc = affWorkplaceHistoryRepository.getByEmpIdAndStandDate(sid, baseDate);
		if (!affWrkPlc.isPresent())
			return Optional.empty();

		// get AffWorkplaceHistoryItem
		String historyId = affWrkPlc.get().getHistoryItems().get(0).identifier();
		Optional<AffWorkplaceHistoryItem> affWrkPlcItem = affWorkplaceHistoryItemRepository.getByHistId(historyId);
		if (!affWrkPlcItem.isPresent())
			return Optional.empty();

		// Get workplace info.
		Optional<WorkplaceInfo> optWorkplaceInfo = workplaceInfoRepo.findByWkpId(affWrkPlcItem.get().getWorkplaceId(),
				baseDate);

		// Check exist
		if (!optWorkplaceInfo.isPresent()) {
			return Optional.empty();
		}

		// Return workplace id
		return optWorkplaceInfo;
	}

}
