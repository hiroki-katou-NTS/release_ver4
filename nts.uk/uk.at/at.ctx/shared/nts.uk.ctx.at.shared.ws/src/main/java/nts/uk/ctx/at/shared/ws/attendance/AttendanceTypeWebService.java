package nts.uk.ctx.at.shared.ws.attendance;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.find.attendance.AttendanceTypeDto;
import nts.uk.ctx.at.shared.app.find.attendance.AttendanceTypeFinder;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Path("at/share/attendanceType")
@Produces("application/json")
public class AttendanceTypeWebService extends WebService{
	
	@Inject
	private AttendanceTypeFinder attendanceTypeFinder;
	
	@POST
	@Path("getByType/{type}")
	public List<AttendanceTypeDto> getItemByType(@PathParam("type") int type){
		return this.attendanceTypeFinder.getItemByType(type);
	} 
	
}
