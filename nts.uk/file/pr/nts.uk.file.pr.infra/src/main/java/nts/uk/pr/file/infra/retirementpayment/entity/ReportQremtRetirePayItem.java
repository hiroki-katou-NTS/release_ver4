/**
 * 
 */
package nts.uk.pr.file.infra.retirementpayment.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hungnm
 *
 */
@Setter
@Getter
@Entity
@Table(name = "QREMT_RETIRE_PAY_ITEM")
public class ReportQremtRetirePayItem extends UkJpaEntity implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected ReportQremtRetirePayItemPK reportQremtRetirePayItemPK;
	
	@Basic(optional = false)
	@Column(name = "ITEM_NAME")
	private String itemName;
	
	@Basic(optional = false)
	@Column(name = "ITEM_AB_NAME")
	private String itemAbName;
	
	@Basic(optional = true)
	@Column(name = "ITEM_AB_NAME_E")
	private String itemAbNameE;
	
	@Basic(optional = true)
	@Column(name = "ITEM_AB_NAME_O")
	private String itemAbNameO;
	
	@Basic(optional = true)
	@Column(name = "MEMO")
	private String memo;

	public ReportQremtRetirePayItem() {
		super();
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return this.reportQremtRetirePayItemPK;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((reportQremtRetirePayItemPK == null) ? 0 : reportQremtRetirePayItemPK.hashCode());
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
		ReportQremtRetirePayItem other = (ReportQremtRetirePayItem) obj;
		if (reportQremtRetirePayItemPK == null) {
			if (other.reportQremtRetirePayItemPK != null)
				return false;
		} else if (!reportQremtRetirePayItemPK.equals(other.reportQremtRetirePayItemPK))
			return false;
		return true;
	}
	
	
}
