package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim;

import java.util.List;
import java.util.Optional;

import nts.arc.time.calendar.period.DatePeriod;

public interface TmpAnnualHolidayMngRepository {
	/**
	 * 検索　By　ID
	 * @param mngId
	 * @return
	 */
	public Optional<TmpAnnualHolidayMng> getById(String mngId);
	/**
	 * 削除
	 * @param mngId
	 */
	public void deleteById(String mngId);
	/**
	 * 登録および更新
	 * @param dataMng
	 */
	public void persistAndUpdate(TmpAnnualHolidayMng dataMng);
	/**
	 * 検索　 暫定年休管理データ
	 * @param sid　社員ID
	 * @param period 期間
	 * @return
	 */
	List<TmpAnnualHolidayMng> getBySidPeriod(String sid, DatePeriod period);
}
