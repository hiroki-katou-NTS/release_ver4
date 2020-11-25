package nts.uk.ctx.sys.portal.dom.toppagepart.createflowmenu.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * フローメニューレイアウトのメニュー設定
 */
@Getter
@Builder
public class MenuSettingDto {
	
	/**
	 * 会社ID									
	 */
	private String cid;
	
	/**
	 * フローメニューコード									
	 */
	private String flowMenuCode;
	
	/**
	 * column																
	 */
	private int column;
	
	/**
	 * row									
	 */
	private int row;
	
	/**
	 * メニュー名称																	
	 */
	private String menuName;
	
	/**
	 * メニューコード																		
	 */
	private String menuCode;
	
	/**
	 * メニュー分類
	 */
	private int menuClassification;
	
	/**
	 * システム区分
	 */
	private int systemType;
	
	/**
	 * width
	 */
	private int width;
	
	/**
	 * height
	 */
	private int height;
	
	/**
	 * 文字のサイズ									
	 */
	private int fontSize;
	
	/**
	 * 太字
	 */
	private int bold;

	/**
	 * 横の位置
	 */
	private int horizontalPosition;
	
	/**
	 * 縦の位置
	 */
	private int verticalPosition;
}