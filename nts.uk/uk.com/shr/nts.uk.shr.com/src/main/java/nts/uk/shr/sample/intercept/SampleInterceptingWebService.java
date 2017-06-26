package nts.uk.shr.sample.intercept;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/sample/intercept")
public class SampleInterceptingWebService {

	@Inject
	private SampleIntercepted intercepted;
	

	@POST
	@Path("test")
	public void test() {
		String s = this.intercepted.join("AA", "BBB");
		this.intercepted.hoge();
		s.toString();
	}
	
}
