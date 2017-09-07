/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ws.worktype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.app.command.worktype.InsertWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.RemoveWorkTypeCommand;
import nts.uk.ctx.at.shared.app.command.worktype.RemoveWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.UpdateWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.WorkTypeCommandBase;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispOrderCommand;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispOrderCommandHandler;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeDto;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeFinder;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;

/**
 * The Class WorkTypeWebService.
 */
@Path("at/share/worktype")
@Produces("application/json")
public class WorkTypeWebService extends WebService {

	/** The find. */
	@Inject
	private WorkTypeFinder find;

	/** The basic schedule. */
	@Inject
	private BasicScheduleService basicSchedule;

	@Inject
	private InsertWorkTypeCommandHandler insertWorkTypeCommandHandler;

	@Inject
	private RemoveWorkTypeCommandHandler removeWorkTypeCommandHandler;

	@Inject
	private WorkTypeDispOrderCommandHandler workTypeDispOrderCommandHandler;

	@Inject
	private UpdateWorkTypeCommandHandler updateWorkTypeCommandHandler;

	private static final List<Integer> workstyleList = Arrays.asList(WorkStyle.AFTERNOON_WORK.value,
			WorkStyle.MORNING_WORK.value, WorkStyle.ONE_DAY_REST.value, WorkStyle.ONE_DAY_WORK.value);

	/**
	 * Gets the possible work type.
	 *
	 * @param lstPossible
	 *            the lst possible
	 * @return the possible work type
	 */
	@POST
	@Path("getpossibleworktype")
	public List<WorkTypeDto> getPossibleWorkType(List<String> lstPossible) {
		return this.find.getPossibleWorkType(lstPossible);
	}

	/**
	 * Gets the by C id and display atr.
	 *
	 * @return the by C id and display atr
	 */
	@POST
	@Path("getByCIdAndDisplayAtr")
	public List<WorkTypeDto> getByCIdAndDisplayAtr() {
		return this.find.findByCIdAndDisplayAtr();
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<WorkTypeDto> findAll() {
		return this.find.findByCompanyId();
	}

	/**
	 * Find not deprecated.
	 *
	 * @return the list
	 */
	@POST
	@Path("findNotDeprecated")
	public List<WorkTypeDto> findNotDeprecated() {
		return this.find.findNotDeprecated();
	}

	/**
	 * Find not deprecated by list code.
	 *
	 * @param codes
	 *            the codes
	 * @return the list
	 */
	@POST
	@Path("findNotDeprecatedByListCode")
	public List<WorkTypeDto> findNotDeprecatedByListCode(List<String> codes) {
		return this.find.findNotDeprecatedByListCode(codes);
	}

	/**
	 * Find by id.
	 *
	 * @param workTypeCode
	 *            the work type code
	 * @return the work type dto
	 */
	@POST
	@Path("findById/{workTypeCode}")
	public WorkTypeDto findById(@PathParam("workTypeCode") String workTypeCode) {
		return this.find.findById(workTypeCode);
	}

	/**
	 * Find selectable.
	 *
	 * @param workStyleLst
	 *            the work style lst
	 * @return the list
	 */
	@POST
	@Path("findSelectAble")
	public List<String> findSelectable(WorkStyleListDto workStyleLst) {
		// Get WorkTypeCode List
		List<String> worktypeCodeList = this.find.findByCompanyId().stream().map(item -> {
			return item.getWorkTypeCode();
		}).collect(Collectors.toList());

		// Case: input workstyleList is Null
		if (CollectionUtil.isEmpty(workStyleLst.getWorkStyleLst())) {
			return new ArrayList<>();
		}
		// Case: input workstyleList contains full values of enum WorkStyle
		if (workStyleLst.getWorkStyleLst().containsAll(workstyleList)) {
			return worktypeCodeList;
		}
		// Other cases
		List<String> codeList = new ArrayList<>();
		worktypeCodeList.stream().forEach(item -> {
			WorkStyle workstyle = this.basicSchedule.checkWorkDay(item);
			if (workstyleList.contains(workstyle)) {
				codeList.add(item);
			}
		});
		return codeList;

	}

	/**
	 * Find by companyId and languageId in WORK TYPE LANGUAGE
	 * 
	 * @param workTypeCode
	 * @return
	 */
	@POST
	@Path("getByCIdAndLangId/{langId}")
	public List<WorkTypeDto> checkLanguageWorkType(@PathParam("langId") String langId) {
		return this.find.checkLanguageWorkType(langId);
	}

	/**
	 * 
	 * @param Work
	 *            Type
	 * 
	 */
	@POST
	@Path("add")
	public void add(WorkTypeCommandBase command) {
		this.insertWorkTypeCommandHandler.handle(command);
	}

	/**
	 * 
	 * @param command
	 */
	@POST
	@Path("update")
	public void update(WorkTypeCommandBase command) {
		this.updateWorkTypeCommandHandler.handle(command);
	}

	/**
	 * 
	 * @param command
	 */
	@POST
	@Path("remove")
	public void remove(RemoveWorkTypeCommand command) {
		this.removeWorkTypeCommandHandler.handle(command);
	}

	/**
	 * 
	 * @param Work
	 *            Type Order
	 */
	@POST
	@Path("order")
	public void order(List<WorkTypeDispOrderCommand> command) {
		this.workTypeDispOrderCommandHandler.handle(command);
	}
}
