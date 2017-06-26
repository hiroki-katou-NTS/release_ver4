package nts.uk.shr.com.program;

/**
 * @author manhnd
 * 
 * WebAppId
 */
public enum WebAppId {
	// Common
	COM("com"),
	// Payroll
	PR("pr"),
	// Attendance
	AT("at");
	
	public String name;
	WebAppId(String name) {
		this.name = name;
	}
}
