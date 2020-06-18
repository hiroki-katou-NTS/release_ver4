package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class ConfirmStatusMonthlyTest {

	private ConfirmStatusMonthly targetClass;
	private List<String> listEmployeeId;
	
	@Before
	public  void setup() throws Exception{
		this.targetClass = new ConfirmStatusMonthly();
		listEmployeeId = ConfirmStatusMonthlyHelper.employeesHelper();
	}
	
	@Test
	public void testGetConfirmStatusMonthly() {
		ClosureMonth cm = ConfirmStatusMonthlyHelper.closureMonthHelper(1, 202003);
		Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod = ConfirmStatusMonthlyHelper.cacheHelper(cm, listEmployeeId);

		Optional<StatusConfirmMonthDto> result = targetClass.getConfirmStatusMonthly(
				"000000000000-0001", cm.closureId(), cm.closureDate(), listEmployeeId, cm.yearMonth(), null, cachedClosurePeriod);
		
		assertThat(result.isPresent(), is(true));
		assertThat(
				result.get().getListConfirmStatus().stream().allMatch(s -> listEmployeeId.contains(s.getEmployeeId())),
				is(true));
	}
}
