package nts.uk.ctx.at.schedule.ws.shift.rank;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.uk.ctx.at.schedule.app.find.shift.rank.RankDto;
import nts.uk.ctx.at.schedule.app.find.shift.rank.RankFinder;

@Path("at/schedule/shift/rank")
@Produces(MediaType.APPLICATION_JSON)
public class RankWS {
	@Inject
	RankFinder rankFinder;

	@POST
	@Path("findAll")
	public List<RankDto> getAllTeamSet() {
		return this.rankFinder.getAllListRank();
	}
}
