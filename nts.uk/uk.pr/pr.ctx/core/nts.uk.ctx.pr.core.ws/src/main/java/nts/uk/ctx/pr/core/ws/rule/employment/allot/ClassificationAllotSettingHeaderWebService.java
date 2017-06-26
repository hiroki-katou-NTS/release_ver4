package nts.uk.ctx.pr.core.ws.rule.employment.allot;

import java.io.StringReader;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.UpdateClassificationAllotSettingHeaderCommand;
import nts.uk.ctx.pr.core.app.command.rule.employment.allot.UpdateClassificationAllotSettingHeaderHandler;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.ClassificationAllotSettingHeaderDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.ClassificationAllotSettingHeaderFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("pr/core/allot")
@Produces(MediaType.APPLICATION_JSON)
public class ClassificationAllotSettingHeaderWebService {

	@Inject
	private ClassificationAllotSettingHeaderFinder find;
	
	@Inject
	private UpdateClassificationAllotSettingHeaderHandler updateClassificationAllotSetting;
	
	@POST 
	@Path("getallclassificationallotsettingheader")
	public List<ClassificationAllotSettingHeaderDto> getAllClassificationAllotSettingHeader(String data) {
		JsonObject json = Json.createReader(new StringReader(data)).readObject();
		String ccd = AppContexts.user().companyCode();
		String companyCode = json.getString("companyCode");			
		return this.find.getAllClassificationAllotSettingHeader(ccd );
	}
	
	@POST
	@Path("updateclassificationallotsettingheader")
	public void update(UpdateClassificationAllotSettingHeaderCommand command){
	   this.updateClassificationAllotSetting.handle(command);
	}
}

