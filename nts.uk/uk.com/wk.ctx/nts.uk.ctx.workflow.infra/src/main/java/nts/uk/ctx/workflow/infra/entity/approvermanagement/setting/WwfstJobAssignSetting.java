package nts.uk.ctx.workflow.infra.entity.approvermanagement.setting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "WWFST_JOB_ASSIGN_SET")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WwfstJobAssignSetting extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主キー */
	/** 会社ID */
	@Id
	@Column(name = "CID")
	public String companyId;

	/** 兼務者を含める */
	@Column(name = "IS_CONCURRENTLY")
	public int isConcurrently;

	@Override
	protected Object getKey() {
		return companyId;
	}

}
