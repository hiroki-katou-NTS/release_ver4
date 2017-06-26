package nts.uk.shr.com.context;

public class LoginUserContext {

	public String companyCode() {
		return "0001";
	}
	
	public String employeeCode() {
		return "1234567890AB";
	} 
	
	public String personId(){
		return "000426a2-181b-4c7f-abc8-6fff9f4f983a";
	} 
	
	public String employeeId(){
		return "000426a2-181b-4c7f-abc8-6fff9f4f983a";
	}
	
	public String contractCode() {
		return "000000000000";
	}
	
	public String companyId() {
		return this.contractCode() + "-" + this.companyCode();
	}
}
