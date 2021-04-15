package nts.uk.ctx.at.request.dom.applicationreflect.algorithm.reflectprocess;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.stamp.AppStamp;
import nts.uk.ctx.at.request.dom.applicationreflect.AppReflectExecutionCondition;
import nts.uk.ctx.at.request.dom.applicationreflect.algorithm.checkprocess.PreCheckProcessWorkRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.algorithm.checkprocess.PreCheckProcessWorkSchedule.PreCheckProcessResult;
import nts.uk.ctx.at.request.dom.applicationreflect.algorithm.common.ReflectApplicationHelper;
import nts.uk.ctx.at.request.dom.applicationreflect.object.PreApplicationWorkScheReflectAttr;
import nts.uk.ctx.at.request.dom.applicationreflect.object.ReflectStatusResult;
import nts.uk.ctx.at.shared.dom.scherec.application.common.ApplicationShare;
import nts.uk.ctx.at.shared.dom.scherec.application.common.ReflectedStateShare;
import nts.uk.ctx.at.shared.dom.scherec.application.reflect.ReflectStatusResultShare;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@RunWith(JMockit.class)
public class ProcessReflectWorkRecordTest {

	@Injectable	
	private ProcessReflectWorkRecord.Require require;

	private String companyId;

	private Integer closureId;

	private ReflectStatusResult statusWorkRecord;

	private GeneralDate dateRefer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		companyId = "cid";
		closureId = 1;
		statusWorkRecord = new ReflectStatusResult(ReflectedState.NOTREFLECTED, null, null);
		dateRefer = GeneralDate.ymd(2020, 05, 10);
	}

	/*
	 * テストしたい内容
	 * 
	 * →勤務予定の反映状態が「反映済み」
	 * 
	 * 準備するデータ
	 * 
	 * → 「申請反映実行条件.勤務実績が確定状態でも反映する」の設定= する。
	 * 
	 */
	@Test
	public void testWorkReflected() {
		Application application = ReflectApplicationHelper.createApp(PrePostAtr.POSTERIOR);
		AppStamp stamp = new AppStamp(application);
		stamp.setListDestinationTimeApp(new ArrayList<>());
		stamp.setListDestinationTimeZoneApp(new ArrayList<>());
		stamp.setListTimeStampApp(new ArrayList<>());
		stamp.setListTimeStampAppOther(new ArrayList<>());
		new Expectations() {
			{
				require.findAppReflectExecCond(companyId);
				result = Optional
						.of(new AppReflectExecutionCondition(companyId, PreApplicationWorkScheReflectAttr.NOT_REFLECT, NotUseAtr.NOT_USE, NotUseAtr.USE));// 勤務実績が確定状態でも反映する

				require.processWork((ApplicationShare)any, dateRefer, (ReflectStatusResultShare) any);
				result = Pair.of(new ReflectStatusResultShare(ReflectedStateShare.REFLECTED, null, null),
						Optional.empty());

			}
		};

		val actualResult = ProcessReflectWorkRecord.processReflect(require, companyId, closureId,  stamp,
				true, dateRefer, statusWorkRecord);

		assertThat(actualResult.getLeft().getReflectStatus()).isEqualTo(ReflectedState.REFLECTED);
	}

	/*
	 * テストしたい内容
	 * 
	 * →勤務予定の反映状態が「未反映」
	 * 
	 * 準備するデータ
	 * 
	 * → 「申請反映実行条件.勤務実績が確定状態でも反映する」の設定= しない。
	 * 
	 * → 事前チェック後、反映ができない
	 * 
	 */
	@Test
	public void testBeforeNoReflect(@Mocked PreCheckProcessWorkRecord preCheck) {
		Application application = ReflectApplicationHelper.createApp(PrePostAtr.POSTERIOR);
		AppStamp stamp = new AppStamp(application);
		stamp.setListDestinationTimeApp(new ArrayList<>());
		stamp.setListDestinationTimeZoneApp(new ArrayList<>());
		stamp.setListTimeStampApp(new ArrayList<>());
		stamp.setListTimeStampAppOther(new ArrayList<>());
		new Expectations() {
			{
				require.findAppReflectExecCond(companyId);
				result = Optional
						.of(new AppReflectExecutionCondition(companyId, PreApplicationWorkScheReflectAttr.NOT_REFLECT, NotUseAtr.NOT_USE, NotUseAtr.NOT_USE));// 勤務実績が確定状態でも反映する

				PreCheckProcessWorkRecord.preCheck(require, companyId, stamp, closureId, anyBoolean,
						(ReflectStatusResult) any, dateRefer);
				result = new PreCheckProcessResult(NotUseAtr.NOT_USE, statusWorkRecord);
			}
		};

		val actualResult = ProcessReflectWorkRecord.processReflect(require, companyId, closureId, stamp,
				true, dateRefer, statusWorkRecord);

		assertThat(actualResult.getLeft().getReflectStatus()).isEqualTo(ReflectedState.NOTREFLECTED);
	}

	/*
	 * テストしたい内容
	 * 
	 * →勤務予定の反映状態が「反映済み」
	 * 
	 * 準備するデータ
	 * 
	 * → 「申請反映実行条件.勤務実績が確定状態でも反映する」の設定= しない。
	 * 
	 * → 事前チェック後、反映ができる
	 * 
	 */
	@Test
	public void testBeforeReflect(@Mocked PreCheckProcessWorkRecord preCheck) {
		Application application = ReflectApplicationHelper.createApp(PrePostAtr.POSTERIOR);
		AppStamp stamp = new AppStamp(application);
		stamp.setListDestinationTimeApp(new ArrayList<>());
		stamp.setListDestinationTimeZoneApp(new ArrayList<>());
		stamp.setListTimeStampApp(new ArrayList<>());
		stamp.setListTimeStampAppOther(new ArrayList<>());
		new Expectations() {
			{
				require.findAppReflectExecCond(companyId);
				result = Optional
						.of(new AppReflectExecutionCondition(companyId, PreApplicationWorkScheReflectAttr.NOT_REFLECT, NotUseAtr.NOT_USE, NotUseAtr.NOT_USE));// 勤務実績が確定状態でも反映する

				PreCheckProcessWorkRecord.preCheck(require, companyId, stamp, closureId, anyBoolean,
						(ReflectStatusResult) any, dateRefer);
				result = new PreCheckProcessResult(NotUseAtr.USE, statusWorkRecord);
				
				require.processWork((ApplicationShare)any, dateRefer, (ReflectStatusResultShare) any);
				result = Pair.of(new ReflectStatusResultShare(ReflectedStateShare.REFLECTED, null, null),
						Optional.empty());
			}
		};

		val actualResult = ProcessReflectWorkRecord.processReflect(require, companyId, closureId, stamp,
				true, dateRefer, statusWorkRecord);

		assertThat(actualResult.getLeft().getReflectStatus()).isEqualTo(ReflectedState.REFLECTED);
	}
	
}
