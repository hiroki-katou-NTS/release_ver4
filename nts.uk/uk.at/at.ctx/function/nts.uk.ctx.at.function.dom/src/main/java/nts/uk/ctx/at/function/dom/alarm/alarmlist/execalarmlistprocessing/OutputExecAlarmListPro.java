package nts.uk.ctx.at.function.dom.alarm.alarmlist.execalarmlistprocessing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutputExecAlarmListPro {

	private boolean checkExecAlarmListPro;
	
	private String errorMessage;
	
	private boolean checkStop;

	public OutputExecAlarmListPro(boolean checkExecAlarmListPro, String errorMessage, boolean checkStop) {
		super();
		this.checkExecAlarmListPro = checkExecAlarmListPro;
		this.errorMessage = errorMessage;
		this.checkStop = checkStop;
	}

	
	
}
