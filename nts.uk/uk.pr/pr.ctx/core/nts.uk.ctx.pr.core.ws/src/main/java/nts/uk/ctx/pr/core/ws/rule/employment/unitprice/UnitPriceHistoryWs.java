/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.rule.employment.unitprice;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.app.rule.employment.unitprice.command.CreateUnitPriceHistoryCommand;
import nts.uk.ctx.pr.core.app.rule.employment.unitprice.command.CreateUnitPriceHistoryCommandHandler;
import nts.uk.ctx.pr.core.app.rule.employment.unitprice.command.UpdateUnitPriceHistoryCommand;
import nts.uk.ctx.pr.core.app.rule.employment.unitprice.command.UpdateUnitPriceHistoryCommandHandler;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceHistoryService;
import nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.HistoryModel;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.MasterModel;
import nts.uk.ctx.pr.core.ws.rule.employment.unitprice.dto.UnitPriceHistoryModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class UnitPriceHistoryWs.
 */
@Path("pr/proto/unitprice")
@Produces(MediaType.APPLICATION_JSON)
public class UnitPriceHistoryWs extends SimpleHistoryWs<UnitPrice, UnitPriceHistory> {

	/** The create unit price history command handler. */
	@Inject
	private CreateUnitPriceHistoryCommandHandler createUnitPriceHistoryCommandHandler;

	/** The update unit price history command handler. */
	@Inject
	private UpdateUnitPriceHistoryCommandHandler updateUnitPriceHistoryCommandHandler;

	/** The unit price repo. */
	@Inject
	private UnitPriceRepository unitPriceRepo;

	/** The unit price history repo. */
	@Inject
	private UnitPriceHistoryRepository unitPriceHistoryRepo;

	/** The service. */
	@Inject
	private UnitPriceHistoryService service;

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the unit price history model
	 */
	@POST
	@Path("find/{id}")
	public UnitPriceHistoryModel find(@PathParam("id") String id) {
		// Get the detail history.
		Optional<UnitPriceHistory> optUnitPriceHistory = this.unitPriceHistoryRepo
				.findHistoryByUuid(id);
		UnitPriceHistoryModel dto = null;

		// Check exsit.
		if (optUnitPriceHistory.isPresent()) {
			UnitPriceHistory unitPrisceHistory = optUnitPriceHistory.get();
			Optional<UnitPrice> optUnitPrice = this.unitPriceRepo.findByCode(
					unitPrisceHistory.getCompanyCode(), unitPrisceHistory.getUnitPriceCode());
			dto = UnitPriceHistoryModel.builder().build();
			unitPrisceHistory.saveToMemento(dto);
			dto.setUnitPriceName(optUnitPrice.get().getName());
		}

		// Return
		return dto;
	}

	/**
	 * Creates the.
	 *
	 * @param command the command
	 * @return the history model
	 */
	@POST
	@Path("create")
	public HistoryModel create(CreateUnitPriceHistoryCommand command) {
		UnitPriceHistory history = this.createUnitPriceHistoryCommandHandler.handle(command);
		return HistoryModel.builder().uuid(history.getUuid()).start(history.getStart().v())
				.end(history.getEnd().v()).build();
	}

	/**
	 * Update.
	 *
	 * @param command the command
	 */
	@POST
	@Path("update")
	public void update(UpdateUnitPriceHistoryCommand command) {
		this.updateUnitPriceHistoryCommandHandler.handle(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs#
	 * loadMasterModelList()
	 */
	@Override
	@Path("masterhistory")
	@POST
	public List<MasterModel> loadMasterModelList() {
		// Get list of unit price.
		List<UnitPrice> unitPrices = this.unitPriceRepo.findAll(AppContexts.user().companyCode());

		// Get list of unit price history.
		List<UnitPriceHistory> unitPriceHistories = this.unitPriceHistoryRepo
				.findAll(new CompanyCode(AppContexts.user().companyCode()));

		// Group histories by unit code.
		Map<UnitPriceCode, List<HistoryModel>> historyMap = unitPriceHistories.stream()
				.collect(Collectors.groupingBy(UnitPriceHistory::getUnitPriceCode,
						Collectors.mapping((res) -> {
							return HistoryModel.builder().uuid(res.getUuid())
									.start(res.getStart().v()).end(res.getEnd().v()).build();
						}, Collectors.toList())));

		// Return
		return unitPrices.stream().map(item -> {
			return MasterModel.builder().code(item.getCode().v()).name(item.getName().v())
					.historyList(historyMap.get(item.getCode())).build();
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs#getServices()
	 */
	@Override
	protected SimpleHistoryBaseService<UnitPrice, UnitPriceHistory> getServices() {
		return this.service;
	}
}
