package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * 承認ルート中間データ
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppRootInstance {
	
	/**
	 * 承認ルート中間データID
	 */
	@Setter
	private String rootID;
	
	/**
	 * 会社ID
	 */
	private String companyID;
	
	/**
	 * 対象者
	 */
	private String employeeID;
	
	/**
	 * 履歴期間
	 */
	@Setter
	private DatePeriod datePeriod;
	
	/**
	 * ルート種類
	 */
	private RecordRootType rootType;
	
	/**
	 * 承認フェーズ
	 */
	private List<AppPhaseInstance> listAppPhase;
	
}
