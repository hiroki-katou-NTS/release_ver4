package nts.uk.ctx.at.record.infra.entity.monthly.vtotalmethod;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import java.math.BigDecimal;


/**
 * @author HoangNDH
 * The persistent class for the KRCST_VERT_MON_METHOD database table.
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="KRCST_VERT_MON_METHOD")
public class KrcstVertMonMethod extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 会社ID
	@Id
	@Column(name="CID")
	private String cid;

	// 勤務種類
	@Column(name="DUTY_TYPE")
	private int dutyType;

	// 振出日数カウント条件
	@Column(name="TRANS_ATTEND_DAY")
	private int transAttendDay;

	@Override
	protected Object getKey() {
		return cid;
	}

}