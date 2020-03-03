package nts.uk.ctx.at.shared.dom.workrule.shiftmaster;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrgIdenInfor;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;

@RunWith(JMockit.class)
public class ShiftMasterOrganizationTest {

	@Test
	public void ShiftMasterOrganization_inv1_emptyList() {
		TargetOrgIdenInfor targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE, "workplaceId",
				"workplaceGroupId");

		NtsAssert.systemError(() -> {

			new ShiftMasterOrganization(null, // dummy
					targetOrgIdenInfor, // dummy
					Collections.emptyList());
		});
	}

	@Test
	public void ShiftMasterOrganization_inv2_duplicated() {
		List<String> listShiftMaterCode = new ArrayList<>();
		listShiftMaterCode.add("123");// dummy
		listShiftMaterCode.add("123");// dummy

		TargetOrgIdenInfor targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE, "workplaceId",
				"workplaceGroupId");
		NtsAssert.systemError(() -> {
			new ShiftMasterOrganization(null, // dummy
					targetOrgIdenInfor, // dummy
					listShiftMaterCode);
		});
	}

	@Test
	public void testChange() {
		List<String> listShiftMaterCode = new ArrayList<>();
		listShiftMaterCode.add("123");// dummy
		listShiftMaterCode.add("124");// dummy
		TargetOrgIdenInfor targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE, "workplaceId",
				"workplaceGroupId");
		ShiftMasterOrganization shiftMasterOrg = new ShiftMasterOrganization("companyId", targetOrgIdenInfor,
				Arrays.asList("123"));
		shiftMasterOrg.change(listShiftMaterCode);
		assertThat(shiftMasterOrg.getListShiftMaterCode()).isEqualTo(listShiftMaterCode);
	}

	@Test
	public void testCopy() {
		TargetOrgIdenInfor targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE, "workplaceId",
				"workplaceGroupId");
		ShiftMasterOrganization shiftMasterOrg = new ShiftMasterOrganization("companyId", targetOrgIdenInfor,
				Arrays.asList("123"));
		TargetOrgIdenInfor targetOrgIdenInforNew = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE_GROUP, // dummy
				"workplaceId", // dummy
				"workplaceGroupId");// dummy
		ShiftMasterOrganization newShiftMasterOrganization = shiftMasterOrg.copy(targetOrgIdenInforNew);
		assertThat(newShiftMasterOrganization.getCompanyId()).isEqualTo(shiftMasterOrg.getCompanyId());
		assertThat(newShiftMasterOrganization.getListShiftMaterCode())
				.isEqualTo(shiftMasterOrg.getListShiftMaterCode());
		assertThat(newShiftMasterOrganization.getTargetOrg()).isEqualTo(targetOrgIdenInforNew);
	}

	@Test
	public void getters() {
		TargetOrgIdenInfor targetOrgIdenInfor = new TargetOrgIdenInfor(TargetOrganizationUnit.WORKPLACE, "workplaceId",
				"workplaceGroupId");
		ShiftMasterOrganization shiftMasterOrg = new ShiftMasterOrganization("companyId", targetOrgIdenInfor,
				Arrays.asList("123"));
		NtsAssert.invokeGetters(shiftMasterOrg);
	}
}
