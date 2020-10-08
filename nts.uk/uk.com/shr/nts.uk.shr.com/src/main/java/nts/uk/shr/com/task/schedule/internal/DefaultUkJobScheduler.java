package nts.uk.shr.com.task.schedule.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.schedule.JobScheduleOptions;
import nts.arc.task.schedule.JobScheduler;
import nts.arc.task.schedule.ScheduledJob;
import nts.arc.task.schedule.UnscheduleParams;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.task.schedule.ScheduleInfo;
import nts.uk.shr.com.task.schedule.UkJobScheduleOptions;
import nts.uk.shr.com.task.schedule.UkJobScheduler;

@Stateless
public class DefaultUkJobScheduler implements UkJobScheduler {

	@Inject
	private JobScheduler scheduler;

	@Override
	public ScheduleInfo scheduleOnCurrentCompany(UkJobScheduleOptions options) {
		val scheduleInfo = ScheduleInfo.createNew();
		String tenantCode = AppContexts.user().contractCode();
		
		val ntsOptions = new JobScheduleOptions(
				tenantCode,
				options.getJobClass(),
				createJobContextKey(scheduleInfo.getScheduleId()),
				options.getUserData(),
				options.getSchedulingMethod(),
				options.getStartDateTime(),
				options.getEndDateTime());
		
		this.scheduler.schedule(ntsOptions);
		
		return scheduleInfo;
	}

	@Override
	public void unscheduleOnCurrentCompany(Class<? extends ScheduledJob> jobClass, String scheduleId) {

		String tenantCode = AppContexts.user().contractCode();
		val params = new UnscheduleParams(tenantCode, jobClass, createJobContextKey(scheduleId));
		
		this.scheduler.unschedule(params);
	}

	@Override
	public Optional<GeneralDateTime> getNextFireTime(Class<? extends ScheduledJob> jobClass, String scheduleId) {
		return this.scheduler.getNextFireTime(jobClass, createJobContextKey(scheduleId));
	}

	@Override
	public Optional<GeneralDateTime> getNextFireTime(String scheduleId) {
		return this.scheduler.getNextFireTime(createJobContextKey(scheduleId));
	}
	
	private static String createJobContextKey(String scheduleId) {
		return scheduleId + "@" + AppContexts.user().companyId();
	}
}
