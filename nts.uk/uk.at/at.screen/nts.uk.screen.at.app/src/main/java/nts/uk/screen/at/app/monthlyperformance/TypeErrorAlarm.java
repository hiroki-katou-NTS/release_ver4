package nts.uk.screen.at.app.monthlyperformance;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypeErrorAlarm {
	ERROR(0), 
	ALARM(1), 
	ERROR_ALARM(2),
	NO_ERROR_ALARM(3);
	public final int value;
}
