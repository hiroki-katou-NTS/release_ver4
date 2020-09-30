package nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.consecutiveworktime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.consecutivework.ConsecutiveNumberOfDays;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

@RunWith(JMockit.class)
public class MaxDaysOfContinuousWorkTimeComTest {
	@Test
	public void getters() {
		val maxNoDaysOfContinuousAttOrg = MaxNumberDaysOfContinuousWorkTimeHelper.DUMMY;

		NtsAssert.invokeGetters(maxNoDaysOfContinuousAttOrg);

	}

	@Test
	public void create_maxNumberDaysOfContinuousWorkTimeCom_success() {
		val maxDaysConsWorkTime = new MaxDaysOfConsecutiveWorkTime(
				Arrays.asList(new WorkTimeCode("001"), new WorkTimeCode("002"), new WorkTimeCode("003")),
				new ConsecutiveNumberOfDays(5));
		val maxDaysOfConsAtt = new MaxDaysOfContinuousWorkTimeCompany(new ConsecutiveWorkTimeCode("003"),
				new ConsecutiveWorkTimeName("name"), maxDaysConsWorkTime);

		assertThat(maxDaysOfConsAtt.getCode().v()).isEqualTo("003");
		assertThat(maxDaysOfConsAtt.getName().v()).isEqualTo("name");
		assertThat(maxDaysOfConsAtt.getMaxDaysContiWorktime()).isEqualTo(maxDaysConsWorkTime);
	}
}
