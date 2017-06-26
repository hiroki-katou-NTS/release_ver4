/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import lombok.Getter;

/**
 * The Class SalaryAggregateItemHeader.
 */
@Getter
public class SalaryAggregateItemHeader {

	/** The code. */
	private SalaryAggregateItemCode aggregateItemCode;

	/** The tax division. */
	private TaxDivision taxDivision;

	/** The company code. */
	private String companyCode;

	/**
	 * Instantiates a new salary aggregate item header.
	 */
	public SalaryAggregateItemHeader(SalaryAggregateItemHeaderGetMemento memento) {
		super();
		this.aggregateItemCode = memento.getSalaryAggregateItemCode();
		this.taxDivision = memento.getTaxDivision();
		this.companyCode = memento.getCompanyCode();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(SalaryAggregateItemHeaderSetMemento memento) {
		memento.setCompanyCode(this.companyCode);
		memento.setSalaryAggregateItemCode(this.aggregateItemCode);
		memento.setTaxDivision(this.taxDivision);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aggregateItemCode == null) ? 0 : aggregateItemCode.hashCode());
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
        SalaryAggregateItemHeader other = (SalaryAggregateItemHeader) obj;
        if (aggregateItemCode == null) {
            if (other.aggregateItemCode != null)
                return false;
        } else if (!aggregateItemCode.equals(other.aggregateItemCode))
            return false;
        return true;
    }
}
