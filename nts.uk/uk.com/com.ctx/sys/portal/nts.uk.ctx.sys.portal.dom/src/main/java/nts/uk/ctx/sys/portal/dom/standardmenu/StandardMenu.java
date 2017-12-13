package nts.uk.ctx.sys.portal.dom.standardmenu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.sys.portal.dom.enums.MenuAtr;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.System;
import nts.uk.ctx.sys.portal.dom.enums.WebMenuSetting;

/**
 * The Class StandardMenu.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class StandardMenu extends AggregateRoot {
	

	/** The company id. */
	private String companyId;
	
	/** The menu code. */
	private MenuCode code;

	/** The Target Items. */
	private String targetItems;
	
	/** The Display Name. */
	private MenuDisplayName displayName;
	
	/** The Display Order. */
	private int displayOrder;
	
	/** The menuAtr. */
	private MenuAtr menuAtr;
	
	/** The url. */
	private String url;
	
	/** The system. */
	private System system;
	
	/** The Menu classification. */
	private MenuClassification classification;
	
	/** The webMenuSetting. */
	private WebMenuSetting webMenuSetting;
	
	/** The afterLoginDisplay. */
	private int afterLoginDisplay;
	
	/** The logSettingDisplay. */
	private int logSettingDisplay;
	
	/** プログラムID **/
	private String programId;
	
	/** 遷移先の画面ID **/
	private String screenId;

	/**
	 * Instantiates a new Standard Menu.
	 *
	 * @param companyId the company Id
	 * @param code the menu code
	 * @param targetItems the target items
	 * @param displayName the display name
	 * @param displayOrder the displayOrder
	 * @param menuAtr the menuAtr
	 * @param url the url
	 * @param system the system
	 * @param classification the classification
	 * @param webMenuSetting the webMenuSetting
	 * @param afterLoginDisplay the afterLoginDisplay
	 * @param logSettingDisplay the logSettingDisplay
	 */
	public StandardMenu(String companyId, MenuCode code, String targetItems, MenuDisplayName displayName, int displayOrder,
			MenuAtr menuAtr, String url, System system, MenuClassification classification, WebMenuSetting webMenuSetting,
			int afterLoginDisplay, int logSettingDisplay) {
		
		this.companyId = companyId;
		this.code = code;
		this.targetItems = targetItems;
		this.displayName = displayName;
		this.displayOrder = displayOrder;
		this.menuAtr = menuAtr;
		this.url = url;
		this.system = system;
		this.classification = classification;
		this.webMenuSetting = webMenuSetting;
		this.afterLoginDisplay = afterLoginDisplay;
		this.logSettingDisplay = logSettingDisplay;
	}
	
	
	
	public StandardMenu(String companyId, MenuClassification classification, MenuCode code, MenuDisplayName displayName,
			System system) {
		this.companyId = companyId;
		this.classification = classification;
		this.code = code;
		this.displayName = displayName;
		this.system = system;
	}
	
	/**
	 * author: yennth
	 * update displayName
	 * @param companyId
	 * @param classification
	 * @param code
	 * @param displayName
	 * @param system
	 * @return
	 */
	public static StandardMenu updateName(String companyId, int classification, String code, String displayName, int system)
	{
		return new StandardMenu(companyId, EnumAdaptor.valueOf(classification, MenuClassification.class), new MenuCode(code), new MenuDisplayName(displayName), EnumAdaptor.valueOf(system, System.class));
	}


	/**
	 * Creates the from java type.
	 *
	 * @param companyId the company Id
	 * @param code the menu code
	 * @param targetItems the target items
	 * @param displayName the display name
	 * @param displayOrder the displayOrder
	 * @param menuAtr the menuAtr
	 * @param url the url
	 * @param system the system
	 * @param classification the classification
	 * @param webMenuSetting the webMenuSetting
	 * @param afterLoginDisplay the afterLoginDisplay
	 * @param logSettingDisplay the logSettingDisplay
	 */
	public static StandardMenu createFromJavaType(String companyId, String code, String targetItems, String displayName, 
			int displayOrder, int menuAtr, String url, int system, int classification, int webMenuSetting,
			int afterLoginDisplay, int logSettingDisplay) {
		return new StandardMenu(companyId, new MenuCode(code), targetItems, new MenuDisplayName(displayName), displayOrder, EnumAdaptor.valueOf(menuAtr, MenuAtr.class), url,
				EnumAdaptor.valueOf(system, System.class),EnumAdaptor.valueOf(classification, MenuClassification.class) , 
				EnumAdaptor.valueOf(webMenuSetting, WebMenuSetting.class), afterLoginDisplay, logSettingDisplay);
	}	
	
	
}
