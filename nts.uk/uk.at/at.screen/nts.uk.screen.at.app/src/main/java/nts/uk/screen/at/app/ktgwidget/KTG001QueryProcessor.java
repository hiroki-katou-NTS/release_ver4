
package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.auth.app.find.employmentrole.InitDisplayPeriodSwitchSetFinder;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.DateProcessed;
import nts.uk.ctx.at.auth.app.find.employmentrole.dto.InitDisplayPeriodSwitchSetDto;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTarget;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTrackRecordApprovalDay;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureInfo;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class KTG001QueryProcessor {

	
	@Inject
	private CheckTrackRecordApprovalDay checkTrackRecordApprovalDay;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private InitDisplayPeriodSwitchSetFinder initDisplayPeriodSwitchSetFinder; 

	public boolean checkDataDayPerConfirm(String employeeId) {
		String CID = AppContexts.user().companyId();
		//[RQ609]ログイン社員のシステム日時点の処理対象年月を取得する
		InitDisplayPeriodSwitchSetDto initDisplayPeriodSwitchSetDto = initDisplayPeriodSwitchSetFinder.targetDateFromLogin();
		List<DateProcessed> listDateProcessed = initDisplayPeriodSwitchSetDto.getListDateProcessed();
		//取得した「締め」からチェック対象を作成する
		List<CheckTargetItem> listCheckTargetItem = new ArrayList<>();
		for(DateProcessed dateProcess : listDateProcessed) {
			listCheckTargetItem.add(new CheckTargetItem(dateProcess.getClosureID(),dateProcess.getTargetDate()));
		}
		//CheckTargetOutPut checkTargetOutPut = new CheckTargetOutPut(listCheckTargetItem);
		
		//承認すべき月の実績があるかチェックする
		boolean result = checkTrackRecordApprovalDay.checkTrackRecordApprovalDay(CID, employeeId, listCheckTargetItem.stream().map(c -> new CheckTarget(c.getClosureId() , c.getYearMonth())).collect(Collectors.toList()));
		return result;
	}
}