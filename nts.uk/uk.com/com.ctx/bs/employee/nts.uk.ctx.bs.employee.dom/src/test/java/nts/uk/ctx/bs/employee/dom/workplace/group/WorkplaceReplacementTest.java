package nts.uk.ctx.bs.employee.dom.workplace.group;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class WorkplaceReplacementTest {
	@Test
	public void testCheckWplReplaceTrue() {
		WorkplaceReplacement add = WorkplaceReplacement.ADD;
		assertThat(add.checkWplReplace() == true).isTrue();
	}
	
	@Test
	public void testCheckWplReplaceTrue2() {
		WorkplaceReplacement alreadyBelonged = WorkplaceReplacement.ALREADY_BELONGED;
		assertThat(alreadyBelonged.checkWplReplace() == true).isTrue();
	}
	
	@Test
	public void testCheckWplReplaceFalse() {
		WorkplaceReplacement delete = WorkplaceReplacement.DELETE;
		assertThat(delete.checkWplReplace() == false).isTrue();
	}
	
	@Test
	public void testCheckWplReplaceFalse2() {
		WorkplaceReplacement belongedAnother = WorkplaceReplacement.BELONGED_ANOTHER;
		assertThat(belongedAnother.checkWplReplace() == false).isTrue();
	}
}
