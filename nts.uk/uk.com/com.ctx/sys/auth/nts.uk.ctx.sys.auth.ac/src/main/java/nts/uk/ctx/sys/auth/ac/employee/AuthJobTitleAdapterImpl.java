package nts.uk.ctx.sys.auth.ac.employee;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.jobtitle.EmployeeJobHistExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.JobTitleExport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;
import nts.uk.ctx.sys.auth.dom.adapter.employee.JobTitleAdapter;
import nts.uk.ctx.sys.auth.dom.employee.dto.JobTitleValueImport;
import nts.uk.ctx.sys.auth.dom.employee.dto.SimpleJobTitleImport;

@Stateless
public class AuthJobTitleAdapterImpl implements JobTitleAdapter {
	@Inject
	private SyJobTitlePub syJobTitlePub;

	private JobTitleValueImport toImport(JobTitleExport ex) {
		return new JobTitleValueImport(ex.getCompanyId(), ex.getJobTitleId(), ex.getJobTitleCode(), ex.getJobTitleName(), ex.getSequenceCode(), ex.getStartDate(), ex.getEndDate());
	}

	@Override
	public List<JobTitleValueImport> findJobTitleBySid(String employeeId) {
		return this.syJobTitlePub.findJobTitleBySid(employeeId).stream().map(x -> this.toImport(x)).collect(Collectors.toList());
	}

	@Override
	public JobTitleValueImport findJobTitleBySid(String employeeId, GeneralDate baseDate) {
		//Optional<EmployeeJobHistExport> export = this.syJobTitlePub.findBySid(employeeId, baseDate);
		return convertToExport(this.syJobTitlePub.findBySid(employeeId, baseDate).orElse(null));
	}

	@Override
	public JobTitleValueImport findJobTitleByPositionId(String companyId, String positionId, GeneralDate baseDate) {
		return this.syJobTitlePub.findByJobId(companyId, positionId, baseDate).map(x -> this.toImport(x)).orElse(null);
	}

	@Override
	public List<JobTitleValueImport> findAll(String companyId, GeneralDate baseDate) {
		List<JobTitleValueImport> data = syJobTitlePub.findAll(companyId, baseDate).stream().map(x -> this.toImport(x)).collect(Collectors.toList());
		return data;
	}

	@Override
	public List<SimpleJobTitleImport> findByIds(String companyId, List<String> jobIds, GeneralDate baseDate) {
		return syJobTitlePub.findByIds(companyId, jobIds, baseDate).stream().map(item -> new SimpleJobTitleImport(item.getJobTitleId(), item.getJobTitleCode(), item.getJobTitleName(), item.getDisporder())).collect(Collectors.toList());
	}

	private JobTitleValueImport convertToExport(EmployeeJobHistExport export) {
		if(export != null){
		return new JobTitleValueImport(
				null,
				export.getJobTitleID(),
				null,
				export.getJobTitleName(),
				null,
				export.getStartDate(),
				export.getEndDate());
		}
		else
			return null;

	}
}
