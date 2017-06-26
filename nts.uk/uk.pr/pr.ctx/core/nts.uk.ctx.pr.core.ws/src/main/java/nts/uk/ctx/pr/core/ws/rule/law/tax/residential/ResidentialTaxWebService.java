package nts.uk.ctx.pr.core.ws.rule.law.tax.residential;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.AddResidentialTaxCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.AddResidentialTaxCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.DeleteResidentialTaxCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.DeleteResidentialTaxCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.UpdateResidentialTaxCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.UpdateResidentialTaxCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.UpdateResidentialTaxReportCommand;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.UpdateResidentialTaxReportCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.ResidentialTaxDetailDto;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.ResidentialTaxDto;
import nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.ResidentialTaxFinder;

@Path("pr/core/residential")
@Produces("application/json")
public class ResidentialTaxWebService extends WebService {
	@Inject
	private AddResidentialTaxCommandHandler addData;
	@Inject
	private DeleteResidentialTaxCommandHandler deleleData;
	@Inject
	private UpdateResidentialTaxCommandHandler updateData;
	@Inject
	private UpdateResidentialTaxReportCommandHandler updateReport;
	@Inject
	private ResidentialTaxFinder finder;

	@POST
	@Path("findallresidential")
	public List<ResidentialTaxDto> getAllResidential() {
		return this.finder.getAllResidentialTax();
	}

	// companyCode != 0000
	@POST
	@Path("findResidentialTax/{resiTaxCode}")
	public ResidentialTaxDetailDto getResidialTaxDetail(@PathParam("resiTaxCode") String resiTaxCode) {
		return this.finder.getResidentialTax(resiTaxCode).get();
	}

	// companyCode = 0000
	@POST
	@Path("findResidentialTax/{companyCd}/{resiTaxCode}")
	public ResidentialTaxDetailDto getResidialTaxDetail(@PathParam("companyCd") String companyCd, @PathParam("resiTaxCode") String resiTaxCode) {
		return this.finder.getResidentialTax(companyCd, resiTaxCode).get();
	}

	// companyCode =0000
	@POST
	@Path("findallByCompanyCode")
	public List<ResidentialTaxDto> getAllResidentialByCompanyCode() {
		String companyCode = "0000";
		return this.finder.getAllResidentialTax(companyCode);
	}
	
	@POST
	@Path("findResidentialTax/{resiTaxCode}/{resiTaxReportCode}")
	public List<ResidentialTaxDto> getAllResidialTax(@PathParam("resiTaxCode") String resiTaxCode,
			@PathParam("resiTaxReportCode") String resiTaxReportCode) {
		return this.finder.getAllResidentialTax(resiTaxCode, resiTaxReportCode);
	}

	@POST
	@Path("findallresidential/resiTaxCode")
	public List<ResidentialTaxDto> getAllResidential(String resiTaxCode, String resiTaxReportCode) {
		return this.finder.getAllResidentialTax();
	}
//	@POST
//	@Path("findallresidential1/{resiTaxReportCode}")
//	public List<ResidentialTaxDetailDto> getAllResiTax( @PathParam("resiTaxReportCode") String resiTaxReportCode){
//		String companyCode ="";
//		if(AppContexts.user() != null){
//			companyCode = AppContexts.user().companyCode();
//			return this.finder.getAllResiTax(companyCode, resiTaxReportCode);
//		}else{
//			
//			return null;
//		}
//	}

	@POST
	@Path("addresidential")
	public void addResidential(AddResidentialTaxCommand command) {
		this.addData.handle(command);
	}

	@POST
	@Path("updateresidential")
	public void updateResidential(UpdateResidentialTaxCommand command) {
		this.updateData.handle(command);
	}
	
	@POST
	@Path("updatereportCode")
	public void updateReportCode(UpdateResidentialTaxReportCommand command) {
		this.updateReport.handle(command);
	}

	@POST
	@Path("deleteresidential")
	public void deleleResidential(DeleteResidentialTaxCommand command) {
		this.deleleData.handle(command);
	}

}
