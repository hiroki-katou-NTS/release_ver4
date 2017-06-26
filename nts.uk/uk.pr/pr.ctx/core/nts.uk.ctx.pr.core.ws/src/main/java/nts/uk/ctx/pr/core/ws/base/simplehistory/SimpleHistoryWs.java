/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.base.simplehistory;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import nts.arc.layer.ws.WebService;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.app.command.base.simplehistory.CreateHistoryCommand;
import nts.uk.ctx.pr.core.app.command.base.simplehistory.DeleteHistoryCommand;
import nts.uk.ctx.pr.core.app.command.base.simplehistory.UpdateStartHistoryCommand;
import nts.uk.ctx.pr.core.dom.base.simplehistory.History;
import nts.uk.ctx.pr.core.dom.base.simplehistory.Master;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.HistoryModel;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.MasterModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SimpleHistoryWs.
 */
public abstract class SimpleHistoryWs<M extends Master, H extends History<H>> extends WebService {

	/**
	 * Load master model list.
	 * Implement your self please.
	 * Just load master code name / history uuid start end.
	 */
	public abstract List<MasterModel> loadMasterModelList();

	/**
	 * Creates the history.
	 *
	 * @param command the command
	 * @return the history model
	 */
	@Path("history/create")
	@POST
	public HistoryModel createHistory(CreateHistoryCommand command) {
		H history;
		if (command.isCopyFromLatest()) {
			history = this.getServices().copyFromLasterHistory(
					AppContexts.user().companyCode(),
					command.getMasterCode(),
					new YearMonth(command.getStartYearMonth()));
		} else {
			history = this.getServices().createHistory(
					AppContexts.user().companyCode(),
					command.getMasterCode(),
					new YearMonth(command.getStartYearMonth()));
		}

		// Ret.
		return HistoryModel.builder()
				.uuid(history.getUuid())
				.start(history.getStart().v())
				.end(history.getEnd().v())
				.build();
	}

	/**
	 * Removes the history.
	 *
	 * @param uuid the uuid
	 */
	@POST
	@Path("history/delete")
	public void removeHistory(DeleteHistoryCommand command) {
		this.getServices().deleteHistory(command.getHistoryId());
	}

	/**
	 * Removes the history.
	 *
	 * @param uuid the uuid
	 */
	@POST
	@Path("history/update/start")
	public void updateHistoryStart(UpdateStartHistoryCommand command) {
		this.getServices().updateHistoryStart(command.getHistoryId(),
				new YearMonth(command.getNewYearMonth()));
	}

	/**
	 * Gets the services.
	 *
	 * @return the services
	 */
	protected abstract SimpleHistoryBaseService<M, H> getServices();
}
