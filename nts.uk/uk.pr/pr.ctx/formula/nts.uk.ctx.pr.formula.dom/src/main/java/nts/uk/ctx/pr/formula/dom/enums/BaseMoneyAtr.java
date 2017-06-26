package nts.uk.ctx.pr.formula.dom.enums;

import lombok.AllArgsConstructor;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
public enum BaseMoneyAtr {
	// 0:固定値
	FIXED_VALUE(0),
	// 1:会社単価
	COMPANY_UNIT_PRICE(1),
	// 2:個人単価
	PERSON_UNIT_PRICE(2),
	// 3:支給項目
	PAYMENT_ITEM(3),
	// 4:控除項目
	DEDUCTION_ITEM(4);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "固定値";
			break;
		case 1:
			name = "会社単価";
			break;
		case 2:
			name = "個人単価";
			break;
		case 3:
			name = "支給項目";
			break;
		case 4:
			name = "控除項目";
			break;
		default:
			name = "固定値";
			break;
		}

		return name;
	}

}
