package nts.uk.shr.com.task.schedule.internal;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.schedule.JobScheduleOptions;
import nts.arc.task.schedule.JobScheduler;
import nts.arc.task.schedule.ScheduledJob;
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
	public ScheduleInfo scheduleOnCurrentcompany(UkJobScheduleOptions options) {
		val scheduleInfo = ScheduleInfo.createNew();
		
		val ntsOptions = new JobScheduleOptions(
				options.getJobClass(),
				createJobContextKey(scheduleInfo.getScheduleId()),
				options.getUserData(),
				options.getCronSchedule(),
				options.getStartDateTime(),
				options.getEndDateTime());
		
		this.scheduler.schedule(ntsOptions);
		
		return scheduleInfo;
	}

	@Override
	public void unscheduleOnCurrentCompany(Class<? extends ScheduledJob> jobClass, String scheduleId) {
		this.scheduler.unschedule(jobClass, createJobContextKey(scheduleId));
	}

	@Override
	public Optional<GeneralDateTime> getNextFireTime(Class<? extends ScheduledJob> jobClass, String scheduleId) {
		return this.scheduler.getNextFireTime(jobClass, createJobContextKey(scheduleId));
	}

	private static String createJobContextKey(String scheduleId) {
		return scheduleId + "@" + AppContexts.user().companyId();
	}
}
