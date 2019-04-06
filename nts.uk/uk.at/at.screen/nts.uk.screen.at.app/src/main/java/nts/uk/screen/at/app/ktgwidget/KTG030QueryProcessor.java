package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTargetItemDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.checktrackrecord.CheckTrackRecord;
import nts.uk.ctx.at.shared.app.query.workrule.closure.GetCloseOnKeyDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class KTG030QueryProcessor {
	@Inject
	private CheckTrackRecord  checkTrackRecord;

	@Inject
	private GetCloseOnKeyDate getCloseOnKeyDate;
	
	/**
	 *  月別実績確認すべきデータ有無取得
	 * @author tutk
	 */
	public boolean checkDataMonPerConfirm(String employeeId) {
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
		boolean result = checkTrackRecord.checkTrackRecord(CID, employeeId, listCheckTargetItem.stream().map(c -> new CheckTargetItemDto(c.getClosureId(), c.getYearMonth())).collect(Collectors.toList()));
		return result;
	}
}
