/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.social;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.DeleteSocialOfficeCommand;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.DeleteSocialOfficeCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.RegisterSocialOfficeCommand;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.RegisterSocialOfficeCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.UpdateSocialOfficeCommand;
import nts.uk.ctx.pr.core.app.insurance.social.office.command.UpdateSocialOfficeCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.office.find.SocialInsuranceOfficeDto;
import nts.uk.ctx.pr.core.app.insurance.social.office.find.SocialInsuranceOfficeFinder;
import nts.uk.ctx.pr.core.app.insurance.social.office.find.SocialInsuranceOfficeItemDto;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SocialInsuranceOfficeWs.
 */
@Path("pr/insurance/social")
@Produces("application/json")
@Stateless
public class SocialInsuranceOfficeWs extends WebService {

	/** The register social office command handler. */
	@Inject
	private RegisterSocialOfficeCommandHandler registerSocialOfficeCommandHandler;

	/** The update social office command handler. */
	@Inject
	private UpdateSocialOfficeCommandHandler updateSocialOfficeCommandHandler;

	/** The delete social office command handler. */
	@Inject
	private DeleteSocialOfficeCommandHandler deleteSocialOfficeCommandHandler;

	/** The social insurance office finder. */
	@Inject
	private SocialInsuranceOfficeFinder socialInsuranceOfficeFinder;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<SocialInsuranceOfficeItemDto> findAll() {
		// Get the current company code
		String companyCode = AppContexts.user().companyCode();

		// Find all office
		return socialInsuranceOfficeFinder.findAll(companyCode);
	}

	/**
	 * Find all detail.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall/detail")
	public List<SocialInsuranceOfficeDto> findAllDetail() {
		// Get the current company code
		String companyCode = AppContexts.user().companyCode();

		// Return office detail
		return socialInsuranceOfficeFinder.findAllDetail(companyCode);
	}

	/**
	 * Find office.
	 *
	 * @param officeCode
	 *            the office code
	 * @return the social insurance office dto
	 */
	@POST
	@Path("find/{officeCode}")
	public SocialInsuranceOfficeDto findOffice(@PathParam("officeCode") String officeCode) {
		// Get the current company code
		String companyCode = AppContexts.user().companyCode();

		// Return office info
		return socialInsuranceOfficeFinder.find(companyCode, new OfficeCode(officeCode)).get();
	}

	/**
	 * Find rounding.
	 */
	@POST
	@Path("find/rounding")
	public void findRounding() {
		// TODO convert class RoundingMethod to values and return
	}

	/**
	 * Creates the office.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("create")
	public void createOffice(RegisterSocialOfficeCommand command) {
		this.registerSocialOfficeCommandHandler.handle(command);
	}

	/**
	 * Update office.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void updateOffice(UpdateSocialOfficeCommand command) {
		this.updateSocialOfficeCommandHandler.handle(command);
	}

	/**
	 * Removes the office.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("remove")
	public void removeOffice(DeleteSocialOfficeCommand command) {
		this.deleteSocialOfficeCommandHandler.handle(command);
	}
}
