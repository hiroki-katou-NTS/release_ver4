package nts.uk.screen.at.ws.application;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.request.dom.application.applist.service.ListOfAppTypes;
import nts.uk.screen.at.app.application.ApplicationScreenFinder;

@Path("at/request/application/screen/applist")
@Produces("application/json")
public class ApplicationScreenWebService {
	
	@Inject
	private ApplicationScreenFinder applicationScreenFinder;
	
	@Path("getAppNameInAppList")
	public List<ListOfAppTypes> getAppNameInAppList() {
		return applicationScreenFinder.getAppNameInAppList();
	}
	
}
