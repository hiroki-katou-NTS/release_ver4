package nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.estimate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;

@RunWith(JMockit.class)
public class EstimatePriceDetailTest {
	@Test
	public void getters() {
		val estimatePrices = Helper.createEstimatePriceDetail();
		NtsAssert.invokeGetters(estimatePrices);
	}
	
	@Test
	public void create_estimatePrices_success() {
		val yearEstimatePrice = EstimateAmountList.create(Arrays.asList(
				  EstimateAmountByCondition.create(new EstimateAmountNo(1), new EstimateAmount(90000))
				, EstimateAmountByCondition.create(new EstimateAmountNo(2), new EstimateAmount(100000))
				, EstimateAmountByCondition.create(new EstimateAmountNo(3), new EstimateAmount(110000))
				));
		
		val monthEstimatePrice = EstimateAmountList.create(Arrays.asList(
				  EstimateAmountByCondition.create(new EstimateAmountNo(1), new EstimateAmount(1000))
				, EstimateAmountByCondition.create(new EstimateAmountNo(2), new EstimateAmount(2000))
				, EstimateAmountByCondition.create(new EstimateAmountNo(3), new EstimateAmount(3000))
				));
		
		val estimatePriceDetail = new EstimateAmountDetail(yearEstimatePrice, monthEstimatePrice);
		
		assertThat(estimatePriceDetail.getYearEstimatePrice()).isEqualTo(yearEstimatePrice);
		assertThat(estimatePriceDetail.getMonthEstimatePrice()).isEqualTo(monthEstimatePrice);
		
	}
	
	public static class Helper{
		/**　目安金額詳細を作成する*/
		public static EstimateAmountDetail createEstimatePriceDetail() {
			val yearEstimatePrice = createYearEstimatePrices();
			val monthEstimatePrice = createMonthEstimatePrices();
			return new EstimateAmountDetail(yearEstimatePrice, monthEstimatePrice);
		}
		
		/**　年間目安金額を作成する*/
		public static EstimateAmountList createYearEstimatePrices() {
			return EstimateAmountList.create(Arrays.asList(
					  EstimateAmountByCondition.create(new EstimateAmountNo(1), new EstimateAmount(90000))
					, EstimateAmountByCondition.create(new EstimateAmountNo(2), new EstimateAmount(100000))
					, EstimateAmountByCondition.create(new EstimateAmountNo(3), new EstimateAmount(110000))
					));
		}
		
		/**　月度目安金額を作成する*/
		public static EstimateAmountList createMonthEstimatePrices() {
			return EstimateAmountList.create(Arrays.asList(
					  EstimateAmountByCondition.create(new EstimateAmountNo(1), new EstimateAmount(1000))
					, EstimateAmountByCondition.create(new EstimateAmountNo(2), new EstimateAmount(2000))
					, EstimateAmountByCondition.create(new EstimateAmountNo(3), new EstimateAmount(3000))
					));
		}
	}
}
