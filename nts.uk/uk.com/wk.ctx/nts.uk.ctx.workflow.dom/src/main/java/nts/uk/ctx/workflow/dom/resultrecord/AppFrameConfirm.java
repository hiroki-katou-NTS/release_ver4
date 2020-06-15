package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 承認済承認者
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class AppFrameConfirm {
	
	/**
	 * 順序
	 */
	private Integer frameOrder;
	
	/**
	 * 承認者
	 */
	private Optional<String> approverID;
	
	/**
	 * 代行者
	 */
	private Optional<String> representerID;
	
	/**
	 * 承認日
	 */
	private GeneralDate approvalDate;
	
}
