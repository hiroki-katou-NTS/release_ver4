package nts.uk.ctx.bs.employee.dom.temporaryabsence;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;

public interface TempAbsItemRepository {

	// ------------------------------GET HISTORY ITEM
	/**
	 * Get by employee Id and reference Date
	 * 
	 * @param sid
	 * @param referenceDate
	 * @return
	 */
	Optional<TempAbsenceHisItem> getItemByEmpIdAndReferDate(String employeeId, GeneralDate referenceDate);

	/**
	 * get TempAbsenceHisItem by history Id
	 * 
	 * @param historyId
	 * @return
	 */
	Optional<TempAbsenceHisItem> getItemByHitoryID(String historyId);

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
	void addTemporaryAbsence(TempAbsenceHisItem domain);

	/**
	 * 取得した「休職休業」を更新する
	 * 
	 * @param domain
	 */
	void updateTemporaryAbsence(TempAbsenceHisItem domain);

	/**
	 * ドメインモデル「休職休業」を削除する
	 * 
	 * @param domain
	 */
	void deleteTemporaryAbsence(String histId);

}
