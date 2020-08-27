package nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.AbsRecMngInPeriodRefactParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.DaikyuFurikyuHelper;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.NumberRemainVacationLeaveRangeQueryTest;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.SelectedAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.vacation.setting.ApplyPermission;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;

@RunWith(JMockit.class)
public class ProcessCompenSuspensionAllTest {

	private static String CID = "000000000000-0117";

	private static String SID = "292ae91c-508c-4c6e-8fe8-3e72277dec16";

	@Injectable
	private ProcessCompenSuspensionAll.Require require;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	/*
	 * テストしたい内容
	 *　　暫定データから「逐次発生の休暇明細」を作成
	 * 準備するデータ
	 * 　  暫定振出振休紐付け管理がある
	 *             →　相殺済みデータ
	 *       モード : True: 月次か
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test() {

		new Expectations() {
			{

				//暫定振出振休紐付け管理
				require.getRecOrAbsMngs((List<String>) (any), anyBoolean, DataManagementAtr.INTERIM);
				result = Arrays.asList(
						createRecAbs("a1", 1.0),//使用日数 

						createRecAbs("a4", 1.0), //使用日数 
						createRecAbs("a5", 1.0), //使用日数 
						createRecAbs("a6", 1.0));//使用日数 

				require.findEmploymentHistory(CID, SID, (GeneralDate) any);
				result = Optional.of(new BsEmploymentHistoryImport(SID, "00", "A",
						new DatePeriod(GeneralDate.min(), GeneralDate.max())));

//				require.getClosureDataByEmployee(SID, (GeneralDate) any);
//				result = NumberRemainVacationLeaveRangeQueryTest.createClosure();

				require.getFirstMonth(CID);
				result = new CompanyDto(11);

				require.findEmpById(anyString, anyString);
				result = Optional.of(new EmpSubstVacation(CID, "00", 
						new SubstVacationSetting(ManageDistinct.YES,
						ExpirationTime.THIS_MONTH, 
						ApplyPermission.ALLOW)));

			}
		};

		List<InterimAbsMng> useAbsMng = Arrays.asList(DaikyuFurikyuHelper.createAbsMng("a1", 1.0),//未相殺日数
				DaikyuFurikyuHelper.createAbsMng("a2", 0.5), DaikyuFurikyuHelper.createAbsMng("a3", 1.0));//未相殺日数

		List<InterimRecMng> useRecMng = Arrays.asList(DaikyuFurikyuHelper.createRecMng("a4", GeneralDate.max(), // 期限日
				1.0), // 発生日数
				DaikyuFurikyuHelper.createRecMng("a5", GeneralDate.max(), 1.0),
				DaikyuFurikyuHelper.createRecMng("a6", GeneralDate.max(), 1.0),
				DaikyuFurikyuHelper.createRecMng("a7", GeneralDate.max(), 1.0),
				DaikyuFurikyuHelper.createRecMng("a8", GeneralDate.max(), 0.5),
				DaikyuFurikyuHelper.createRecMng("a9", GeneralDate.ymd(2010, 10, 4), 1.0));

		List<InterimRemain> interimMng = Arrays.asList(
				DaikyuFurikyuHelper.createRemain("a1", GeneralDate.ymd(2019, 11, 4), CreateAtr.SCHEDULE,
						RemainType.PAUSE),
				DaikyuFurikyuHelper.createRemain("a2", GeneralDate.ymd(2019, 11, 6), CreateAtr.RECORD,
						RemainType.PAUSE),
				DaikyuFurikyuHelper.createRemain("a3", GeneralDate.ymd(2019, 11, 7), CreateAtr.RECORD,
						RemainType.PAUSE),

				DaikyuFurikyuHelper.createRemain("a4", GeneralDate.ymd(2019, 11, 5), CreateAtr.SCHEDULE,
						RemainType.PICKINGUP),
				DaikyuFurikyuHelper.createRemain("a5", GeneralDate.ymd(2019, 11, 14), CreateAtr.RECORD,
						RemainType.PICKINGUP),
				DaikyuFurikyuHelper.createRemain("a6", GeneralDate.ymd(2019, 11, 15), CreateAtr.RECORD,
						RemainType.PICKINGUP),
				DaikyuFurikyuHelper.createRemain("a7", GeneralDate.ymd(2019, 11, 13), CreateAtr.RECORD,
						RemainType.PICKINGUP),
				DaikyuFurikyuHelper.createRemain("a8", GeneralDate.ymd(2019, 11, 11), CreateAtr.RECORD,
						RemainType.PICKINGUP),
				DaikyuFurikyuHelper.createRemain("a9", GeneralDate.ymd(2019, 11, 16), CreateAtr.RECORD,
						RemainType.PICKINGUP));

		AbsRecMngInPeriodRefactParamInput inputParam = new AbsRecMngInPeriodRefactParamInput(CID, SID, 
				new DatePeriod(GeneralDate.ymd(2019, 11, 01), GeneralDate.ymd(2020, 10, 31)),
				GeneralDate.ymd(2019, 11, 30), true, true, useAbsMng, interimMng, useRecMng,
				Optional.empty(), Optional.empty(), Optional.empty(),
				new FixedManagementDataMonth(new ArrayList<>(), new ArrayList<>()));

		List<AccumulationAbsenceDetail> actual = ProcessCompenSuspensionAll.process(require, inputParam);

		assertThat(actual)
				.extracting(x -> x.getManageId(), 
						x -> x.getDateOccur().isUnknownDate(), x -> x.getDateOccur().getDayoffDate(),//年月日
						x -> x.getOccurrentClass(), //発生消化区分
						x -> x.getUnbalanceNumber().getDay().v(),//未相殺数
						x -> x.getUnbalanceNumber().getTime())//未相殺数
				.containsExactly(
						Tuple.tuple("a1", false,
								Optional.of(GeneralDate.ymd(2019, 11, 04)),
								OccurrenceDigClass.DIGESTION, 0.0, Optional.empty()),

						Tuple.tuple("a2", false,
								Optional.of(GeneralDate.ymd(2019, 11, 6)),
								OccurrenceDigClass.DIGESTION, 0.5, Optional.empty()),

						Tuple.tuple("a3",  false,
								Optional.of(GeneralDate.ymd(2019, 11, 7)), 
								OccurrenceDigClass.DIGESTION, 1.0, Optional.empty()),

						Tuple.tuple("a4", false,
								Optional.of(GeneralDate.ymd(2019, 11, 5)), 
								OccurrenceDigClass.OCCURRENCE, 0.0, Optional.empty()),

						Tuple.tuple("a5", false,
								Optional.of(GeneralDate.ymd(2019, 11, 14)),
								OccurrenceDigClass.OCCURRENCE, 0.0, Optional.empty()),

						Tuple.tuple("a6", false,
								Optional.of(GeneralDate.ymd(2019, 11, 15)), 
								OccurrenceDigClass.OCCURRENCE, 0.0, Optional.empty()),
						
						Tuple.tuple("a7", false,
								Optional.of(GeneralDate.ymd(2019, 11, 13)),
								OccurrenceDigClass.OCCURRENCE, 1.0, Optional.empty()),

						Tuple.tuple("a8", false,
								Optional.of(GeneralDate.ymd(2019, 11, 11)),
								OccurrenceDigClass.OCCURRENCE, 0.5, Optional.empty()),

						Tuple.tuple("a9", false,
								Optional.of(GeneralDate.ymd(2019, 11, 16)),
								OccurrenceDigClass.OCCURRENCE, 1.0, Optional.empty()));
	}
	
	private InterimRecAbsMng createRecAbs(String id, Double useDay) {
		return new InterimRecAbsMng(id,
				DataManagementAtr.INTERIM, id, DataManagementAtr.INTERIM, new UseDay(useDay), SelectedAtr.MANUAL);
	}
	

}
