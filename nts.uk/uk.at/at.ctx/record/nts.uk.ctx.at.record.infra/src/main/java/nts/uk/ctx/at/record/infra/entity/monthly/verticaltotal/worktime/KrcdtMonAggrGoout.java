package nts.uk.ctx.at.record.infra.entity.monthly.verticaltotal.worktime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 集計外出
 * @author shuichu_ishida
 */
@Entity
@Table(name = "KRCDT_MON_AGGR_GOOUT")
@NoArgsConstructor
@AllArgsConstructor
public class KrcdtMonAggrGoout extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** プライマリキー */
	@EmbeddedId
	public KrcdtMonAggrGooutPK PK;
	
	/** 外出回数 */
	@Column(name = "GOOUT_TIMES")
	public int goOutTimes;
	
	/** 法定内時間 */
	@Column(name = "LEGAL_TIME")
	public int legalTime;
	
	/** 計算法定内時間 */
	@Column(name = "CALC_LEGAL_TIME")
	public int calcLegalTime;
	
	/** 法定外時間 */
	@Column(name = "ILLEGAL_TIME")
	public int illegalTime;
	
	/** 計算法定外時間 */
	@Column(name = "CALC_ILLEGAL_TIME")
	public int calcIllegalTime;
	
	/** 合計時間 */
	@Column(name = "TOTAL_TIME")
	public int totalTime;
	
	/** 計算合計時間 */
	@Column(name = "CALC_TOTAL_TIME")
	public int calcTotalTime;

	/** マッチング：月別実績の勤怠時間 */
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "SID", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.SID", insertable = false, updatable = false),
		@JoinColumn(name = "YM", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.YM", insertable = false, updatable = false),
		@JoinColumn(name = "CLOSURE_ID", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.CLOSURE_ID", insertable = false, updatable = false),
		@JoinColumn(name = "CLOSURE_DAY", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.CLOSURE_DAY", insertable = false, updatable = false),
		@JoinColumn(name = "IS_LAST_DAY", referencedColumnName = "KRCDT_MON_ATTENDANCE_TIME.IS_LAST_DAY", insertable = false, updatable = false)
	})
	public KrcdtMonAttendanceTime krcdtMonAttendanceTime;
	
	/**
	 * キー取得
	 */
	@Override
	protected Object getKey() {		
		return this.PK;
	}
}
