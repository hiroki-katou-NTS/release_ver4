package nts.uk.ctx.at.record.app.find.standardtime;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.app.find.standardtime.dto.AgreementTimeOfWorkPlaceDetailDto;
import nts.uk.ctx.at.record.app.find.standardtime.dto.AgreementTimeOfWorkPlaceListDto;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfCompany;
import nts.uk.ctx.at.record.dom.standardtime.BasicAgreementSetting;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeCompanyRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;
import nts.uk.ctx.at.record.dom.standardtime.repository.BasicAgreementSettingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * 
 * @author nampt 雇用 screen
 */
@Stateless
public class AgreementTimeOfWorkPlaceFinder {

	@Inject
	private AgreementTimeOfWorkPlaceRepository agreementTimeOfWorkPlaceRepository;
	
	@Inject
	private BasicAgreementSettingRepository basicAgreementSettingRepository;

	@Inject
	private AgreementTimeCompanyRepository agreementTimeCompanyRepository;
	
	public AgreementTimeOfWorkPlaceListDto findAll(int laborSystemAtr) {

		AgreementTimeOfWorkPlaceListDto agreementTimeOfWorkPlaceListDto = new AgreementTimeOfWorkPlaceListDto();
		// get list workplaces have been setting
		List<String> workPlaceIds = agreementTimeOfWorkPlaceRepository
				.findWorkPlaceSetting(EnumAdaptor.valueOf(laborSystemAtr, LaborSystemtAtr.class));

		agreementTimeOfWorkPlaceListDto.setWorkPlaceIds(workPlaceIds);
		return agreementTimeOfWorkPlaceListDto;
	}

	public AgreementTimeOfWorkPlaceDetailDto findDetail(int laborSystemAtr, String workplaceId) {
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
		AgreementTimeOfWorkPlaceDetailDto agreementTimeOfWorkPlaceDto = new AgreementTimeOfWorkPlaceDetailDto();
		// get basicSettingId of workplace selected
		Optional<String> basicSettingIdOfWorkplace = agreementTimeOfWorkPlaceRepository.find(workplaceId,
				EnumAdaptor.valueOf(laborSystemAtr, LaborSystemtAtr.class));
		
		if(basicSettingIdOfWorkplace.isPresent()){
			// get basicSetting detail of workplace selected
			Optional<BasicAgreementSetting> basicSettingOfWorkplace = basicAgreementSettingRepository
					.find(basicSettingIdOfWorkplace.get());
			// set error time + alarm time
			if(basicSettingOfWorkplace.isPresent()){
				agreementTimeOfWorkPlaceDto.setErrorWeek(basicSettingOfWorkplace.get().getErrorWeek().v());
				agreementTimeOfWorkPlaceDto.setAlarmWeek(basicSettingOfWorkplace.get().getAlarmWeek().v());
				agreementTimeOfWorkPlaceDto.setErrorTwoWeeks(basicSettingOfWorkplace.get().getErrorTwoWeeks().v());
				agreementTimeOfWorkPlaceDto.setAlarmTwoWeeks(basicSettingOfWorkplace.get().getAlarmTwoWeeks().v());
				agreementTimeOfWorkPlaceDto.setErrorFourWeeks(basicSettingOfWorkplace.get().getErrorFourWeeks().v());
				agreementTimeOfWorkPlaceDto.setAlarmFourWeeks(basicSettingOfWorkplace.get().getAlarmFourWeeks().v());
				agreementTimeOfWorkPlaceDto.setErrorOneMonth(basicSettingOfWorkplace.get().getErrorOneMonth().v());
				agreementTimeOfWorkPlaceDto.setAlarmOneMonth(basicSettingOfWorkplace.get().getAlarmOneMonth().v());
				agreementTimeOfWorkPlaceDto.setErrorTwoMonths(basicSettingOfWorkplace.get().getErrorTwoMonths().v());
				agreementTimeOfWorkPlaceDto.setAlarmTwoMonths(basicSettingOfWorkplace.get().getAlarmTwoMonths().v());
				agreementTimeOfWorkPlaceDto.setErrorThreeMonths(basicSettingOfWorkplace.get().getErrorThreeMonths().v());
				agreementTimeOfWorkPlaceDto.setAlarmThreeMonths(basicSettingOfWorkplace.get().getAlarmThreeMonths().v());
				agreementTimeOfWorkPlaceDto.setErrorOneYear(basicSettingOfWorkplace.get().getErrorOneYear().v());
				agreementTimeOfWorkPlaceDto.setAlarmOneYear(basicSettingOfWorkplace.get().getAlarmOneYear().v());			
			} else {
				return null;
			}
			
		}
		
		// get basicSettingId of Company
		Optional<AgreementTimeOfCompany> agreementTimeOfCompany = agreementTimeCompanyRepository.find(companyId,
				EnumAdaptor.valueOf(laborSystemAtr, LaborSystemtAtr.class));
		// get Limit Time of company
		Optional<BasicAgreementSetting> basicSettingOfCom = basicAgreementSettingRepository
				.find(agreementTimeOfCompany.get().getBasicSettingId());
		
		// set limit time
		agreementTimeOfWorkPlaceDto.setLimitWeek(basicSettingOfCom.get().getLimitWeek().v());
		agreementTimeOfWorkPlaceDto.setLimitTwoWeeks(basicSettingOfCom.get().getLimitTwoWeeks().v());
		agreementTimeOfWorkPlaceDto.setLimitFourWeeks(basicSettingOfCom.get().getLimitFourWeeks().v());
		agreementTimeOfWorkPlaceDto.setLimitOneMonth(basicSettingOfCom.get().getLimitOneMonth().v());
		agreementTimeOfWorkPlaceDto.setLimitTwoMonths(basicSettingOfCom.get().getLimitTwoMonths().v());
		agreementTimeOfWorkPlaceDto.setLimitThreeMonths(basicSettingOfCom.get().getLimitThreeMonths().v());
		agreementTimeOfWorkPlaceDto.setLimitOneYear(basicSettingOfCom.get().getLimitOneYear().v());
		
		return agreementTimeOfWorkPlaceDto;
	}
}
