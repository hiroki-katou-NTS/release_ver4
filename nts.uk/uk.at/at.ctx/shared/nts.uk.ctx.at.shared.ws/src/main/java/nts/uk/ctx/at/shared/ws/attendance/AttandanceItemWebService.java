package nts.uk.ctx.at.shared.ws.attendance;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.find.attendance.AttendanceItemDto;
import nts.uk.ctx.at.shared.app.find.attendance.AttendanceItemFinder;

@Path("at/share/attendanceitem")
@Produces("application/json")
public class AttandanceItemWebService extends WebService {
	@Inject
	private AttendanceItemFinder find;

	/**
	 * get all divergence time
	 * 
	 * @return
	 */
	@POST
	@Path("getPossibleAttendanceItem")
	public List<AttendanceItemDto> getPossibleAttendanceItem(List<Integer> lstPossible) {
		return this.find.getPossibleAttendanceItem(lstPossible);
	}

}
