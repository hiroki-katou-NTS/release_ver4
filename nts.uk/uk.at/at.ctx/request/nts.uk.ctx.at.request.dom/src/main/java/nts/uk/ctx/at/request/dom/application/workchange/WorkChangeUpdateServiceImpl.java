package nts.uk.ctx.at.request.dom.application.workchange;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after.DetailAfterUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before.DetailBeforeUpdate;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;

@Stateless
public class WorkChangeUpdateServiceImpl implements IWorkChangeUpdateService {

	@Inject 
	private ApplicationRepository_New appRepository;
	
	@Inject
	private DetailAfterUpdate detailAfterUpdate;
	
	@Inject
	private IAppWorkChangeRepository workChangeRepository;
	@Inject
	private DetailBeforeUpdate detailBeforeUpdate;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Override
	public ProcessResult UpdateWorkChange(Application_New app, AppWorkChange workChange) {
		
		detailBeforeUpdate.processBeforeDetailScreenRegistration(
				app.getCompanyID(), 
				app.getEmployeeID(), 
				app.getAppDate(), 
				ApplicationType.WORK_CHANGE_APPLICATION.value, 
				app.getAppID(), 
				app.getPrePostAtr(), app.getVersion());
		
		//ドメインモデル「勤務変更申請」の更新をする
		appRepository.updateWithVersion(app);
		workChangeRepository.update(workChange);
		
		// 暫定データの登録
		GeneralDate startDateParam = app.getStartDate().orElse(app.getAppDate());
		GeneralDate endDateParam = app.getEndDate().orElse(app.getAppDate());
		List<GeneralDate> listDate = new ArrayList<>();
		for(GeneralDate loopDate = startDateParam; loopDate.beforeOrEquals(endDateParam); loopDate = loopDate.addDays(1)){
			listDate.add(loopDate);
		}
		interimRemainDataMngRegisterDateChange.registerDateChange(
				app.getCompanyID(), 
				app.getEmployeeID(), 
				listDate);
		
		//アルゴリズム「4-2.詳細画面登録後の処理」を実行する
		return detailAfterUpdate.processAfterDetailScreenRegistration(app);		
	}

}
