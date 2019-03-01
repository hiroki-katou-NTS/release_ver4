package nts.uk.ctx.at.record.ac.employee;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeAdapter;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeDto;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class EmployeeDtoAdapterImpl implements EmployeeAdapter{

	@Inject
	private SyEmployeePub syEmployeePub;
	
	@Override
	public List<EmployeeDto> getByListSID(List<String> sIds) {
		return syEmployeePub.getByListSid(sIds).stream().map(x -> {
			return new EmployeeDto(x.getSid(), x.getScd(), x.getBussinessName());
		}).collect(Collectors.toList());
	}

}

