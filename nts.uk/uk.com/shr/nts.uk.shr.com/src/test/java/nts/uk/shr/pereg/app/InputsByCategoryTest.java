package nts.uk.shr.pereg.app;

import static mockit.Deencapsulation.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import lombok.val;
import nts.uk.shr.pereg.app.command.ItemsByCategory;

public class InputsByCategoryTest {

	@Test
	public void test() {
		val item1 = new ItemValue("IS00001", "日通太郎", 1);
		val item2 = new ItemValue("IS00002", "ニッツウタロウ", 1);
		val inputs = new ItemsByCategory("CS00001", "1", Arrays.asList(item1, item2));
		val command = (TestCommand)inputs.createCommandForSystemDomain(TestCommand.class);

		assertThat(command.getFullName(), is("日通太郎"));
		assertThat(command.getFullNameKana(), is("ニッツウタロウ"));
	}

}
