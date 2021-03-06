package nts.uk.screen.com.ws.user.information;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.uk.screen.com.app.find.user.information.UserInformationSettingDto;
import nts.uk.screen.com.app.find.user.information.UserInformationSettingScreenQuery;

@Path("query/cmm049userinformationsetting")
@Produces(MediaType.APPLICATION_JSON)
public class Cmm049GetUserInformationSettingWs {
	@Inject
	private UserInformationSettingScreenQuery screenQuery;
	
	@POST
    @Path("get")
	public UserInformationSettingDto getUserInformationSettings() {
		return screenQuery.getUserInformationSettings();
	}
}
