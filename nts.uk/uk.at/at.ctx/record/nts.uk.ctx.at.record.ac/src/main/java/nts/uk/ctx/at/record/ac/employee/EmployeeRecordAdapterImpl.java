package nts.uk.ctx.at.record.ac.employee;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeDataMngInfoImport;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeBasicInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeDataMngInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;

@Stateless
public class EmployeeRecordAdapterImpl implements EmployeeRecordAdapter {
	@Inject
	private SyEmployeePub employeePub;

	@Override
	public EmployeeRecordImport getPersonInfor(String employeeId) {
		EmployeeBasicInfoExport infor = employeePub.findBySId(employeeId);
		if (Objects.isNull(infor))
			return null;
		EmployeeRecordImport data = new EmployeeRecordImport(infor.getPId(), infor.getPName(), infor.getEntryDate(),
				infor.getGender(), infor.getBirthDay(), infor.getEmployeeId(), infor.getEmployeeCode(),
				infor.getRetiredDate());

		return data;
	}

	@Override
	public List<EmployeeRecordImport> getPersonInfor(List<String> listEmployeeId) {
		List<EmployeeBasicInfoExport> lstInfor = employeePub.findBySIds(listEmployeeId);
		return lstInfor.stream()
				.map(infor -> new EmployeeRecordImport(infor.getPId(), infor.getPName(), infor.getEntryDate(),
						infor.getGender(), infor.getBirthDay(), infor.getEmployeeId(), infor.getEmployeeCode(),
						infor.getRetiredDate()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDataMngInfoImport> findBySidNotDel(List<String> sids) {
		return this.employeePub.findBySidNotDel(sids).stream().map(export -> toInfoImport(export))
				.collect(Collectors.toList());
	}

	private EmployeeDataMngInfoImport toInfoImport(EmployeeDataMngInfoExport export) {

		return new EmployeeDataMngInfoImport(export.getCompanyId(), export.getPersonId(), export.getEmployeeId(),
				export.getEmployeeCode(), export.getDeletedStatus(), export.getDeleteDateTemporary(),
				export.getRemoveReason(), export.getExternalCode());
	}

}
