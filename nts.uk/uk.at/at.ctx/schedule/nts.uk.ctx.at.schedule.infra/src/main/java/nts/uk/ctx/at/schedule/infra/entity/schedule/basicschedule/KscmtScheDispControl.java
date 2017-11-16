package nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 勤務予定の表示制御
 * 
 * @author trungtran
 *
 */
@Getter
@Setter
@Entity
@Table(name = "KSCMT_SCHE_DISP_CONTROL")
public class KscmtScheDispControl extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KscmtScheDispControlPK kscmtScheDispControlPK;
	/** 個人情報区分 */
	@Column(name = "PERSON_INFO_ATR")
	public int personInforAtr;

	/** 表示区分 */
	@Column(name = "PERSON_DISPLAY_ATR")
	public int personDisplayAtr;

	/** 資格表示記号 */
	@Column(name = "PERSON_SYQUALIFY")
	public String personSyQualify;

	/** 取得不足表示区分 */
	@Column(name = "PUB_HD_SHORTAGE_ATR")
	public boolean pubHolidayShortageAtr;

	/** 取得超過表示区分 */
	@Column(name = "PUB_HD_EXCESS_ATR")
	public boolean pubHolidayExcessAtr;

	/** 勤務就業記号表示区分 */
	@Column(name = "SYMBOL_ATR")
	public boolean symbolAtr;

	/** 半日表示区分 */
	@Column(name = "SYMBOL_HALF_DAY_ATR")
	public boolean symbolHalfDayAtr;

	/** 半日記号 */
	@Column(name = "SYMBOL_HALF_DAY_NAME")
	public boolean symbolHalfDayName;

	@Override
	protected Object getKey() {
		return this.kscmtScheDispControlPK;
	}
}
