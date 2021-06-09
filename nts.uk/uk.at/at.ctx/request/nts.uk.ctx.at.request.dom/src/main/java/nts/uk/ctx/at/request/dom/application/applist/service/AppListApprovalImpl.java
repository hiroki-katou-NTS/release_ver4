package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.task.AsyncTask;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.ApprovalProcessAfterResult;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterApproval_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.applicationreflect.service.AppReflectManagerFromRecord;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationcommonsetting.AppCommonSet;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class AppListApprovalImpl implements AppListApprovalRepository{

	@Inject
	private DetailBeforeUpdate detailBefUpdate;
	@Inject
	private DetailAfterApproval_New detailAfAppv;
	
	@Inject
	private AppReflectManagerFromRecord appReflectManager;
	
	@Inject
	private ManagedParallelWithContext managedParallelWithContext;
	
	/**
	 * 15 - 申請一覧承認登録チェック
	 */
	@Override
	public boolean checkResAppvListApp(AppCommonSet appCommonSet, PrePostAtr prePostAtr, String achievement,
			String workOperation) {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * 16 - 申請一覧承認登録実行
	 */
	@Override
	public List<String> approvalListApp(List<AppVersion> lstApp, boolean appCheck, boolean achievementCheck,
			boolean scheduleCheck) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		List<String> lstRefAppId = new ArrayList<>();
		List<ApprovalProcessAfterResult> lstAppProcessResult = new ArrayList<>();
		for (AppVersion app : lstApp) {
			//アルゴリズム「承認する」を実行する
			//EA修正履歴 No.3254
			//アルゴリズム「排他チェック」を実行する (thực hiện xử lý 「check version」) - (CMM045)
			boolean checkversion = detailBefUpdate.exclusiveCheckErr(companyID, app.getAppID(), app.getVersion());
			if(!checkversion){
				continue;
			}
			//共通アルゴリズム「詳細画面承認後の処理」を実行する(thực hiện xử lý 「詳細画面承認後の処理」) - 8.2
			ApprovalProcessAfterResult result = detailAfAppv.doApprovalSimple(companyID, app.getAppID(), employeeID, "", "", false);
			lstAppProcessResult.add(result);
			if(!Strings.isBlank(result.getReflectAppId())){
				lstRefAppId.add(result.getReflectAppId());
			}
		}
		//fix bug 117050 response mail, process when close browser 
		//send mail
		ExecutorService executorServiceMai = Executors.newFixedThreadPool(1);
		AsyncTask taskMail = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
				.build(() -> {
					managedParallelWithContext.forEach(lstAppProcessResult, appResult -> {
						detailAfAppv.processMail(companyID, appResult.getApplication().getAppID(), employeeID,
								appResult.getApplication(), appResult.getAllApprovalFlg(), appResult.getPhaseNumber());
					});
				});
		executorServiceMai.submit(taskMail);
		
		// reflect app
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		AsyncTask task = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
				.build(() -> {
					appReflectManager.reflectApplication(lstRefAppId);
					
				});
		executorService.submit(task);
		return lstRefAppId;
	}
}
