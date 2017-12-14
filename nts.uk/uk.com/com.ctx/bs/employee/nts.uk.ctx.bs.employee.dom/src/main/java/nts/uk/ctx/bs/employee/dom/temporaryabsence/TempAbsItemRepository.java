package nts.uk.ctx.bs.employee.dom.temporaryabsence;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface TempAbsItemRepository {

	// ------------------------------GET HISTORY ITEM
	/**
	 * get TempAbsenceHisItem by history Id
	 * 
	 * @param historyId
	 * @return
	 */
	Optional<TempAbsenceHisItem> getItemByHitoryID(String historyId);
	
	/**
	 * get with employeeId and standardDate
	 * @param employeeId
	 * @param standardDate
	 * @return
	 */
	Optional<TempAbsenceHisItem> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate);

	/**
	 * get List of TempAbsenceHisItem by employeeId
	 * 
	 * @param sid
	 * @return
	 */
	List<TempAbsenceHisItem> getListItemByEmpId(String employeeId);

	// ------------------------------ COMMAND HISTORY ITEM
	/**
	 * ドメインモデル「休職休業」を新規登録する
	 * 
	 * @param domain
	 */
	void add(TempAbsenceHisItem domain);

	/**
	 * 取得した「休職休業」を更新する
	 * 
	 * @param domain
	 */
	void update(TempAbsenceHisItem domain);

	/**
	 * ドメインモデル「休職休業」を削除する
	 * 
	 * @param domain
	 */
	void delete(String histId);

}
