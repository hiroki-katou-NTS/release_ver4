package nts.uk.ctx.pr.core.ws.rule.employment.allot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.AllotLayoutHistFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.layout.LayoutHistoryDto;

@Path("pr/core/allotlayouthist")
@Produces("application/json")
public class AllotLayoutHistWebServices extends WebService {
	@Inject
	private AllotLayoutHistFinder find;

	@POST
	@Path("getdata/{baseYm}")
	public Object[] getData(@PathParam("baseYm") int baseYm) {
		List<String> objs = new ArrayList<String>();

		List<LayoutHistoryDto> layouts = this.find.getSel1LayoutHistory(baseYm);
		for (LayoutHistoryDto dto : layouts) {
			objs.add(this.find.getAllotLayoutName(dto.getStmtCode()));
		}

		return new Object[] { layouts, objs };
	}

	@POST
	@Path("findname/{stmtCode}")
	public String GetAllotLayoutName(@PathParam("stmtCode") String stmtCode) {
		return this.find.getAllotLayoutName(stmtCode);
	}
}
