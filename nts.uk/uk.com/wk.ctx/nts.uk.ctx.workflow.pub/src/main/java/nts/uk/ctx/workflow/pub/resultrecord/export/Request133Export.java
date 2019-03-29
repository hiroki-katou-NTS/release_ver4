package nts.uk.ctx.workflow.pub.resultrecord.export;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class Request133Export {
	/**
	 * 承認ルートの状況
	 */
	private List<AppRootStateStatusSprExport> appRootStateStatusLst;
	
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
