package nts.uk.ctx.sys.portal.dom.mypage.setting;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.sys.portal.dom.enums.PermissionDivision;
import nts.uk.ctx.sys.portal.dom.enums.UseDivision;
import nts.uk.ctx.sys.portal.dom.primitive.CompanyId;

/**
 * The Class MyPageSetting.
 */
@Getter
public class MyPageSetting {

	/** The company id. */
	private CompanyId companyId;

	/** The use my page. */
	private UseDivision useMyPage;

	/** The use widget. */
	private UseDivision useWidget;

	/** The use dashboard. */
	private UseDivision useDashboard;

	/** The use flow menu. */
	private UseDivision useFlowMenu;

	/** The external url permission. */
	private PermissionDivision externalUrlPermission;

	/** The top page part use setting. */
	private List<TopPagePartUseSetting> topPagePartUseSetting;

	/**
	 * Instantiates a new my page setting.
	 *
	 * @param companyId
	 *            the company id
	 * @param useMyPage
	 *            the use my page
	 * @param useWidget
	 *            the use widget
	 * @param useDashboard
	 *            the use dashboard
	 * @param useFlowMenu
	 *            the use flow menu
	 * @param externalUrlPermission
	 *            the external url permission
	 * @param topPagePartUseSetting
	 *            the top page part use setting
	 */
	public MyPageSetting(CompanyId companyId, UseDivision useMyPage, UseDivision useWidget, UseDivision useDashboard,
			UseDivision useFlowMenu, PermissionDivision externalUrlPermission,
			List<TopPagePartUseSetting> topPagePartUseSetting) {
		super();
		this.companyId = companyId;
		this.useMyPage = useMyPage;
		this.useWidget = useWidget;
		this.useDashboard = useDashboard;
		this.useFlowMenu = useFlowMenu;
		this.externalUrlPermission = externalUrlPermission;
		this.topPagePartUseSetting = topPagePartUseSetting;
	}
}
