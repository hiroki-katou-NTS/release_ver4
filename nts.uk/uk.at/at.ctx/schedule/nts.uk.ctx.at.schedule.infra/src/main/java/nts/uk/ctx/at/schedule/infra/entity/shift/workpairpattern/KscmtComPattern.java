package nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 会社勤務ペアパターングループ
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_COM_PATTERN")
public class KscmtComPattern extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KscmtComPatternPK kscmtComPatternPk;

	@Column(name = "GROUP_NAME")
	public String groupName;

	@Column(name = "GROUP_USAGE_ATR")
	public int groupUsageAtr;

	@Column(name = "NOTE")
	public String note;

	@Override
	protected Object getKey() {
		return this.kscmtComPatternPk;
	}

}
