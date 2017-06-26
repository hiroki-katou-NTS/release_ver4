package nts.uk.ctx.basic.ws.system.era;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.app.command.system.era.AddEraCommand;
import nts.uk.ctx.basic.app.command.system.era.AddEraCommandHandler;
import nts.uk.ctx.basic.app.command.system.era.DeleteEraCommand;
import nts.uk.ctx.basic.app.command.system.era.DeleteEraCommandHandler;
import nts.uk.ctx.basic.app.command.system.era.UpdateEraCommand;
import nts.uk.ctx.basic.app.command.system.era.UpdateEraCommandHandler;
import nts.uk.ctx.basic.app.find.system.era.EraDto;
import nts.uk.ctx.basic.app.find.system.era.EraFinder;

@Path("ctx/basic/era")
@Produces("application/json")
public class EraWebServices extends WebService {
	@Inject
	private AddEraCommandHandler addEra;
	@Inject
	private UpdateEraCommandHandler updateEra;
	@Inject
	private DeleteEraCommandHandler deleteEra;
	@Inject
	private EraFinder finder;
	@POST
	@Path("finderas")
	public List<EraDto> getAllEras(){
		return this.finder.getEras();
	}
	
//	@POST
//	@Path("find/{startDate}")
//	public EraDto getDetail(@PathParam("startDate")LocalDate startDate){
//		return this.finder.getEraDetail((startDate))
//				.orElseThrow(() -> new BusinessException(new RawErrorMessage("Not Found Era")));
//	}
	
	@POST
	@Path("deleteData")
	public void deleteData(DeleteEraCommand command){
		this.deleteEra.handle(command);
	}
	@POST
	@Path("updateData")
	public void updateData(UpdateEraCommand command){
		this.updateEra.handle(command);
	}
	@POST
	@Path("addData")
	public EraDto addData(AddEraCommand command){
		return this.addEra.handle(command);
	}
	
	@POST
	@Path("find/{eraHist}")
	public  Optional<EraDto> getDetail(@PathParam("eraHist") String eraHist){
		//return this.finder.getEraDetail(GeneralDate.fromString(startDate, "yyyy-MM-dd"));
		return this.finder.getEraDetail(eraHist);
	}
	
	@POST
	@Path("getFixAttribute/{eraHist}")
	public  int getFixAttribute(@PathParam("eraHist") String eraHist){
		return this.finder.getFixAtribute(eraHist);
	}
	
	@POST
	@Path("checkStartDate/{startDate}")
	public boolean checkStartDate(@PathParam("startDate") Date startDate){		
		return this.finder.checkStartDate(GeneralDate.legacyDate(startDate));
	}

}
