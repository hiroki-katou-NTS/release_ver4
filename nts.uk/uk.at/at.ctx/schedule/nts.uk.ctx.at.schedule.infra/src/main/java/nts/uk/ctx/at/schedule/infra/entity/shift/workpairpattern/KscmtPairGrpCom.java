package nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 会社勤務ペアパターングループ
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_PAIR_GRP_COM")
public class KscmtPairGrpCom extends ContractUkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KscmtComPatternPK kscmtComPatternPk;

	@Column(name = "GROUP_NAME")
	public String groupName;

	@Column(name = "GROUP_USAGE_ATR")
	public int groupUsageAtr;

	@Column(name = "NOTE")
	public String note;

	@OneToMany(targetEntity=KscmtPairPatrnCom.class, cascade = CascadeType.ALL, mappedBy = "kscmtComPattern", orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinTable(name = "KSCMT_PAIR_PATRN_COM")
	public List<KscmtPairPatrnCom> kscmtComPatternItem;

	@Override
	protected Object getKey() {
		return this.kscmtComPatternPk;
	}

}
