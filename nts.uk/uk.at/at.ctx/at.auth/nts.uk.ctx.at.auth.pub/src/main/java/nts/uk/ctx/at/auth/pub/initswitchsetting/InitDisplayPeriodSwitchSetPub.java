/**
 * 
 */
package nts.uk.ctx.at.auth.pub.initswitchsetting;

import java.util.Optional;


/**
 * @author hieult
 *
 */

public interface InitDisplayPeriodSwitchSetPub {
	/** [RQ609]ログイン社員のシステム日時点の処理対象年月を取得する **/
	 InitDisplayPeriodSwitchSetDto targetDateFromLogin();
}
