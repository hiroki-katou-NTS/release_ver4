package nts.uk.ctx.sys.portal.app.mypage.setting.find;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.sys.portal.dom.mypage.setting.MyPageSetting;

public class MyPageSettingDto {

	/** The My page setting code. */
	public String companyId;

	/** The use my page. */
	public Integer useMyPage;

	/** The use widget. */
	public Integer useWidget;

	/** The use dashboard. */
	public Integer useDashboard;

	/** The use flow menu. */
	public Integer useFlowMenu;

	/** The external url permission. */
	public Integer externalUrlPermission;

	/** The top page part use setting dto. */
	public List<TopPagePartUseSettingDto> topPagePartUseSettingDto;

	public static MyPageSettingDto fromDomain(MyPageSetting myPageSetting) {
		MyPageSettingDto myPageSettingDto = new MyPageSettingDto();
		myPageSettingDto.useMyPage = myPageSetting.getUseMyPage().value;
		myPageSettingDto.useWidget = myPageSetting.getUseWidget().value;
		myPageSettingDto.useDashboard = myPageSetting.getUseDashboard().value;
		myPageSettingDto.useFlowMenu = myPageSetting.getUseFlowMenu().value;
		myPageSettingDto.externalUrlPermission = myPageSetting.getExternalUrlPermission().value;
		myPageSettingDto.topPagePartUseSettingDto = myPageSetting.getTopPagePartUseSetting().stream().map(item -> {
			return TopPagePartUseSettingDto.fromDomain(item);
		}).collect(Collectors.toList()) ;
		return myPageSettingDto;
	}
}
