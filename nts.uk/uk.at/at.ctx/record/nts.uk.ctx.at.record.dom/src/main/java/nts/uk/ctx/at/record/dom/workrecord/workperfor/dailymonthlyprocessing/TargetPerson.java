/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.List;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
 * 対象者
 * @author danpv
 *
 */
@Getter
public class TargetPerson extends AggregateRoot{

	/** 社員ID */
	private String employeeId;
	
	/** 就業計算と集計実行ログID */
	private String empCalAndSumExecLogId;
	
	/** 状態 */
	private List<ComplStateOfExeContents> state;
	
	public TargetPerson(String employeeId, String empCalAndSumExecLogId,
			List<ComplStateOfExeContents> state) {
		super();
		this.employeeId = employeeId;
		this.empCalAndSumExecLogId = empCalAndSumExecLogId;
		this.state = state;
	}
}