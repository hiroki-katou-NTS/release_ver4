package nts.uk.ctx.at.shared.dom.workingcondition;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

@Getter
public class WorkingConditionItemCustom {
	/** The employee id. */
	// 社員ID
	private String employeeId;
	/** The labor system. */
	// 労働制
	private WorkingSystem laborSystem = WorkingSystem.REGULAR_WORK;

	public WorkingConditionItemCustom(String employeeId, int laborSystem) {
		this.employeeId = employeeId;
		this.laborSystem = EnumAdaptor.valueOf(laborSystem, WorkingSystem.class);
	}
}
