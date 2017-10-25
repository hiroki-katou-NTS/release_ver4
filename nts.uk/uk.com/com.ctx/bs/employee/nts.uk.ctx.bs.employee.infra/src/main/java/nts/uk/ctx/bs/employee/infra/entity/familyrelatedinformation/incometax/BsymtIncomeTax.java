package nts.uk.ctx.bs.employee.infra.entity.familyrelatedinformation.incometax;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


@Getter
@Setter
@Entity
@Table(name="BSYMT_INCOME_TAX")
public class BsymtIncomeTax extends UkJpaEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**the income tax id*/
	@Embedded
	private BsymtIncomeTaxPK bsymtIncomeTaxPK;
	
	/** The sid. */
	@Column(name = "SID")
	private String sId;
	
	/** The FAMILY_MEMBER_ID. */
	@Column(name = "FAMILY_MEMBER_ID")
	private String familyMemberId;
	
	/** The DISABILITY_TYPE. */
	@Column(name = "DISABILITY_TYPE")
	private int disabilityType;
	
	/** The DEDUCTION_TARGET_TYPE. */
	@Column(name = "DEDUCTION_TARGET_TYPE")
	private int deductionTargetType;
	
	/** The SUPPORTER. */
	@Column(name = "SUPPORTER")
	private int supporter;
	
	/** The str D. */
	@Column(name = "STR_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate endD;

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return this.bsymtIncomeTaxPK;
	}

}
