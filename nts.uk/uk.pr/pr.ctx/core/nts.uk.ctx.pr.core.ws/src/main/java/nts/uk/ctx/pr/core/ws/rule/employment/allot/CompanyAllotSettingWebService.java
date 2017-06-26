package nts.uk.ctx.pr.core.ws.rule.employment.allot;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.DelAllotCompanyCmd;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.DelAllotCompanyCmdHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.InsertAllotCompanyCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.InsertAllotCompanyCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.UpdateAllotCompanyCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.UpdateAllotCompanyCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.CompanyAllotSettingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.CompanyAllotSettingFinder;

@Path("pr/core/allot")
@Produces("application/json")
public class CompanyAllotSettingWebService extends WebService{
	@Inject
	private CompanyAllotSettingFinder find;
	@Inject
	private UpdateAllotCompanyCommandHandler update;
	@Inject 
	private InsertAllotCompanyCommandHandler insert;
	@Inject
	private DelAllotCompanyCmdHandler delete;

	@POST
	@Path("findallcompanyallot")
	public List<CompanyAllotSettingDto> GetAllCompanyAllotSetting() {
		List<CompanyAllotSettingDto> test = this.find.getAllCompanyAllotSetting();
		return test;
	}

	@POST
	@Path("findcompanyallotlayoutname/{stmtCode}")
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String GetAllotLayoutName(@PathParam("stmtCode") String stmtCode) {
		return this.find.getAllotLayoutName(stmtCode);
	}
	
	
	@POST
	@Path("findcompanyallotmaxdate")
	public CompanyAllotSettingDto GetAllotMaxDate() {
		CompanyAllotSettingDto test = this.find.getMaxStartYM().get();
		return test;
	}
	

	@POST
	@Path("update")
	public void update(UpdateAllotCompanyCommand command) {
		this.update.handle(command);
	}
	
	@POST
	@Path("insert")
	public void insert(InsertAllotCompanyCommand command) {
		this.insert.handle(command);
	}
	
	@POST
	@Path("delete")
	public void delete(DelAllotCompanyCmd command) {
		this.delete.handle(command);
	}
}