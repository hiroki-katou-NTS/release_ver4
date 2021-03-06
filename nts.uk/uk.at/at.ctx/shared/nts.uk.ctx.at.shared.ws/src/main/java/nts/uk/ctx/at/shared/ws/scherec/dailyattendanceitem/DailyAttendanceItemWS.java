package nts.uk.ctx.at.shared.ws.scherec.dailyattendanceitem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.find.scherec.dailyattendanceitem.DailyAttendanceItemDto;
import nts.uk.ctx.at.shared.app.find.scherec.dailyattendanceitem.DailyAttendanceItemSharedFinder;

@Path("at/shared/scherec/dailyattditem")
@Produces("application/json")
public class DailyAttendanceItemWS extends WebService {
	
	@Inject
	private DailyAttendanceItemSharedFinder finder;
	
	@POST
	@Path("getalldailyattd")
	public List<DailyAttendanceItemDto> getListDailyAttdItem(){
		List<DailyAttendanceItemDto> data = finder.getAllDailyAttdItem();
		return data;
	}

}
