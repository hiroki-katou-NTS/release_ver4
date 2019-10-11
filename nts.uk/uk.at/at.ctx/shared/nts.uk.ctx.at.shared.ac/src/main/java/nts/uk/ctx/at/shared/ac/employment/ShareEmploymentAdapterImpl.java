/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ac.employment;

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
import nts.uk.ctx.at.shared.dom.adapter.employment.AffPeriodEmpCodeImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.EmpCdNameImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.SharedSidPeriodDateEmploymentImport;
import nts.uk.ctx.bs.employee.pub.employment.EmpCdNameExport;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ShareEmploymentAdapterImpl.
 */
@Stateless
public class ShareEmploymentAdapterImpl implements ShareEmploymentAdapter{
	
	/** The employment. */
	@Inject
	public SyEmploymentPub employment;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter#findAll(java.lang.String)
	 */
	@Override
	public List<EmpCdNameImport> findAll(String companyId) {
		List<EmpCdNameExport> data = employment.findAll(companyId);
		return data.stream().map(x -> {
			return new EmpCdNameImport(x.getCode(), x.getName());
		}).collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter
	 * #findByEmpCodes(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<BsEmploymentHistoryImport> findEmploymentHistory(String companyId, String employeeId, GeneralDate baseDate) {
		return employment.findSEmpHistBySid(companyId, employeeId, baseDate).map(empHist -> 
												new BsEmploymentHistoryImport(empHist.getEmployeeId(), empHist.getEmploymentCode(),
													empHist.getEmploymentName(), empHist.getPeriod()));
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<SharedSidPeriodDateEmploymentImport> getEmpHistBySidAndPeriod(List<String> sids, DatePeriod datePeriod) {
		List<SharedSidPeriodDateEmploymentImport> lstEmpHist = employment.getEmpHistBySidAndPeriod(sids, datePeriod)
				.stream()
				.map(x -> {
					List<AffPeriodEmpCodeImport> lstEmpCode = x.getAffPeriodEmpCodeExports().stream()
							.map(y -> {
								return new AffPeriodEmpCodeImport(y.getPeriod(), y.getEmploymentCode());
							}).collect(Collectors.toList());
					return new SharedSidPeriodDateEmploymentImport(x.getEmployeeId(), lstEmpCode);
				}).collect(Collectors.toList());
				
		return lstEmpHist;
	}

	@Override
	public Map<String, BsEmploymentHistoryImport> findEmpHistoryVer2(String companyId, List<String> lstSID,
			GeneralDate baseDate) {
		Map<String, SEmpHistExport> lar = employment.findSEmpHistBySidVer2(companyId, lstSID, baseDate);
		Map<String, BsEmploymentHistoryImport> mapResult = new HashMap<>();
		for (String sid : lstSID) {
			if(!lar.containsKey(sid)){
				continue;
			}
			SEmpHistExport empHist = lar.get(sid);
			if(empHist == null){
				continue;
			}
			mapResult.put(sid, new BsEmploymentHistoryImport(empHist.getEmployeeId(), empHist.getEmploymentCode(),
					empHist.getEmploymentName(), empHist.getPeriod()));
		}		
		return mapResult;
	}
	
}
