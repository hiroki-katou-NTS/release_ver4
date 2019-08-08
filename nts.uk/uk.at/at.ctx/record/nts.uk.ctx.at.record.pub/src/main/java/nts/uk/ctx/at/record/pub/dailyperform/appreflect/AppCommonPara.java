package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

@AllArgsConstructor
@Getter
@Setter
public class AppCommonPara {
	private String cid;
	private String sid;
	private GeneralDate ymd;
	/**
	 * 「申請承認設定」.データが確立されている場合の承認済申請の反映のチェックをする
	 */
	private ReflectRecordAtr reflectAtr;	
	/**
	 * ドメインモデル「反映情報」．実績強制反映をチェックする
	 */
	private boolean recordReflect;
	/**
	 * 実績反映状態
	 */
	private ReflectedStatePubRecord stateReflectionReal;
	/**
	 * 予定反映状態
	 */
	private ReflectedStatePubRecord stateReflection;
	/**
	 * 
	 */
	private PrePostRecordAtr prePostAtr;
	private ApplicationType appType;
	/**
	 * ドメインモデル「反映情報」．予定強制反映をチェックする
	 */
	private boolean scheReflect;
}
