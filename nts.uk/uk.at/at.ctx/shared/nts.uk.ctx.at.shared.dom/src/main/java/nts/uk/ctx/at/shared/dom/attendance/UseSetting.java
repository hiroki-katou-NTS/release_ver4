package nts.uk.ctx.at.shared.dom.attendance;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UseSetting {
	/* 使用しない*/
	UseAtr_NotUse(0),
	/* 使用する*/
	UseAtr_Use(1);
	
	public final int value;
}
