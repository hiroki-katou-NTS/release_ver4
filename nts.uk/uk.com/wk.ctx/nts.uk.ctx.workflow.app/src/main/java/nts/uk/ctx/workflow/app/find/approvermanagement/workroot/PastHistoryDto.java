package nts.uk.ctx.workflow.app.find.approvermanagement.workroot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;

/**
 * @author sang.nv
 * 過去履歴
 */
@Getter
@AllArgsConstructor
public class PastHistoryDto {
	/** 開始日 */
	private GeneralDate startDate;
	/** 終了日 */
	private GeneralDate endDate;
	/** 所属長のコード */
	private String codeB17;
	/** 所属長の名前 */
	private String nameB18;
	/** 日別の承認する人のコード */
	private String codeB110;
	/** 日別の承認する人の名前 */
	private String nameB111;
	/** 日別の承認する人のコード */
	private String codeB112;
	/** 日別の承認する人の名前 */
	private String nameB113;
}