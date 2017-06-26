package nts.uk.ctx.pr.screen.app.query.qpp005.result;

import lombok.Value;

/**
 * 番目印字カテゴリ
 * 
 * @author vunv
 *
 */
@Value
public class PrintPositionCategoryDto {

	/**
	 * 位置
	 */
	private final int categoryAtr;

	/**
	 * 行
	 */
	private final int lines;

	public static PrintPositionCategoryDto fromDomain(int categoryAtr, int lines) {
		return new PrintPositionCategoryDto(categoryAtr, lines);
	}

}
