package nts.uk.ctx.sys.portal.app.mypage.setting.find;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.mypage.setting.MyPageSetting;
import nts.uk.ctx.sys.portal.dom.mypage.setting.MyPageSettingRepository;


@Stateless
public class MyPageSettingFinder {

	@Inject
	MyPageSettingRepository myPageSettingRepository;

	public MyPageSettingDto findByCode(String constractCode, String companyCode, String topPageCode) {
		Optional<MyPageSetting> myPageSetting = myPageSettingRepository.findByCode(companyCode, constractCode,
				topPageCode);
		// convert toppage domain to dto
		if (myPageSetting.isPresent()) {
			MyPageSetting mps = myPageSetting.get();
			return MyPageSettingDto.fromDomain(mps);
		}
		return null;
	}
}
