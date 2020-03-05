package nts.uk.ctx.at.schedule.dom.shift.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsHelper.ShiftPalletsComHelper.PalletHelper.PalletCombinationsHelper;

/**
 * 
 * @author sonnh1
 *
 */
public class ShiftPalletCombinationsTest {

	@Test
	public void create_shiftPalletsCombi_position0_fail() {
		NtsAssert.businessException("Msg_1616", () -> {
			new ShiftPalletCombinations(
					0, 
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(new Combinations(
							1, // dummy
							new ShiftPalletCode("0000001")))); // dummy
		});
	}
	
	@Test
	public void create_shiftPalletsCombi_position21_fail() {
		NtsAssert.businessException("Msg_1616", () -> {
			new ShiftPalletCombinations(
					21, 
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(new Combinations(
							1, // dummy
							new ShiftPalletCode("0000001")))); // dummy
		});
	}
	
	@Test
	public void create_shiftPalletsCombi_size0_fail() {
		NtsAssert.businessException("Msg_1627", () -> {
			new ShiftPalletCombinations(
					1, // dummy
					new ShiftCombinationName("shiftComName"), // dummy
					Collections.emptyList());
		});
	}
	
	@Test
	public void create_shiftPalletsCombi_size32_fail() {
		NtsAssert.businessException("Msg_1627", () -> {
			new ShiftPalletCombinations(
					1, // dummy
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(
							new Combinations(1, new ShiftPalletCode("0000001")), 
							new Combinations(2, new ShiftPalletCode("0000002")),
							new Combinations(3, new ShiftPalletCode("0000003")),
							new Combinations(4, new ShiftPalletCode("0000004")),
							new Combinations(5, new ShiftPalletCode("0000005")),
							new Combinations(6, new ShiftPalletCode("0000006")),
							new Combinations(7, new ShiftPalletCode("0000007")),
							new Combinations(8, new ShiftPalletCode("0000008")),
							new Combinations(9, new ShiftPalletCode("0000009")),
							new Combinations(10, new ShiftPalletCode("0000010")),
							new Combinations(11, new ShiftPalletCode("0000011")),
							new Combinations(12, new ShiftPalletCode("0000012")),
							new Combinations(13, new ShiftPalletCode("0000013")),
							new Combinations(14, new ShiftPalletCode("0000014")),
							new Combinations(15, new ShiftPalletCode("0000015")),
							new Combinations(16, new ShiftPalletCode("0000016")),
							new Combinations(17, new ShiftPalletCode("0000017")),
							new Combinations(18, new ShiftPalletCode("0000018")),
							new Combinations(19, new ShiftPalletCode("0000019")),
							new Combinations(20, new ShiftPalletCode("0000020")),
							new Combinations(21, new ShiftPalletCode("0000021")),
							new Combinations(22, new ShiftPalletCode("0000022")),
							new Combinations(23, new ShiftPalletCode("0000023")),
							new Combinations(24, new ShiftPalletCode("0000024")),
							new Combinations(25, new ShiftPalletCode("0000025")),
							new Combinations(26, new ShiftPalletCode("0000026")),
							new Combinations(27, new ShiftPalletCode("0000027")),
							new Combinations(28, new ShiftPalletCode("0000028")),
							new Combinations(29, new ShiftPalletCode("0000029")),
							new Combinations(30, new ShiftPalletCode("0000030")),
							new Combinations(31, new ShiftPalletCode("0000031")),
							new Combinations(32, new ShiftPalletCode("0000032"))));
		});
	}
	
	@Test
	public void create_shiftPalletsCombi_duplicate() {
		NtsAssert.businessException("Msg_1627", () -> {
			new ShiftPalletCombinations(
					1, // dummy
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(
							new Combinations(
									1, 
									new ShiftPalletCode("0000001")),
							new Combinations(
									1, 
									new ShiftPalletCode("0000002"))));
		});
	}


	@Test 
	public void create_shiftPalletsCombi_position1_success() {
		ShiftPalletCombinations target = new ShiftPalletCombinations(
				1, 
				new ShiftCombinationName("shiftComName"),
				Arrays.asList(new Combinations(
								1, 
								new ShiftPalletCode("0000001"))));
		
		assertThat(target)
			.extracting(
					d->d.getPositionNumber(), 
					d->d.getCombinationName().v(),
					d->d.getCombinations().get(0).getOrder(), 
					d->d.getCombinations().get(0).getShiftCode().v())
			.containsExactly(1,
					"shiftComName",
					1,
					"0000001");
	}
	
