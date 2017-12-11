package nts.uk.ctx.pereg.ws.layout;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pereg.app.find.layout.GetLayoutByCeateTypeDto;
import nts.uk.ctx.pereg.app.find.layout.RegisterLayoutFinder;
import nts.uk.ctx.pereg.app.find.layout.dto.EmpMaintLayoutDto;
import nts.uk.ctx.pereg.app.find.layoutdef.NewLayoutDto;
import nts.uk.ctx.pereg.app.find.person.category.PerInfoCtgFullDto;
import nts.uk.ctx.pereg.app.find.processor.PeregProcessor;
import nts.uk.shr.pereg.app.find.PeregQuery;

/**
 * @author sonnlb
 *
 */
@Path("ctx/pereg/layout")
@Produces(MediaType.APPLICATION_JSON)
public class LayoutWebService extends WebService {

	@Inject
	private RegisterLayoutFinder layoutFinder;
	
	@Inject
	private PeregProcessor layoutProcessor;

	@Path("getByCreateType")
	@POST
	public NewLayoutDto getByCreateType(GetLayoutByCeateTypeDto command) {
		return this.layoutFinder.getByCreateType(command);
	}
	
	/**
	 * get category and it's children
	 * @author xuan vinh
	 * 
	 * @param ctgId
	 * @return
	 */
	
	@Path("find/getctgtab/{categoryid}")
	@POST
	public List<PerInfoCtgFullDto> getCtgTab(@PathParam("categoryid")String ctgId){
		return this.layoutProcessor.getCtgTab(ctgId);
	}
	
	/**
	 * @author xuan vinh
	 * @param query
	 * @return
	 */
	
	@Path("find/gettabdetail")
	@POST
	public EmpMaintLayoutDto getTabDetail(PeregQuery query){
		return this.layoutProcessor.getCategoryChild(query);
	}
	
	/**
	 * @author xuan vinh
	 * @param query
	 * @return
	 */
	
	@Path("find/gettabsubdetail")
	@POST
	public EmpMaintLayoutDto getTabSubDetail(PeregQuery query){
		return this.layoutProcessor.getSubDetailInCtgChild(query);
	}
	
	
}
