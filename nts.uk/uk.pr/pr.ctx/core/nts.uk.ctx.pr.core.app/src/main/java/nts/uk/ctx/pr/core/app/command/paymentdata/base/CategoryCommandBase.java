package nts.uk.ctx.pr.core.app.command.paymentdata.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCommandBase {

	/** カテゴリ区分 */
	int categoryAttribute;
	/** カテゴリ名 */
	String categoryName;
	/** カテゴリ表示位置 */
	int categoryPosition;

	private List<LineCommandBase> lines;
}
