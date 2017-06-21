package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum TargetSettingAtr {
	
	/*
	 * �Ώۋ敪
	 */
	// 0: ���ʎ��уf�[�^
	MONTHLY_ACTUAL_DATA(0),
	// 1: �⏕���f�[�^
	AUXILIARY_MONTH_DATA(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "���ʎ��уf�[�^";
			break;
		case 1:
			name = "�⏕���f�[�^";
			break;
		default:
			name = "���ʎ��уf�[�^";
			break;
		}
		return name;
	}
}
