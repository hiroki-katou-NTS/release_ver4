/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.pubimp.employment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employment.Employment;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentInfo;
import nts.uk.ctx.bs.employee.dom.employment.EmploymentRepository;
import nts.uk.ctx.bs.employee.dom.employment.EmpmInfo;
import nts.uk.ctx.bs.employee.dom.employment.history.DateHistItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistory;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItem;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryItemRepository;
import nts.uk.ctx.bs.employee.dom.employment.history.EmploymentHistoryRepository;
import nts.uk.ctx.bs.employee.pub.employment.AffPeriodEmpCdHistExport;
import nts.uk.ctx.bs.employee.pub.employment.AffPeriodEmpCdHistExport.AffPeriodEmpCdHistExportBuilder;
import nts.uk.ctx.bs.employee.pub.employment.AffPeriodEmpCodeExport;
import nts.uk.ctx.bs.employee.pub.employment.EmpCdNameExport;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentCodeAndPeriod;
import nts.uk.ctx.bs.employee.pub.employment.EmploymentHisExport;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.ShEmploymentExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class EmploymentPubImp.
 */
@Stateless
public class EmploymentPubImp implements SyEmploymentPub {

	/** The employment history repository. */
	@Inject
	private EmploymentHistoryRepository empHistRepo;

	/** The employment history item repository. */
	@Inject
	private EmploymentHistoryItemRepository empHistItemRepo;

