package nts.uk.ctx.at.shared.ws.specialholiday.yearservicecom;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearservicecom.InsertYearServiceComCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearservicecom.InsertYearServiceComCommandHandler;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearservicecom.UpdateYearServiceComCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.yearservicecom.UpdateYearServiceComCommandHandler;
import nts.uk.ctx.at.shared.app.find.specialholiday.yearservicecom.YearServiceComDto;
import nts.uk.ctx.at.shared.app.find.specialholiday.yearservicecom.YearServiceComFinder;

@Path("at/shared/yearservicecom")
@Produces("application/json")
public class YearServiceComWebService extends WebService{
	@Inject
	private YearServiceComFinder finder;
	@Inject
	private InsertYearServiceComCommandHandler add;
	@Inject
	private UpdateYearServiceComCommandHandler update;
	/**
	 * get all data
	 * @return
	 */
	@POST
	@Path("findAll") 
	public List<YearServiceComDto> finder(){
		return this.finder.finder();
	}
	@POST
	@Path("add")
	public void insert(InsertYearServiceComCommand command){
		this.add.handle(command);
	}
	@POST
	@Path("update")
	public void insert(UpdateYearServiceComCommand command){
		this.update.handle(command);
	}
}
