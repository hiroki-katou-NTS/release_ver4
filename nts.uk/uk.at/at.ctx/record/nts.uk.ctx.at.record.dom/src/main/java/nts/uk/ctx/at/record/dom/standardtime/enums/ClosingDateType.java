package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 *
 */
@AllArgsConstructor
public enum ClosingDateType {
	
	/*
	 * �R�U������ߓ�
	 */
	// 0: 1��
	FIRST(0),
	// 1: 2��
	SECOND(1),
	// 2: 3��
	THIRD(2),
	// 3: 4��
	FOURTH(3),
	// 4: 5��
	FIFTH(4),
	// 5: 6��
	SIXTH(5),
	// 6: 7��
	SEVENTH(6),
	// 7: 8��
	EIGHTH(7),
	// 8: 9��
	NINTH(8),
	// 9: 10��
	TENTH(9),
	// 10: 11��
	ELEVENTH(10),
	// 11: 12��
	TWELFTH(11),
	// 12: 13��
	THIRTEENTH(12),
	// 13: 14��
	FOURTHTEENTH(13),
	// 14: 15��
	FIFTEENTH(14),
	// 15: 16��
	SIXTEENTH(15),
	// 16: 17��
	SEVENTEENTH(16),
	// 17: 18��
	EIGHTEENTH(17),
	// 18: 19��
	NINETEENTH(18),
	// 19: 20��
	TWENTIETH(19),
	// 20: 21��
	TWENTY_FIRST(20),
	// 21: 22��
	TWENTY_SECOND(21),
	// 22: 23��
	TWENTY_THIRD(22),
	// 23: 24��
	TWENTY_FOURTH(23),
	// 24: 25��
	TWENTY_FIFTH(24),
	// 25: 26��
	TWENTY_SIXTH(25),
	// 26: 27��
	TWENTY_SEVENTH(26),
	// 27: 28��
	TWENTY_EIGHTH(27),
	// 28: 29��
	TWENTY_NINTH(28),
	// 29: 30��
	THIRTIETH(29),
	// 30: ����
	LASTDAY(30);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "1��";
			break;
		case 1:
			name = "2��";
			break;
		case 2:
			name = "3��";
			break;
		case 3:
			name = "4��";
			break;
		case 4:
			name = "5��";
			break;
		case 5:
			name = "6��";
			break;
		case 6:
			name = "7��";
			break;
		case 7:
			name = "8��";
			break;
		case 8:
			name = "9��";
			break;
		case 9:
			name = "10��";
			break;
		case 10:
			name = "11��";
			break;
		case 11:
			name = "12��";
			break;
		case 12:
			name = "13��";
			break;
		case 13:
			name = "14��";
			break;
		case 14:
			name = "15��";
			break;
		case 15:
			name = "16��";
			break;
		case 16:
			name = "17��";
			break;
		case 17:
			name = "18��";
			break;
		case 18:
			name = "19��";
			break;
		case 19:
			name = "20��";
			break;
		case 20:
			name = "21��";
			break;
		case 21:
			name = "22��";
			break;
		case 22:
			name = "23��";
			break;
		case 23:
			name = "24��";
			break;
		case 24:
			name = "25��";
			break;
		case 25:
			name = "26��";
			break;
		case 26:
			name = "27��";
			break;
		case 27:
			name = "28��";
			break;
		case 28:
			name = "29��";
			break;
		case 29:
			name = "30��";
			break;
		case 30:
			name = "����";
			break;
		default:
			name = "1��";
			break;
		}
		return name;
	}
}
