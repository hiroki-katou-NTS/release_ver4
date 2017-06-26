package nts.uk.ctx.pr.core.ws.rule.employment.unitprice.personal;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.AddPersonalUnitPriceCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.AddPersonalUnitPriceCommandHandler;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.RemovePersonalUnitPriceCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.RemovePersonalUnitPriceCommandHander;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.UpdatePersonalUnitPriceCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal.UpdatePersonalUnitPriceCommandHander;
import nts.uk.ctx.pr.core.app.find.rule.employment.unitprice.personal.PersonalUnitPriceFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.unitprice.personal.dto.PersonalUnitPriceDto;

@Path("pr/core/rule/employment/unitprice/personal")
@Produces("application/json")
public class PersonalUnitPriceWebService extends WebService {
	@Inject
	private PersonalUnitPriceFinder personalUnitPriceFinder;

	@Inject
	private AddPersonalUnitPriceCommandHandler addCommandHandler;

	@Inject
	private UpdatePersonalUnitPriceCommandHander updateCommandHandler;

	@Inject
	private RemovePersonalUnitPriceCommandHander removeCommandHander;

	@POST
	@Path("find/all")
	public List<PersonalUnitPriceDto> findAll() {
		return this.personalUnitPriceFinder.findAll();
	}

	/**
	 * Find all personal unit price
	 * 
	 * @param unitPriceCodeList
	 * 
	 * @return list of unit price code
	 */
	@POST
	@Path("find/all/codes")
	public List<PersonalUnitPriceDto> findAll(List<String> unitPriceCodeList) {
		return this.personalUnitPriceFinder.findAll(unitPriceCodeList);
	}

	/**
	 * Find all personal unit price has property display_set = 1 (none display)
	 * 
	 * @return list of unit price code has display_set = 1
	 */
	@POST
	@Path("find/all/nonedisplay")
	public List<PersonalUnitPriceDto> findAllNoneDisplay() {
		return this.personalUnitPriceFinder.findAll().stream().filter(x -> x.getDisplaySet() == 1)
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@POST
	@Path("find/{code}")
	public PersonalUnitPriceDto find(@PathParam("code") String code) {
		return this.personalUnitPriceFinder.find(code);
	}

	/**
	 * Insert personal unit price
	 * 
	 * @param command
	 */
	@POST
	@Path("add")
	public void add(AddPersonalUnitPriceCommand command) {
		this.addCommandHandler.handle(command);
	}

	/**
	 * Update personal unit price base-on code
	 * 
	 * @param command
	 */
	@POST
	@Path("update")
	public void update(UpdatePersonalUnitPriceCommand command) {
		this.updateCommandHandler.handle(command);
	}

	/**
	 * Remove personal unit price base-on code
	 * 
	 * @param command
	 */
	@POST
	@Path("remove")
	public void remove(RemovePersonalUnitPriceCommand command) {
		this.removeCommandHander.handle(command);
	}
}
