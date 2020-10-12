package nts.uk.ctx.at.schedule.dom.shift.management.shifttable;

import java.util.Optional;

import lombok.Value;

@Value
public class NotificationInfo {
	
	/**
	 * 通知するか
	 */
	private boolean notify;
	
	/**
	 * 	締切日と期間
	 */
	private Optional<DeadlineAndPeriodOfExpectation> deadlineAndPeriod;
	
	/**
	 * 通知なしで作る
	 * @return
	 */
	public static NotificationInfo createWithoutNotify() {
		
		return new NotificationInfo(false, Optional.empty());
	}
	
	/**
	 * 通知情報を作る
	 * @param deadlineAndPeriod
	 * @return
	 */
	public static NotificationInfo createNotification(DeadlineAndPeriodOfExpectation deadlineAndPeriod) {
		
		return new NotificationInfo(true, Optional.of(deadlineAndPeriod));
	}
	
}
