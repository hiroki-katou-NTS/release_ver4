package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.checkconfirm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
@NoArgsConstructor
public class StateConfirm {
	
	GeneralDate date;
	
	String employeeId;
	
	boolean state;

	public StateConfirm(GeneralDate date, boolean state, String employeeId) {
		super();
		this.date = date;
		this.state = state;
		this.employeeId = employeeId;
	}

}