	@Test
	public void create_shiftPalletsCombi_position20_success() {
		ShiftPalletCombinations target = new ShiftPalletCombinations(
				20, 
				new ShiftCombinationName("shiftComName"),
				Arrays.asList(new Combinations(
								1, 
								new ShiftPalletCode("0000001"))));
		
		assertThat(target)
			.extracting(
					d->d.getPositionNumber(), 
					d->d.getCombinationName().v(),
					d->d.getCombinations().get(0).getOrder(), 
					d->d.getCombinations().get(0).getShiftCode().v())
			.containsExactly(
					20,
					"shiftComName",
					1,
					"0000001");
	}
	
	@Test
	public void create_shiftPalletsCombi_size1_success() {
		ShiftPalletCombinations target = new ShiftPalletCombinations(
					1, // dummy
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(
							new Combinations(1, new ShiftPalletCode("0000001"))));
		
		assertThat(target)
			.extracting(
					d->d.getPositionNumber(), 
					d->d.getCombinationName().v(),
					d->d.getCombinations().get(0).getOrder(), 
					d->d.getCombinations().get(0).getShiftCode().v(),
					d->d.getCombinations().size()) // check size
			.containsExactly(
					1,
					"shiftComName",
					1,
					"0000001",
					1);
	}
	
	@Test
	public void create_shiftPalletsCombi_size31_success() {
		ShiftPalletCombinations target = new ShiftPalletCombinations(
					1, // dummy
					new ShiftCombinationName("shiftComName"), // dummy
					Arrays.asList(
							new Combinations(1, new ShiftPalletCode("0000001")), 
							new Combinations(2, new ShiftPalletCode("0000002")),
							new Combinations(3, new ShiftPalletCode("0000003")),
							new Combinations(4, new ShiftPalletCode("0000004")),
							new Combinations(5, new ShiftPalletCode("0000005")),
							new Combinations(6, new ShiftPalletCode("0000006")),
							new Combinations(7, new ShiftPalletCode("0000007")),
							new Combinations(8, new ShiftPalletCode("0000008")),
							new Combinations(9, new ShiftPalletCode("0000009")),
							new Combinations(10, new ShiftPalletCode("0000010")),
							new Combinations(11, new ShiftPalletCode("0000011")),
							new Combinations(12, new ShiftPalletCode("0000012")),
							new Combinations(13, new ShiftPalletCode("0000013")),
							new Combinations(14, new ShiftPalletCode("0000014")),
							new Combinations(15, new ShiftPalletCode("0000015")),
							new Combinations(16, new ShiftPalletCode("0000016")),
							new Combinations(17, new ShiftPalletCode("0000017")),
							new Combinations(18, new ShiftPalletCode("0000018")),
							new Combinations(19, new ShiftPalletCode("0000019")),
							new Combinations(20, new ShiftPalletCode("0000020")),
							new Combinations(21, new ShiftPalletCode("0000021")),
							new Combinations(22, new ShiftPalletCode("0000022")),
							new Combinations(23, new ShiftPalletCode("0000023")),
							new Combinations(24, new ShiftPalletCode("0000024")),
							new Combinations(25, new ShiftPalletCode("0000025")),
							new Combinations(26, new ShiftPalletCode("0000026")),
							new Combinations(27, new ShiftPalletCode("0000027")),
							new Combinations(28, new ShiftPalletCode("0000028")),
							new Combinations(29, new ShiftPalletCode("0000029")),
							new Combinations(30, new ShiftPalletCode("0000030")),
							new Combinations(31, new ShiftPalletCode("0000031"))));
		
		assertThat(target)
			.extracting(
					d->d.getPositionNumber(), 
					d->d.getCombinationName().v(),
					d->d.getCombinations().get(0).getOrder(), 
					d->d.getCombinations().get(0).getShiftCode().v(),
					d->d.getCombinations().size()) // check size
			.containsExactly(
					1,
					"shiftComName",
					1,
					"0000001",
					31);
	}
	
	@Test
	public void sortCombinations_success() {
		ShiftPalletCombinations target = new ShiftPalletCombinations(
				1, // dummy
				new ShiftCombinationName("shiftComName"),  // dummy
				Arrays.asList(
						new Combinations(
								15, 
								new ShiftPalletCode("0000015")), 
						new Combinations(
								10, 
								new ShiftPalletCode("0000010")), 
						new Combinations(
								30, 
								new ShiftPalletCode("0000030"))));
		
		target.sortCombinations();
		
		assertThat(target.getCombinations())
			.extracting(d -> d.getOrder(), d -> d.getShiftCode().v())
			.containsExactly(tuple(1, "0000010"), tuple(2, "0000015"), tuple(3, "0000030"));
	}
	
	@Test
	public void getters() {
		ShiftPalletCombinations target = PalletCombinationsHelper.DUMMY;
		NtsAssert.invokeGetters(target);
	}

}
