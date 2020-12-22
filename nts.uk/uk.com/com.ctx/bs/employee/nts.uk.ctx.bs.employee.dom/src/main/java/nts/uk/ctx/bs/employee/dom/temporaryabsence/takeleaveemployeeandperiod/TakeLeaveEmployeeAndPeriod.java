package nts.uk.ctx.bs.employee.dom.temporaryabsence.takeleaveemployeeandperiod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsHistRepository;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsItemRepository;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsenceHisItem;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.TempAbsenceHistory;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 社員（List）と期間から休職休業を取得する
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.基幹.社員.休職休業.アルゴリズム.Query.社員（List）と期間から休職休業を取得する.社員（List）と期間から休職休業を取得する
 * @author tutk
 *
 */
@Stateless
public class TakeLeaveEmployeeAndPeriod {
	@Inject
	private TempAbsHistRepository tempAbsHistRepository;
	
	@Inject
	private TempAbsItemRepository tempAbsItemRepository;
	
	public Map<DateHistoryItem, TempAbsenceHisItem> takeLeaveEmployeeAndPeriod(List<String> listEmp,DatePeriod period) {
		Map<DateHistoryItem, TempAbsenceHisItem> mapData = new HashMap<>();
		//ドメインモデル「休職休業履歴」を取得する(Get domain model 「TempAbsenceHistory」)
		//パラメータ．期間を含む履歴項目の履歴IDを取得する (Get ID lịch sử của item lịch sử bao gồm Parameter. period)
		List<TempAbsenceHistory>  listTempAbsenceHistory  = tempAbsHistRepository.getByListSid(listEmp, period);
		for(TempAbsenceHistory tempAbsenceHistory :listTempAbsenceHistory) {
			List<DateHistoryItem> listDateHistoryItem = tempAbsenceHistory.getDateHistoryItems();
			for(DateHistoryItem dhi :listDateHistoryItem) {
				//ドメインモデル「休職休業履歴項目」を取得する(Get domain model 「Item TempAbsenceHistory」)
				Optional<TempAbsenceHisItem> tempAbsenceHisItem = tempAbsItemRepository.getItemByHitoryID(dhi.identifier());
				if(tempAbsenceHisItem.isPresent()) {
					//取得した「休職休業履歴項目」と、履歴IDの一致する「休職休業履歴」をセットで返す (Trả về bằng set「TempAbsenceHistory」khớp với lịch sử ID và 「Item TempAbsenceHistoryỉ」 đã get )
					mapData.put(dhi, tempAbsenceHisItem.get());
				}
			}
		}
		return mapData;
	}
}
