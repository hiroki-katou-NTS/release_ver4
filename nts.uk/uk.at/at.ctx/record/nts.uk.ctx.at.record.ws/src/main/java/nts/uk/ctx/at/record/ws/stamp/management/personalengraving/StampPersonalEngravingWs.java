package nts.uk.ctx.at.record.ws.stamp.management.personalengraving;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.stamp.management.personalengraving.StampSettingsEmbossFinder;
import nts.uk.ctx.at.record.app.find.stamp.management.personalengraving.dto.KDP002AStartPageSettingDto;

@Path("at/record/stamp/management/personal")
@Produces("application/json")
public class StampPersonalEngravingWs extends WebService {
	
	@Inject
	private StampSettingsEmbossFinder stampSettingsEmbossFinder;
	
	@POST
	@Path("startPage")
	public KDP002AStartPageSettingDto getStampSetting() {
		return this.stampSettingsEmbossFinder.getSettings();
	}
	
}
