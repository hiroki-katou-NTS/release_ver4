/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.portal.ws.toppage;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.portal.app.command.toppage.CopyTopPageCommand;
import nts.uk.ctx.sys.portal.app.command.toppage.CopyTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.command.toppage.DeleteTopPageCommand;
import nts.uk.ctx.sys.portal.app.command.toppage.DeleteTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.command.toppage.RegisterTopPageCommand;
import nts.uk.ctx.sys.portal.app.command.toppage.RegisterTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.command.toppage.UpdateTopPageCommand;
import nts.uk.ctx.sys.portal.app.command.toppage.UpdateTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.find.toppage.FlowMenuOutput;
import nts.uk.ctx.sys.portal.app.find.toppage.LayoutNewDto;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageFinder;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageItemDto;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageNewDto;
import nts.uk.ctx.sys.portal.app.find.toppagesetting.DisplayInTopPage;
import nts.uk.ctx.sys.portal.app.find.toppagesetting.DisplayMyPageFinder;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class TopPageWebService.
 */
@Path("/toppage")
@Produces("application/json")
public class TopPageWebService extends WebService {

	/** The top page finder. */
	@Inject
	private TopPageFinder topPageFinder;

	/** The register top page command handler. */
	@Inject
	private RegisterTopPageCommandHandler registerTopPageCommandHandler;

	/** The update top page command handler. */
	@Inject
	private UpdateTopPageCommandHandler updateTopPageCommandHandler;

	/** The delete top page command handler. */
	@Inject
	private DeleteTopPageCommandHandler deleteTopPageCommandHandler;

	@Inject
	private CopyTopPageCommandHandler copyTopPageCommandHandler;
	
	@Inject
	private DisplayMyPageFinder displayMyPageFinder;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<TopPageItemDto> findAll() {
		String companyId = AppContexts.user().companyId();
		return topPageFinder.findAll(companyId);
	}

	/**
	 * Gets the top page detail.
	 *
	 * @param topPageCode
	 *            the top page code
	 * @return the top page detail
	 */
	@POST
	@Path("topPageDetail/{topPageCode}")
	public TopPageNewDto getTopPageDetail(@PathParam("topPageCode") String topPageCode) {
		String companyId = AppContexts.user().companyId();
		return topPageFinder.findByCode(companyId, topPageCode);
	}

	/**
	 * Creates the top page.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("create")
	public void createTopPage(RegisterTopPageCommand command) {
		registerTopPageCommandHandler.handle(command);
	}

	/**
	 * Copy top page.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("copyTopPage")
	public void copyTopPage(CopyTopPageCommand command) {
		copyTopPageCommandHandler.handle(command);
	}
	
	/**
	 * Update top page.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void updateTopPage(UpdateTopPageCommand command) {
		updateTopPageCommandHandler.handle(command);
	}

	/**
	 * Delete top page.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("remove")
	public void deleteTopPage(DeleteTopPageCommand command) {
		deleteTopPageCommandHandler.handle(command);
	}
	
	@POST
	@Path("getLayout/{topPageCd}")
	public LayoutNewDto getLayout(@PathParam("topPageCd") String topPageCd) {
		String companyId = AppContexts.user().companyId();
		return topPageFinder.getLayout(companyId, topPageCd);
	}
	
	@POST
	@Path("getFlowMenu")
	public  List<FlowMenuOutput> getFlowMenu(ChangeLayoutRequest changeLayoutRequest) {
		String companyId = AppContexts.user().companyId();
		return topPageFinder.getFlowMenuOrFlowMenuUploadList(companyId, changeLayoutRequest.getTopPageCd(), changeLayoutRequest.getLayoutType());
	}
	
//	@POST
//	@Path("updateLayoutFlowMenu")
//	public void updateLayoutFlowMenu(LayoutNewDto request) {
//		String companyId = AppContexts.user().companyId();
//		topPageFinder.updateLayoutFlowMenu(companyId, request);
//	}
	
	@POST
	@Path("getDisplayTopPage/{topPageCd}")
	public DisplayInTopPage getDisplayTopPage(@PathParam("topPageCd") String topPageCd) {
		return displayMyPageFinder.displayTopPage(topPageCd);
	}

}
