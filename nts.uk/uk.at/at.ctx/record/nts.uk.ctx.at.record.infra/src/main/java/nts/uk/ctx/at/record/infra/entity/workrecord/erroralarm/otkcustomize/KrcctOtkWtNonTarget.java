package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.otkcustomize;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * The persistent class for the KRCCT_OTK_WT_NONTARGET database table.
 * 
 */
@Entity
@Table(name="KRCCT_OTK_WT_NONTARGET")
//@NamedQuery(name="KrcctOtkWtNontarget.findAll", query="SELECT k FROM KrcctOtkWtNontarget k")
public class KrcctOtkWtNonTarget extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcctOtkWtPK id;

	@ManyToOne
	@JoinColumn(name = "SID", referencedColumnName = "SID", insertable = false, updatable = false)
	public KrcctOtkVacationCk krcctOtkVacationCk;
	
	public KrcctOtkWtNonTarget() {
	}

	public KrcctOtkWtNonTarget(KrcctOtkWtPK id) {
		super();
		this.id = id;
	}
	
	public KrcctOtkWtNonTarget(String cid, String workTypeCode) {
		super();
		this.id = new KrcctOtkWtPK(cid, workTypeCode);
	}

	@Override
	protected Object getKey() {
		return this.id;
	}

}