package nts.uk.ctx.pr.core.ws.rule.employment.layout;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.CreateLayoutCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.CreateLayoutCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.CreateLayoutHistoryCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.CreateLayoutHistoryCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.DeleteLayoutHistoryCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.DeleteLayoutHistoryCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.UpdateLayoutHistoryCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.UpdateLayoutHistoryCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.register.RegisterLayoutCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.layout.register.RegisterLayoutCommandHandler;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutHeadDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutHistoryDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutMasterFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.category.LayoutMasterCategoryDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.category.LayoutMasterCategoryFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail.LayoutMasterDetailDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.detail.LayoutMasterDetailFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("pr/proto/layout")
@Produces("application/json")
public class LayoutWebService extends WebService {

	@Inject
	private CreateLayoutCommandHandler createLayoutData;
	@Inject
	private CreateLayoutHistoryCommandHandler createHistoryData;
	@Inject
	private UpdateLayoutHistoryCommandHandler updateData;
	@Inject
	private DeleteLayoutHistoryCommandHandler deleteData;
	@Inject
	private RegisterLayoutCommandHandler registerLayoutHandler;
	@Inject
	private LayoutMasterFinder find;
//	@Inject
//	private LayoutHisFinder find1;
	@Inject
	private LayoutMasterCategoryFinder categoryFinder;
	@Inject
	private LayoutMasterDetailFinder detailFinder;

	@POST
	@Path("findalllayoutHead")
	public List<LayoutHeadDto> getAllLayoutHead() {
		return this.find.getAllLayoutHead(AppContexts.user().companyCode());
	}
	
//	@POST
//	@Path("findallMaxHistory")
//	public List<LayoutHistoryDto> getAllHistoryMaxStart() {
//		return this.find.getHistoryWithMaxStartYm(AppContexts.user().companyCode());
//	}

	@POST
	@Path("findalllayoutHist")
	public List<LayoutHistoryDto> getAllLayoutHist() {
		return this.find.getAllLayoutHist(AppContexts.user().companyCode());
	}

	@POST
	@Path("findlayout/{stmtCode}/{historyId}")
	public LayoutHeadDto getLayout(@PathParam("stmtCode") String stmtCode, @PathParam("historyId") String historyId) {
		return this.find.getLayout(AppContexts.user().companyCode(), stmtCode, historyId).get();
	}

	@POST
	@Path("findlayoutdetail/{stmtCode}/{startYm}/{categoryAtr}/{itemCd}")
	public LayoutMasterDetailDto getDetail(@PathParam("stmtCode") String stmtCode, @PathParam("startYm") int startYm,
			@PathParam("categoryAtr") int categoryAtr, @PathParam("itemCd") String itemCd) {
		try {
			if (this.detailFinder.getDetail(stmtCode, startYm, categoryAtr, itemCd).isPresent()) {
				return this.detailFinder.getDetail(stmtCode, startYm, categoryAtr, itemCd).get();
			}
			return null;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@POST
	@Path("findCategoies/full/{layoutCd}/{historyId}/{startYm}")
	public List<LayoutMasterCategoryDto> getCategoriesFullData(@PathParam("layoutCd") String layoutCd,
			@PathParam("historyId") String historyId, @PathParam("startYm") int startYm) {
		return this.categoryFinder.getCategoriesFullData(layoutCd, historyId, startYm);
	}

	@POST
	@Path("findlayoutwithmaxstartym")
	public List<LayoutHeadDto> getLayoutsWithMaxStartYm() {
		return this.find.getLayoutsWithMaxStartYm(AppContexts.user().companyCode());
	}

	@POST
	@Path("createlayout")
	public void createLayout(CreateLayoutCommand command) {
		this.createLayoutData.handle(command);
	}

	@POST
	@Path("createlayouthistory")
	public void createLayoutHistory(CreateLayoutHistoryCommand command) {
		this.createHistoryData.handle(command);
	}

	@POST
	@Path("updatedata")
	public void updateData(UpdateLayoutHistoryCommand command) {
		this.updateData.handle(command);
	}

	@POST
	@Path("deletedata")
	public void deleteData(DeleteLayoutHistoryCommand command) {
		this.deleteData.handle(command);
	}

	@POST
	@Path("register")
	public void registerLayout(RegisterLayoutCommand command) {
		this.registerLayoutHandler.handle(command);
	}

}
