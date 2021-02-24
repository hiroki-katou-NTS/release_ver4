package nts.uk.ctx.at.schedule.dom.schedule.workschedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import nts.arc.i18n.I18NText.Builder;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.CreateWorkSchedule.Require;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;

public class CreateWorkScheduleByShiftTest {
	
	@Injectable
	CreateWorkScheduleByShift.Require require;
	
	@Test
	public void testCreate_hasError(
			@Mocked Builder builder) {
		
		new Expectations() {{
			
			require.getShiftMaster( (ShiftMasterCode) any );
			// result = empty
			
			builder.build().buildMessage();
			result = "content 1705";
		}};
		
		ResultOfRegisteringWorkSchedule result = 
				CreateWorkScheduleByShift.create(require, "empId", GeneralDate.ymd(2020, 11, 1), new ShiftMasterCode("001"));
		
		assertThat( result.getAtomTask() ).isEmpty();
		assertThat( result.isHasError() ).isTrue();
		assertThat( result.getErrorInformation() )
			.extracting( 
					e -> e.getEmployeeId(),
					e -> e.getDate(),
					e -> e.getAttendanceItemId(),
					e -> e.getErrorMessage() )
			.containsExactly(
				tuple(
					"empId",
					GeneralDate.ymd(2020, 11, 1),
					Optional.empty(),
					"content 1705"));
		
	}
	
	@Test
	public <T> void testCreate_successfully(
			@Injectable ShiftMaster shiftMaster,
			@Injectable ResultOfRegisteringWorkSchedule mockResult
			) {
		
		new Expectations() {{
			require.getShiftMaster( (ShiftMasterCode) any );
			result = Optional.of(shiftMaster);
		}};
		
		new MockUp<CreateWorkSchedule>() {
			
			@Mock
			public ResultOfRegisteringWorkSchedule create(
					Require require, 
					String employeeId, 
					GeneralDate date, 
					WorkInformation workInformation,
					List<TimeSpanForCalc> breakTimeList,
					Map<Integer, T> updateInfoMap) {
				return mockResult;
			}
		};
		
		ResultOfRegisteringWorkSchedule result = 
				CreateWorkScheduleByShift.create(require, "empId", GeneralDate.ymd(2020, 11, 1), new ShiftMasterCode("001"));
		
		assertThat( result ).isEqualTo( mockResult );
	}

}
