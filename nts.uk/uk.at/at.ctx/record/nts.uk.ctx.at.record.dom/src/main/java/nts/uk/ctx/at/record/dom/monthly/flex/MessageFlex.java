package nts.uk.ctx.at.record.dom.monthly.flex;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageFlex {
	
	//・状態：正常
	Normal(0),
	
	//状態：翌月フレックスでない
	Not_Next_Month(1),
	
	//・状態：当月退職
	Retirement(2) ;
	
	public final int value;
}
