package nts.uk.ctx.sys.gateway.dom.login;

import java.util.Optional;

import org.junit.Test;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.shared.dom.employee.EmployeeDataMngInfoImport;
import nts.uk.ctx.sys.shared.dom.employee.employment.SyaEmpHistImport;
import nts.uk.ctx.sys.shared.dom.employee.sycompany.SyaCompanyHistImport;
import nts.uk.ctx.sys.shared.dom.employee.syjobtitle.SyaJobHistImport;
import nts.uk.ctx.sys.shared.dom.employee.syworkplace.SyaWkpHistImport;

public class CheckEmployeeAvailabilityTest {
	@Mocked EmployeeDataMngInfoImport employeeDataMngInfoImport;
	@Mocked IdentifiedEmployeeInfo identifiedEmployeeInfo;
	@Mocked SyaCompanyHistImport comHistImp;
	@Mocked SyaEmpHistImport empHistImp;
	@Mocked SyaJobHistImport jobHistImp;
	@Mocked SyaWkpHistImport wkpHistImp;
	
	@Injectable
	private CheckEmployeeAvailability.Require require;
	
	private static class Dummy{
		private static String employeeId = "empemp";
		private static GeneralDate today = GeneralDate.today();
	}

	@Test
	public void not_affCom_Test() {
		
		new Expectations() {{
			employeeDataMngInfoImport.getEmployeeId();
			result = Dummy.employeeId;
			
			require.getCompanyHist(Dummy.employeeId, Dummy.today);
			result = Optional.empty();
		}};
		
		NtsAssert.businessException("Msg_2169", () ->{
			CheckEmployeeAvailability.check(require, identifiedEmployeeInfo);
		});
	}

	@Test
	public void not_affEmp_Test() {
		
		new Expectations() {{
			employeeDataMngInfoImport.getEmployeeId();
			result = Dummy.employeeId;
			
			require.getCompanyHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(comHistImp);
			
			require.getEmploymentHist(Dummy.employeeId, Dummy.today);
			result = Optional.empty();
			
			require.getJobtitleHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(jobHistImp);
			
			require.getWorkplaceHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(wkpHistImp);
		}};
		
		NtsAssert.businessException("Msg_1420", () ->{
			CheckEmployeeAvailability.check(require, identifiedEmployeeInfo);
		});
	}

	@Test
	public void not_affJob_Test() {
		
		new Expectations() {{
			employeeDataMngInfoImport.getEmployeeId();
			result = Dummy.employeeId;
			
			require.getCompanyHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(comHistImp);
			
			require.getEmploymentHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(empHistImp);
			
			require.getJobtitleHist(Dummy.employeeId, Dummy.today);
			result = Optional.empty();
			
			require.getWorkplaceHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(wkpHistImp);
		}};
		
		NtsAssert.businessException("Msg_1420", () ->{
			CheckEmployeeAvailability.check(require, identifiedEmployeeInfo);
		});
	}

	@Test
	public void not_affWkp_Test() {
		
		new Expectations() {{
			employeeDataMngInfoImport.getEmployeeId();
			result = Dummy.employeeId;
			
			require.getCompanyHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(comHistImp);
			
			require.getEmploymentHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(empHistImp);
			
			require.getJobtitleHist(Dummy.employeeId, Dummy.today);
			result = Optional.of(jobHistImp);
			
			require.getWorkplaceHist(Dummy.employeeId, Dummy.today);
			result = Optional.empty();
		}};
		
		NtsAssert.businessException("Msg_1420", () ->{
			CheckEmployeeAvailability.check(require, identifiedEmployeeInfo);
		});
	}

}
