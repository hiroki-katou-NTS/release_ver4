package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum WorkPlaceUseAtr {
	
	// 0: �g�p���Ȃ�
	NOT_USE(0),
	// 1: �g�p����
	USE(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "�g�p���Ȃ�";
			break;
		case 1:
			name = "�g�p����";
			break;
		default:
			name = "�g�p����";
			break;
		}
		return name;
	}
}
