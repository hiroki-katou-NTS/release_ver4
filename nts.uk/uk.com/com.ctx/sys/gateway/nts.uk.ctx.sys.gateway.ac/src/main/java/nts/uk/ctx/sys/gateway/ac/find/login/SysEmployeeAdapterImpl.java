/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.ac.find.login;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.pub.employee.EmployeeBasicInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.EmployeeDataMngInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.StatusOfEmployeeExport;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmpInfoByCidSidExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmpInfoByCidSidPub;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoDtoExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoPub;
import nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeDataMngInfoImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImport;
import nts.uk.ctx.sys.gateway.dom.login.dto.EmployeeImportNew;
import nts.uk.ctx.sys.gateway.dom.login.dto.SDelAtr;
import nts.uk.ctx.sys.gateway.dom.login.dto.StatusOfEmployeeImport;

/**
 * The Class SysEmployeeAdapterImpl.
 */
@Stateless
public class SysEmployeeAdapterImpl implements SysEmployeeAdapter {

	/** The employee info pub. */
	@Inject
	private EmployeeInfoPub employeeInfoPub;
	
	/** The emp info by cid sid pub. */
	@Inject
	private EmpInfoByCidSidPub empInfoByCidSidPub;
	
	/** The sy employee pub. */
	@Inject
	private SyEmployeePub syEmployeePub;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.gateway.dom.adapter.EmployeeAdapter#getByEmployeeCode(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public Optional<EmployeeImport> getCurrentInfoByScd(String companyId, String employeeCode) {
		Optional<EmployeeInfoDtoExport> opEmployee = employeeInfoPub.getEmployeeInfo(companyId,
				employeeCode);

		// Check exist
		if (opEmployee.isPresent()) {
			EmployeeInfoDtoExport employee = opEmployee.get();
			// convert dto
			EmployeeImport em = new EmployeeImport(employee.getCompanyId(), employee.getPersonId(),
					employee.getEmployeeId(), employee.getEmployeeCode());
			return Optional.of(em);
		}

		// Return
		return Optional.empty();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter#getByPid(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<EmployeeImport> getByPid(String companyId, String pid) {
		EmpInfoByCidSidExport emExport = empInfoByCidSidPub.getEmpInfoBySidCid(pid, companyId);
		EmployeeImport emImport = new EmployeeImport(emExport.getCid(), emExport.getPid(), emExport.getSid(),
				emExport.getScd());
		return Optional.of(emImport);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter#getSdataMngInfo(java.lang.String)
	 */
	@Override
	public Optional<EmployeeDataMngInfoImport> getSdataMngInfo(String sid) {
		
		Optional<EmployeeDataMngInfoExport> optEmployeeDataMngInfoExport = syEmployeePub.getSdataMngInfo(sid);
		
		if(!optEmployeeDataMngInfoExport.isPresent()) {
			return Optional.empty();
		}
		
		EmployeeDataMngInfoExport infoExport = optEmployeeDataMngInfoExport.get();
		
		return Optional.of(EmployeeDataMngInfoImport.builder()
				.companyId(infoExport.getCompanyId())
				.personId(infoExport.getPersonId()).employeeId(infoExport.getEmployeeId())
				.employeeCode(infoExport.getEmployeeCode())
				.deletedStatus(this.convertToDelAtr(infoExport.getDeletedStatus()))
				.deleteDateTemporary(infoExport.getDeleteDateTemporary())
				.removeReason(infoExport.getRemoveReason())
				.externalCode(infoExport.getExternalCode())
				.build());
	}
	
	/**
	 * Convert to del atr.
	 *
	 * @param employeeDeletionAttr the employee deletion attr
	 * @return the s del atr
	 */
	private SDelAtr convertToDelAtr(int employeeDeletionAttr) {
		switch (employeeDeletionAttr) {
		case 0:
			return SDelAtr.NOTDELETED;

		default:
			return SDelAtr.DELETED;
		}
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.gateway.dom.login.adapter.SysEmployeeAdapter#getStatusOfEmployee(java.lang.String)
	 */
	@Override
	public StatusOfEmployeeImport getStatusOfEmployee(String sid) {
		//get status
		StatusOfEmployeeExport status = this.syEmployeePub.getStatusOfEmployee(sid);
		
		//return
		return new StatusOfEmployeeImport(status.isDeleted());
	}

	@Override
	public Optional<EmployeeImportNew> getEmployeeBySid(String sid) {
		EmployeeBasicInfoExport em = this.syEmployeePub.findBySId(sid);
		if (em == null) {
			return Optional.empty();
		} else {
			// convert to Import Dto
			return Optional.of(new EmployeeImportNew(em.getPId(), em.getPName(), em.getEntryDate(), em.getGender(),
					em.getBirthDay(), em.getEmployeeId(), em.getEmployeeCode(), em.getRetiredDate()));
		}
	}
}
