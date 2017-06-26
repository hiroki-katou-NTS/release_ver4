package nts.uk.ctx.sys.portal.ws.mypage.setting;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import nts.uk.ctx.sys.portal.app.mypage.setting.find.MyPageSettingDto;
import nts.uk.ctx.sys.portal.app.mypage.setting.find.MyPageSettingFinder;


@Path("/mypage")
@Stateless
public class MyPageSettingWs {
	
	@Inject
	MyPageSettingFinder myPageSettingFinder;
	
	@POST
	@Path("myPageSetting/{myPageCode}")
	public MyPageSettingDto getMyPageSettingDetail(@PathParam("myPageCode") String myPageCode) {
		String companyCode = "";
		String constractCode = "";
		return myPageSettingFinder.findByCode(constractCode, companyCode, myPageCode);
	}
}
