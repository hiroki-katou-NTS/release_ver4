package nts.uk.shr.sample.domevent;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/sample/domevent")
@Produces("application/json")
public class SampleDomainEventWebService {

	@POST
	@Path("test")
	public void test() {
		new SomeDomain().doSomethingWithEvent(true);
	}
}
