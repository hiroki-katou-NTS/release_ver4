package nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.stampsetting;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.AppCommentSet;

/**
 * refactor 4
 * UKDesign.ドメインモデル."NittsuSystem.UniversalK".就業.contexts.申請承認.設定.会社別.申請承認設定.打刻申請設定.各種類の設定
 * @author Doan Duy Hung
 *
 */
@Getter
@Setter
public class SettingForEachType {
	
	/**
	 * 下部コメント
	 */
	private AppCommentSet bottomComment;
	
	/**
	 * 上部コメント
	 */
	private AppCommentSet topComment;
	
	/**
	 * 打刻分類
	 */
	private StampAtr stampAtr;
	
}
