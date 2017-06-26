package nts.uk.shr.sample.intercept;

import javax.ejb.Stateless;

@Stateless
public class SampleInterceptedImpl extends SampleInterceptedBase implements SampleIntercepted {

	@Override
	public String join(String a, String b) {
		this.hoge();
		return a + b;
	}

	@Override
	public void hoge() {
		String s = "a";
		s.toCharArray();
	}
	
}
