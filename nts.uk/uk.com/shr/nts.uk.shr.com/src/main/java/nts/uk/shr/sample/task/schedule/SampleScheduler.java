package nts.uk.shr.sample.task.schedule;

import java.util.Arrays;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.schedule.ScheduledJobUserData;
import nts.arc.task.schedule.cron.CronSchedule;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.task.schedule.UkJobScheduleOptions;
import nts.uk.shr.com.task.schedule.UkJobScheduler;

@Stateless
public class SampleScheduler {

	@Inject
	private UkJobScheduler scheduler;
	
	public String schedule() {
		
		val scheduletimeData = new ScheduledJobUserData();
		scheduletimeData.put("TEST", "1234567890");
		
		// http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html
		val cron = new CronSchedule(Arrays.asList("0/5 * * * * ?"));
		
		val options = UkJobScheduleOptions.builder(SampleScheduledJob.class, "sample", cron)
				.userData(scheduletimeData)
				.startDate(GeneralDate.ymd(2018, 4, 1))
				.build();
		
		return this.scheduler.scheduleOnCurrentCompany(options).getScheduleId();
		
	}
	
	public void unschedule(String scheduleId) {
		
		this.scheduler.unscheduleOnCurrentCompany(SampleScheduledJob.class, scheduleId);
	}
}
