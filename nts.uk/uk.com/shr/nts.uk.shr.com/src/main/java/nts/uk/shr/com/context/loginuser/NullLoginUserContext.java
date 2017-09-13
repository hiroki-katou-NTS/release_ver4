package nts.uk.shr.com.context.loginuser;

import java.io.Serializable;

import nts.uk.shr.com.context.LoginUserContext;

public class NullLoginUserContext implements LoginUserContext, Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean hasLoggedIn() {
		return false;
	}

	@Override
	public boolean isEmployee() {
		return false;
	}

	@Override
	public String userId() {
		return null;
	}

	@Override
	public String loginCode() {
		return null;
	}

	@Override
	public String personId() {
		return "000426a2-181b-4c7f-abc8-6fff9f4f983a";
	}

	@Override
	public String contractCode() {
		return "000000000000";
	}

	@Override
	public String companyId() {
		return this.contractCode() + "-" + this.companyCode();
	}

	@Override
	public String companyCode() {
		return "0001";
	}

	@Override
	public String employeeId() {
		return "000426a2-181b-4c7f-abc8-6fff9f4f983a";
	}

	@Override
	public String employeeCode() {
		return "1234567890AB";
	}

}
