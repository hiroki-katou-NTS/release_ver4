package nts.uk.ctx.workflow.dom.service.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class Request133Output {
	/**
	 * 承認ルートの状況
	 */
	private List<ApprovalRootStateStatus> appRootStatusLst;
	
	/**
	 * エラーフラグ
	 */
	private boolean errorFlg;
	
	/**
	 * エラーメッセージID
	 */
	private String errorMsgID;
	
	/**
	 * エラー社員IDリスト
	 */
	private List<String> empLst;
}
