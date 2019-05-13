package nts.uk.shr.com.task.schedule;

import java.util.Optional;

import lombok.Value;
import nts.arc.primitive.TimeClockPrimitiveValue;
import nts.arc.task.schedule.ScheduledJob;
import nts.arc.task.schedule.ScheduledJobUserData;
import nts.arc.task.schedule.SchedulingMethod;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

@Value
public class UkJobScheduleOptions {

	private final Class<? extends ScheduledJob> jobClass;
	private final Optional<Class<? extends ScheduledJob>> cleanupJobClass;
	private final String scheduleId;
	private final SchedulingMethod schedulingMethod;
	private final ScheduledJobUserData userData;
	private final Optional<GeneralDate> startDate;
	private final Optional<GeneralDate> endDate;
	private final Optional<TimeClockPrimitiveValue<?>> startClock;
	private final Optional<TimeClockPrimitiveValue<?>> endClock;
	
	public Optional<GeneralDateTime> getStartDateTime() {
		return merge(this.startDate, this.startClock);
	}
	
	public Optional<GeneralDateTime> getEndDateTime() {
		return merge(this.endDate, this.endClock);
	}
	
	private static Optional<GeneralDateTime> merge(Optional<GeneralDate> date, Optional<TimeClockPrimitiveValue<?>> clock) {
		return date.map(d -> {
			return clock.map(c -> {
				return GeneralDateTime.ymdhms(d.year(), d.month(), d.day(), c.hour(), c.minute(), 0);
			}).orElseGet(() -> {
				return GeneralDateTime.ymdhms(d.year(), d.month(), d.day(), 0, 0, 0);
			});
		});
	}
	
	public static Builder builder(Class<? extends ScheduledJob> jobClass, String scheduleId, SchedulingMethod schedulingMethod) {
		return new Builder(jobClass, scheduleId, schedulingMethod);
	}
	
	public static class Builder {
		final Class<? extends ScheduledJob> jobClass;
		final String scheduleId;
		final SchedulingMethod schedulingMethod;
		final ScheduledJobUserData userData = new ScheduledJobUserData();
		Class<? extends ScheduledJob> cleanupJobClass;
		GeneralDate startDate = null;
		GeneralDate endDate = null;
		TimeClockPrimitiveValue<?> startClock = null;
		TimeClockPrimitiveValue<?> endClock = null;
		
		public Builder(Class<? extends ScheduledJob> jobClass, String scheduleId, SchedulingMethod schedulingMethod) {
			this.jobClass = jobClass;
			this.scheduleId = scheduleId;
			this.schedulingMethod = schedulingMethod;
		}
		
		public Builder cleanupJobClass(Class<? extends ScheduledJob> cleanupJobClass) {
			this.cleanupJobClass = cleanupJobClass;
			return this;
		}
		
		public Builder startDate(GeneralDate startDate) {
			this.startDate = startDate;
			return this;
		}
		
		public Builder endDate(GeneralDate endDate) {
			this.endDate = endDate;
			return this;
		}
		
		public Builder startClock(TimeClockPrimitiveValue<?> startClock) {
			this.startClock = startClock;
			return this;
		}
		
		public Builder endClock(TimeClockPrimitiveValue<?> endClock) {
			this.endClock = endClock;
			return this;
		}
		
		public Builder userData(ScheduledJobUserData userData) {
			userData.forEach((key, value) -> this.userData.put(key, value));
			return this;
		}
		
		public UkJobScheduleOptions build() {
			return new UkJobScheduleOptions(
					jobClass,
					Optional.ofNullable(cleanupJobClass),
					scheduleId,
					schedulingMethod,
					userData,
					Optional.ofNullable(startDate),
					Optional.ofNullable(endDate),
					Optional.ofNullable(startClock),
					Optional.ofNullable(endClock));
		}
	}
}
