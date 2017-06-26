package nts.uk.ctx.basic.ws.company;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.company.AddCompanyCommand;
import nts.uk.ctx.basic.app.command.company.AddCompanyCommandHandler;
import nts.uk.ctx.basic.app.command.company.UpdateCompanyCommand;
import nts.uk.ctx.basic.app.command.company.UpdateCompanyCommandHandler;
import nts.uk.ctx.basic.app.find.company.CompanyDto;
import nts.uk.ctx.basic.app.find.company.CompanyFinder;

/**
 * 
 * @author lanlt
 *
 */
@Path("ctx/proto/company")
@Produces("application/json")
public class CompanyWebservice extends WebService{
	@Inject
	private AddCompanyCommandHandler addData;
	@Inject
	private UpdateCompanyCommandHandler updateData;
	@Inject
	private CompanyFinder finder;
	
	@POST
	@Path("findallcompany")
	public List<CompanyDto> getAllCompanys(){
		return this.finder.getAllCompanys();
	}
	@POST
	@Path("findCompany")
	public CompanyDto getCompanyDetail(){
		return this.finder.getCompany().get();
	}
	@POST
	@Path("findCompanyDetail/{companyCd}")
	public CompanyDto getCompanyDetail(@PathParam("companyCd") String companyCd){
		return this.finder.getCompanyDetail(companyCd)
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("Not found company")));
	}

	@POST
	@Path("findByUseKtSet/{useKtSet}")
	public CompanyDto getCompanyByUserKtSet(@PathParam("useKtSet") int useKtSet){
		return this.finder.getCompanyByUserKtSet(useKtSet)
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("Not Found Company")));
	}
	
	@POST
	@Path("adddata")
	public void addData(AddCompanyCommand command){
		this.addData.handle(command);
	}
	
	@POST
	@Path("updatedata")
	public void updateData(UpdateCompanyCommand command){
		this.updateData.handle(command);
	}
	
}
