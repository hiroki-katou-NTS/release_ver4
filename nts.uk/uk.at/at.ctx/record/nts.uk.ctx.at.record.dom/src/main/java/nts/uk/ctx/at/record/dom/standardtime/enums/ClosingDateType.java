package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum ClosingDateType {
	// 0:  �Αӂ̒��ߓ��Ɠ���
	SAME_AS_CLOSING_DATE(0),
	// 1: ���ߓ����w��
	DESIGNATE_CLOSING_DATE(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "�Αӂ̒��ߓ��Ɠ���";
			break;
		case 1:
			name = "���ߓ����w��";
			break;
		default:
			name = "�Αӂ̒��ߓ��Ɠ���";
			break;
		}
		return name;
	}
}
