/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ws.workrule.closure;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.shared.app.command.workrule.closure.ClosureSaveCommand;
import nts.uk.ctx.at.shared.app.command.workrule.closure.ClosureSaveCommandHandler;
import nts.uk.ctx.at.shared.app.find.workrule.closure.ClosureFinder;
import nts.uk.ctx.at.shared.app.find.workrule.closure.CurrentClosureFinder;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.CheckSaveDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureDetailDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureEmployDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureFindDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureForLogDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureHistoryInDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.CurrentClosureDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.DayMonthChangeDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.DayMonthChangeInDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.DayMonthDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.DayMonthInDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.DayMonthOutDto;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureGetMonthDay;
import nts.uk.ctx.at.shared.dom.workrule.closure.DayMonthChange;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ClosureWs.
 */
@Path("ctx/at/shared/workrule/closure")
@Produces("application/json")
public class ClosureWs {
	
	/** The finder. */
	@Inject
	private ClosureFinder finder;
	
	/** The save. */
	@Inject
	private ClosureSaveCommandHandler save;
	
	
	/** The current closure finder. */
	@Inject
	private CurrentClosureFinder currentClosureFinder;
	
	/** The Constant CLOSURE_ID_BEGIN. */
	public static final int CLOSURE_ID_BEGIN = 1;
	
	/** The Constant THREE_MONTH. */
	public static final int THREE_MONTH = 3;
	
	/** The Constant TOTAL_MONTH_OF_YEAR. */
	public static final int TOTAL_MONTH_OF_YEAR = 12;
	
	@POST
	@Path("getClosureEmploy/{referDate}")
	public ClosureEmployDto getClosureEmploy(@PathParam("referDate") int referDate){
		return this.finder.getClosureEmploy(referDate);
	}
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<ClosureFindDto> findAll(){
		return this.finder.findAll();
	}
	/**
	 * Find all for log
	 *
	 * @return the list
	 */
	@POST
	@Path("findallforlog")
	public List<ClosureForLogDto> findAllForLog(){
		return this.finder.findAllForLog();
	}
	
	
	/**
	 * Find by id.
	 *
	 * @param closureId the closure id
	 * @return the closure find dto
	 */
	@POST
	@Path("findById/{closureId}")
	public ClosureFindDto findById(@PathParam("closureId") int closureId){
		return this.finder.findById(closureId);
	}
	
	/**
	 * Find period by id.
	 *
	 * @param closureId the closure id
	 * @return the period
	 */
	@POST
	@Path("findPeriodById/{closureId}")
	public DayMonthOutDto findPeriodById(@PathParam("closureId") int closureId) {
		return new DayMonthOutDto(this.finder.findByIdGetMonthDay(closureId));
	}
	
	/**
	 * Check three month.
	 *
	 * @param baseDate the base date
	 * @return the boolean
	 */
	@POST
	@Path("checkThreeMonth")
	public Boolean checkThreeMonth(CheckSaveDto checksave) {
		DatePeriod period = this.finder.findByIdGetMonthDay(CLOSURE_ID_BEGIN);
		return (period.start().yearMonth().v() + THREE_MONTH < checksave.getBaseDate().yearMonth()
				.v());
	}
	
	
	/**
	 * Check month max.
	 *
	 * @param baseDate the base date
	 * @return the boolean
	 */
	@POST
	@Path("checkMonthMax")
	public Boolean checkMonthMax(CheckSaveDto checksave) {
		return checksave.getBaseDate().before(this.finder.getMaxStartDateClosure());
	}
	
	
	/**
	 * Find by master.
	 *
	 * @param master the master
	 * @return the closure detail dto
	 */
	@POST
	@Path("findByMaster")
	public ClosureDetailDto findByMaster(ClosureHistoryInDto master){
		return this.finder.findByMaster(master);
	}
	
	
	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(ClosureSaveCommand command){
		this.save.handle(command);
	}
	
	/**
	 * Gets the day.
	 *
	 * @param input the input
	 * @return the day
	 */
	@POST
	@Path("getday")
	public DayMonthDto getDay(DayMonthInDto input){
		ClosureGetMonthDay closureGetMonthDay = new ClosureGetMonthDay();
		DatePeriod period = closureGetMonthDay.getDayMonth(
				new ClosureDate(input.getClosureDate(), input.getClosureDate() == 0),
				input.getMonth());
		DayMonthDto dto = new DayMonthDto();
		dto.setBeginDay(period.start().toString());
		dto.setEndDay(period.end().toString());
		return dto;
	}
	/**
	 * Gets the day.
	 *
	 * @param input the input
	 * @return the day
	 */
	@POST
	@Path("getdaychange")
	public DayMonthChangeDto getDayChange(DayMonthChangeInDto input) {
		ClosureGetMonthDay closureGetMonthDay = new ClosureGetMonthDay();
		DayMonthChange dayMonthChange = closureGetMonthDay.getDayMonthChange(
				new ClosureDate(input.getClosureDate(), input.getClosureDate() == 0),
				new ClosureDate(input.getChangeClosureDate(), input.getChangeClosureDate() == 0),
				input.getMonth());
		DayMonthChangeDto dto = new DayMonthChangeDto();
		DayMonthDto beforeClosureDate = new DayMonthDto();
		DayMonthDto afterClosureDate = new DayMonthDto();

		beforeClosureDate.setBeginDay(dayMonthChange.getBeforeClosureDate().start().toString());
		beforeClosureDate.setEndDay(dayMonthChange.getBeforeClosureDate().end().toString());

		afterClosureDate.setBeginDay(dayMonthChange.getAfterClosureDate().start().toString());
		afterClosureDate.setEndDay(dayMonthChange.getAfterClosureDate().end().toString());
		dto.setBeforeClosureDate(beforeClosureDate);
		dto.setAfterClosureDate(afterClosureDate);
		return dto;
	}
	
	@POST
	@Path("findCurrentClosure")
	public List<CurrentClosureDto> findStartEndDate(){
		return this.currentClosureFinder.findCurrentClosure();
	}
	
}
