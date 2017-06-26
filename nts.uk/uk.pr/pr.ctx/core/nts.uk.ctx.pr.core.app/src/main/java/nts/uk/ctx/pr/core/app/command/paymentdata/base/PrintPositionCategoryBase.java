package nts.uk.ctx.pr.core.app.command.paymentdata.base;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPositionCategory;

/**
 * 番目印字カテゴリ
 * 
 * @author vunv
 *
 */
@Getter
@Setter
public class PrintPositionCategoryBase {

	/**
	 * 位置
	 */
	private int categoryAtr;

	/**
	 * 行
	 */
	private int lines;

	public PrintPositionCategory toDomain(int categoryAtr, int lines) {
		return PrintPositionCategory.createFromJavaType(categoryAtr, lines);
	}

}
