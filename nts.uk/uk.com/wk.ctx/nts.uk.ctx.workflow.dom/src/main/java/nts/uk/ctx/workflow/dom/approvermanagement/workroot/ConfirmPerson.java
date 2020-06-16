package nts.uk.ctx.workflow.dom.approvermanagement.workroot;

import lombok.AllArgsConstructor;
import nts.arc.enums.EnumAdaptor;
/**
 * 確定者
 * @author hoatt
 *
 */
@AllArgsConstructor
public enum ConfirmPerson {
	/** 未確定*/
	NOT_CONFIRM(0),
	/** 確定*/
	CONFIRM(1);
	public final int value;
	
	public static ConfirmPerson of(int type) {
		return EnumAdaptor.valueOf(type, ConfirmPerson.class);
	}

}
