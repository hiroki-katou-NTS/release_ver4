/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.request.ac.bs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.ConcurrentEmployeeRequest;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.JobEntryHistoryImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.PesionInforImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SWkpHistImport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employment.SEmpHistExport;
import nts.uk.ctx.bs.employee.pub.employment.SyEmploymentPub;
import nts.uk.ctx.bs.employee.pub.person.IPersonInfoPub;
import nts.uk.ctx.bs.employee.pub.person.PersonInfoExport;
import nts.uk.ctx.bs.employee.pub.workplace.SWkpHistExport;
import nts.uk.ctx.bs.employee.pub.workplace.SyWorkplacePub;

/**
 * The Class EmployeeAdaptorImpl.
 */
@Stateless
public class EmployeeRequestAdapterImpl implements EmployeeRequestAdapter {


	/** The employment pub. */
	@Inject
	private SyEmploymentPub employmentPub;

	/** The workplace pub. */
	@Inject
	private SyWorkplacePub workplacePub;
	
	@Inject
	private IPersonInfoPub personPub;
	@Inject
	private SyEmployeePub syEmployeePub;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeAdaptor#
	 * getEmploymentCode(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	public String getEmploymentCode(String companyId, String employeeId, GeneralDate baseDate) {
		return this.employmentPub.getEmploymentCode(companyId, employeeId, baseDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeAdaptor#
	 * findWpkIdsBySid(java.lang.String, java.lang.String,
	 * nts.arc.time.GeneralDate)
	 */
	@Override
	public List<String> findWpkIdsBySid(String companyId, String sid, GeneralDate baseDate) {
		return this.workplacePub.findWpkIdsBySid(companyId, sid, baseDate);
	}

	/**
	 * 
	 * @param sID
	 * @return
	 */
	@Override
	public String getEmployeeName(String sID) {
		return this.personPub.getPersonInfo(sID).getPname();
	}

	@Override
	public PesionInforImport getEmployeeInfor(String sID) {
		PersonInfoExport personIn = this.personPub.getPersonInfo(sID);
		PesionInforImport person = new PesionInforImport(personIn.getEmployeeCode(),
				personIn.getEmployeeId(),
				personIn.getPname(),
				"",//TODO mail can xem lai 
				new ArrayList<JobEntryHistoryImport>());
		return person;
	}

	@Override
	public String empEmail(String sID) {
		PersonInfoExport data = this.personPub.getPersonInfo(sID);
		/*if(!data.getCompanyMail().isEmpty()) {
			return data.getCompanyMail();
		}else {
			return null;
		}*/
		//TODO mail can xem lai
		return null;
	}

	@Override
	public List<ConcurrentEmployeeRequest> getConcurrentEmployee(String companyId, String jobId, GeneralDate baseDate) {
		List<ConcurrentEmployeeRequest>  data = syEmployeePub.getConcurrentEmployee(companyId, jobId, baseDate)
				.stream()
				.map(x -> new ConcurrentEmployeeRequest(x.getEmployeeId(),
						x.getEmployeeCd(),
						x.getPersonName(),
						x.getJobId(),
						x.getJobCls().value)).collect(Collectors.toList());
		return data;
	}
	
	@Override
	public SEmpHistImport getEmpHist(String companyId, String employeeId,
			GeneralDate baseDate){
		SEmpHistImport sEmpHistImport = new SEmpHistImport();
		Optional<SEmpHistExport> sEmpHistExport = this.employmentPub.findSEmpHistBySid(companyId, employeeId, baseDate);
		if(sEmpHistExport.isPresent()){
			sEmpHistImport.setEmployeeId(sEmpHistExport.get().getEmployeeId());
			sEmpHistImport.setEmploymentCode(sEmpHistExport.get().getEmploymentCode());
			sEmpHistImport.setEmploymentName(sEmpHistExport.get().getEmploymentName());
			sEmpHistImport.setPeriod(sEmpHistExport.get().getPeriod());
			return sEmpHistImport;
		}
		return null;
	}

	@Override
	public SWkpHistImport getSWkpHistByEmployeeID(String employeeId, GeneralDate baseDate) {
		Optional<SWkpHistExport> sWkpHistExport = this.workplacePub.findBySid(employeeId, baseDate);
		if(sWkpHistExport.isPresent()){
			SWkpHistImport sWkpHistImport = new SWkpHistImport(sWkpHistExport.get().getDateRange(),
					sWkpHistExport.get().getEmployeeId(),
					sWkpHistExport.get().getWorkplaceId(),
					sWkpHistExport.get().getWorkplaceCode(), 
					sWkpHistExport.get().getWorkplaceName(),
					sWkpHistExport.get().getWkpDisplayName());
			return sWkpHistImport;	
		}
		return null;
	}
	
}
