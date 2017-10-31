package nts.uk.ctx.at.record.dom.workrecord.role;

import lombok.Getter;

@Getter
public class Role {

	private String roleCode;

	private String roleId;

	private String roleName;

	public Role(String roleCode, String roleId, String roleName) {
		super();
		this.roleCode = roleCode;
		this.roleId = roleId;
		this.roleName = roleName;
	}

}
