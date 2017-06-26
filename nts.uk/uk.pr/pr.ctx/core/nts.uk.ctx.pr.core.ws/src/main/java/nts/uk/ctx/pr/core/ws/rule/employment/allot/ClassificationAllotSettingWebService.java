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

import nts.uk.ctx.pr.core.app.find.rule.employment.allot.ClassificationAllotSettingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.ClassificationAllotSettingFinder;

@Path("pr/core/allot/")
@Produces(MediaType.APPLICATION_JSON)
public class ClassificationAllotSettingWebService {
//
//	@Inject
//	private ClassificationAllotSettingFinder find;
//
//	@POST
//	@Path("getallclassificationallotsetting") 
//	public List<ClassificationAllotSettingDto> getAllClassificationAllotSetting(String data) {
//		JsonObject json = Json.createReader(new StringReader(data)).readObject();
//		String historyId = json.getString("historyId");
//		return this.find.getHistoy(historyId);
//	}

}
