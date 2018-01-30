package nts.uk.ctx.at.function.infra.entity.processexecution;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name="KFNMT_PROC_EXEC_SETTING")
@AllArgsConstructor
@NoArgsConstructor
public class KfnmtProcessExecutionSetting extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	/* 主キー */
	@EmbeddedId
    public KfnmtProcessExecutionSettingPK kfnmtProcExecSetPK;
	
	/* 個人スケジュール作成 */
	@Column(name = "PER_SCHEDULE_CLS")
	public int perScheduleCls;
	
	/* 対象月 */
	@Column(name = "TARGET_MONTH")
	public int targetMonth;
	
	/* 対象日 */
	@Column(name = "TARGET_DATE")
	public Integer targetDate;
	
	/* 作成期間 */
	@Column(name = "CREATION_PERIOD")
	public Integer creationPeriod;
	
	/* 作成対象 */
	@Column(name = "CREATION_TARGET")
	public int creationTarget;
	
	/* 勤務種別変更者を再作成 */
	@Column(name = "RECREATE_WK_TYPE")
	public int recreateWorkType;
	
	/* 手修正を保護する */
	@Column(name = "MANUAL_CORRECTION")
	public int manualCorrection;
	
	/* 新入社員を作成する */
	@Column(name = "CREATE_EMPLOYEE")
	public int createEmployee;
	
	/* 異動者を再作成する */
	@Column(name = "RECREATE_TRANSFER")
	public int recreateTransfer;
	
	/* 日別実績の作成・計算 */
	@Column(name = "DAILY_PERF_CLS")
	public int dailyPerfCls;
	
	/* 作成・計算項目 */
	@Column(name = "DAILY_PERF_ITEM")
	public int dailyPerfItem;

	/* 途中入社は入社日からにする */
	@Column(name = "MID_JOIN_EMP")
	public int midJoinEmployee;
	
	/* 承認結果反映 */
	@Column(name = "REFLECT_RS_CLS")
	public int reflectResultCls;
	
	/* 月別集計 */
	@Column(name = "MONTHLY_AGG_CLS")
	public int monthlyAggCls;
	
	/* アラーム抽出（個人別） */
	@Column(name = "INDV_ALARM_CLS")
	public int indvAlarmCls;
	
	/* 本人にメール送信する */
	@Column(name = "INDV_MAIL_PRIN")
	public int indvMailPrin;
	
	/* 管理者にメール送信する */
	@Column(name = "INDV_MAIL_MNG")
	public int indvMailMng;
	
	/* アラーム抽出（職場別） */
	@Column(name = "WKP_ALARM_CLS")
	public int wkpAlarmCls;
	
	/* 管理者にメール送信する */
	@Column(name = "WKP_MAIL_MNG")
	public int wkpMailMng;
	
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="CID", referencedColumnName="CID", insertable = false, updatable = false),
		@JoinColumn(name="EXEC_ITEM_CD", referencedColumnName="EXEC_ITEM_CD", insertable = false, updatable = false)
	})
	public KfnmtProcessExecution procExec;
	
	@Override
	protected Object getKey() {
		return this.kfnmtProcExecSetPK;
	}

	public KfnmtProcessExecutionSetting(KfnmtProcessExecutionSettingPK kfnmtProcExecSetPK, int perScheduleCls,
			int targetMonth, Integer targetDate, Integer creationPeriod, int creationTarget, int recreateWorkType,
			int manualCorrection, int createEmployee, int recreateTransfer, int dailyPerfCls, int dailyPerfItem,
			int midJoinEmployee, int reflectResultCls, int monthlyAggCls, int indvAlarmCls, int indvMailPrin,
			int indvMailMng, int wkpAlarmCls, int wkpMailMng) {
		super();
		this.kfnmtProcExecSetPK = kfnmtProcExecSetPK;
		this.perScheduleCls = perScheduleCls;
		this.targetMonth = targetMonth;
		this.targetDate = targetDate;
		this.creationPeriod = creationPeriod;
		this.creationTarget = creationTarget;
		this.recreateWorkType = recreateWorkType;
		this.manualCorrection = manualCorrection;
		this.createEmployee = createEmployee;
		this.recreateTransfer = recreateTransfer;
		this.dailyPerfCls = dailyPerfCls;
		this.dailyPerfItem = dailyPerfItem;
		this.midJoinEmployee = midJoinEmployee;
		this.reflectResultCls = reflectResultCls;
		this.monthlyAggCls = monthlyAggCls;
		this.indvAlarmCls = indvAlarmCls;
		this.indvMailPrin = indvMailPrin;
		this.indvMailMng = indvMailMng;
		this.wkpAlarmCls = wkpAlarmCls;
		this.wkpMailMng = wkpMailMng;
	}
}
