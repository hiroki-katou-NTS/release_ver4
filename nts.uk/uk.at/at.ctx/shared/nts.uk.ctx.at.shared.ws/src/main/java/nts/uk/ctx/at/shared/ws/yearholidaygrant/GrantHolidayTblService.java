package nts.uk.ctx.at.shared.ws.yearholidaygrant;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.command.yearholidaygrant.GrantHolidayTblAddCommandHandler;
import nts.uk.ctx.at.shared.app.command.yearholidaygrant.GrantHolidayTblCommand;
import nts.uk.ctx.at.shared.app.command.yearholidaygrant.GrantHolidayTblDeleteCommand;
import nts.uk.ctx.at.shared.app.command.yearholidaygrant.GrantHolidayTblDeleteCommandHandler;
import nts.uk.ctx.at.shared.app.command.yearholidaygrant.GrantHolidayTblUpdateCommandHandler;
import nts.uk.ctx.at.shared.app.find.yearholidaygrant.GrantHolidayTblDto;
import nts.uk.ctx.at.shared.app.find.yearholidaygrant.GrantHolidayTblFinder;

/**
 * The Class YearHolidayGrantService.
 */
@Path("at/share/grantholidaytbl")
@Produces("application/json")
public class GrantHolidayTblService extends WebService {
	/** The find command handler. */
	@Inject
	private GrantHolidayTblFinder find;
	
	/** The add command handler. */
	@Inject
	private GrantHolidayTblAddCommandHandler add;
	
	/** The update command handler. */
	@Inject
	private GrantHolidayTblUpdateCommandHandler update;
	
	/** The delete command handler. */
	@Inject
	private GrantHolidayTblDeleteCommandHandler delete;
	
	/**
	 * Find by codes.
	 *
	 * @param conditionNo the condition No
	 * @param yearHolidayCode the year Holiday Code
	 * @return the GrantHolidayTblDto
	 */
	@POST
	@Path("findByCode/{conditionNo}{yearHolidayCode}")
	public List<GrantHolidayTblDto> findByCode(@PathParam("conditionNo") int conditionNo, @PathParam("yearHolidayCode") String yearHolidayCode){
		return this.find.findByCode(conditionNo, yearHolidayCode);
	}
	
	/**
	 * Add new GrantHolidayTblDto.
	 *
	 * @param command the command
	 */
	@POST
	@Path("add")
	public void addYearHolidayGrant(GrantHolidayTblCommand command) {
		add.handle(command);
	}
	
	/**
	 * Update GrantHolidayTblDto.
	 *
	 * @param command the command
	 */
	@POST
	@Path("update")
	public void updateYearHolidayGrant(GrantHolidayTblCommand command) {
		update.handle(command);
	}
	
	/**
	 * Delete GrantHolidayTblDto.
	 *
	 * @param command the command
	 */
	@POST
	@Path("delete")
	public void deleteYearHolidayGrant(List<GrantHolidayTblDeleteCommand> command) {
		delete.handle(command);
	}
}
