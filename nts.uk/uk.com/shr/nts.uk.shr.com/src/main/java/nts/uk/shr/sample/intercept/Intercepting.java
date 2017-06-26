package nts.uk.shr.sample.intercept;

import javax.ejb.Stateless;

@Stateless
public class Intercepting {

	public String run() {
		String s = "aa";
		return s.toString();
	}  
}
