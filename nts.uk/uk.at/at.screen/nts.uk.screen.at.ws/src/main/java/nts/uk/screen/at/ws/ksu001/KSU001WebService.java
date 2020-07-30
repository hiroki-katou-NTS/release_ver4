package nts.uk.screen.at.ws.ksu001;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.screen.at.app.ksu001.changemode.NextMonthFinder;
import nts.uk.screen.at.app.ksu001.changemode.PreMonthFinder;
import nts.uk.screen.at.app.ksu001.changemode.ShiftModeFinder;
import nts.uk.screen.at.app.ksu001.changemode.ShortNameModeFinder;
import nts.uk.screen.at.app.ksu001.changemode.TimeModeFinder;
import nts.uk.screen.at.app.ksu001.start.StartKSU001;
import nts.uk.screen.at.app.ksu001.start.StartKSU001Dto;
import nts.uk.screen.at.app.ksu001.start.StartKSU001Param;

/**
 * 
 * @author laitv
 *
 */
@Path("screen/at/schedule")
@Produces("application/json")
public class KSU001WebService extends WebService{

	@Inject
	private StartKSU001 startKSU001;
	@Inject
	private ShiftModeFinder shiftModeFinder;
	@Inject
	private ShortNameModeFinder shortNameModeFinder;
	@Inject
	private TimeModeFinder timeModeFinder;
	@Inject
	private NextMonthFinder nextMonthFinder;
	@Inject
	private PreMonthFinder preMonthFinder;
	
	@POST
	@Path("start")
	public StartKSU001Dto getDataStartScreen(StartKSU001Param param){
		StartKSU001Dto data = startKSU001.getDataStartScreen(param);
		return data;
	}
	
	@POST
	@Path("shift")
	public StartKSU001Dto getDataShiftMode(StartKSU001Param param){
		StartKSU001Dto data = shiftModeFinder.getDataStartScreen(param);
		return data;
	}
	
	@POST
	@Path("shortname")
	public StartKSU001Dto getDataShortNameMode(StartKSU001Param param){
		StartKSU001Dto data = shortNameModeFinder.getDataStartScreen(param);
		return data;
	}
	
	@POST
	@Path("time")
	public StartKSU001Dto getDataTimeMode(StartKSU001Param param){
		StartKSU001Dto data = timeModeFinder.getDataStartScreen(param);
		return data;
	}
	
	@POST
	@Path("next-month")
	public StartKSU001Dto getDataNextMonth(StartKSU001Param param){
		StartKSU001Dto data = nextMonthFinder.getDataStartScreen(param);
		return data;
	}

	
	@POST
	@Path("pre-month")
	public StartKSU001Dto getDataPreMonth(StartKSU001Param param){
		StartKSU001Dto data = preMonthFinder.getDataStartScreen(param);
		return data;
	}


	
}
