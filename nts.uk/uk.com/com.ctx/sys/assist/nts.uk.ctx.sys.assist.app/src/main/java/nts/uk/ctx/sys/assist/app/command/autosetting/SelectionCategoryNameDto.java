package nts.uk.ctx.sys.assist.app.command.autosetting;

import lombok.Data;
import nts.uk.ctx.sys.assist.app.find.autosetting.DataStoragePatternSettingDto;

/**
 * 選択カテゴリ名称DTO
 */
@Data
public class SelectionCategoryNameDto {
	
	/**
	 * カテゴリ名称
	 */
	private String categoryName;
	
	/**
	 * パターンコード
	 */
	private String patternCode;
	
	/**
	 * 保存期間
	 */
	private String retentionPeriod;
	
	/**
	 * カテゴリID
	 */
	private String categoryId;
	
	/**
	 * パターン区分
	 */
	private int patternClassification;
	
	/**
	 * システム種類
	 */
	private int systemType;
	
	/**
	 * 詳細設定
	 */
	private DataStoragePatternSettingDto pattern;
}
