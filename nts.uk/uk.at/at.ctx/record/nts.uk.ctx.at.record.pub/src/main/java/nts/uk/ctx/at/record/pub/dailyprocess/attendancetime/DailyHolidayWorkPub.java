package nts.uk.ctx.at.record.pub.dailyprocess.attendancetime;

/**
 * 日別実績の休出時間に関するサービス
 * @author keisuke_hoshina
 *
 */
public interface DailyHolidayWorkPub {

	//RequestList No.XXXX
	public DailyHolidayWorkPubExport calcHolidayWorkTransTime(DailyHolidayWorkPubImport imp);
	
}
