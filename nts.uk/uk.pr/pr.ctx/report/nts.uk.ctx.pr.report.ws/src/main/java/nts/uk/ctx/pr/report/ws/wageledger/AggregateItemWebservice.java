/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.wageledger;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.wageledger.command.AggregateItemRemoveCommand;
import nts.uk.ctx.pr.report.app.wageledger.command.AggregateItemRemoveCommandHandler;
import nts.uk.ctx.pr.report.app.wageledger.command.AggregateItemSaveCommand;
import nts.uk.ctx.pr.report.app.wageledger.command.AggregateItemSaveCommandHandler;
import nts.uk.ctx.pr.report.app.wageledger.command.dto.ItemSubjectDto;
import nts.uk.ctx.pr.report.app.wageledger.find.AggregateItemFinder;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.AggregateItemDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.HeaderSettingDto;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Class AggregateItemWebservice.
 */
@Path("ctx/pr/report/wageledger/aggregateitem")
@Produces("application/json")
public class AggregateItemWebservice extends WebService {

	/** The finder. */
	@Inject
	private AggregateItemFinder finder;

	/** The save command handler. */
	@Inject
	private AggregateItemSaveCommandHandler saveCommandHandler;

	/** The remove command handler. */
	@Inject
	private AggregateItemRemoveCommandHandler removeCommandHandler;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<HeaderSettingDto> findAll() {
		return this.finder.findAll();
	}

	/**
	 * Find aggregate item detail.
	 *
	 * @param subject the subject
	 * @return the aggregate item dto
	 */
	@POST
	@Path("findBySubject")
	public AggregateItemDto findAggregateItemDetail(ItemSubjectDto subject) {
		return this.finder.findDetail(subject);
	}

	/**
	 * Find by category and payment type.
	 *
	 * @param category the category
	 * @param paymentType the payment type
	 * @return the list
	 */
	@POST
	@Path("findByCate/{category}/{paymentType}")
	public List<HeaderSettingDto> findByCategoryAndPaymentType(@PathParam("category") WLCategory category,
			@PathParam("paymentType") PaymentType paymentType) {
		return this.finder.findByCategoryAndPaymentType(category, paymentType);
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(AggregateItemSaveCommand command) {
		this.saveCommandHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(AggregateItemRemoveCommand command) {
		this.removeCommandHandler.handle(command);
	}
}
