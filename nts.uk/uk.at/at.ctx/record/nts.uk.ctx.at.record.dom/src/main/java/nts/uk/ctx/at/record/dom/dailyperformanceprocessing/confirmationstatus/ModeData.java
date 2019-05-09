package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ModeData {

	NORMAL(0, "通常"),
	APPROVAL(1, "承認");
	
	public int value;
	
	public String name;
}
