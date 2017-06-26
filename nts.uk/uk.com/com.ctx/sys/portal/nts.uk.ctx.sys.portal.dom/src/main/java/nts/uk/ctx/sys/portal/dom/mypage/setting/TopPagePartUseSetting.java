package nts.uk.ctx.sys.portal.dom.mypage.setting;

import lombok.Getter;
import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.enums.UseDivision;
import nts.uk.ctx.sys.portal.dom.primitive.CompanyId;
import nts.uk.ctx.sys.portal.dom.primitive.TopPagePartCode;
import nts.uk.ctx.sys.portal.dom.primitive.TopPagePartName;

/**
 * The Class TopPagePartSettingItem.
 */
@Getter
public class TopPagePartUseSetting {

	/** The company id. */
	private CompanyId companyId;

	/** The top page part code. */
	private TopPagePartCode topPagePartCode;

	/** The top page part name. */
	private TopPagePartName topPagePartName;

	/** The use division. */
	private UseDivision useDivision;

	/** The part type. */
	private TopPagePartType topPagePartType;

	/**
	 * Instantiates a new top page part use setting.
	 *
	 * @param companyId
	 *            the company id
	 * @param topPagePartCode
	 *            the top page part code
	 * @param topPagePartName
	 *            the top page part name
	 * @param useDivision
	 *            the use division
	 * @param topPagePartType
	 *            the top page part type
	 */
	public TopPagePartUseSetting(CompanyId companyId, TopPagePartCode topPagePartCode, TopPagePartName topPagePartName,
			UseDivision useDivision, TopPagePartType topPagePartType) {
		super();
		this.companyId = companyId;
		this.topPagePartCode = topPagePartCode;
		this.topPagePartName = topPagePartName;
		this.useDivision = useDivision;
		this.topPagePartType = topPagePartType;
	}
}
