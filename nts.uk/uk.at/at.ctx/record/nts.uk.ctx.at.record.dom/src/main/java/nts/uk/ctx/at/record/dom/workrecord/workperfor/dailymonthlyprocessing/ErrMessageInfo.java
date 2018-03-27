package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;


import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
/**
 * エラーメッセージ情報
 * @author tutk
 *
 */
@Getter
@AllArgsConstructor
public class ErrMessageInfo extends AggregateRoot  {
	
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
	private ErrMessageResource resourceID;
	
	/**
	 * 実行内容
	 */
	private ExecutionContent executionContent;
	/**
	 * 処理日
	 */
	private GeneralDate disposalDay;
	/**
	 * エラーメッセージ
	 */
	private ErrMessageContent messageError;
	
	public static ErrMessageInfo createFromJavaType(
			String employeeID,String empCalAndSumExecLogID,
			String resourceID,int executionContent,
			GeneralDate disposalDay,String messageError) {
		return new ErrMessageInfo(
				employeeID,
				empCalAndSumExecLogID,
				new ErrMessageResource(resourceID),
				EnumAdaptor.valueOf(executionContent, ExecutionContent.class),
				disposalDay,
				new ErrMessageContent(messageError)
				);
		
	}
	
}
