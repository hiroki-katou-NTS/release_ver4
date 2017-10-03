package nts.uk.ctx.at.request.app.find.application.common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationPeriodDto;
import nts.uk.ctx.at.request.dom.application.common.ApplicationRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApplicationFinder {
	@Inject
	private ApplicationRepository appRepo;
	/**
	 * get all application
	 * @return
	 */
	public List<ApplicationDto> getAllApplication(){
		String companyID = AppContexts.user().companyId();
		return this.appRepo.getAllApplication(companyID).stream()
				.map(c->ApplicationDto.fromDomain(c))
				.collect(Collectors.toList());
	}
	/**
	 * get all application by code
	 * @return
	 */
	public Optional<ApplicationDto> getAppById(String applicationID){
		String companyID = AppContexts.user().companyId();
		return this.appRepo.getAppById(companyID, applicationID)
				.map(c->ApplicationDto.fromDomain(c));
	}
	
	/**
	 * get all application by date
	 * @return
	 */
	public List<ApplicationDto> getAllAppByDate(GeneralDate applicationDate){
		String companyID = AppContexts.user().companyId();
		return this.appRepo.getAllAppByDate(companyID, applicationDate).stream()
				.map(c->ApplicationDto.fromDomain(c))
				.collect(Collectors.toList());
	}
	/**
	 * get all application by application type
	 * @return
	 */
	public List<ApplicationDto> getAllAppByAppType(int applicationType){
		String companyID = AppContexts.user().companyId();
		return this.appRepo.getAllAppByAppType(companyID, applicationType).stream()
				.map(c->ApplicationDto.fromDomain(c))
				.collect(Collectors.toList());
	}
	
	public List<String> getAppbyDate(ApplicationPeriodDto dto){
		String companyID = AppContexts.user().companyId();
		return this.appRepo.getApplicationIdByDate(companyID, dto.getStartDate(), dto.getEndDate());
	}

}
