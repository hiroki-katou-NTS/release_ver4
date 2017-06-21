package nts.uk.ctx.at.record.dom.standardtime.enums;

import lombok.AllArgsConstructor;

/**
 * 
 * @author nampt
 * 
 */
@AllArgsConstructor
public enum UseClassificationAtr {
	
	/*
	 * 使用区分
	 */
	// 0: 使用しない
	NOT_USE(0),
	// 1: 使用する
	USE(1);

	public final int value;

	public String toName() {
		String name;
		switch (value) {
		case 0:
			name = "使用しない";
			break;
		case 1:
			name = "使用する";
			break;
		default:
			name = "使用する";
			break;
		}
		return name;
	}
}
