package nts.uk.ctx.at.schedule.dom.employeeinfo.medicalworkstyle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.shared.dom.employeeworkway.medicalworkstyle.EmpLicenseClassification;
import nts.uk.ctx.at.shared.dom.employeeworkway.medicalworkstyle.LicenseClassification;

@RunWith(JMockit.class)
public class EmpLicenseClassificationTest {
	@Test
	public void  getter(){
		EmpLicenseClassification target = new EmpLicenseClassification("sid", Optional.of(LicenseClassification.valueOf(1)));
		NtsAssert.invokeGetters(target);
	}
	
	@Test
	public void  empLicenseClassification(){
		EmpLicenseClassification classification = EmpLicenseClassification.empLicenseClassification("sid");
		assertEquals(classification.getEmpID(),"sid");
		assertThat(classification.getOptLicenseClassification().isPresent()).isFalse();
	}
	
	@Test
	public void  getClassification(){
		EmpLicenseClassification classification = EmpLicenseClassification.get("sid", LicenseClassification.valueOf(1));
		assertEquals(classification.getEmpID(),"sid");
		assertEquals(classification.getOptLicenseClassification().get().name, "准看護師");
	}
}
