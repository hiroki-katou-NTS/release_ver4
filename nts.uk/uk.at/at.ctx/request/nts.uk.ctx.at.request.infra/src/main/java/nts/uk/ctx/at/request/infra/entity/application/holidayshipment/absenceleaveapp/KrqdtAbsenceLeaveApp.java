package nts.uk.ctx.at.request.infra.entity.application.holidayshipment.absenceleaveapp;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 振休申請
 * 
 * @author sonnlb
 */
@Entity
@Table(name = "KRQDT_ABSENCE_LEAVE_APP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrqdtAbsenceLeaveApp extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 申請ID
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "APP_ID")
	private String appID;

	/**
	 * 勤務種類
	 */
	@Basic(optional = false)
	@Column(name = "WORK_TYPE_CD")
	private String workTypeCD;

	/**
	 * 就業時間帯変更
	 */
	@Basic(optional = false)
	@Column(name = "CHANGE_WORK_HOURS_ATR")
	private int changeWorkHoursAtr;

	/**
	 * 勤務場所コード
	 */
	@Basic(optional = true)
	@Column(name = "WORK_LOCATION_CD")
	private String workLocationCD;

	/**
	 * 就業時間帯
	 */
	@Basic(optional = true)
	@Column(name = "WORK_TIME_CD")
	private String workTimeCD;

	/**
	 * 開始時刻
	 */
	@Basic(optional = true)
	@Column(name = "START_WORK_TIME1")
	private int startWorkTime1;

	/**
	 * 終了時刻
	 */
	@Basic(optional = true)
	@Column(name = "END_WORK_TIME1")
	private int endWorkTime1;

	/**
	 * 開始時刻
	 */
	@Basic(optional = true)
	@Column(name = "START_WORK_TIME2")
	private int startWorkTime2;

	/**
	 * 終了時刻
	 */
	@Basic(optional = true)
	@Column(name = "END_WORK_TIME2")
	private int endWorkTime2;

	@Override
	protected Object getKey() {
		return appID;
	}

}
