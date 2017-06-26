package nts.uk.ctx.pr.core.dom.rule.employment.layout.line;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LineDispAtr {
	/**0:印字しないが表示する（グレー表示） */
	DISABLE(0),
	/**1:表示する */
	ENABLE(1);
	public final int value;
}
