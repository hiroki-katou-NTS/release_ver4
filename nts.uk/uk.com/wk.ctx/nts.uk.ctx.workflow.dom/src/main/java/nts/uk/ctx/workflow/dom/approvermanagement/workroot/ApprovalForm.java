package nts.uk.ctx.workflow.dom.approvermanagement.workroot;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;

/**
 * 承認形態
 * @author hoatt
 *
 */
@Getter
public enum ApprovalForm {
	/** 全員承認*/
	EVERYONE_APPROVED(1, "全員承認"),
	/** 誰か一人*/
	SINGLE_APPROVED(2, "誰か一人");
	
	public int value;
	public String name;
	
	private ApprovalForm(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public static ApprovalForm of(int type) {
		return EnumAdaptor.valueOf(type, ApprovalForm.class);
	}

}
