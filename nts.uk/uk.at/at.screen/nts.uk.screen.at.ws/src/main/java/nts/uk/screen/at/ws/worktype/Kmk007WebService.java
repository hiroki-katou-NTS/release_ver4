package nts.uk.screen.at.ws.worktype;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.screen.at.app.worktype.WorkTypeDto;
import nts.uk.screen.at.app.worktype.WorkTypeProcessor;

/**
 * The Class Work Type Query Web Service.
 * 
 * @author sonnh
 *
 */
@Path("at/screen/worktype")
@Produces("application/json")
public class Kmk007WebService {
	
	@Inject
	private WorkTypeProcessor workTypeProcessor;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<WorkTypeDto> findAll(){
		return workTypeProcessor.findWorkTypeAll();
	}
	
	/**
	 * Find work type by 分類.
	 *
	 * @return the list
	 */
	@POST
	@Path("find/dailyworktypea")
	public List<WorkTypeDto> findByDailyWorkType(DailyWorkTypeAtrParam param){
		return workTypeProcessor.findWorkTypeByDailyWorkType(param.getDailyWorkTypeAtr());
	}

}
