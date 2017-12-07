package nts.uk.shr.com.context.loginuser.role;

import java.io.Serializable;

public class DefaultLoginUserRoles implements LoginUserRoles, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String forAttendance = null;
	private String forPayroll = null;
	private String forPersonnel = null;
	private String forPersonalInfo = null;
	private String forOfficeHelper = null;
	private String forSystemAdmin = null;
	private String forCompanyAdmin = "99900000-0000-0000-0000-000000000001";
	
	@Override
	public String forAttendance() {
		return this.forAttendance;
	}

	@Override
	public String forPayroll() {
		return this.forPayroll;
	}

	@Override
	public String forPersonnel() {
		return this.forPersonnel;
	}

	@Override
	public String forPersonalInfo() {
		return this.forPersonalInfo;
	}

	@Override
	public String forOfficeHelper() {
		return this.forOfficeHelper;
	}

	@Override
	public String forSystemAdmin() {
		return this.forSystemAdmin;
	}

	@Override
	public String forCompanyAdmin() {
		return this.forCompanyAdmin;
	}

	public void setRoleIdForAttendance(String roleId) {
		this.forAttendance = roleId;
	}

	public void setRoleIdForPayroll(String roleId) {
		this.forPayroll = roleId;
	}

	public void setRoleIdForPersonnel(String roleId) {
		this.forPersonnel = roleId;
	}

	public void setRoleIdforPersonalInfo(String roleId) {
		this.forPersonalInfo = roleId;
	}

	public void setRoleIdforOfficeHelper(String roleId) {
		this.forOfficeHelper = roleId;
	}

	public void setRoleIdforSystemAdmin(String roleId) {
		this.forSystemAdmin = roleId;
	}

	public void setRoleIdforCompanyAdmin(String roleId) {
		this.forCompanyAdmin = roleId;
	}
}
