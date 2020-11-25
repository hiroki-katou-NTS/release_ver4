package nts.uk.ctx.sys.assist.app.find.autosetting.deletion;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionPatternSetting;
import nts.uk.ctx.sys.assist.dom.deletedata.DataDeletionSelectionCategory;

@Data
public class DataDeletionPatternSettingDto implements DataDeletionPatternSetting.MementoSetter {
		
	/**
	 * パスワード有無
	 */
	private int withoutPassword;
	
	/**
	 * パターン区分
	 */
	private int patternClassification;
	
	/**
	 * 削除パターンパスワード
	 */
	private String patternCompressionPwd;
	
	/**
	 * 月次参照年
	 */
	private Integer monthlyReferYear;
	
	/**
	 * 日次参照年
	 */
	private Integer dailyReferYear;
	
	/**
	 * 年次参照年
	 */
	private Integer annualReferYear;
	
	/**
	 * 日次参照月
	 */
	private Integer dailyReferMonth;
	
	/**
	 * 月次参照月
	 */
	private Integer monthlyReferMonth;
	
	/**
	 * 契約コード
	 */
	private String contractCode;
	
	/**
	 * パターンコード
	 */
	private String patternCode;
	
	/**
	 * パターン名
	 */
	private String patternName;
	
	/**
	 * データ削除の選択カテゴリ
	 */
	private List<DelSelectionCategoryNameDto> selectCategories;
	
	/**
	 * 削除パターン補足説明
	 */
	private String patternSuppleExplanation;

	@Override
	public void setCategories(List<DataDeletionSelectionCategory> categories) {
		//NOT USED
	}
}