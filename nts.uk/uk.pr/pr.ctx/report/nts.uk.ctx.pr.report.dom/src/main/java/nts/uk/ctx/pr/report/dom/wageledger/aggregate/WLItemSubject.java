/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Class WLAggregateItemSubject.
 */
@Getter
public class WLItemSubject extends DomainObject{
	
	/** The category. */
	private WLCategory category;
	
	/** The payment type. */
	private PaymentType paymentType;
	
	/** The company code. */
	private String companyCode;
	
	/** The code. */
	private WLAggregateItemCode code;
	
	/**
	 * Instantiates a new WL item subject.
	 *
	 * @param getMemento the get memento
	 */
	public WLItemSubject(WLItemSubjectGetMemento getMemento) {
		this.category = getMemento.getCategory();
		this.paymentType = getMemento.getPaymentType();
		this.companyCode = getMemento.getCompanyCode();
		this.code = getMemento.getCode();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param setMemento the set memento
	 */
	public void saveToMemento(WLItemSubjectSetMemento setMemento) {
		setMemento.setCateory(this.category);
		setMemento.setCode(this.code);
		setMemento.setPaymentType(this.paymentType);
		setMemento.setCompanyCode(this.companyCode);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WLItemSubject other = (WLItemSubject) obj;
		if (category != other.category)
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (paymentType != other.paymentType)
			return false;
		return true;
	}
	
	
}
