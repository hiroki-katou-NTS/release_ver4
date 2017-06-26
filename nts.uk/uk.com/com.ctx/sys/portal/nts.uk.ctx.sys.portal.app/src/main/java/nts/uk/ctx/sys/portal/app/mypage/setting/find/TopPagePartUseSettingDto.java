package nts.uk.ctx.sys.portal.app.mypage.setting.find;

import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.mypage.setting.TopPagePartUseSetting;

public class TopPagePartUseSettingDto {

	/** The company id. */
	public String companyId;

	/** The part item code. */
	public String partItemCode;

	/** The part item name. */
	public String partItemName;

	/** The use division. */
	public Integer useDivision;

	/** The part type. */
	public TopPagePartType partType;

	public static TopPagePartUseSettingDto fromDomain(TopPagePartUseSetting topPagePartSettingItem) {
		TopPagePartUseSettingDto topPagePartSettingItemDto = new TopPagePartUseSettingDto();
		topPagePartSettingItemDto.partItemCode = topPagePartSettingItem.getTopPagePartCode().toString();
		topPagePartSettingItemDto.partItemName = topPagePartSettingItem.getTopPagePartName().toString();
		topPagePartSettingItemDto.useDivision = topPagePartSettingItem.getUseDivision().value;
		topPagePartSettingItemDto.partType = topPagePartSettingItem.getTopPagePartType();
		return topPagePartSettingItemDto;
	}
}
