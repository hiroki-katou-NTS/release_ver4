package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@Data
@NoArgsConstructor
public class SaveProcessExecutionCommand {
	private boolean newMode;
	
	/* 会社ID */
	private String companyId;
	
	/* コード */
	private String execItemCd;
	
	/* 名称 */
	private String execItemName;
	
	/* 個人スケジュール作成 */
	private boolean perScheduleCls;
	
	/* 対象月 */
	private int targetMonth;
	
	/* 対象日 */
	private Integer targetDate;
	
	/* 作成期間 */
	private Integer creationPeriod;
	
	/* 作成対象 */
	private int creationTarget;
	
	/* 勤務種別変更者を再作成 */
	private boolean recreateWorkType;
	
	/* 手修正を保護する */
	private boolean manualCorrection;
	
	/* 新入社員を作成する */
	private boolean createEmployee;
	
	/* 異動者を再作成する */
	private boolean recreateTransfer;
	
	/* 日別実績の作成・計算 */
	private boolean dailyPerfCls;
	
	/* 作成・計算項目 */
	private int dailyPerfItem;
	
	/* 前回処理日 */
	private GeneralDate lastProcDate;
	
	/* 途中入社は入社日からにする */
	private boolean midJoinEmployee;
	
	/* 承認結果反映 */
	private boolean reflectResultCls;
	
	/* 月別集計 */
	private boolean monthlyAggCls;
	
	/* アラーム抽出（個人別） */
	private boolean indvAlarmCls;
	
	/* 本人にメール送信する */
	private boolean indvMailPrin;
	
	/* 管理者にメール送信する */
	private boolean indvMailMng;
	
	/* アラーム抽出（職場別） */
	private boolean wkpAlarmCls;
	
	/* 管理者にメール送信する */
	private boolean wkpMailMng;

	private int execScopeCls;

	private GeneralDate refDate;
	
    private List<String> workplaceList;
    
    /* 対象者区分 */
	private int targetGroupClassification;

}
