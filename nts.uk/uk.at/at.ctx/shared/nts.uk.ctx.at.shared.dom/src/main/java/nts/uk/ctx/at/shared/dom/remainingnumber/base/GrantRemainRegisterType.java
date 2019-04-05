package nts.uk.ctx.at.shared.dom.remainingnumber.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GrantRemainRegisterType {
	
	/**
	 *  手動
	 */
	MANUAL(0),
	
	/**
	 *  月締め
	 */
	MONTH_CLOSE(1);
	
	public final int value;

}
