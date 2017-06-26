package nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;

/**
 * 番目印字カテゴリ
 * 
 * @author vunv
 *
 */
public class PrintPositionCategory extends DomainObject {

	/**
	 * 位置
	 */
	@Getter
	private final CategoryAtr categoryAtr;

	/**
	 * 行
	 */
	@Getter
	private final PrintPosCatalogLines lines;

	public PrintPositionCategory(CategoryAtr categoryAtr, PrintPosCatalogLines lines) {
		super();
		this.categoryAtr = categoryAtr;
		this.lines = lines;
	}

	public static PrintPositionCategory createFromJavaType(int categoryAtr, int lines) {
		return new PrintPositionCategory(EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class),
				new PrintPosCatalogLines(lines));
	}
}
