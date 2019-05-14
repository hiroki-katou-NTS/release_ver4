package nts.uk.ctx.at.function.dom.alarm.alarmlist.aggregationprocess.daily.dailyaggregationprocess;

import lombok.Value;

@Value
public class AlarmContentMessage {
	private String alarmItem;
	private String alarmContent;
	private String alarmItemNew;
}
