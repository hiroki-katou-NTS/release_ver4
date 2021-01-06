package nts.uk.ctx.at.aggregation.dom.scheduletable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.aggregation.dom.scheduletable.OneRowOutputItem;
import nts.uk.ctx.at.aggregation.dom.scheduletable.ScheduleTableAttendanceItem;
import nts.uk.ctx.at.aggregation.dom.scheduletable.ScheduleTablePersonalInfoItem;

public class OneRowOutputItemTest {
	
	@Test
	public void testGetter() {
		OneRowOutputItem target = new OneRowOutputItem(
				Optional.of(ScheduleTablePersonalInfoItem.CLASSIFICATION), 
				Optional.of(ScheduleTablePersonalInfoItem.EMPLOYMENT), 
				Optional.of(ScheduleTableAttendanceItem.WORK_TYPE));
		
		NtsAssert.invokeGetters(target);
	}
	
	@Test
	public void testCreate_Exception() {
		
		NtsAssert.businessException("Msg_1971", 
				() -> OneRowOutputItem.create( 
						Optional.empty(), 
						Optional.empty(), 
						Optional.empty())
				);
	} 
	
	@Test
	public void testCreate_sucessfully() {
		
		OneRowOutputItem target = 
				OneRowOutputItem.create( 
						Optional.of(ScheduleTablePersonalInfoItem.EMPLOYEE_NAME), 
						Optional.empty(), 
						Optional.of(ScheduleTableAttendanceItem.WORK_TYPE));
		
		assertThat(target.getPersonalInfo().get()).isEqualTo(ScheduleTablePersonalInfoItem.EMPLOYEE_NAME);
		assertThat(target.getAdditionalInfo()).isEmpty();
		assertThat(target.getAttendanceItem().get()).isEqualTo(ScheduleTableAttendanceItem.WORK_TYPE);
		
	}

}
