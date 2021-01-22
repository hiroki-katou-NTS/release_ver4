package nts.uk.ctx.at.shared.dom.scherec.aggregation;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import lombok.val;

/**
 * Test for AggregateValuesByType
 * @author kumiko_otake
 */
public class AggregationByTypeServiceTest {

	/**
	 * Target	: totalize(List<Map<T, BigDecimal>>)
	 */
	@Test
	public void testTotalizeListOfMapOfTBigDecimal() {

		// 値リスト
		val values = new ArrayList<Map<String, BigDecimal>>();
		{
			values.add( Helper.toValuesByType(Arrays.asList(	7158,	6640,	 518	)) );
			values.add( Helper.toValuesByType(Arrays.asList(	7936,	6640,	1296	)) );
			values.add( Helper.toValuesByType(Arrays.asList(	4675,	4675	    	)) );
		}

		// Execute
		val result = AggregationByTypeService.totalize( values );

		// Assertion
		assertThat( result.entrySet() )
			.extracting( Map.Entry::getKey, Map.Entry::getValue )
			.containsExactlyInAnyOrder(
						tuple( Helper.getKeyString(1), BigDecimal.valueOf( 7158 + 7936 + 4675 ))	// T-1
					,	tuple( Helper.getKeyString(2), BigDecimal.valueOf( 6640 + 6640 + 4675 ))	// T-2
					,	tuple( Helper.getKeyString(3), BigDecimal.valueOf(  518 + 1296 ))			// T-3
				);

	}

	/**
	 * Target	: totalize(List<T>, List<Map<T, BigDecimal>>)
	 */
	@Test
	public void testTotalizeListOfTListOfMapOfTBigDecimal() {

		// 集計対象リスト
		val targets = IntStream.of( 1, 3, 5, 99 ).boxed()
				.map(Helper::getKeyString).collect(Collectors.toList());
		// 値リスト
		val values = new ArrayList<Map<String, BigDecimal>>();
		{
			values.add( Helper.toValuesByType(Arrays.asList(	9461,	2433,	1870	)) );
			values.add( Helper.toValuesByType(Arrays.asList(	2606,	7860			)) );
			values.add( Helper.toValuesByType(Arrays.asList(	 479,	8014,	4333   	)) );
		}

		// Execute
		val result = AggregationByTypeService.totalize( targets, values );

		// Assertion
		assertThat( result ).containsOnlyKeys( targets );
		assertThat( result.entrySet() )
			.extracting( Map.Entry::getKey, Map.Entry::getValue )
			.containsExactlyInAnyOrder(
						tuple( targets.get(0), BigDecimal.valueOf( 9461 + 2606 +  479 ))	// T-1
					,	tuple( targets.get(1), BigDecimal.valueOf( 1870 + 4333 ))			// T-3
					,	tuple( targets.get(2), BigDecimal.ZERO)								// T-5
					,	tuple( targets.get(3), BigDecimal.ZERO)								// T-99
				);

	}

	/**
	 * Target	: count
	 */
	@Test
	public void testCount() {

		// 値リスト
		val values = IntStream.of( 3, 2, 1, 2, 2, 3, 1, 1 ).boxed()
				.map(Helper::getKeyString).collect(Collectors.toList());

		// Execute
		val result = AggregationByTypeService.count( values );

		// Assertion
		assertThat( result.entrySet() )
			.extracting( Map.Entry::getKey, Map.Entry::getValue )
			.containsExactlyInAnyOrder(
						tuple( Helper.getKeyString(1), BigDecimal.valueOf( 3 ))	// T-1
					,	tuple( Helper.getKeyString(2), BigDecimal.valueOf( 3 ))	// T-2
					,	tuple( Helper.getKeyString(3), BigDecimal.valueOf( 2 ))	// T-3
				);

	}


	protected static class Helper {

		public static String getKeyString(int key) {
			return String.format("T-%d", key);
		}

		public static Map<String, BigDecimal> toValuesByType(List<Integer> values) {
			return IntStream.range( 0, values.size() ).boxed()
					.collect(Collectors.toMap(
									index -> Helper.getKeyString( index + 1 )
								,	index -> BigDecimal.valueOf( values.get(index) )
							));
		}

	}

}
