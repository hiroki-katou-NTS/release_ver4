package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface InterimBreakDayOffMngRepository {
	
	/**
	 * 暫定休出管理データ
	 * @param breakManaId
	 * @return
	 */
	Optional<InterimBreakMng> getBreakManaBybreakMngId(String breakManaId);
	
	/**
	 * 暫定代休管理データ
	 * @param dayOffManaId
	 * @return
	 */
	Optional<InterimDayOffMng> getDayoffById(String dayOffManaId);
	
	/**
	 * 暫定休出代休紐付け管理
	 * @param mngId
	 * @param breakDay: True: 休出管理データ, false: 代休管理データ
	 * @return
	 */
	List<InterimBreakDayOffMng> getBreakDayOffMng(String mngId, boolean breakDay, DataManagementAtr mngAtr);
	
	/**
	 * 
	 * @param mngId
	 * @param unUseDays ・未使用日数＞unUseDays
	 * @param dateData ・使用期限日が指定期間内
	 * ・使用期限日≧INPUT.期間.開始年月日
	 * ・使用期限日≦INPUT.期間.終了年月日
	 * @return
	 */
	List<InterimBreakMng> getByPeriod(List<String> mngId, double unUseDays, DatePeriod dateData);
	
	/**
	 * 暫定休出管理データ 　を追加および更新
	 * @param domain
	 */
	void persistAndUpdateInterimBreakMng(InterimBreakMng domain);
	
	/**
	 * 暫定休出管理データ  を削除
	 * @param mngId
	 */
	void deleteInterimBreakMng(String mngId);
	
	/**
	 * 暫定休出管理データ  を削除
	 * @param list mngId
	 */
	void deleteInterimBreakMng(List<String> mngIds);
	
	/**
	 * 暫定代休管理データ 　を追加および更新
	 * @param domain
	 */
	void persistAndUpdateInterimDayOffMng(InterimDayOffMng domain);
	
	/**
	 * 暫定代休管理データ  を削除
	 * @param mngId
	 */
	void deleteInterimDayOffMng(String mngId);
	
	/**
	 * 暫定代休管理データ  を削除
	 * @param list mngId
	 */
	void deleteInterimDayOffMng(List<String> mngIds);
	
	/**
	 * 暫定休出代休紐付け管理 　を追加および更新
	 * @param domain
	 */
	void persistAndUpdateInterimBreakDayOffMng(InterimBreakDayOffMng domain);
	
	/**
	 * 暫定休出代休紐付け管理  を削除
	 * @param mngId
	 * @param isBreak：True：　休出、False：代休
	 */
	void deleteBreakDayOffById(String mngId, boolean isBreak);
	
	/**
	 * 暫定休出代休紐付け管理 を削除
	 * @param breakId　休出
	 * @param dayOffId　代休
	 * @param breakAtr　休出管理データ区分
	 * @param dayOffAtr　休出管理データ区分
	 */
	void deleteBreakDayOfByIdAndAtr(String breakId, String dayOffId, DataManagementAtr breakAtr, DataManagementAtr dayOffAtr);
	
	/**
	 * 暫定休出代休紐付け管理  を削除
	 * @param mngId
	 * @param mngAtr
	 * @param isBreak
	 */
	void deleteBreakDayOfById(String mngId, DataManagementAtr mngAtr, boolean isBreak);
	/**
	 * ドメインモデル「暫定休出代休紐付け管理」を取得する
	 * @param breakAtr 休出状態
	 * @param dayOffAtr 代休状態
	 * @param dayOffId 代休＝INPUT.代休管理データ.代休データID
	 * @return
	 */
	List<InterimBreakDayOffMng> getDayOffByIdAndDataAtr(DataManagementAtr breakAtr, DataManagementAtr dayOffAtr, String dayOffId);
	/**
	 * ドメインモデル「暫定休出代休紐付け管理」を取得する
	 * @param breakAtr 休出状態
	 * @param dayOffAtr 代休状態
	 * @param breakId 休出ID
	 * @return
	 */
	List<InterimBreakDayOffMng> getBreakByIdAndDataAtr(DataManagementAtr breakAtr, DataManagementAtr dayOffAtr, String breakId);
}
