package nts.uk.screen.at.ws.kdp.kdp010.a;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.NoArgsConstructor;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.stamp.management.StampPageLayoutDto;
import nts.uk.screen.at.app.query.kdp.kdp001.a.PortalStampSettingsDto;
import nts.uk.screen.at.app.query.kdp.kdp010.a.TimeStampInputSettingFinder;
import nts.uk.screen.at.app.query.kdp.kdp010.a.dto.SettingsSmartphoneStampDto;
import nts.uk.screen.at.app.query.kdp.kdp010.a.dto.SettingsUsingEmbossingDto;
import nts.uk.screen.at.app.query.kdp.kdp010.a.dto.StampSetCommunalDto;

@Path("at/record/stamp/timestampinputsetting")
@Produces("application/json")
public class TimeStampInputSettingsWebService extends WebService {

	@Inject
	private TimeStampInputSettingFinder timeStampInputSettingFinder;
	
	/**打刻の前準備(ポータル)を表示する  */
	@POST
	@Path("portalstampsettings/get")
	public PortalStampSettingsDto getPortalStampSettings() {
		return timeStampInputSettingFinder.getPortalStampSettings().orElse(null);
	}
	
	@POST
	@Path("stampsetcommunal/get")
	public StampSetCommunalDto getStampSetCommunal() {
		return timeStampInputSettingFinder.getStampSetCommunal().orElse(null);
	}
	
	@POST
	@Path("settingssmartphonestamp/get")
	public SettingsSmartphoneStampDto getSettingsSmartphoneStamp() {
		return timeStampInputSettingFinder.getSettingsSmartphoneStamp();
	}
	
	@POST
	@Path("settingsusingembossing/get")
	public SettingsUsingEmbossingDto getSettingsUsingEmbossing() {
		return timeStampInputSettingFinder.getSettingsUsingEmbossing().orElse(null);
	}
	
	@POST
	@Path("layoutsettingssmartphone/get")
	public StampPageLayoutDto getLayoutSettingsSmartphone(paramPageNo param) {
		return timeStampInputSettingFinder.getLayoutSettingsSmartphone(param.pageNo);
	}
	
}

class paramPageNo{
	
	public Integer pageNo;
}