	/** The employment repository. */
	@Inject
	private EmploymentRepository employmentRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findAll(java.lang.
	 * String)
	 */
	@Override
	public List<EmpCdNameExport> findAll(String companyId) {
		return employmentRepository.findAll(companyId).stream().map(item -> EmpCdNameExport.builder()
				.code(item.getEmploymentCode().v()).name(item.getEmploymentName().v()).build())
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findSEmpHistBySid(
	 * java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<SEmpHistExport> findSEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate) {

		// Query
		Optional<EmploymentInfo> employmentInfo = empHistItemRepo
				.getDetailEmploymentHistoryItem(companyId, employeeId, baseDate);

		Optional<DateHistoryItem> optHistoryItem = empHistRepo
				.getByEmployeeIdAndStandardDate(employeeId, baseDate);

		// Check exist
		if (!employmentInfo.isPresent() || !optHistoryItem.isPresent()) {
			return Optional.empty();
		}

		EmploymentInfo employment = employmentInfo.get();

		// Return
		return Optional
				.of(SEmpHistExport.builder().employeeId(employeeId).employmentCode(employment.getEmploymentCode())
						.employmentName(employment.getEmploymentName()).period(optHistoryItem.get().span()).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findByEmpCodes(java
	 * .lang.String, java.util.List)
	 */
	@Override
	public List<ShEmploymentExport> findByEmpCodes(String companyId, List<String> empCodes) {
		List<Employment> empList = this.employmentRepository.findByEmpCodes(companyId, empCodes);
		return empList.stream().map(emp -> {
			ShEmploymentExport empExport = new ShEmploymentExport();
			empExport.setCompanyId(emp.getCompanyId().v());
			empExport.setEmploymentCode(emp.getEmploymentCode().v());
			empExport.setEmploymentName(emp.getEmploymentName().v());
			empExport.setEmpExternalCode(emp.getEmpExternalCode().v());
			empExport.setMemo(emp.getMemo().v());
			return empExport;
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#findByListSidAndPeriod(java.util.List, nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<EmploymentHisExport> findByListSidAndPeriod(List<String> sids, DatePeriod datePeriod) {

		if (sids.isEmpty() || datePeriod.start() == null || datePeriod.end() == null)
			return new ArrayList<>();

		List<EmploymentHistory> lstEmpHist = empHistRepo.getByListSid(sids, datePeriod);

		if (lstEmpHist.isEmpty())
			return new ArrayList<>();

		List<String> historyIds = new ArrayList<>();

		lstEmpHist.stream().forEach(x -> {
			
			List<DateHistoryItem> historyItemList = x.getHistoryItems();
			List<String> hists = new ArrayList<>();
			if (!historyItemList.isEmpty()) {
				hists = historyItemList.stream().map(y -> y.identifier()).collect(Collectors.toList());
				historyIds.addAll(hists);
			}
		});

		if (historyIds.isEmpty())
			return new ArrayList<>();

		List<EmploymentHistoryItem> empHistItems = empHistItemRepo.getByListHistoryId(historyIds);
		Map<String, String> mapHistIdToEmpCode = empHistItems.stream().collect(Collectors.toMap( x -> x.getHistoryId(), x -> x.getEmploymentCode() == null ? null : x.getEmploymentCode().v()));
		
		return lstEmpHist.stream().map(x-> {
			EmploymentHisExport emp = new EmploymentHisExport();
			emp.setEmployeeId(x.getEmployeeId());
			List<EmploymentCodeAndPeriod> lst = x.getHistoryItems().stream().map(c -> new EmploymentCodeAndPeriod(c.identifier(), new DatePeriod(c.start(), c.end()), mapHistIdToEmpCode.get(c.identifier()))).collect(Collectors.toList());
			emp.setLstEmpCodeandPeriod(lst);
			return emp;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#
	 * getEmpHistBySidAndPeriod(java.util.List,
	 * nts.uk.shr.com.time.calendar.period.DatePeriod)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AffPeriodEmpCdHistExport> getEmpHistBySidAndPeriod(List<String> sids,
			DatePeriod datePeriod) {

		List<EmploymentHistory> lstEmpHist = empHistRepo.getByListSid(sids,
				datePeriod);

		List<String> historyIds = lstEmpHist.stream().map(EmploymentHistory::getHistoryItems)
				.flatMap(listContainer -> listContainer.stream()).map(DateHistoryItem::identifier)
				.collect(Collectors.toList());

		List<EmploymentHistoryItem> empHistItems = empHistItemRepo
				.getByListHistoryId(historyIds);
		
		Map<String, String> mapHistIdToEmpCode = empHistItems.stream()
				.collect(Collectors.toMap(x -> x.getHistoryId(),
						x -> x.getEmploymentCode() == null ? null : x.getEmploymentCode().v()));

		return lstEmpHist.stream().map(item -> {
			AffPeriodEmpCdHistExportBuilder empBuilder = AffPeriodEmpCdHistExport.builder();
			empBuilder.employeeId(item.getEmployeeId());
			empBuilder.affPeriodEmpCodeExports(item.getHistoryItems().stream()
					.map(histItem -> AffPeriodEmpCodeExport.builder()
							.employmentCode(mapHistIdToEmpCode.get(histItem.identifier()))
							.period(histItem.span()).build())
					.collect(Collectors.toList()));
			return empBuilder.build();
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub#getEmploymentMap(java.lang.String, java.util.List)
	 */
	@Override
	public Map<String, String> getEmploymentMapCodeName(String companyId, List<String> empCodes) {
		List<Employment> empList = this.employmentRepository.findByEmpCodes(companyId, empCodes);
		return empList.stream().collect(Collectors.toMap(item -> item.getEmploymentCode().v(),
				item -> item.getEmploymentName().v()));
	}

	@Override
	public Map<String, SEmpHistExport> findSEmpHistBySidVer2(String companyId, List<String> lstSID, GeneralDate baseDate) {
		//get emp hist
		Map<String, EmpmInfo> mapEmpInfo = empHistItemRepo.getLstDetailEmpHistItem(companyId, lstSID, baseDate);

		Map<String, DateHistItem> mapHistItem = empHistRepo.getBySIdAndate(lstSID, baseDate);

		// Check exist
		if (mapEmpInfo.isEmpty() || mapHistItem.isEmpty()) {
			return new HashMap<>();
		}
		Map<String, SEmpHistExport> mapResult = new HashMap<>();
		for(String sid : lstSID){		
			if(!mapEmpInfo.containsKey(sid) || !mapHistItem.containsKey(sid)){
				continue;
			}
			EmpmInfo emp = mapEmpInfo.get(sid);
			DateHistItem hist = mapHistItem.get(sid);
			mapResult.put(sid, SEmpHistExport.builder().employeeId(emp.getSid()).employmentCode(emp.getEmploymentCode())
						.employmentName(emp.getEmploymentName()).period(hist.getPeriod()).build());
		}
		return mapResult;
	}

}
