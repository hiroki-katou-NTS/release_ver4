package nts.uk.ctx.hr.develop.ws.databeforereflecting.retirementinformation;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.uk.ctx.hr.develop.app.databeforereflecting.retirementinformation.find.RetiDateDto;
import nts.uk.ctx.hr.develop.app.databeforereflecting.retirementinformation.find.RetirementInformationFinder;
import nts.uk.ctx.hr.develop.app.databeforereflecting.retirementinformation.find.SearchRetiredEmployeesQuery;
import nts.uk.ctx.hr.develop.app.databeforereflecting.retirementinformation.find.SearchRetiredResultDto;

@Path("databeforereflecting/")
@Produces(MediaType.APPLICATION_JSON)
public class RetirementInformationWebService {

	@Inject
	private RetirementInformationFinder finder;

	@POST
	@Path("startPage")
	public RetiDateDto startPage() {
		return this.finder.startPage();
	}
	
	@POST
	@Path("search-retired")
	public SearchRetiredResultDto searchRetiredEmployees(SearchRetiredEmployeesQuery query) {
		return this.finder.searchRetiredEmployees(query);
	}
	
}
