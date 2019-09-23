package nts.uk.ctx.at.function.ac.executionlog;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.executionlog.ScheduleErrorLogAdapter;
import nts.uk.ctx.at.function.dom.adapter.executionlog.ScheduleErrorLogImport;
import nts.uk.ctx.at.schedule.pub.executionlog.ScheduleErrorLogEx;
import nts.uk.ctx.at.schedule.pub.executionlog.ScheduleErrorLogPub;

@Stateless
public class ScheduleErrorLogAcFinder implements ScheduleErrorLogAdapter {

	@Inject
	private ScheduleErrorLogPub scheduleErrorLogPub;

	@Override
	public List<ScheduleErrorLogImport> findByExecutionId(String executionId) {
		List<ScheduleErrorLogEx> data = scheduleErrorLogPub.findByExecutionId(executionId);
		if(data == null) {
			return Collections.emptyList();
		}
		return data.stream().map(c -> convertToEx(c)).collect(Collectors.toList());
	}

	@Override
	public Boolean checkExistErrorByKey(String executionId, String employeeId, GeneralDate baseDate) {
		boolean check = scheduleErrorLogPub.checkExistErrorByKey(executionId, employeeId, baseDate);
		return check;
	}

	@Override
	public void add(ScheduleErrorLogImport domain) {
		scheduleErrorLogPub.add(convertToImport(domain));

	}

	private ScheduleErrorLogImport convertToEx(ScheduleErrorLogEx ex) {
		return new ScheduleErrorLogImport(ex.getErrorContent(), ex.getExecutionId(), ex.getDate(), ex.getEmployeeId());
	}

	private ScheduleErrorLogEx convertToImport(ScheduleErrorLogImport imp) {
		return new ScheduleErrorLogEx(imp.getErrorContent(), imp.getExecutionId(), imp.getDate(), imp.getEmployeeId());
	}

}
