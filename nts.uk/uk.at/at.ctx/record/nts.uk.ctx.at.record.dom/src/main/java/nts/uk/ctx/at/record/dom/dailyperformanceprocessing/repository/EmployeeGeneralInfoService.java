package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.generalinfo.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.record.dom.adapter.generalinfo.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmployeeGeneralInfoService {
	
	@Inject
	private EmployeeGeneralInfoAdapter employeeGeneralInfoAdapter;

	public EmployeeGeneralInfoImport getEmployeeGeneralInfo(List<String> emloyeeIds, DatePeriod periodTime){
		
		EmployeeGeneralInfoImport employeeGeneralInfoImport = this.employeeGeneralInfoAdapter.getEmployeeGeneralInfo(emloyeeIds, periodTime,true,true,true,true,true);
		
		return employeeGeneralInfoImport;
	}
	
}
