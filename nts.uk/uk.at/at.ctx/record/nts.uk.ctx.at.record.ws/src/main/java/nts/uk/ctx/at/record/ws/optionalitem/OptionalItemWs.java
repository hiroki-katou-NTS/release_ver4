/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.ws.optionalitem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.optitem.OptionalItemSaveCommand;
import nts.uk.ctx.at.record.app.command.optitem.OptionalItemSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.optitem.OptItemEnumDto;
import nts.uk.ctx.at.record.app.find.optitem.OptionalItemDto;
import nts.uk.ctx.at.record.app.find.optitem.OptionalItemFinder;
import nts.uk.ctx.at.record.app.find.optitem.OptionalItemHeaderDto;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

/**
 * The Class OptionalItemWs.
 */
@Path("ctx/at/record/optionalitem/")
@Produces("application/json")
public class OptionalItemWs extends WebService {

	/** The finder. */
	@Inject
	private OptionalItemFinder finder;

	/** The save. */
	@Inject
	private OptionalItemSaveCommandHandler handler;

	/** The i 18 n. */
	@Inject
	private I18NResourcesForUK i18n;

	/**
	 * Find.
	 *
	 * @return the optional item dto
	 */
	@POST
	@Path("find/{itemNo}")
	public OptionalItemDto find(@PathParam("itemNo") Integer itemNo) {
		return this.finder.find(itemNo);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<OptionalItemHeaderDto> findAll() {
		return this.finder.findAll();
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(OptionalItemSaveCommand command) {
		this.handler.handle(command);
	}

	/**
	 * Gets the enum.
	 *
	 * @return the enum
	 */
	@POST
	@Path("getenum")
	public OptItemEnumDto getEnum() {
		return OptItemEnumDto.init(i18n);
	}

}
