package nts.uk.ctx.sys.portal.dom.mypage.setting;

import java.util.Optional;

public interface MyPageSettingRepository {
	
	/**
	 * Find by code.
	 *
	 * @param companyCode the company code
	 * @param constractCode the constract code
	 * @param topPageCode the top page code
	 * @return the optional
	 */
	Optional<MyPageSetting> findByCode(String companyCode, String constractCode,String myPageCode);
	
	/**
	 * Update.
	 *
	 * @param myPageSetting the my page setting
	 */
	void update(MyPageSetting myPageSetting);
}
