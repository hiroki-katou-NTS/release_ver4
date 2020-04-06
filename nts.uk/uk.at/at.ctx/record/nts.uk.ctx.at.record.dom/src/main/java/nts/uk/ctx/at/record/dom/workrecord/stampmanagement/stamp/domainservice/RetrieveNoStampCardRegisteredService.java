package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;

/**
 * DS : 打刻カード未登録の打刻データを取得する
 * 
 * @author tutk
 *
 */
public class RetrieveNoStampCardRegisteredService {

	public static List<StampInfoDisp> get(Require require, DatePeriod period) {
		List<StampRecord> listStampRecord = require.getStempRcNotResgistNumber(period);
		List<Stamp> listStamp = require.getStempRcNotResgistNumberStamp(period);
		return createStampInfoDisplay(listStampRecord, listStamp);
	}

	/**
	 * [prv-1] 表示する打刻情報を作成する
	 * 
	 * @param listStampRecord
	 * @param listStamp
	 * @return
	 */
	private static List<StampInfoDisp> createStampInfoDisplay(List<StampRecord> listStampRecord,
			List<Stamp> listStamp) {
		List<StampInfoDisp> datas = new ArrayList<>();
		for (StampRecord stampRecord : listStampRecord) {
			for (Stamp stamp : listStamp) {
				if (stampRecord.getStampNumber().equals(stamp.getCardNumber())
						&& stampRecord.getStampDateTime().clockHourMinuteSecond().equals(stamp.getStampDateTime().clockHourMinuteSecond())) {
					datas.add(new StampInfoDisp(stampRecord.getStampNumber(), stampRecord.getStampDateTime(),
							stampRecord, Optional.of(stamp)));
					break;
				}
			}
		}
		return datas;

	}

	public static interface Require {
		/**
		 * [R-1] 打刻記録を取得する StampRecordRepository
		 * 
		 * @param period
		 * @return
		 */
		public List<StampRecord> getStempRcNotResgistNumber(DatePeriod period);

		/**
		 * [R-2] 打刻を取得する StampDakokuRepository
		 * 
		 * @param period
		 * @return
		 */
		public List<Stamp> getStempRcNotResgistNumberStamp(DatePeriod period);

	}
}
