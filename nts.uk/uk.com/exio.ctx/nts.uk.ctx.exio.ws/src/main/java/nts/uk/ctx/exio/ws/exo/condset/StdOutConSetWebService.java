package nts.uk.ctx.exio.ws.exo.condset;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.exio.app.command.exo.condset.ExcuteCopyOutCondSetCommandHandler;
import nts.uk.ctx.exio.app.command.exo.condset.StdOutputCondSetCommand;
import nts.uk.ctx.exio.app.find.exo.condset.StdOutputCondSetFinder;
import nts.uk.ctx.exio.dom.exo.condset.StdOutputCondSet;

@Path("exio/exo/stdoutconset")
@Produces("application/json")
public class StdOutConSetWebService extends WebService{
	
	@Inject
	private StdOutputCondSetFinder stdOutputCondSetFinder;
	
	@Inject
	private ExcuteCopyOutCondSetCommandHandler excuteCopyOutCondSetCommandHandler;
	
	
	@POST
	@Path("getCndSet/{cId}")
	public Optional<StdOutputCondSet> getCndSet(@PathParam("cId") String cId){
		return stdOutputCondSetFinder.getCndSet();
	}
	
	@POST
	@Path("excuteCopy")
	public Map<String, String> ExcuteCopy(StdOutputCondSetCommand command ){
	  return excuteCopyOutCondSetCommandHandler.handle(command);
	}

}
