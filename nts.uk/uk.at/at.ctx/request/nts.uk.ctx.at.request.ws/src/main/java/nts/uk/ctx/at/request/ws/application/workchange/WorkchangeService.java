package nts.uk.ctx.at.request.ws.application.workchange;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.workchange.AddAppWorkChangeCommand;
import nts.uk.ctx.at.request.app.command.application.workchange.AddAppWorkChangeCommandHandler;
import nts.uk.ctx.at.request.app.command.application.workchange.ApplicationDateCommand;
import nts.uk.ctx.at.request.app.command.application.workchange.CheckChangeAppDateCommandHandler;
import nts.uk.ctx.at.request.app.command.application.workchange.UpdateAppWorkChangeCommandHandler;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeCommonSetDto;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeCommonSetFinder;
import nts.uk.ctx.at.request.app.find.application.workchange.AppWorkChangeRecordWorkInfoFinder;
import nts.uk.ctx.at.request.app.find.application.workchange.RecordWorkInfoDto;
import nts.uk.ctx.at.request.app.find.application.workchange.WorkChangeDetailDto;
import nts.uk.ctx.at.request.app.find.application.workchange.WorkChangeDetailFinder;

@Path("at/request/application/workchange")
@Produces("application/json")
public class WorkchangeService extends WebService {
	
	@Inject
	AppWorkChangeCommonSetFinder commonFinder;
	
	@Inject
	CheckChangeAppDateCommandHandler checkHander;
	
	@Inject
	AddAppWorkChangeCommandHandler addHandler;
	
	@Inject
	UpdateAppWorkChangeCommandHandler updateHandler;
	
	@Inject
	WorkChangeDetailFinder detailFinder;
	
	@Inject
	AppWorkChangeRecordWorkInfoFinder workInfoFinder;
	
	/**
	 * 起動する
	 * アルゴリズム「勤務変更申請画面初期（新規）」を実行する
	 * @return
	 */
	@POST
	@Path("getWorkChangeCommonSetting")
	public AppWorkChangeCommonSetDto getWorkChangeCommonSetting()
	{		
		return commonFinder.getWorkChangeCommonSetting();
	}
	/**
	 * 共通アルゴリズム「申請日を変更する」を実行する
	 * @param command : 申請日付分　（開始日～終了日）
	 */
	@POST
	@Path("checkChangeApplicationDate")
	public void checkChangeApplicationDate(ApplicationDateCommand command){
		checkHander.handle(command);
	}
	
	/**
	 * アルゴリズム「勤務変更申請登録」を実行する
	 */
	@POST
	@Path("addworkchange")
	public List<String> addWorkChange(AddAppWorkChangeCommand command){
		return addHandler.handle(command);
	}
	/**
	 * 
	 * @param appId
	 * @return
	 */
	@POST
	@Path("getWorkchangeByAppID/{appId}")
	public WorkChangeDetailDto getWorkchangeByAppID(@PathParam("appId") String appId) {
		return detailFinder.getWorkChangeDetailById(appId);
	}
	
	/**
	 * アルゴリズム「勤務変更申請登録（更新）」を実行する
	 */
	@POST
	@Path("updateworkchange")
	public void updateWorkChange(AddAppWorkChangeCommand command){
		updateHandler.handle(command);
	}
	
	@POST
	@Path("getRecordWorkInfoByDate")
	public RecordWorkInfoDto getRecordWorkInfoByDate(String appDate){
		return workInfoFinder.getRecordWorkInfor(appDate);
	}
	
}
