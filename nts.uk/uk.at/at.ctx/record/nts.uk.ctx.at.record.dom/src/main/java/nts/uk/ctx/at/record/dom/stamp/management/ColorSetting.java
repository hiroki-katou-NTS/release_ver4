package nts.uk.ctx.at.record.dom.stamp.management;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.common.color.ColorCode;

/**
 * 打刻画面の日時の色設定
 * @author phongtq
 *
 */
public class ColorSetting {

	/** 文字色 */
	@Getter
	private ColorCode textColor;
	
	/** 背景色 */
	@Getter
	private ColorCode backGroundColor;
	
}
