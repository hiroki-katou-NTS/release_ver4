package nts.uk.ctx.at.shared.infra.entity.entranceexit;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * The persistent class for the KSHST_MANAGE_ENTRY_EXIT database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="KSHST_MANAGE_ENTRY_EXIT")
public class KshstManageEntryExit extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Id
	@Column(name="CID")
	private String cid;


	/** The use cls. */
	@Column(name="USE_CLS")
	private BigDecimal useCls;

	/**
	 * Instantiates a new kshst manage entry exit.
	 */
	public KshstManageEntryExit() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.cid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((useCls == null) ? 0 : useCls.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		KshstManageEntryExit other = (KshstManageEntryExit) obj;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (useCls == null) {
			if (other.useCls != null)
				return false;
		} else if (!useCls.equals(other.useCls))
			return false;
		return true;
	}
	
	

}