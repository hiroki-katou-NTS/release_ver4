/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.entity.workplace.affiliate;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class BsymtAffiWorkplaceHistItem.
 * 所属職場履歴項目
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "BSYMT_AFF_WPL_HIST_ITEM")
public class BsymtAffiWorkplaceHistItem extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The historyid -  PK. */
	@Id
	@Column(name = "HIST_ID")
	private String hisId;

	/** The employeeId. */
	@Basic(optional = false)
	@Column(name = "SID")
	private String sid;
	
	/** The workPlaceId. */
	@Basic(optional = false)
	@Column(name = "WORKPLACE_ID")
	private String workPlaceId;
	
	/** The workPlaceId. */
	@Basic(optional = false)
	@Column(name = "NORMAL_WORKPLACE_ID")
	private String normalWkpId;
	
	/**
	 * Instantiates a new cempt employment.
	 */
	public BsymtAffiWorkplaceHistItem() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.hisId;
	}

	


}
