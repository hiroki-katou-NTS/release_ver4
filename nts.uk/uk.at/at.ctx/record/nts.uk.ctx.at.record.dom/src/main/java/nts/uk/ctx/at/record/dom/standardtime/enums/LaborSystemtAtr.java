package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum LaborSystemtAtr {
	//0: ��ʘJ����
	GENERAL_LABOR_SYSTEM(0),
	//1: �ό`�J�����Ԑ�
	DEFORMATION_WORKING_TIME_SYSTEM(1);
	
	public final int value;
	
	public String toName(){
		String name;
		switch (value) {
		case 0:
			name = "��ʘJ����";
			break;
		case 1:
			name = "�`�J�����Ԑ�";
			break;
		default:
			name = "��ʘJ����";
			break;
		}
		
		return name;
	}
}
