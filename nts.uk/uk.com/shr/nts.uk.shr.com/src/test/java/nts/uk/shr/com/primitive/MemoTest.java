package nts.uk.shr.com.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

import lombok.val;
import nts.arc.primitive.PrimitiveValueConstraintException;

public class MemoTest {

	@Test
	public void test() {
		new Memo(create500CharsFullWidth()).validate();
	}
	
	@Test(expected = PrimitiveValueConstraintException.class)
	public void failed_maxLength() {
		new Memo(create500CharsFullWidth() + "a").validate();
	}

	private static String create500CharsFullWidth() {
		val sb = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				sb.append("１２３４５６７８９０");
			}
		}
		return sb.toString();
	}
}
