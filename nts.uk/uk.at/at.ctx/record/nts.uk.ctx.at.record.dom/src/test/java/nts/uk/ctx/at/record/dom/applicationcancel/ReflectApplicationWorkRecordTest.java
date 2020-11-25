package nts.uk.ctx.at.record.dom.applicationcancel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.AuthcMethod;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.RefectActualResult;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Relieve;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampMeans;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeCalArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ChangeClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SetPreClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampType;
import nts.uk.ctx.at.shared.dom.application.common.ApplicationShare;
import nts.uk.ctx.at.shared.dom.application.common.ApplicationTypeShare;
import nts.uk.ctx.at.shared.dom.application.common.PrePostAtrShare;
import nts.uk.ctx.at.shared.dom.application.common.ReflectedStateShare;
import nts.uk.ctx.at.shared.dom.application.common.StampRequestModeShare;
import nts.uk.ctx.at.shared.dom.application.reflect.ReflectStatusResultShare;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.RCCreateDailyAfterApplicationeReflect;
import nts.uk.ctx.at.shared.dom.application.stamp.AppRecordImageShare;
import nts.uk.ctx.at.shared.dom.application.stamp.AppStampShare;
import nts.uk.ctx.at.shared.dom.application.stamp.EngraveShareAtr;
import nts.uk.ctx.at.shared.dom.workrule.goingout.GoingOutReason;
import nts.uk.shr.com.time.AttendanceClock;

@RunWith(JMockit.class)
public class ReflectApplicationWorkRecordTest {

	@Injectable
	private ReflectApplicationWorkRecord.Require require;

	/*
	 * テストしたい内容
	 * 
	 * → 打刻申請（NRモード）を反映する
	 * 
	 * → レコーダイメージ申請の対象日を取得する
	 * 
	 * → 勤務実績の更新
	 * 
	 * 準備するデータ
	 * 
	 * → 打刻申請モード = レコーダイメージ申請
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test(@Mocked TimeStampApplicationNRMode nrMode, @Mocked GetTargetDateRecordApplication app) {

		ApplicationShare application = ReflectApplicationHelper.createAppShare(ApplicationTypeShare.STAMP_APPLICATION,
				PrePostAtrShare.POSTERIOR, GeneralDate.ymd(2020, 01, 01));
		AppRecordImageShare appImg = new AppRecordImageShare(EngraveShareAtr.ATTENDANCE, new AttendanceClock(1),
				Optional.empty(), application);

		appImg.setOpStampRequestMode(Optional.of(StampRequestModeShare.STAMP_ONLINE_RECORD));

		ReflectStatusResultShare reflectStatus = new ReflectStatusResultShare();
		reflectStatus.setReflectStatus(ReflectedStateShare.WAITREFLECTION);

		new Expectations() {
			{

				// 「 打刻申請（NRモード）を反映する」のテスト呼び出す
				TimeStampApplicationNRMode.process(require, (GeneralDate) any, (AppRecordImageShare) any,
						(DailyRecordOfApplication) any, (Optional<Stamp>) any);
				times = 1;

				// 「レコーダイメージ申請の対象日を取得する」のテスト呼び出す
				GetTargetDateRecordApplication.getTargetDate(require, (AppRecordImageShare) any);
				result = Pair.of(Optional.of(GeneralDate.ymd(2020, 01, 01)), Optional.of(createStamp()));
				times = 1;

			}

		};

		val actualResult = ReflectApplicationWorkRecord.process(require, appImg, GeneralDate.ymd(2020, 01, 01),
				reflectStatus);

		assertThat(actualResult.getLeft().getReflectStatus()).isEqualTo(ReflectedStateShare.REFLECTED);

		NtsAssert.atomTask(() -> actualResult.getRight().get(), any -> require.addAllDomain(any.get()));

	}

	/*
	 * テストしたい内容
	 * 
	 * → 申請の反映（勤務実績）
	 * 
	 * → 勤務実績の更新
	 * 
	 * 準備するデータ
	 * 
	 * → 打刻申請モード != レコーダイメージ申請
	 * 
	 */
	@Test
	public void testReflectAll(@Mocked RCCreateDailyAfterApplicationeReflect reflect) {

		AppStampShare appImg = ReflectApplicationHelper.createAppStamp(PrePostAtrShare.POSTERIOR);

		appImg.setOpStampRequestMode(Optional.empty());

		ReflectStatusResultShare reflectStatus = new ReflectStatusResultShare();
		reflectStatus.setReflectStatus(ReflectedStateShare.WAITREFLECTION);

		new Expectations() {
			{

				// 「[RQ667]申請反映後の日別勤怠(work）を作成する（勤務実績）」のテスト呼び出す
				RCCreateDailyAfterApplicationeReflect.process(require, (ApplicationShare) any,
						(DailyRecordOfApplication) any, (GeneralDate) any);
				times = 1;

			}

		};

		val actualResult = ReflectApplicationWorkRecord.process(require, appImg, GeneralDate.ymd(2020, 01, 01),
				reflectStatus);

		assertThat(actualResult.getLeft().getReflectStatus()).isEqualTo(ReflectedStateShare.REFLECTED);

		NtsAssert.atomTask(() -> actualResult.getRight().get(), any -> require.addAllDomain(any.get()));

	}

	public Stamp createStamp() {
		return new Stamp(new ContractCode("1"), // 契約コード
				new StampNumber("1"), // 打刻カード
				GeneralDateTime.ymdhms(2020, 01, 01, 10, 0, 0), // 打刻日時
				new Relieve(AuthcMethod.ID_AUTHC, // 打刻する方法. 認証方法
						StampMeans.NAME_SELECTION), /// 打刻する方法. 打刻手段
				new StampType(false, GoingOutReason.valueOf(1), // 外出理由
						SetPreClockArt.NONE, // 所定時刻セット区分
						ChangeClockArt.GOING_TO_WORK, // 時刻変更区分
						ChangeCalArt.NONE), // 計算区分変更対象
				new RefectActualResult(null, null, null, null), Optional.empty());
	}
}