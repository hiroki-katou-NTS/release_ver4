package nts.uk.ctx.at.record.app.command.workrecord.log;

import java.util.List;
import lombok.Getter;
/**
 * 
 * @author hieult
 *
 */
@Getter
public class AddEmpCalSumAndTargetCommand {

    /** Screen B or J*/
    private String screen;
	
	/** ＜画面Aから受け取るパラメータ＞ | Params from A screen */
    /** 締めID */
    private int closureID;
    
    /** ＜画面Bから受け取るパラメータ＞ | Params from B screen */
    /** 日別作成(打刻反映)実施区分 */
    private boolean dailyCreation;
    /** 作成区分 */
    private int creationType;
    /** 再作成区分 */
    private int resetClass;
    /** 計算区分を再度設定 */
    private boolean calClassReset;
    /** マスタ情報を再度設定 */
    private boolean masterReconfiguration;
    /** 特定日区分を再度設定 */
    private boolean specDateClassReset;
    /** 育児・介護短時間再設定 */
    private boolean resetTimeForChildOrNurseCare;
    /** 打刻のみを再度反映 */
    private boolean refNumberFingerCheck;
    /** 休業再設定 */
    private boolean closedHolidays;
    /** 就業時間帯再設定 */
    private boolean resettingWorkingHours;
    /** 申し送り時間再設定 */
    private boolean resetTimeForAssig;
    /** 日別計算実施区分 */
    private boolean dailyCalClass;
    /** 計算区分 */
    private int calClass;
    /** 承認結果反映実施区分 */
    private boolean refApprovalresult;
    /** 反映区分 */
    private int refClass;
    /** 確定済みの場合にも強制的に反映する */
    private boolean alsoForciblyReflectEvenIfItIsConfirmed;
    /** 月別集計実施区分 */
    private boolean monthlyAggregation;
    /** 集計区分 */
    private int summaryClass;

    /** ＜画面Cから受け取るパラメータ＞  | Params from C screen*/
    /** List 社員ID */
    private List<String> lstEmployeeID;
    /** 対象期間開始日 */
    private String periodStartDate;
    /** 対象期間終了日 */
    private String periodEndDate;
    /* 当月 */
    private String processingMonth;
    
    /**＜画面 J から受け取るパラメータ＞  | Params from J screen */
    /** ケース別実行実施内容ID */
    private String caseSpecExeContentID;
    
    private Integer isCalWhenLock;
    
}
