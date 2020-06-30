package nts.uk.ctx.at.request.dom.application.workchange;
import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
/**
 * 勤務変更申請の登録を実行する
 */
public interface IWorkChangeRegisterService {
	
	/**
	 * ドメインモデル「勤務変更申請設定」の新規登録をする
	 * 勤務変更申請（新規）登録処理
	 * @param workChange: 勤務変更申請設定
	 * @param app: 申請
	 * @return List approval email.
	 */
    ProcessResult registerData(AppWorkChange_Old workChange, Application_New app, List<GeneralDate> lstDateHd);
	/**
	 * アルゴリズム「勤務変更申請就業時間チェックの内容」を実行する
	 * 就業時間
	 * @param AppWorkChange:  勤務変更申請
	 */	
	void checkWorkHour(AppWorkChange_Old workChange);
	/**
	 * アルゴリズム「勤務変更申請休憩時間１チェックの内容」を実行する
	 * 休憩時間
	 * @param AppWorkChange:  勤務変更申請
	 */
	void checkBreakTime1(AppWorkChange_Old workChange);
	
	/**
	 * 就業時間帯の必須チェック
	 * @param workTypeCD
	 * @return
	 */
	boolean isTimeRequired(String workTypeCD);
	
}
