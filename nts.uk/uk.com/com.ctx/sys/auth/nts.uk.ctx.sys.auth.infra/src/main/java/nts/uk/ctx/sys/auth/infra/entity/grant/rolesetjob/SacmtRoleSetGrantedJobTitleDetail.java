package nts.uk.ctx.sys.auth.infra.entity.grant.rolesetjob;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * @author HungTT
 *
 */

@NoArgsConstructor
@Entity
@Table(name = "SACMT_ROLESET_JOB_DETAIL")
public class SacmtRolesetGrantedJobTitleDetail extends ContractUkJpaEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public SacmtRolesetGrantedJobTitleDetailPK roleSetGrantedJobTitleDetailPK;

	@Basic(optional = false)
	@Column(name = "ROLESET_CD")
	public String roleSetCd;
	
	@ManyToOne
	@JoinColumns({
        @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false)
    })
	public SacmtRolesetGrantedJobTitle roleSetGrantedJobTitle;
	
	@Override
	protected Object getKey() {
		return this.roleSetGrantedJobTitleDetailPK;
	}

	public SacmtRolesetGrantedJobTitleDetail(String roleSetCd, String jobTitleId, String companyId) {
		super();
		this.roleSetGrantedJobTitleDetailPK = new SacmtRolesetGrantedJobTitleDetailPK(jobTitleId, companyId);
		this.roleSetCd = roleSetCd;
	}
	
}
