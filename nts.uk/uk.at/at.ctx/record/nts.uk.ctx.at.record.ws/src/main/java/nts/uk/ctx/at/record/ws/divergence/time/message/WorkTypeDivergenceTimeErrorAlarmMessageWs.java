package nts.uk.ctx.at.record.ws.divergence.time.message;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.record.app.command.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTimeSettingFinder;
import nts.uk.ctx.at.record.app.find.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageDto;
import nts.uk.ctx.at.record.app.find.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageFinder;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessageWs.
 */
@Path("at/record/divergence/time/message/workTypeDivergenceTimeErrAlarmMsg")
@Produces("application/json")
public class WorkTypeDivergenceTimeErrorAlarmMessageWs {

	/** The divergence time setting finder. */
	@Inject
	private DivergenceTimeSettingFinder divergenceTimeSettingFinder;

	/** The WorkType Divergence Time Error Alarm Message finder. */
	@Inject
	private WorkTypeDivergenceTimeErrorAlarmMessageFinder finder;

	/** The save command. */
	@Inject
	private WorkTypeDivergenceTimeErrorAlarmMessageSaveCommandHandler saveCommand;

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
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type divergence time error alarm message dto
	 */
	@POST
	@Path("findByWorkTypeDivergenceTimeNo/{divergenceTimeNo}/{workTypeCode}")
	public WorkTypeDivergenceTimeErrorAlarmMessageDto findByDivergenceTimeNo(
			@PathParam("divergenceTimeNo") Integer divergenceTimeNo,
			@PathParam("workTypeCode") BusinessTypeCode workTypeCode) {
		return this.finder.findByWorkTypeDivTimeErrAlarmMsg(divergenceTimeNo, workTypeCode);
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(WorkTypeDivergenceTimeErrorAlarmMessageCommand command) {
		this.saveCommand.handle(command);
	}
}
