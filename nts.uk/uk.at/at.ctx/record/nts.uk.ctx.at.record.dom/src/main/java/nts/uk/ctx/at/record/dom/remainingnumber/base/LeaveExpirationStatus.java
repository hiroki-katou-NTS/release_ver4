package nts.uk.ctx.at.record.dom.remainingnumber.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeaveExpirationStatus {
	
	// 使用可能
	AVAILABLE(0),
	
	// 期限切れ
	EXPIRED(1);
	
	public final int value;
	
}
