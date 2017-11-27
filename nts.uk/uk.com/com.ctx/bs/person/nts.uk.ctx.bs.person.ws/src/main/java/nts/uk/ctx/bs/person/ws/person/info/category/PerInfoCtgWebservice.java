package nts.uk.ctx.bs.person.ws.person.info.category;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import command.person.info.category.AddPerInfoCtgCommand;
import command.person.info.category.AddPerInfoCtgCommandHandler;
import command.person.info.category.UpdatePerInfoCtgCommand;
import command.person.info.category.UpdatePerInfoCtgCommandHandler;
import find.person.info.category.PerInfoCategoryFinder;
import find.person.info.category.PerInfoCtgDataEnumDto;
import find.person.info.category.PerInfoCtgFullDto;
import find.person.info.category.PerInfoCtgWithItemsNameDto;
import find.person.info.category.PerInfoCtgWithParentMapDto;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/info/category")
@Produces("application/json")
public class PerInfoCtgWebservice extends WebService {
	@Inject
	private PerInfoCategoryFinder perInfoCtgFinder;

	@Inject
	private AddPerInfoCtgCommandHandler addPerInfoCtgCommand;

	@Inject
	private UpdatePerInfoCtgCommandHandler updatePerInfoCtgCommand;


	
	@POST
	@Path("findAll")
	public List<PerInfoCtgFullDto> getAllPerInfoCtg() {
		return perInfoCtgFinder.getAllPerInfoCtg();
	}

	@POST
	@Path("find/companyby/{Id}")
	public PerInfoCtgFullDto getPerInfoCtg(@PathParam("Id") String id) {
		return perInfoCtgFinder.getPerInfoCtg(id);
	}

	@POST
	@Path("find/withItemsName/{Id}")
	public PerInfoCtgWithItemsNameDto getPerInfoCtgWithItemsName(@PathParam("Id") String id) {
		return perInfoCtgFinder.getPerInfoCtgWithItemsName(id);
	}
	
	//vinhpx: start
	
	
	@POST
	@Path("find/getPerInfoWithParent")
	public List<PerInfoCtgWithParentMapDto> getPerInfoWithParent(String parentCd){
		return perInfoCtgFinder.getPerInfoCtgWithParent(parentCd);
	}
	
	//vinhpx: end

	@POST
	@Path("findby/company")
	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompany() {
		return perInfoCtgFinder.getAllPerInfoCtgByCompany();
	}
	
	@POST
	@Path("findAll/company/root")
	public PerInfoCtgDataEnumDto getAllPerInfoCtgByCompanyRoot() {
		return perInfoCtgFinder.getAllPerInfoCtgByCompanyRoot();
	}
	
	@POST
	@Path("add")
	public void addPerInfoCtg(AddPerInfoCtgCommand newPerInfoCtg) {
		addPerInfoCtgCommand.handle(newPerInfoCtg);
	}

	@POST
	@Path("update")
	public void updatePerInfoCtg(UpdatePerInfoCtgCommand newPerInfoCtg) {
		updatePerInfoCtgCommand.handle(newPerInfoCtg);
	}
}
