/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.social.healthrate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.insurance.social.healthrate.command.DeleteHealthInsuRateCommand;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.command.DeleteHealthInsuRateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.command.UpdateHealthInsuRateCommand;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.command.UpdateHealthInsuRateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.find.HealthInsuranceOfficeItemDto;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.find.HealthInsuranceRateDto;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.find.HealthInsuranceRateFinder;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service.HealthInsuranceRateService;
import nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.HistoryModel;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.MasterModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class HealthInsuranceRateWs.
 */
@Path("ctx/pr/core/insurance/social/healthrate")
@Produces("application/json")
public class HealthInsuranceRateWs
		extends SimpleHistoryWs<SocialInsuranceOffice, HealthInsuranceRate> {

	/** The health insurance rate finder. */
	@Inject
	private HealthInsuranceRateFinder healthInsuranceRateFinder;

	/** The update health insurance command handler. */
	@Inject
	private UpdateHealthInsuRateCommandHandler updateHealthInsuranceCommandHandler;

	/** The delete health insurance command handler. */
	@Inject
	private DeleteHealthInsuRateCommandHandler deleteHealthInsuranceCommandHandler;

	/** The social insurance office repository. */
	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	/** The health insurance rate repository. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepository;

	/** The service. */
	@Inject
	private HealthInsuranceRateService service;

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the health insurance rate dto
	 */
	// find by historyId
	@POST
	@Path("find/{id}")
	public HealthInsuranceRateDto find(@PathParam("id") String id) {
		return healthInsuranceRateFinder.find(id).get();
	}

	/**
	 * Findby code.
	 *
	 * @return the list
	 */
	// find All by OfficeCode
	@POST
	@Path("findAllHistory")
	public List<HealthInsuranceOfficeItemDto> findbyCode() {
		return healthInsuranceRateFinder.findAllHistory();
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(UpdateHealthInsuRateCommand command) {
		updateHealthInsuranceCommandHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command
	 *            the command
	 */
	// remove by historyId
	@POST
	@Path("remove")
	public void remove(DeleteHealthInsuRateCommand command) {
		deleteHealthInsuranceCommandHandler.handle(command);
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
		// Get list of Office.
		List<SocialInsuranceOffice> socialInsuranceOffices = this.socialInsuranceOfficeRepository
				.findAll(AppContexts.user().companyCode());

		// Get list of unit price history.
		List<HealthInsuranceRate> healthInsuranceRates = this.healthInsuranceRateRepository
				.findAll(AppContexts.user().companyCode());

		// Group histories by unit code.
		Map<OfficeCode, List<HistoryModel>> historyMap = healthInsuranceRates.stream()
				.collect(Collectors.groupingBy(HealthInsuranceRate::getOfficeCode,
						Collectors.mapping((res) -> {
							return HistoryModel.builder().uuid(res.getUuid())
									.start(res.getStart().v()).end(res.getEnd().v()).build();
						}, Collectors.toList())));

		// Return
		return socialInsuranceOffices.stream().map(item -> {
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
	protected SimpleHistoryBaseService<SocialInsuranceOffice, HealthInsuranceRate> getServices() {
		return this.service;
	}
}
