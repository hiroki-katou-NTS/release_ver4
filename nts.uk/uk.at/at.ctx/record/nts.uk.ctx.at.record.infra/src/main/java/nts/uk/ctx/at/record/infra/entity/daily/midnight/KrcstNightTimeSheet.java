package nts.uk.ctx.at.record.infra.entity.daily.midnight;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The persistent class for the KRCST_NIGHT_TIMESHEET database table.
 * 
 * @author yennh 深夜時間帯
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "KRCST_NIGHT_TIMESHEET")
public class KrcstNightTimeSheet extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/* 会社コード */
	@Id
	@Column(name = "CID")
	private String cid;

	/* 開始時刻 */
	@Column(name = "START_TIME")
	private Integer startTime;

	/* 終了時刻 */
	@Column(name = "END_TIME")
	private Integer endTime;

	@Override
	protected Object getKey() {
		return cid;
	}
}
