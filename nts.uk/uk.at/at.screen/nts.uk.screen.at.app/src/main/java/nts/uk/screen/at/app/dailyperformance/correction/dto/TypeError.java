package nts.uk.screen.at.app.dailyperformance.correction.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypeError {
	DUPLICATE(0), 
	COUPLE(1), 
	CONTINUOUS(2), 
	ITEM28(3),
	DEVIATION_REASON(4);
	public final int value;
}
