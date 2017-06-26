package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum ConditionAtr {
//	0:条件分岐を利用しない
	DO_NOT_USE_CONDITIONAL_BRANCH(0),
//	1:条件分岐を利用する
	USE_CONDITIONAL_BRANCH(1);
	
	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "条件分岐を利用しない";
			break;
		case 1:
			name = "条件分岐を利用する";
			break;
		default:
			name = "条件分岐を利用しない";
			break;
		}

		return name;
	}
}
