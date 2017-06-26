/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.salarydetail.aggregate;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.command.SalaryAggregateItemSaveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.command.SalaryAggregateItemSaveCommandHandler;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.SalaryAggregateItemFinder;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto.SalaryAggregateItemFindDto;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.find.dto.SalaryAggregateItemInDto;

/**
 * The Class SalaryAggregateItemWs.
 */
@Path("ctx/pr/report/salary/aggregate/item")
@Produces("application/json")
public class SalaryAggregateItemWs extends WebService {

	/** The find. */
	@Inject
	private SalaryAggregateItemFinder finder;

	/** The save. */
	@Inject
	private SalaryAggregateItemSaveCommandHandler handler;

	/**
	 * Find salary aggregate item.
	 *
	 * @param salaryAggregateItemInDto
	 *            the salary aggregate item in dto
	 * @return the salary aggregate item find dto
	 */
	@POST
	@Path("findSalaryAggregateItem")
	public SalaryAggregateItemFindDto findSalaryAggregateItem(
		SalaryAggregateItemInDto salaryAggregateItemInDto) {
		return this.finder.findSalaryAggregateItem(salaryAggregateItemInDto);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<SalaryAggregateItemFindDto> findAll() {
		return this.finder.findAll();
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(SalaryAggregateItemSaveCommand command) {
		this.handler.handle(command);
	}

}
