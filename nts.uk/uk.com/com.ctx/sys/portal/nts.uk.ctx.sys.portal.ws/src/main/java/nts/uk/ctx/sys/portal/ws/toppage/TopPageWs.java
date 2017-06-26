package nts.uk.ctx.sys.portal.ws.toppage;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.sys.portal.app.toppage.command.DeleteTopPageCommand;
import nts.uk.ctx.sys.portal.app.toppage.command.DeleteTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.toppage.command.RegisterTopPageCommand;
import nts.uk.ctx.sys.portal.app.toppage.command.RegisterTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.toppage.command.UpdateTopPageCommand;
import nts.uk.ctx.sys.portal.app.toppage.command.UpdateTopPageCommandHandler;
import nts.uk.ctx.sys.portal.app.toppage.find.TopPageDto;
import nts.uk.ctx.sys.portal.app.toppage.find.TopPageFinder;
import nts.uk.ctx.sys.portal.app.toppage.find.TopPageItemDto;

@Path("/toppage")
@Stateless
public class TopPageWs extends WebService {

	@Inject
	TopPageFinder topPageFinder;

	@Inject
	RegisterTopPageCommandHandler registerTopPageCommandHandler;

	@Inject
	UpdateTopPageCommandHandler updateTopPageCommandHandler;

	@Inject
	DeleteTopPageCommandHandler deleteTopPageCommandHandler;

	@POST
	@Path("findAll")
	public List<TopPageItemDto> findAll() {
		String companyId = "";
		return topPageFinder.findAll(companyId);
	}

	@POST
	@Path("topPageDetail/{topPageCode}")
	public TopPageDto getTopPageDetail(@PathParam("topPageCode") String topPageCode) {
		String companyId = "";
		return topPageFinder.findByCode(companyId, topPageCode, "0");
	}

	@POST
	@Path("create")
	public void createTopPage(RegisterTopPageCommand command) {
		registerTopPageCommandHandler.handle(command);
	}

	@POST
	@Path("update")
	public void updateTopPage(UpdateTopPageCommand command) {
		updateTopPageCommandHandler.handle(command);
	}

	@POST
	@Path("delete")
	public void deleteTopPage(DeleteTopPageCommand command) {
		deleteTopPageCommandHandler.handle(command);
	}
}
