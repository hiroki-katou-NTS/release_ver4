package nts.uk.ctx.workflow.dom.adapter.bs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 職位
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class SimpleJobTitleImport {
	
	/** The job title id. */
	// 職位ID
	private String jobTitleId;

	/** The job title code. */
	// 職位コード
	private String jobTitleCode;

	/** The job title name. */
	// 職位名称
	private String jobTitleName;

	/** The disporder. */
	// 序列
	private Integer disporder;
}
