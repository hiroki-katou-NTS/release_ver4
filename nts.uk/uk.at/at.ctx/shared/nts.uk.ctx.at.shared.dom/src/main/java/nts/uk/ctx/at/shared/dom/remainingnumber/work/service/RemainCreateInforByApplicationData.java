package nts.uk.ctx.at.shared.dom.remainingnumber.work.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.AppRemainCreateInfor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface RemainCreateInforByApplicationData {
	/**
	 * 残数作成元の申請を取得する
	 * @param cid
	 * @param sid
	 * @param dateData 期間
	 * @return
	 */
	List<AppRemainCreateInfor> lstRemainDataFromApp(String cid, String sid, DatePeriod dateData);
	/**
	 * 残数作成元の申請を取得する
	 * @param cid
	 * @param sid
	 * @param dateData　リスト
	 * @return
	 */
	List<AppRemainCreateInfor> lstRemainDataFromApp(String cid, String sid, List<GeneralDate> dates);
	/**
	 * 休日を除外する 1: 除く, 0: 除かない
	 * @param appID
	 * @return
	 */
	Integer excludeHolidayAtr(String cid,String appID); 

}
