package nts.uk.ctx.at.shared.dom;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.shared.dom.WorkInformation.Require;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workrule.ErrorStatusWorkInfo;
import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.time.TimeWithDayAttr;

@RunWith(JMockit.class)
public class WorkInformationTest {

	@Injectable
	private Require require;

	@Test
	public void getters() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		NtsAssert.invokeGetters(workInformation);
	}

	@Test
	public void testRemoveWorkTimeInHolydayWorkType() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		workInformation.removeWorkTimeInHolydayWorkType();
		assertThat(workInformation.getWorkTimeCode()).isEqualTo(null);
	}

	@Test
	public void testWorkInformation() {
		WorkTimeCode workTimeCode = new WorkTimeCode("abc");
		WorkTypeCode workTypeCode = new WorkTypeCode("123");
		WorkInformation workInformation = new WorkInformation(workTimeCode, workTypeCode);
		assertThat(workInformation.getWorkTimeCode()).isEqualTo(workTimeCode);
		assertThat(workInformation.getWorkTypeCode()).isEqualTo(workTypeCode);
	}

	@Test
	public void testSetWorkTimeCode() {
		WorkTimeCode workTimeCode = new WorkTimeCode("abc");
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		workInformation.setWorkTimeCode(workTimeCode);
		assertThat(workInformation.getWorkTimeCode()).isEqualTo(workTimeCode);
	}

	@Test
	public void testSetWorkTypeCode() {
		WorkTypeCode workTypeCode = new WorkTypeCode("123");
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		workInformation.setWorkTypeCode(workTypeCode);
		assertThat(workInformation.getWorkTypeCode()).isEqualTo(workTypeCode);
	}

	@Test
	public void checkNormalCondition_is_true() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.NOT_ABOLISH);
		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.REQUIRED;

				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.empty();
				result = Optional.of(workTimeSetting);
			}
		};
		assertThat(workInformation.checkNormalCondition(require)).isTrue();
	}

	@Test
	public void checkNormalCondition_is_false() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
			}
		};
		assertThat(workInformation.checkNormalCondition(require)).isFalse();
	}

	@Test
	public void testCheckErrorCondition_is_WORKTYPE_WAS_DELETE() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
			}
		};
		assertThat(workInformation.checkErrorCondition(require)).isEqualTo(ErrorStatusWorkInfo.WORKTYPE_WAS_DELETE);
	}

	/**
	 * if SetupType = REQUIRED 
	 * @就業時間帯コード ==null
	 * 
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_ARE_REQUIRE_NOT_SET() {
		WorkInformation workInformation = new WorkInformation(null, "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.REQUIRED;
				
			}
		};

		assertThat(workInformation.checkErrorCondition(require)).isEqualTo(ErrorStatusWorkInfo.WORKTIME_ARE_REQUIRE_NOT_SET);
	}
	/**
	 * if SetupType = REQUIRED 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) empty
	 * 
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_WAS_DELETE() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.REQUIRED;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_WAS_DELETE);
	}
	
	
	/**
	 * if SetupType = REQUIRED 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 != 廃止する
	 * if $就業時間帯.廃止区分 == 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_HAS_BEEN_ABOLISHED() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.NotDeprecated);
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.ABOLISH);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.REQUIRED;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.of(workTimeSetting);
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_HAS_BEEN_ABOLISHED);
	}
	
	/**
	 * if SetupType = REQUIRED 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 != 廃止する
	 * if $就業時間帯.廃止区分 != 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_NORMAL() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.NotDeprecated);
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.NOT_ABOLISH);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.REQUIRED;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.of(workTimeSetting);
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.NORMAL);
	}
	
	/**
	 * if SetupType = OPTIONAL 
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) empty
	 * 
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_WAS_DELETE_1() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.OPTIONAL;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.empty();
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_WAS_DELETE);
	}
	
	/**
	 * 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 == 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTYPE_WAS_ABOLISHED_1() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.Deprecated);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(workType);
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTYPE_WAS_ABOLISHED);
	}
	
	/**
	 * if SetupType = OPTIONAL 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 != 廃止する
	 * if $就業時間帯.廃止区分 == 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_HAS_BEEN_ABOLISHED_1() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.NotDeprecated);
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.ABOLISH);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(workType);

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.OPTIONAL;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.of(workTimeSetting);
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_HAS_BEEN_ABOLISHED);
	}
	
	/**
	 * if SetupType = OPTIONAL 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 != 廃止する
	 * if $就業時間帯.廃止区分 != 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_NORMAL_1() {
		WorkInformation workInformation = new WorkInformation("worktimecode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.NotDeprecated);
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.NOT_ABOLISH);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(workType);

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.OPTIONAL;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
				result = Optional.of(workTimeSetting);
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.NORMAL);
	}
	
	
	/**
	 * if SetupType = NOT_REQUIRED 
	 * @就業時間帯コード == null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) empty
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_WAS_DELETE_2() {
		WorkInformation workInformation = new WorkInformation(null, "workTypeCode");
		WorkType workType = new WorkType();
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(workType);

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.NOT_REQUIRED;
				
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.NORMAL);
	}
	
	
	
	/**
	 * if SetupType = NOT_REQUIRED 
	 * @就業時間帯コード !=null
	 * require.就業時間帯を取得する(ログイン会社ID, @就業時間帯コード) not empty
	 * if $勤務種類.廃止区分 != 廃止する
	 * if $就業時間帯.廃止区分 != 廃止する
	 */
	@Test
	public void testCheckErrorCondition_is_WORKTIME_ARE_SET_WHEN_UNNECESSARY() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		WorkType workType = new WorkType();
		workType.setDeprecate(DeprecateClassification.NotDeprecated);
		WorkTimeSetting workTimeSetting = WorkinfoHelper.getWorkTimeSettingDefault();
		workTimeSetting.setAbolishAtr(AbolishAtr.NOT_ABOLISH);
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.NOT_REQUIRED;
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_ARE_SET_WHEN_UNNECESSARY);
	}



	@Test
	public void testGetWorkStyle_1() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.checkWorkDay(workInformation.getWorkTypeCode().v());
				result = null;

			}
		};
		assertThat(workInformation.getWorkStyle(require).isPresent()).isFalse();
	}

	@Test
	public void testGetWorkStyle_2() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.checkWorkDay(workInformation.getWorkTypeCode().v());
				result = WorkStyle.ONE_DAY_REST;

			}
		};

		assertThat(workInformation.getWorkStyle(require).get()).isEqualTo(WorkStyle.ONE_DAY_REST);
	}

	@Test
	public void getWorkInfoAndTimeZone_1() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());

			}
		};

		assertThat(workInformation.getWorkInfoAndTimeZone(require).isPresent()).isFalse();
	}

	@Test
	public void getWorkInfoAndTimeZone_2() {
		WorkInformation workInformation = new WorkInformation(null, "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

			}
		};

		assertThat(workInformation.getWorkInfoAndTimeZone(require).get().getListTimeZone().isEmpty()).isTrue();
		assertThat(workInformation.getWorkInfoAndTimeZone(require).get().getWorkTime().isPresent()).isFalse();
	}

	@Test
	public void getWorkInfoAndTimeZone_3() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.findByCode(anyString);

			}
		};

		assertThat(workInformation.getWorkInfoAndTimeZone(require).isPresent()).isFalse();
	}

	@Test
	public void getWorkInfoAndTimeZone_4() {
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");

		List<TimezoneUse> listTimezoneUse = new ArrayList<>();
		listTimezoneUse.add(new TimezoneUse(new TimeWithDayAttr(1), new TimeWithDayAttr(2), UseSetting.USE, 1));
		listTimezoneUse.add(new TimezoneUse(new TimeWithDayAttr(2), new TimeWithDayAttr(3), UseSetting.USE, 0));
		new Expectations() {
			{
				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.findByCode(anyString);
				result = Optional.of(new WorkTimeSetting());

				require.getPredeterminedTimezone(anyString, anyString, null).getTimezones();
				result = listTimezoneUse;

			}
		};

		assertThat(workInformation.getWorkInfoAndTimeZone(require).isPresent()).isTrue();
	}
	
	/**
	 * test ver 5
	 * worktimecode is null
	 */
	@Test
	public void testCheckErrorCondition_1() { 
		WorkInformation workInformation = new WorkInformation(null, "workTypeCode");
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.OPTIONAL;
				
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.NORMAL);
	}
	
	/**
	 * test ver 5
	 * worktimecode not null
	 */
	@Test
	public void testCheckErrorCondition_2() { 
		WorkInformation workInformation = new WorkInformation("workTimeCode", "workTypeCode");
		new Expectations() {
			{

				require.findByPK(workInformation.getWorkTypeCode().v());
				result = Optional.of(new WorkType());

				require.checkNeededOfWorkTimeSetting(workInformation.getWorkTypeCode().v());
				result = SetupType.OPTIONAL;
				
				require.findByCode(workInformation.getWorkTimeCode().v());
			}
		};

		assertThat(workInformation.checkErrorCondition(require))
				.isEqualTo(ErrorStatusWorkInfo.WORKTIME_WAS_DELETE);
	}

}
