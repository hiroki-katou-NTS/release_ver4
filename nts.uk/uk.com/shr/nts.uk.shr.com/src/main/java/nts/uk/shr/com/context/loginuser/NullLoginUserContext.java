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
		return null;
	}

	@Override
	public String contractCode() {
		return null;
	}

	@Override
	public String companyId() {
		return null;
	}

	@Override
	public String companyCode() {
		return null;
	}

	@Override
	public String employeeId() {
		return null;
	}

	@Override
	public String employeeCode() {
		return null;
	}

}
