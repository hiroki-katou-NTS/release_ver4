package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.init;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.NewAppCommonSetService;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DetailAppCommonSetImpl implements DetailAppCommonSetService {

	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private NewAppCommonSetService newAppCommonSetService;
	
	@Override
	public ApplicationMetaOutput getDetailAppCommonSet(String companyID, String applicationID) {
		Optional<Application_New> opApplication = applicationRepository.findByID(companyID, applicationID);
		return new ApplicationMetaOutput(
				opApplication.get().getAppID(),
				opApplication.get().getAppType(), 
				opApplication.get().getAppDate());
	}

}
