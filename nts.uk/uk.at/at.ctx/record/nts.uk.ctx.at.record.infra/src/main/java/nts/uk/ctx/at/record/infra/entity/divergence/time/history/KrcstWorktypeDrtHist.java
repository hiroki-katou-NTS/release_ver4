package nts.uk.ctx.at.record.infra.entity.divergence.time.history;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * The persistent class for the KRCST_WORKTYPE_DRT_HIST database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="KRCST_WORKTYPE_DRT_HIST")
public class KrcstWorktypeDrtHist extends UkJpaEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The hist id. */
	@Id
	@Column(name="HIST_ID")
	private String histId;

	/** The cid. */
	@Column(name="CID")
	private String cid;

	/** The end D. */
	@Column(name="END_D")
	@Convert(converter= GeneralDateToDBConverter.class)
	private GeneralDate endD;

	/** The str D. */
	@Column(name="STR_D")
	@Convert(converter= GeneralDateToDBConverter.class)
	private GeneralDate strD;

	/** The worktype cd. */
	@Column(name="WORKTYPE_CD")
	private String worktypeCd;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.histId;
	}


}