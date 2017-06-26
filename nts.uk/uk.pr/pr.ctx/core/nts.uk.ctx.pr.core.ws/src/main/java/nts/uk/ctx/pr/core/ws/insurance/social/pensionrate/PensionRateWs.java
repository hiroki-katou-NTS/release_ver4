/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.social.pensionrate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command.DeletePensionRateCommand;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command.DeletePensionRateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command.UpdatePensionRateCommand;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command.UpdatePensionRateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find.PensionOfficeItemDto;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find.PensionRateDto;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find.PensionRateFinder;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.service.PensionRateService;
import nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.HistoryModel;
import nts.uk.ctx.pr.core.ws.base.simplehistory.dto.MasterModel;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class PensionRateWs.
 */
@Path("ctx/pr/core/insurance/social/pensionrate")
@Produces("application/json")
public class PensionRateWs extends SimpleHistoryWs<SocialInsuranceOffice, PensionRate> {

	/** The pension rate finder. */
	@Inject
	private PensionRateFinder pensionRateFinder;

	/** The update pension command handler. */
	@Inject
	private UpdatePensionRateCommandHandler updatePensionCommandHandler;

	/** The delete pension command handler. */
	@Inject
	private DeletePensionRateCommandHandler deletePensionCommandHandler;

	/** The social insurance office repository. */
	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	/** The pension rate repository. */
	@Inject
	private PensionRateRepository pensionRateRepository;

	/** The service. */
	@Inject
	private PensionRateService service;

	/**
	 * Find.
	 *
	 * @param id the id
	 * @return the pension rate dto
	 */
	@POST
	@Path("find/{id}")
	public PensionRateDto find(@PathParam("id") String id) {
		return pensionRateFinder.find(id).get();
	}

	/**
	 * Findby code.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAllHistory")
	public List<PensionOfficeItemDto> findbyCode() {
		return pensionRateFinder.findAllHistory();
	}

	/**
	 * Update.
	 *
	 * @param command the command
	 */
	@POST
	@Path("update")
	public void update(UpdatePensionRateCommand command) {
		updatePensionCommandHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(DeletePensionRateCommand command) {
		deletePensionCommandHandler.handle(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.ws.base.simplehistory.SimpleHistoryWs#
	 * loadMasterModelList()
	 */
	@POST
	@Path("masterhistory")
	@Override
	public List<MasterModel> loadMasterModelList() {
		// Get list of Office.
		List<SocialInsuranceOffice> socialInsuranceOffices = this.socialInsuranceOfficeRepository
				.findAll(AppContexts.user().companyCode());

		// Get list of unit price history.
		List<PensionRate> pensionRates = this.pensionRateRepository
				.findAll(AppContexts.user().companyCode());

		// Group histories by unit code.
		Map<OfficeCode, List<HistoryModel>> historyMap = pensionRates.stream().collect(
				Collectors.groupingBy(PensionRate::getOfficeCode, Collectors.mapping((res) -> {
					return HistoryModel.builder().uuid(res.getUuid()).start(res.getStart().v())
							.end(res.getEnd().v()).build();
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
	protected SimpleHistoryBaseService<SocialInsuranceOffice, PensionRate> getServices() {
		return this.service;
	}
}
