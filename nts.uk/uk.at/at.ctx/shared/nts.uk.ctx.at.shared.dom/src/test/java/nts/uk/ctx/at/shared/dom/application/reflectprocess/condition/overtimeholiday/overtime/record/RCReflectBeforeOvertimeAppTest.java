package nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.overtimeholiday.overtime.record;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.common.ReflectApplicationHelper;
import nts.uk.ctx.at.shared.dom.scherec.application.overtime.AttendanceTypeShare;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.otworkapply.BeforeOtWorkAppReflect;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author thanh_nx
 *
 *         事前残業申請の反映（勤務実績）
 */
@RunWith(JMockit.class)
public class RCReflectBeforeOvertimeAppTest {

	@Injectable
	private BeforeOtWorkAppReflect.Require require;

	/*
	 * テストしたい内容
	 * 
	 * 
	 * →勤務情報、始業終業の反映が出来る
	 * 
	 * 
	 * 準備するデータ
	 * 
	 * →[勤務情報、始業終業を反映する] = する
	 * 
	 */
	@Test
	public void test1() {

		val overTimeApp = ReflectApplicationHelper.createOverTimeAppNone();// 勤務情報 = ("003", "003")
		DailyRecordOfApplication dailyApp = ReflectApplicationHelper
				.createRCWithTimeLeavFull(ScheduleRecordClassifi.RECORD, 1);// 勤務情報 = ("001", "001")
		val reflectOvertimeBeforeSet = BeforeOtWorkAppReflect.create(1, 0, 0);
		reflectOvertimeBeforeSet.processRC(require, "", overTimeApp, dailyApp);
		assertThat(dailyApp.getWorkInformation().getRecordInfo().getWorkTimeCode().v()).isEqualTo("003");// 就業時間帯コード
		assertThat(dailyApp.getWorkInformation().getRecordInfo().getWorkTypeCode().v()).isEqualTo("003");// 勤務種類コード

	}

	/*
	 * テストしたい内容
	 * 
	 * 
	 * →勤務情報、始業終業の反映が出来ない
	 * 
	 * 
	 * 準備するデータ
	 * 
	 * →[勤務情報、始業終業を反映する] = しない
	 * 
	 */
	@Test
	public void test2() {

		val overTimeApp = ReflectApplicationHelper.createOverTimeAppNone();// 勤務情報 = ("003", "003")
		DailyRecordOfApplication dailyApp = ReflectApplicationHelper
				.createRCWithTimeLeavFull(ScheduleRecordClassifi.RECORD, 1);// 勤務情報 = ("001", "001")
		val reflectOvertimeBeforeSet = BeforeOtWorkAppReflect.create(0, 0, 0);
		reflectOvertimeBeforeSet.processRC(require, "", overTimeApp, dailyApp);
		assertThat(dailyApp.getWorkInformation().getRecordInfo().getWorkTimeCode().v()).isEqualTo("001");// 就業時間帯コード
		assertThat(dailyApp.getWorkInformation().getRecordInfo().getWorkTypeCode().v()).isEqualTo("001");// 勤務種類コード

	}

	/*
	 * テストしたい内容
	 * 
	 * 
	 * →残業時間を実績項目へ反映する
	 * 
	 * 
	 * 準備するデータ
	 * 
	 * →[残業時間を実績項目へ反映する]の設定 = する
	 * 
	 */
	@Test
	public void test3() {

		val overTimeApp = ReflectApplicationHelper.createOverTime(AttendanceTypeShare.NORMALOVERTIME, 120);// 残業休出申請.申請時間
																											// = 120
		DailyRecordOfApplication dailyApp = ReflectApplicationHelper
				.createRCWithTimeLeavFull(ScheduleRecordClassifi.RECORD, 1);// 勤務情報 = ("001", "001")
		val reflectOvertimeBeforeSet = BeforeOtWorkAppReflect.create(0, NotUseAtr.USE.value, 0);
		reflectOvertimeBeforeSet.processRC(require, "", overTimeApp, dailyApp);

		assertThat(dailyApp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
				.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()
				.getOverTimeWorkFrameTime().get(0).getOverTimeWork().getTime().v()).isEqualTo(120);

	}

	/*
	 * テストしたい内容
	 * 
	 * 
	 * →残業時間を実績項目へ反映しない
	 * 
	 * 
	 * 準備するデータ
	 * 
	 * →[残業時間を実績項目へ反映する]の設定 = しない
	 * 
	 */
	@Test
	public void test4() {

		val overTimeApp = ReflectApplicationHelper.createOverTime(AttendanceTypeShare.NORMALOVERTIME, 120);// 残業休出申請.申請時間
																											// = 120
		DailyRecordOfApplication dailyApp = ReflectApplicationHelper
				.createRCWithTimeLeavFull(ScheduleRecordClassifi.RECORD, 1);// 勤務情報 = ("001", "001")
		val reflectOvertimeBeforeSet = BeforeOtWorkAppReflect.create(0, NotUseAtr.NOT_USE.value, 0);
		reflectOvertimeBeforeSet.processRC(require, "", overTimeApp, dailyApp);

		assertThat(dailyApp.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
				.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get()
				.getOverTimeWorkFrameTime().get(0).getOverTimeWork().getTime().v()).isEqualTo(0);

	}
}
