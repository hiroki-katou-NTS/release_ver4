package nts.uk.ctx.sys.gateway.infra.entity.singlesignon;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The primary key class for the SGWMT_WINDOW_ACC database table.
 * 
 */
@Setter
@Getter
@Embeddable
public class SgwmtWindowAccPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CID")
	private String cid;
	
	@Column(name="SID")
	private String employeeId;
	
	@Column(name="NO")
	private Integer no;


	public SgwmtWindowAccPK() {
		super();
	}
	
	public SgwmtWindowAccPK(String cid, String userId, Integer no) {
		super();
		this.cid = cid;
		this.employeeId = userId;
		this.no = no;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((no == null) ? 0 : no.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SgwmtWindowAccPK other = (SgwmtWindowAccPK) obj;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (no == null) {
			if (other.no != null)
				return false;
		} else if (!no.equals(other.no))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}
	
}