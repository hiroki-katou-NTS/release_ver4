package nts.uk.shr.com.context.loginuser;

public interface SessionLowLayer {

	void loggedIn();
	
	void loggedOut();
	
	boolean isLoggedIn();
	
	int secondsSessionTimeout();
}
