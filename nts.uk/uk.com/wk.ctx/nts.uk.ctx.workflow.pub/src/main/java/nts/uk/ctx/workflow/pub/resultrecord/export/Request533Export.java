package nts.uk.ctx.workflow.pub.resultrecord.export;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class Request533Export {
	/*
	 * 承認ルートの状況
	 */
	private List<AppRootSttMonthExport> appRootSttMonthExportLst;
	
	/*
	 * エラーフラグ
	 */
	private boolean errorFlg;
	
	/*
	 * エラーメッセージID
	 */
	private String errorMsgID;
	
	/*
	 * エラー社員IDリスト
	 */
	private List<String> errorEmpLst;
}
