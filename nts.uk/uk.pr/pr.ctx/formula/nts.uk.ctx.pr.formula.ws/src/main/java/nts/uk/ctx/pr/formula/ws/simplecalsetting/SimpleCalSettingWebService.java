package nts.uk.ctx.pr.formula.ws.simplecalsetting;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.formula.app.find.formula.SimpleCalSettingDto;
import nts.uk.ctx.pr.formula.app.find.formula.SimpleCalSettingFinder;

@Path("pr/formula/simplecalsetting")
@Produces("application/json")
public class SimpleCalSettingWebService {
	@Inject
	private SimpleCalSettingFinder simpleCalSettingFinder;
	
	@POST
	@Path("getAll")
	public List<SimpleCalSettingDto> getAllSimpleCalSettingDto() {
		return this.simpleCalSettingFinder.getAll();
	}
}
