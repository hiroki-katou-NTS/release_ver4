package nts.uk.ctx.at.schedule.pubimp.executionlog;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.ctx.at.schedule.pub.executionlog.ScheduleErrorLogEx;
import nts.uk.ctx.at.schedule.pub.executionlog.ScheduleErrorLogPub;

@Stateless
public class ScheduleErrorLogPubImpl implements ScheduleErrorLogPub {

	@Inject
	private ScheduleErrorLogRepository scheduleErrorLogRepository;

	@Override
	public List<ScheduleErrorLogEx> findByExecutionId(String executionId) {
		List<ScheduleErrorLog> data = scheduleErrorLogRepository.findByExecutionId(executionId);
		if(data == null) {
			return Collections.emptyList();
		}
		return data.stream().map(c -> convertToDomain(c))
				.collect(Collectors.toList());
	}

	@Override
	public Boolean checkExistErrorByKey(String executionId, String employeeId, GeneralDate baseDate) {
		return scheduleErrorLogRepository.checkExistErrorByKey(executionId, employeeId, baseDate);
	}

	@Override
	public void add(ScheduleErrorLogEx domain) {
		scheduleErrorLogRepository.add(convertToEx(domain));

	}

	private ScheduleErrorLogEx convertToDomain(ScheduleErrorLog domain) {
		return new ScheduleErrorLogEx(domain.getErrorContent(), domain.getExecutionId(), domain.getDate(),
				domain.getEmployeeId());
	}

	private ScheduleErrorLog convertToEx(ScheduleErrorLogEx ex) {
		return new ScheduleErrorLog(ex.getErrorContent(), ex.getExecutionId(), ex.getDate(), ex.getEmployeeId());
	}
}
