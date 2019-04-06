
package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTarget;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTrackRecordApprovalDay;
import nts.uk.ctx.at.shared.app.query.workrule.closure.GetCloseOnKeyDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class KTG001QueryProcessor {

	@Inject
	private GetCloseOnKeyDate getCloseOnKeyDate;
	
	@Inject
	private CheckTrackRecordApprovalDay checkTrackRecordApprovalDay;

	public boolean checkDataDayPerConfirm(String employeeId) {
		String CID = AppContexts.user().companyId();
		
		//基準日の会社の締めをすべて取得する
		List<Closure> listClosure = getCloseOnKeyDate.getCloseOnKeyDate(GeneralDate.today());
		
		//取得した「締め」からチェック対象を作成する
		List<CheckTargetItem> listCheckTargetItem = new ArrayList<>();
		for(Closure closure : listClosure) {
			listCheckTargetItem.add(new CheckTargetItem(closure.getClosureId().value,closure.getClosureMonth().getProcessingYm()));
		}
		//CheckTargetOutPut checkTargetOutPut = new CheckTargetOutPut(listCheckTargetItem);
		
		//承認すべき月の実績があるかチェックする
		boolean result = checkTrackRecordApprovalDay.checkTrackRecordApprovalDay(CID, employeeId, listCheckTargetItem.stream().map(c -> new CheckTarget(c.getClosureId() , c.getYearMonth())).collect(Collectors.toList()));
		return result;
	}
}