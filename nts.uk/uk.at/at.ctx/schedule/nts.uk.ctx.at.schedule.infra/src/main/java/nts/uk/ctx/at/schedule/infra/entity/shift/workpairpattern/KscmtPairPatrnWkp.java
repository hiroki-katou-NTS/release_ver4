package nts.uk.ctx.at.schedule.infra.entity.shift.workpairpattern;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 勤務ペアパターン
 * 
 * @author sonnh1
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSCMT_PAIR_PATRN_WKP")
public class KscmtPairPatrnWkp extends ContractUkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KscmtWkpPatternItemPK kscmtWkpPatternItemPk;

	@Column(name = "PATTERN_NAME")
	public String patternName;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "WKP_ID", referencedColumnName = "WKP_ID", insertable = false, updatable = false),
			@JoinColumn(name = "GROUP_NO", referencedColumnName = "GROUP_NO", insertable = false, updatable = false) })
	private KscmtPairGrpWkp kscmtWkpPattern;

	@OneToMany(targetEntity=KscmtPairWkp.class, cascade = CascadeType.ALL, mappedBy = "kscmtWkpPatternItem", orphanRemoval = true)
	@JoinTable(name = "KSCMT_PAIR_WKP")
	public List<KscmtPairWkp> kscmtWkpWorkPairSet;

	@Override
	protected Object getKey() {
		return this.kscmtWkpPatternItemPk;
	}

	public KscmtPairPatrnWkp(KscmtWkpPatternItemPK kscmtWkpPatternItemPk, String patternName,
			List<KscmtPairWkp> kscmtWkpWorkPairSet) {
		super();
		this.kscmtWkpPatternItemPk = kscmtWkpPatternItemPk;
		this.patternName = patternName;
		this.kscmtWkpWorkPairSet = kscmtWkpWorkPairSet;
	}
}
