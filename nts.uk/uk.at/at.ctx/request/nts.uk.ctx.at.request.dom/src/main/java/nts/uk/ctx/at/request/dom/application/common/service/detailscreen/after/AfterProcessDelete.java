package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.ProcessDeleteResult;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;

/**
 * refactor 4
 * UKDesign.UniversalK.就業.KAF_申請.共通アルゴリズム.5-2.詳細画面削除後の処理(afterDelete)
 * @author Doan Duy Hung
 *
 */
public interface AfterProcessDelete {
	
	/**
	 * 5-2.詳細画面削除後の処理
	 * @param appID
	 * @param application
	 * @param appDispInfoStartupOutput
	 * @return
	 */
	public ProcessDeleteResult screenAfterDelete (String appID, Application application, AppDispInfoStartupOutput appDispInfoStartupOutput);

}
