package nts.uk.ctx.at.request.app.find.application.stamp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.dto.AppCommonSettingDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampNewPreDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.AppStampSetDto;
import nts.uk.ctx.at.request.app.find.application.stamp.dto.StampCombinationDto;
import nts.uk.ctx.at.request.app.find.setting.applicationreason.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.setting.company.request.stamp.dto.StampRequestSettingDto;
import nts.uk.ctx.at.request.app.find.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingDto;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampCombinationAtr;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampCommonDefaultImpl;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampCommonDomainService;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampNewDomainService;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.stamp.output.AppStampNewPreOutput;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AppStampFinder {
	
	@Inject
	private AppStampNewDomainService appStampNewDomainService;
	
	@Inject 
	private AppStampCommonDomainService appStampCommonDomainService;
	
	public AppStampNewPreDto newAppStampPreProcess() {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		System.out.println(employeeID);
		AppStampNewPreOutput appStampNewPreOutput = this.appStampNewDomainService.appStampPreProcess(companyID, employeeID, GeneralDate.today());
		AppStampNewPreDto appStampNewPreDto = new AppStampNewPreDto();
		appStampNewPreDto.appCommonSettingDto = new AppCommonSettingDto(
				GeneralDate.today().toString("yyyy/MM/dd"), 
				ApplicationSettingDto.convertToDto(appStampNewPreOutput.appCommonSettingOutput.applicationSetting), 
				null, 
				appStampNewPreOutput.appCommonSettingOutput.appTypeDiscreteSettings.stream().map(x -> AppTypeDiscreteSettingDto.convertToDto(x)).collect(Collectors.toList()), 
				null);
		appStampNewPreDto.appStampSetDto = new AppStampSetDto(
				new StampRequestSettingDto(
						companyID, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getTopComment().getComment().v(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getTopComment().getFontColor(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getTopComment().getFontWeight(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getBottomComment().getComment().v(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getBottomComment().getFontColor(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getBottomComment().getFontWeight(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getResultDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getSupFrameDispNO().v(), 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampPlaceDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampDisplayControl().getStampAtrWorkDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampDisplayControl().getStampAtrGoOutDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampDisplayControl().getStampAtrCareDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampDisplayControl().getStampAtrSupDisp().value,
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getStampDisplayControl().getStampAtrChildCareDisp().value,
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getGoOutTypeDisplayControl().getStampGoOutAtrPrivateDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getGoOutTypeDisplayControl().getStampGoOutAtrPublicDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getGoOutTypeDisplayControl().getStampGoOutAtrCompensationDisp().value, 
						appStampNewPreOutput.appStampSetOutput.getStampRequestSetting().getGoOutTypeDisplayControl().getStampGoOutAtrUnionDisp().value),  
				appStampNewPreOutput.appStampSetOutput.getApplicationReasons().stream()
					.map(x -> new ApplicationReasonDto(
							x.getCompanyId(),
							x.getAppType().value,
							x.getReasonID(),
							x.getDispOrder(),
							x.getReasonTemp(),
							x.getDefaultFlg().value))
					.collect(Collectors.toList()));
		appStampNewPreDto.companyID = companyID;
		appStampNewPreDto.employeeID = employeeID;
		appStampNewPreDto.employeeName = appStampNewPreOutput.employeeName;
		return appStampNewPreDto;
	}
	
	public List<StampCombinationDto> getStampCombinationAtr(){
		List<StampCombinationDto> stampCombinationDtos = new ArrayList<>();
		for(AppStampCombinationAtr a : AppStampCombinationAtr.values()){
			stampCombinationDtos.add(new StampCombinationDto(a.value, a.name));
		}
		return stampCombinationDtos;
	}
	
	public AppStampDto getAppStampByID(String appID){
		String companyID = AppContexts.user().companyId();
		AppStamp appStamp = appStampCommonDomainService.findByID(companyID, appID);
		String employeeName = appStampCommonDomainService.getEmployeeName(appStamp.getApplication_New().getEmployeeID());
		String inputEmpName = appStampCommonDomainService.getEmployeeName(appStamp.getApplication_New().getEnteredPersonID());
		return AppStampDto.convertToDto(appStamp, employeeName, inputEmpName);
	}
}
