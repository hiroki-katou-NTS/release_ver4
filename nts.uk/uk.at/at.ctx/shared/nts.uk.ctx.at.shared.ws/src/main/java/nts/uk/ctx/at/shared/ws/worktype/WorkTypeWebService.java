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

import nts.arc.error.BusinessException;
import nts.arc.layer.ws.WebService;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.app.command.worktype.InsertWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.RemoveWorkTypeCommand;
import nts.uk.ctx.at.shared.app.command.worktype.RemoveWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.UpdateWorkTypeCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.WorkTypeCommandBase;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispInitializeOrderCommand;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispInitializeOrderCommandHandler;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispOrderCommand;
import nts.uk.ctx.at.shared.app.command.worktype.worktypedisporder.WorkTypeDispOrderCommandHandler;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeDto;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeFinder;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeInfor;

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

	/** The insert work type command handler. */
	@Inject
	private InsertWorkTypeCommandHandler insertWorkTypeCommandHandler;

	/** The remove work type command handler. */
	@Inject
	private RemoveWorkTypeCommandHandler removeWorkTypeCommandHandler;

	/** The work type disp order command handler. */
	@Inject
	private WorkTypeDispOrderCommandHandler workTypeDispOrderCommandHandler;

	/** The update work type command handler. */
	@Inject
	private UpdateWorkTypeCommandHandler updateWorkTypeCommandHandler;

	/** The work type init disp order command handler. */
	@Inject
	private WorkTypeDispInitializeOrderCommandHandler workTypeDispInitializeOrderCommandHandler;

	/** The Constant workstyleList. */
	private static final List<Integer> workstyleList = Arrays.asList(WorkStyle.AFTERNOON_WORK.value,
			WorkStyle.MORNING_WORK.value, WorkStyle.ONE_DAY_REST.value, WorkStyle.ONE_DAY_WORK.value);

	/**
	 * Gets the possible work type.
	 *
	 * @param lstPossible the lst possible
	 * @return the possible work type
	 */
	@POST
	@Path("getpossibleworktype")
	public List<WorkTypeInfor> getPossibleWorkType(List<String> lstPossible) {
		return this.find.getPossibleWorkType(lstPossible);
	}
	
	/**
	 * Gets the possible work type with no master.
	 *
	 * @param lstPossible the lst possible
	 * @return the possible work type
	 */
	@POST
	@Path("get_possible_work_type_with_no_master")
	public List<WorkTypeInfor> getPossibleWorkTypewithNoMaster(List<String> lstPossible) {
		return this.find.getPossibleWorkTypeWithNoMaster(lstPossible);
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
	 * Find all by order.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAllByOrder")
	public List<WorkTypeInfor> findAllByOrder() {
		return this.find.findAllByOrder();
	}
	
	/**
	 * Find work type by condition.
	 *
	 * @return the list
	 */
	@POST
	@Path("findWorkTypeByCondition")
	public List<WorkTypeDto> findWorkTypeByCondition() {
		List<WorkTypeDto> dto = this.find.findWorkTypeByCondition();
		
		if (!CollectionUtil.isEmpty(dto)){
			return dto;
		} else {
			throw new BusinessException("Msg_986");
		}
	}

	/**
	 * Find not deprecated by list code.
	 *
	 * @param codes the codes
	 * @return the list
	 */
	@POST
	@Path("findNotDeprecatedByListCode")
	public List<WorkTypeDto> findNotDeprecatedByListCode(List<String> codes) {
		return this.find.findNotDeprecatedByListCode(codes);
	}

	/**
	 * Find by code.
	 *
	 * @param workTypeCode the work type code
	 * @return the work type dto
	 */
	@POST
	@Path("findById/{workTypeCode}")
	public WorkTypeDto findByCode(@PathParam("workTypeCode") String workTypeCode) {
		return this.find.findByCode(workTypeCode);
	}

	/**
	 * Find selectable.
	 *
	 * @param workStyleLst the work style lst
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
			if (workstyleList.contains(workstyle.value)) {
				codeList.add(item);
			}
		});
		return codeList;

	}

	/**
	 * Find work type language.
	 *
	 * @param langId the lang id
	 * @return the list
	 */
	@POST
	@Path("getByCIdAndLangId/{langId}")
	public List<WorkTypeDto> findWorkTypeLanguage(@PathParam("langId") String langId) {
		return this.find.findWorkTypeLanguage(langId);
	}

	/**
	 * Adds the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("add")
	public void add(WorkTypeCommandBase command) {
		this.insertWorkTypeCommandHandler.handle(command);
	}

	/**
	 * Update.
	 *
	 * @param command the command
	 */
	@POST
	@Path("update")
	public void update(WorkTypeCommandBase command) {
		this.updateWorkTypeCommandHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(RemoveWorkTypeCommand command) {
		this.removeWorkTypeCommandHandler.handle(command);
	}

	/**
	 * Order.
	 *
	 * @param command the command
	 */
	@POST
	@Path("order")
	public void order(List<WorkTypeDispOrderCommand> command) {
		this.workTypeDispOrderCommandHandler.handle(command);
	}
	
	/**
	 * initialize
	 *
	 * @param command the command
	 */
	@POST
	@Path("initializeOrder")
	public List<WorkTypeDto> initializeOrder(WorkTypeDispInitializeOrderCommand command) {
		return this.workTypeDispInitializeOrderCommandHandler.handle(command);
	}
	
	@POST
	@Path("getpossiblewktypeKDL002")
	public List<WorkTypeInfor> getPossibleWkTypeKDL002(List<String> lstPossible) {
		return this.find.getPossibleWorkTypeKDL002(lstPossible);
	}
}
