package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;

/**
 * 元々日別作成側に存在していたエラーアラーム処理
 * @author keisuke_hoshina
 *
 */
public interface DailyRecordCreateErrorAlermService {

	//打刻漏れ
	public List<EmployeeDailyPerError> lackOfTimeLeavingStamping(IntegrationOfDaily integrationOfDaily);
	//入退門打刻漏れ
	public List<EmployeeDailyPerError> lackOfAttendanceGateStamping(IntegrationOfDaily integrationOfDaily);
	//PCログオン打刻漏れ
	public List<EmployeeDailyPerError> lackOfAttendancePCLogOnStamping(IntegrationOfDaily integrationOfDaily);
	//打刻順序不正
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithm(IntegrationOfDaily integrationOfDaily);
	//打刻順序不正(出退勤打刻の実)
	public EmployeeDailyPerError stampIncorrect(IntegrationOfDaily integrationOfDaily);
	//打刻順序不正(出退勤打刻以外)
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithmOtherStamp(IntegrationOfDaily integrationOfDaily);
	//打刻二重チェック
	public List<EmployeeDailyPerError> doubleStampAlgorithm(IntegrationOfDaily integrationOfDaily);
	//休日打刻チェック
	public Optional<EmployeeDailyPerError> checkHolidayStamp(IntegrationOfDaily integration);
}
