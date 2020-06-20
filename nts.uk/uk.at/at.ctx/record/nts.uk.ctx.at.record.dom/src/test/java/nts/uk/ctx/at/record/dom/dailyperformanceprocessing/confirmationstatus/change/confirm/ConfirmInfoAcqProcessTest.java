/**
 * 
 */
package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public class ConfirmInfoAcqProcessTest {

	private ConfirmInfoAcqProcess targetClass;
	
	private String cid;
	private Optional<DatePeriod> opPeriod;
	private Optional<YearMonth> yearMonth;
	private List<String> employeeIds;
	Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cache;

	@Injectable
	private ConfirmStatusInfoEmp confirmStatusInfoEmp;
	
	@Before
	public void setUp() throws Exception {
		this.targetClass = new ConfirmInfoAcqProcess();
		cid = "000000000000-0001";
		opPeriod = Optional.empty();
		yearMonth = Optional.of(new YearMonth(202003));
		cache = new HashMap<>();
	}

	@Test
	public void testGetConfirmInfoAcp_case0() {
		employeeIds = Collections.emptyList();
		new Expectations() {
	        {
	        	confirmStatusInfoEmp.confirmStatusInfoMulEmp(employeeIds, opPeriod, yearMonth, cache);
	            result = Arrays.asList(new ConfirmInfoResult());
	        }
	    };
	    
		this.targetClass.getConfirmInfoAcp(cid, employeeIds, opPeriod, yearMonth);
		
		new Verifications() { { times = 1; } };
	}

	@Test
	public void testGetConfirmInfoAcp_case1() {
		employeeIds =Arrays.asList("employee1");
		
		new Expectations() {
	        {
	        	confirmStatusInfoEmp.confirmStatusInfoOneEmp(employeeIds.get(0), opPeriod, yearMonth, cache);
	        	result = Arrays.asList(new ConfirmInfoResult());
	        }
	    };
	    
		this.targetClass.getConfirmInfoAcp(cid, employeeIds, opPeriod, yearMonth);
		
		new Verifications() { { times = 1; } };
	}

	@Test
	public void testGetConfirmInfoAcp_case2() {
		employeeIds =Arrays.asList("employee1","employee2");
		
		new Expectations() {
	        {
	        	confirmStatusInfoEmp.confirmStatusInfoMulEmp(employeeIds, opPeriod, yearMonth, cache);
	        	result = Arrays.asList(new ConfirmInfoResult());
	        }
	    };
	    
		this.targetClass.getConfirmInfoAcp(cid, employeeIds, opPeriod, yearMonth);
		
		new Verifications() { { times = 1; } };
	}
}
