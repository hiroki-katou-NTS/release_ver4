/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.pubimp.employee;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.query.app.employee.RegulationInfoEmployeeFinder;
import nts.uk.query.pub.employee.SearchEmployeePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class SearchEmployeePubImpl.
 */
@Stateless
public class SearchEmployeePubImpl implements SearchEmployeePub {

	/** The employee finder. */
	@Inject
	private RegulationInfoEmployeeFinder employeeFinder;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.pub.employee.SearchEmployeePub#searchByEmployeeCode(java.
	 * lang.String, java.lang.Integer)
	 */
	@Override
	public List<String> searchByEmployeeCode(String sCd, Integer systemType, GeneralDate referenceDate) {
		return this.employeeFinder.searchByEmployeeCode(sCd, systemType, referenceDate);
	}
	@Override
	public List<String> searchByEmployeeCode(String sCd, Integer systemType) {
		return this.searchByEmployeeCode(sCd, systemType, GeneralDate.today());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.pub.employee.SearchEmployeePub#searchByEmployeeName(java.
	 * lang.String, java.lang.Integer)
	 */
	@Override
	public List<String> searchByEmployeeName(String sName, Integer systemType, GeneralDate referenceDate) {
		return this.employeeFinder.searchByEmployeeName(sName, systemType, referenceDate);
	}
	@Override
	public List<String> searchByEmployeeName(String sName, Integer systemType) {
		return this.searchByEmployeeName(sName, systemType, GeneralDate.today());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.pub.employee.SearchEmployeePub#searchByEntryDate(nts.uk.shr.
	 * com.time.calendar.period.DatePeriod, java.lang.Integer)
	 */
	@Override
	public List<String> searchByEntryDate(DatePeriod period, Integer systemType, GeneralDate referenceDate) {
		return this.employeeFinder.searchByEntryDate(period, systemType, referenceDate);
	}
	@Override
	public List<String> searchByEntryDate(DatePeriod period, Integer systemType) {
		return this.searchByEntryDate(period, systemType, GeneralDate.today());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.query.pub.employee.SearchEmployeePub#searchByRetirementDate(nts.uk
	 * .shr.com.time.calendar.period.DatePeriod, java.lang.Integer)
	 */
	@Override
	public List<String> searchByRetirementDate(DatePeriod period, Integer systemType, GeneralDate referenceDate) {
		return this.employeeFinder.searchByRetirementDate(period, systemType, referenceDate);
	}
	@Override
	public List<String> searchByRetirementDate(DatePeriod period, Integer systemType) {
		return this.searchByRetirementDate(period, systemType, GeneralDate.today());
	}

}
