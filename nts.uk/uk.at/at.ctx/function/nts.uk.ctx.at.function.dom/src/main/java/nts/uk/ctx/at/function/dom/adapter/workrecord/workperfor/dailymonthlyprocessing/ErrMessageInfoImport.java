package nts.uk.ctx.at.function.dom.adapter.workrecord.workperfor.dailymonthlyprocessing;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author tutk
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrMessageInfoImport   {
	
	/**
	 * 実行社員ID
	 */
	private String employeeID;
	
	/**
	 * 就業計算と集計実行ログID
	 */
	private String empCalAndSumExecLogID;
	/**
	 * リソースID
	 */
	private String resourceID;
	
	/**
	 * 実行内容
	 */
	private int executionContent;
	/**
	 * 処理日
	 */
	private GeneralDate disposalDay;
	/**
	 * エラーメッセージ
	 */
	private String messageError;
	
	public ErrMessageInfoImport(String employeeID, String empCalAndSumExecLogID, String resourceID, int executionContent,
			GeneralDate disposalDay, String messageError) {
		super();
		this.employeeID = employeeID;
		this.empCalAndSumExecLogID = empCalAndSumExecLogID;
		this.resourceID = resourceID;
		this.executionContent = executionContent;
		this.disposalDay = disposalDay;
		this.messageError = messageError;
	}
	
	
}
