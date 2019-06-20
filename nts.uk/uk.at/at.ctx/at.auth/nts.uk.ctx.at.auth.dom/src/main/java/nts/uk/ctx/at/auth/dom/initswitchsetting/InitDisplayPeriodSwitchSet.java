/**
 * 
 */
package nts.uk.ctx.at.auth.dom.initswitchsetting;

import lombok.Getter;

/**
 * @author hieult
 *
 */
/** 初期表示期間切替設定 **/
@Getter
public class InitDisplayPeriodSwitchSet {

	/** 会社ID **/
	private String companyID;
	/** ロールID **/
	private String roleID;
	/** 日数 **/
	private int day;

	public InitDisplayPeriodSwitchSet(String companyID, String roleID, int day) {
		super();
		this.companyID = companyID;
		this.roleID = roleID;
		this.day = day;
	}
}
