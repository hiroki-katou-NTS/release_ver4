package nts.uk.ctx.sys.gateway.dom.login.password.identification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Verifications;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.sys.gateway.dom.login.password.identification.EmployeeIdentify.IdentificationResult;
import nts.uk.ctx.sys.gateway.dom.login.password.identification.helper.Helper;

public class EmployeeIdentifyTest {

	@Injectable
	private EmployeeIdentify.Require require;
	
	@Test  
	public void success() {
		int expireDaysAfter = 3;
		val dummyUser = new Helper.DummyUser.Builder().addDay(expireDaysAfter).build();
		
		new Expectations() {{
			require.getEmployeeDataMngInfoImportByEmployeeCode(Helper.DUMMY.COMPANY_ID,Helper.DUMMY.EMPLOYEE_CD);
			result = Optional.of(Helper.dummyImported);
			
			require.getUserByPersonId(Helper.DUMMY.PERSON_ID);
			result = Optional.of(dummyUser);
		}};
		
		val result = EmployeeIdentify.identifyByEmployeeCode(require, Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
		assertThat(result.isSuccess()).isTrue();
	}
	
	@Test
	public void unableToIdentifyEmployeeByCode() {
		new Expectations() {{
			require.getEmployeeDataMngInfoImportByEmployeeCode(Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
			result = Optional.empty();
		}};
		
		val result = EmployeeIdentify.identifyByEmployeeCode(require, Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
		
		assertThat(result.isFailed()).isTrue();
		assertThat(result.getEmployeeInfo()).isEqualTo(Optional.empty());
	}

	@Test
	public void unableToIdentifyUserByUserId() {
		new Expectations() {{
			require.getEmployeeDataMngInfoImportByEmployeeCode(Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
			result = Optional.of(Helper.dummyImported);
			
			require.getUserByPersonId(Helper.DUMMY.PERSON_ID);
			result = Optional.empty();
		}};
		
		val result = EmployeeIdentify.identifyByEmployeeCode(require, Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
		
		assertThat(result.isFailed()).isTrue();
		assertThat(result.getEmployeeInfo()).isEqualTo(Optional.empty());
	}
	
	@Test
	public void expiredUser() {
		int expireDaysBefore = -3;
		val dummyUser = new Helper.DummyUser.Builder().addDay(expireDaysBefore).build();
		
		new Expectations() {{
			require.getEmployeeDataMngInfoImportByEmployeeCode(Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
			result = Optional.of(Helper.dummyImported);
			
			require.getUserByPersonId(Helper.DUMMY.PERSON_ID);
			result = Optional.of(dummyUser);
		}};
		
		val result = EmployeeIdentify.identifyByEmployeeCode(require, Helper.DUMMY.COMPANY_ID, Helper.DUMMY.EMPLOYEE_CD);
		assertThat(result.isFailed()).isTrue();
	}
	
	@Test
	public void callSaveFailureLog() {
		val result =  (IdentificationResult)NtsAssert.Invoke.staticMethod(
				EmployeeIdentify.class,
				"identifyFailure",
				require,
				Helper.DUMMY.COMPANY_ID,
				Helper.DUMMY.EMPLOYEE_ID
				);
		new Verifications() {{
			require.addFailureLog((PasswordAuthIdentificateFailureLog)any);
			times = 0;
		}};
		result.getFailureLog().get().run();
		new Verifications() {{
			require.addFailureLog((PasswordAuthIdentificateFailureLog)any);
			times = 1;
		}};
	}
}
