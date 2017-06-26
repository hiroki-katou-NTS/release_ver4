package nts.uk.ctx.pr.core.pub.rule.employment.processing.yearmonth;

import java.util.List;

public interface IPaydayProcessingPub {
	List<PaydayProcessingDto> getPaydayProcessing(String companyCd);
}
