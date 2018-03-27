package nts.uk.ctx.at.schedule.ac.executionlog;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.EmployeeDto;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.person.IPersonInfoPub;
import nts.uk.ctx.bs.employee.pub.person.PersonInfoExport;

/**
 * The Class SCEmployeeAdapterImpl.
 */
@Stateless
public class ScEmployeeAdapterImpl implements SCEmployeeAdapter {

	/** The pub. */
	@Inject
	private IPersonInfoPub pub;
	@Inject
	private SyEmployeePub syEmployeePub;

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.adapter.executionlog.SCEmployeeAdapter#findByEmployeeId(java.lang.String)
	 */
	@Override
	public EmployeeDto findByEmployeeId(String sid) {
		PersonInfoExport per = pub.getPersonInfo(sid);
		// covert to dto
		EmployeeDto dto = new EmployeeDto(sid, per.getEmployeeCode(), per.getBusinessName());
		return dto;
	}

	@Override
	public List<EmployeeDto> findByEmployeeIds(List<String> sids) {
		return syEmployeePub.findBySIds(sids).stream()
				.map(x -> new EmployeeDto(x.getEmployeeId(), x.getEmployeeCode(), x.getPName()))
				.collect(Collectors.toList());
	}
}
