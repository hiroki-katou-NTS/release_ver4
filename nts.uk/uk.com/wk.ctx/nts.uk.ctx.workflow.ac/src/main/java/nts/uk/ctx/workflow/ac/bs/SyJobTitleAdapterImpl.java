/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.workflow.ac.bs;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.jobtitle.EmployeeJobHistExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.JobTitleExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;
import nts.uk.ctx.workflow.dom.adapter.bs.SyJobTitleAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.JobTitleImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.SimpleJobTitleImport;

/**
 * @author vunv
 */
@Stateless
public class SyJobTitleAdapterImpl implements SyJobTitleAdapter{

	@Inject
	private SyJobTitlePub syJobTitlePub;
	
	
	@Override
	public List<JobTitleImport> findJobTitleBySid(String employeeId) {
		return this.syJobTitlePub.findJobTitleBySid(employeeId).stream().map(x-> this.toImport(x)).collect(Collectors.toList());
	}

	@Override
	public JobTitleImport findJobTitleBySid(String employeeId, GeneralDate baseDate) {
		Optional<EmployeeJobHistExport> export = this.syJobTitlePub.findBySid(employeeId, baseDate);
		return export.map(x -> {
			return new JobTitleImport(x.getJobTitleID(), null , x.getJobTitleName(), "", x.getStartDate(), x.getEndDate());
		}).orElse(new JobTitleImport("", "", "", "", null, null));
	}

	@Override
	public JobTitleImport findJobTitleByPositionId(String companyId, String positionId, GeneralDate baseDate) {
		return this.syJobTitlePub.findByJobId(companyId, positionId, baseDate)
				.map(x-> this.toImport(x)).orElse(null);
	}

	
	/**
	 * convert Export to Import data
	 * @param ex
	 * @return
	 */
	private JobTitleImport toImport(JobTitleExport ex) {
		return new JobTitleImport(
				ex.getJobTitleId(), 
				ex.getJobTitleCode(), 
				ex.getJobTitleName(), 
				ex.getSequenceCode(), 
				ex.getStartDate(), 
				ex.getEndDate());
	}

	@Override
	public List<JobTitleImport> findAll(String companyId, GeneralDate baseDate) {
		List<JobTitleImport> data = syJobTitlePub.findAll(companyId, baseDate)
				.stream()
				.map(x -> this.toImport(x))
				.collect(Collectors.toList());
		return data;
	}

	@Override
	public List<SimpleJobTitleImport> findByIds(String companyId, List<String> jobIds, GeneralDate baseDate) {
		return syJobTitlePub.findByIds(companyId, jobIds, baseDate).stream()
				.map(x -> new SimpleJobTitleImport(
						x.getJobTitleId(), 
						x.getJobTitleCode(), 
						x.getJobTitleName(), 
						x.getDisporder()))
				.collect(Collectors.toList());
	}
}
