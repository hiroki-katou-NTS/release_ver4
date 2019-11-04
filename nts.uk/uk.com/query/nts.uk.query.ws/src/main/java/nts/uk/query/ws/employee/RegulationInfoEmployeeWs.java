/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.ws.employee;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.time.GeneralDateTime;
import nts.uk.query.app.employee.*;

/**
 * The Class RegulationInfoEmployeeWs.
 */
@Path("query/employee")
@Produces(MediaType.APPLICATION_JSON)
public class RegulationInfoEmployeeWs {

	/** The finder. */
	@Inject
	private RegulationInfoEmployeeFinder finder;

	/**
	 * Find regulation info employee.
	 *
	 * @param query the query
	 * @return the list
	 */
	@POST
	@Path("find")
	public List<RegulationInfoEmployeeDto> findRegulationInfoEmployee(RegulationInfoEmpQueryDto query) {
		return this.finder.find(query);
	}

	/**
	 * Find by code.
	 *
	 * @param query the query
	 * @return the list
	 */
	@POST
	@Path("find/code")
	public List<RegulationInfoEmployeeDto> findByCode(SearchEmployeeQuery query) {
		return this.finder.findByEmployeeCode(query);
	}

	/**
	 * Find by name.
	 *
	 * @param query the query
	 * @return the list
	 */
	@POST
	@Path("find/name")
	public List<RegulationInfoEmployeeDto> findByName(SearchEmployeeQuery query) {
		return this.finder.findByEmployeeName(query);
	}

	/**
	 * Find by entry date.
	 *
	 * @param query the query
	 * @return the list
	 */
	@POST
	@Path("find/entrydate")
	public List<RegulationInfoEmployeeDto> findByEntryDate(SearchEmployeeQuery query) {
		return this.finder.findByEmployeeEntryDate(query);
	}

	/**
	 * Find by retirement date.
	 *
	 * @param query the query
	 * @return the list
	 */
	@POST
	@Path("find/retirementdate")
	public List<RegulationInfoEmployeeDto> findByRetirementDate(SearchEmployeeQuery query) {
		return this.finder.findByEmployeeRetirementDate(query);
	}

	/**
	 * Find current login employee.
	 *
	 * @param query the login employee query
	 * @return the regulation info employee dto
	 */
	@POST
	@Path("find/currentlogin")
	public RegulationInfoEmployeeDto findCurrentLoginEmployee(LoginEmployeeQuery query) {
		return this.finder.findCurrentLoginEmployeeInfo(query.getBaseDate(), query.getSystemType());
	}

}
