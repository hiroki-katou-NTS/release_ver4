package nts.uk.ctx.exio.ws.exo.outcnddetail;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.exio.app.command.exo.outcnddetail.OutCndDetailInfoCommand;
import nts.uk.ctx.exio.app.command.exo.outcnddetail.RegisterOutCndDetailCommandHandler;
import nts.uk.ctx.exio.app.find.exo.categoryitemdata.CtgItemDataCndDetailDto;
import nts.uk.ctx.exio.app.find.exo.categoryitemdata.CtgItemDataFinder;

@Path("exio/exo/outcnddetail")
@Produces("application/json")
public class OutCndDetailWebService {

	@Inject
	private CtgItemDataFinder ctgItemDataFinder;

	@Inject
	private RegisterOutCndDetailCommandHandler registerOutCndDetailCommandHandler;

	@POST
	@Path("getListCtgItems/{condSetCd}/{categoryId}")
	public CtgItemDataCndDetailDto getListCtgItems(@PathParam("condSetCd") String condSetCd,
			@PathParam("categoryId") String categoryId) {
		return ctgItemDataFinder.getDataItemDetail(condSetCd, Integer.valueOf(categoryId));

	}

	@POST
	@Path("register")
	public void register(OutCndDetailInfoCommand command) {
		registerOutCndDetailCommandHandler.handle(command);
	}

}
