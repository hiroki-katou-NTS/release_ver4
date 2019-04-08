package nts.uk.shr.sample.test.pub;

import javax.ejb.Stateless;

@Stateless
public class SamplePubTestImpl implements SamplePubTest {

	@Override
	public String greeting(String name) {
		return "hello, " + name;
	}

}
