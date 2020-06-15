package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance.AppRootInstanceBuilder;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 承認枠中間データ
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class AppFrameInstance {
	
	/**
	 * 順序
	 */
	private Integer frameOrder;
	
	/**
	 * 確定区分
	 */
	private boolean confirmAtr;
	
	/**
	 * 承認者リスト
	 */
	private List<String> listApprover;
	
}
