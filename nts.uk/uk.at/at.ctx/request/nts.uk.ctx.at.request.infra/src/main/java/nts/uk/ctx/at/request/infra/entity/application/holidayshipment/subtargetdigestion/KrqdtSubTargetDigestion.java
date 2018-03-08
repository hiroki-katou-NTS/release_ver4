package nts.uk.ctx.at.request.infra.entity.application.holidayshipment.subtargetdigestion;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 消化対象代休管理
 * 
 * @author sonnlb
 */
@Entity
@Table(name = "KRQDT_ABSENCE_LEAVE_APP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KrqdtSubTargetDigestion extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KrqdtSubTargetDigestionPK pk;

	/**
	 * 使用時間数
	 */
	@Basic(optional = false)
	@Column(name = "HOURS_USED")
	private GeneralDate hoursUsed;

	/**
	 * 休出管理データ
	 */
	@Basic(optional = true)
	@Column(name = "LEAVE_MNG_DATA_ID")
	private String leaveMngDataID;

	/**
	 * 休出発生日
	 */
	@Basic(optional = false)
	@Column(name = "BREAK_OUT_DATE")
	private GeneralDate breakOutDate;

	/**
	 * 休出状態
	 */
	@Basic(optional = false)
	@Column(name = "REST_STATE")
	private int restState;

	@Override
	protected Object getKey() {
		return pk;
	}

}
