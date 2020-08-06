package nts.uk.ctx.at.schedule.dom.employeeinfo.employeesort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nts.arc.testing.assertion.NtsAssert;

public class SortSettingTest {
	
	@Test
	public void getters() {
		SortSetting sortSetting = new SortSetting("cid",new ArrayList<>());
		NtsAssert.invokeGetters(sortSetting);
	}

	@Test
	public void testGetSortSet_Throw_1612() {
		List<OrderedList> listOrderedList = Arrays.asList(new OrderedList(SortType.SORT_ASC, SortOrder.CLASSIFY),
				new OrderedList(SortType.SORT_ASC, SortOrder.CLASSIFY));
		NtsAssert.businessException("Msg_1612",
				() -> SortSetting.getSortSet("cid",listOrderedList));
	}
	
	/**
	 *  inv-2  @並び替え優先順.Size < 1
	 */
	@Test
	public void testGetSortSet_Throw_1613_2() {
		List<OrderedList> listOrderedList = new ArrayList<>();
		NtsAssert.businessException("Msg_1613",
				() -> SortSetting.getSortSet("cid",listOrderedList));
	}
	
	/**
	 *  inv-2  @並び替え優先順.Size >= 6
	 */
	@Test
	public void testGetSortSet_Throw_1613_1() {
		List<OrderedList> listOrderedList = Arrays.asList(new OrderedList(SortType.SORT_ASC, SortOrder.CLASSIFY),
				new OrderedList(SortType.SORT_ASC, SortOrder.POSITION),
				new OrderedList(SortType.SORT_ASC, SortOrder.LISENCE_ATR),
				new OrderedList(SortType.SORT_ASC, SortOrder.RANK),
				new OrderedList(SortType.SORT_ASC, SortOrder.SCHEDULE_TEAM),
				new OrderedList(SortType.SORT_DESC, SortOrder.SCHEDULE_TEAM)
				);
		NtsAssert.businessException("Msg_1613",
				() -> SortSetting.getSortSet("cid",listOrderedList));
	}
	
	@Test
	public void testGetSortSet() {
		String companyId = "cid";
		List<OrderedList> listOrderedList = Arrays.asList(new OrderedList(SortType.SORT_ASC, SortOrder.CLASSIFY));
		SortSetting sortSettingNew = SortSetting.getSortSet(companyId,listOrderedList);
		
		assertSame(companyId, sortSettingNew.getCompanyID());
		assertSame(listOrderedList.get(0).getType(), sortSettingNew.getOrderedList().get(0).getType());
		assertSame(listOrderedList.get(0).getSortOrder(), sortSettingNew.getOrderedList().get(0).getSortOrder());
		
		assertThat(listOrderedList)
		.extracting(d->d.getType().value,d -> d.getSortOrder().value)
		.containsExactly(tuple(0,4));
		
	}
	

}
