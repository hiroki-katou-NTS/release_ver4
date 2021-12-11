package nts.uk.ctx.at.shared.dom.remainingnumber.paymana;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
//import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.arc.time.calendar.period.DatePeriod;

public interface SubstitutionOfHDManaDataRepository {
	
	// ドメインモデル「振休管理データ」を取得
	List<SubstitutionOfHDManagementData> getBysiDRemCod(String cid, String sid);
	
	Map<String ,Double> getAllBysiDRemCod(String cid, List<String> sids);
	// ドメインモデル「振休管理データ」を作成する
	void create(SubstitutionOfHDManagementData domain);
	
	// ドメインモデル「振休管理データ」を作成する
	void addAll(List<SubstitutionOfHDManagementData> domains);
	
	/**
	 * 
	 * @param cid
	 * @param sid
	 * @param ymd 振休日<INPUT．集計開始日
	 * @return
	 */
	List<SubstitutionOfHDManagementData> getBySidDate(String cid, String sid, GeneralDate ymd);
	
	List<SubstitutionOfHDManagementData> getBysiD(String cid, String sid);
	
	List<SubstitutionOfHDManagementData> getBysiDAndAtr(String cid, String sid);
	
	List<SubstitutionOfHDManagementData> getBySidsAndCid(String cid, List<String> sids);
	
//	void deletePayoutSubOfHDMana(String subOfHDID);
	
	void delete(String subOfHDID);
	
	void deleteAllByEmployeeId(String employeeId);
	
	void update(SubstitutionOfHDManagementData domain);
	
	Optional<SubstitutionOfHDManagementData> findByID(String Id);
	
	Optional<SubstitutionOfHDManagementData> find(String cID,String sID,GeneralDate holidayDate );
	
	List<SubstitutionOfHDManagementData> getBySidListHoliday (String cID, String sid, List<GeneralDate> holidayDate);
	
	// ドメイン「振休管理データ」より紐付け対象となるデータを取得する
	List<SubstitutionOfHDManagementData> getBySidDatePeriod(String sid,String payoutID, Double remainDays);
	
	List<SubstitutionOfHDManagementData> getBySidRemainDayAndInPayout(String sid);
	
	List<SubstitutionOfHDManagementData> getBySidPeriodAndInPayout(String sid, GeneralDate startDate, GeneralDate endDate);
	/**
	 * ドメインモデル「振休管理データ」を取得する
	 * @param sid
	 * @param dateData ・振休日が指定期間内
	 * ・振休日≧INPUT.期間.開始年月日
	 * ・振休日≦INPUT.期間.終了年月日
	 * @return
	 */
	List<SubstitutionOfHDManagementData> getBySidAndDatePeriod(String sid, DatePeriod dateData);
	/**
	 * ドメインモデル「振休管理データ」を取得
	 * @param sid
	 * @param remainDays 未相殺日数>remainDays
	 * @return
	 */
	List<SubstitutionOfHDManagementData> getByRemainDays(String sid, double remainDays);
	
	List<SubstitutionOfHDManagementData> getByHoliday(String sid, Boolean unknownDate, DatePeriod dayoffDate);
	
	void deleteById(List<String> subOfHDID);
	/**
	 * 
	 * @param cid
	 * @param sid
	 * @param ymd 振休日<INPUT．集計開始日
	 * @param unOffseDays 未相殺日数>0
	 * @return
	 */
	List<SubstitutionOfHDManagementData> getByYmdUnOffset(String cid, String sid, GeneralDate ymd, double unOffseDays);
	
	List<SubstitutionOfHDManagementData> getAllBySid(String sid);
	
	void delete(List<SubstitutionOfHDManagementData> listItem);
	
	List<SubstitutionOfHDManagementData> getByListId(List<String> subOfHDIDs);

}
