package nts.uk.ctx.at.record.dom.monthly.agreement.approver;

import lombok.val;
import nts.arc.testing.assertion.NtsAssert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author khai.dh
 *
 *					test01		test02		test03		test04
 *	companyId		"cid"		"cid"		"cid"		"cid"
 *	workplaceId		"wid"		"wid"		"wid"		"wid"
 *	period			01-30/9		01-30/9		01-30/9		01-30/9
 *	approverIds		size=5		size=0		size=6		size=1
 *	confirmerIds	size=5		size=1		size=1		size=6
 * 	Expected		normal		Msg_1790	Msg_1791	Msg_1792
 */
public class Approver36AgrByWorkplaceTest {

	@Test
	public void getters() {
		val domain = Helper.createApprover36AgrByWorkplace();
		NtsAssert.invokeGetters(domain);
	}

	@Test
	public void test01(){

		val approverList = Arrays.asList("appr01", "appr02", "appr03", "appr04", "appr05");
		val confirmerList = Arrays.asList("cfm01", "cfm02", "cfm03", "cfm04", "cfm05");
		val domain = Approver36AgrByWorkplace.create(
				Helper.workplaceId,
				Helper.period,
				approverList,
				confirmerList
		);

		assertThat(domain.getWorkplaceId()).isEqualTo(Helper.workplaceId);
		assertThat(domain.getPeriod()).isEqualTo(Helper.period);
		assertThat(domain.getApproverIds()).isEqualTo(approverList);
		assertThat(domain.getConfirmerIds()).isEqualTo(confirmerList);
	}

	@Test
	public void test02(){
		NtsAssert.businessException("Msg_1790", () -> Approver36AgrByWorkplace.create(
				Helper.workplaceId,
				Helper.period,
				Collections.emptyList(),
				Arrays.asList("cfm01")
		));
	}

	@Test
	public void test03(){
		NtsAssert.businessException("Msg_1791", () -> Approver36AgrByWorkplace.create(
				Helper.workplaceId,
				Helper.period,
				Arrays.asList("appr01", "appr02", "appr03", "appr04", "appr05", "appr06"),
				Arrays.asList("cfm01")
		));
	}

	@Test
	public void test04(){
		NtsAssert.businessException("Msg_1792", () -> Approver36AgrByWorkplace.create(
				Helper.workplaceId,
				Helper.period,
				Arrays.asList("appr01"),
				Arrays.asList("cfm01", "cfm02", "cfm03", "cfm04", "cfm05", "cfm06")
		));
	}
}
