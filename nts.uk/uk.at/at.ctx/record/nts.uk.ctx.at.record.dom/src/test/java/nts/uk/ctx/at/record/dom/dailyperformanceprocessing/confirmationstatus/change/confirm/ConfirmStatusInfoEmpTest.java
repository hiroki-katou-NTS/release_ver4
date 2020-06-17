package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import lombok.val;
import mockit.Injectable;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class ConfirmStatusInfoEmpTest {

	private ConfirmStatusInfoEmp targetClass;

	private String cid;
	private Optional<DatePeriod> opPeriod;
	private Optional<YearMonth> yearMonth;
	private List<String> employeeIds;
	Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cache;
	GeneralDate closureDay;
	ClosureMonth cm;
	AggrPeriodEachActualClosure actualClosure;
	ClosurePeriodCacheKey cachekey;
	
	@Injectable
	private GetClosurePeriod getClosurePeriod;
	
	@Before
	public void setUp() throws Exception {
		targetClass = new ConfirmStatusInfoEmp();
		cid = "000000000000-0001";
		yearMonth = Optional.of(new YearMonth(202003));
		cm = new ClosureMonth(yearMonth.get(), 1, new ClosureDate(1, true));
		opPeriod = Optional.of(cm.defaultPeriod());
		closureDay = GeneralDate.ymd(cm.yearMonth().year(), cm.yearMonth().month(), cm.yearMonth().lastDateInMonth());

		employeeIds =Arrays.asList(
				"ba7f7d44-1f82-42bd-b551-0153a1c972b1",
				"362da6e6-a893-46e4-bdf9-22cd9441f17c",
				"0168730d-47ec-44da-86c5-760db4fd6371",
				"8dc540a4-0581-4387-b16a-d3943641e5fc",
				"fccb8f4f-2fde-4c14-bf68-4d7bc605863c",
				"6d57123b-c7db-4efb-9976-12b92f7a3aa5",
				"f784ad8f-54ee-4fc3-ad3b-2e319f629b16",
				"6a6fbd3a-0882-4883-829c-1e34b8bc8fa4",
				"4d442a4c-36a7-403d-9dfd-3dd0b713c4e8");
		
		cache = new HashMap<>();

		actualClosure = AggrPeriodEachActualClosure.of(
				ClosureId.valueOf(cm.closureId()), cm.closureDate(), cm.yearMonth(), cm.defaultPeriod(), cm.defaultPeriod());
		cachekey = new ClosurePeriodCacheKey(employeeIds.get(0), closureDay, yearMonth, opPeriod);
	}

	@Test
	public void testConfirmStatusInfoOneEmp_yearmonth() {

		val result = this.targetClass.confirmStatusInfoOneEmp(cid, employeeIds.get(0), Optional.empty(), yearMonth, cache);

		assertThat(cache.isEmpty(), is(false));
		assertThat(cache.get(cachekey), is(Arrays.asList(cm.defaultPeriod())));
		assertThat(result.isEmpty(), is(false));
		
	}

	@Test
	public void testConfirmStatusInfoOneEmp_period() {

		val result = this.targetClass.confirmStatusInfoOneEmp(cid, employeeIds.get(0), opPeriod, Optional.empty(), cache);

		assertThat(cache.isEmpty(), is(false));
		assertThat(cache.get(cachekey), is(Arrays.asList(cm.defaultPeriod())));
		assertThat(result.isEmpty(), is(false));
		
	}

	@Test
	public void testConfirmStatusInfoMulEmp_yearmonth() {
		val result = this.targetClass.confirmStatusInfoMulEmp(cid, employeeIds, Optional.empty(), yearMonth, cache);

		assertThat(cache.isEmpty(), is(false));
		assertThat(cache.get(cachekey), is(Arrays.asList(cm.defaultPeriod())));
		assertThat(result.isEmpty(), is(false));
	}

	@Test
	public void testConfirmStatusInfoMulEmp_period() {
		val result = this.targetClass.confirmStatusInfoMulEmp(cid, employeeIds, opPeriod, Optional.empty(), cache);

		assertThat(cache.isEmpty(), is(false));
		assertThat(cache.get(cachekey), is(Arrays.asList(cm.defaultPeriod())));
		assertThat(result.isEmpty(), is(false));
	}

	@Test
	public void testConfirmStatusInfoMulEmp_unuseCache() {
		val result = this.targetClass.confirmStatusInfoMulEmp(cid, employeeIds, opPeriod, yearMonth);
		
		assertThat(result.isEmpty(), is(false));
	}

}
