package nts.uk.ctx.pr.core.ws.personalwage;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.personalinfo.wage.PersonalWageNameDto;
import nts.uk.ctx.pr.core.app.find.personalinfo.wage.PersonalWageNameFinder;
import nts.uk.shr.com.context.AppContexts;



@Path("pr/proto/personalwage")
@Produces("application/json")
public class PersonalWageWebService extends WebService {

	@Inject
	private PersonalWageNameFinder findPersonalWageName;


	
	@POST
	@Path("findalls/{categoryAtr}")
	public List<PersonalWageNameDto> getAll(@PathParam("categoryAtr") int categoryAtr){
		String companyCode = AppContexts.user().companyCode();
		return this.findPersonalWageName.getPersonalWageName(companyCode, categoryAtr);		
	}

	
	
	
	
}
