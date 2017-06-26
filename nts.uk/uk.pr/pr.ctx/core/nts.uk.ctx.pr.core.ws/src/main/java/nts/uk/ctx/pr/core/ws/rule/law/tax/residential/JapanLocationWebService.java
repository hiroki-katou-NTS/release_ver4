package nts.uk.ctx.pr.core.ws.rule.law.tax.residential;

import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.service.JapanLocationService;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.service.RegionObject;
@Path("pr/core/residential")
@Produces("application/json")
public class JapanLocationWebService extends WebService{
	@POST
	@Path("getlistLocation")
	public List<RegionObject> getAllRegionPrefecture() {
		return JapanLocationService.getJapanLocation();
	}

}
