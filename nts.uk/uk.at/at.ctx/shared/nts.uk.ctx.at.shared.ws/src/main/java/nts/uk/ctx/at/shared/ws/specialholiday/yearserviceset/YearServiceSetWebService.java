package nts.uk.ctx.at.shared.ws.specialholiday.yearserviceset;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearserviceset.InsertYearServiceSetCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearserviceset.InsertYearServiceSetCommandHandler;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearserviceset.UpdateYearServiceSetCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearserviceset.UpdateYearServiceSetCommandHandler;
import nts.uk.ctx.at.shared.app.find.specialholiday.yearserviceset.YearServiceSetDto;
import nts.uk.ctx.at.shared.app.find.specialholiday.yearserviceset.YearServiceSetFinder;
@Path("at/shared/yearserviceset")
@Produces("application/json")
public class YearServiceSetWebService extends WebService{
	@Inject
	private YearServiceSetFinder finder;
	@Inject
	private InsertYearServiceSetCommandHandler add;
	@Inject
	private UpdateYearServiceSetCommandHandler update;
	/**
	 * get all data
	 * @return
	 */
	@POST
	@Path("findAll") 
	public List<YearServiceSetDto> finder(){
		return this.finder.finder();
	}
	@POST
	@Path("add")
	public void insert(InsertYearServiceSetCommand command){
		this.add.handle(command);
	}
	@POST
	@Path("update")
	public void insert(UpdateYearServiceSetCommand command){
		this.update.handle(command);
	}
}
