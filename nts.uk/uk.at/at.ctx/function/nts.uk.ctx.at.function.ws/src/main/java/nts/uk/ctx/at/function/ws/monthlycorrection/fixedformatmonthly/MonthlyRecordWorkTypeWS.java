package nts.uk.ctx.at.function.ws.monthlycorrection.fixedformatmonthly;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.function.app.command.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeCmd;
import nts.uk.ctx.at.function.app.command.monthlycorrection.fixedformatmonthly.UpdateMonthlyRecordWorkTypeCmdHandler;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeDto;
import nts.uk.ctx.at.function.app.find.monthlycorrection.fixedformatmonthly.MonthlyRecordWorkTypeFinder;

/**
 * The Class LinkPlanTimeItemWS.
 */
@Path("at/function/monthlycorrection")
@Produces(MediaType.APPLICATION_JSON)
public class MonthlyRecordWorkTypeWS extends WebService {

	@Inject
	private MonthlyRecordWorkTypeFinder fider;
	
	@Inject
	private UpdateMonthlyRecordWorkTypeCmdHandler updateHandler;
	
	
	@POST
	@Path("findall")
	public List<MonthlyRecordWorkTypeDto> findAll(){
		return this.fider.getListMonthlyRecordWorkType();
	}
	
	@POST
	@Path("findbycode/{businessTypeCode}")
	public MonthlyRecordWorkTypeDto findByCode(@PathParam("businessTypeCode") String businessTypeCode){
		return this.fider.getMonthlyRecordWorkTypeByCode(businessTypeCode);
	}
	
	@POST
	@Path("updatemonthly")
	public void updateMonthly(MonthlyRecordWorkTypeCmd command){
		this.updateHandler.handle(command);
	}
	
	
}
