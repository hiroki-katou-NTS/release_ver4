package nts.uk.ctx.at.record.ws.divergence.time.message;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.record.app.command.divergence.time.message.DivergenceTimeErrorAlarmMessageCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.message.DivergenceTimeErrorAlarmMessageSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeSettingFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.message.DivergenceTimeErrorAlarmMessageDto;
import nts.uk.ctx.at.record.app.find.divergence.time.message.DivergenceTimeErrorAlarmMessageFinder;

/**
 * The Class DivergenceTimeErrorAlarmMessageWs.
 */
@Path("at/record/divergence/time/message/divergenceTimeErrAlarmMsg")
@Produces("application/json")
public class DivergenceTimeErrorAlarmMessageWs {

	/** The divergence time setting finder. */
	@Inject
	private DivergenceTimeSettingFinder divergenceTimeSettingFinder;

	/** The save command. */
	@Inject
	private DivergenceTimeErrorAlarmMessageSaveCommandHandler saveCommand;

	/** The finder. */
	@Inject
	private DivergenceTimeErrorAlarmMessageFinder finder;

	/**
	 * Gets the all div time.
	 *
	 * @return the all div time
	 */
	@POST
	@Path("getAllDivTime")
	public List<DivergenceTimeDto> getAllDivTime() {
		return this.divergenceTimeSettingFinder.getAllDivTime();
	}

	/**
	 * Find by divergence time no.
	 *
	 * @param divergenceTimeNo
	 *            the divergence time no
	 * @return the divergence time error alarm message dto
	 */
	@POST
	@Path("findByDivergenceTimeNo/{divergenceTimeNo}")
	public DivergenceTimeErrorAlarmMessageDto findByDivergenceTimeNo(
			@PathParam("divergenceTimeNo") Integer divergenceTimeNo) {
		return this.finder.findByDivergenceTimeNo(divergenceTimeNo);
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(DivergenceTimeErrorAlarmMessageCommand command) {
		this.saveCommand.handle(command);
	}
}
