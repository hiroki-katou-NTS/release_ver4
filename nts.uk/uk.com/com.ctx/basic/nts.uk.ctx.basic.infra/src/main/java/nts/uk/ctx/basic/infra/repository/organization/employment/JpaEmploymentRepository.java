package nts.uk.ctx.basic.infra.repository.organization.employment;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.basic.dom.organization.employment.Employment;
import nts.uk.ctx.basic.dom.organization.employment.EmploymentRepository;
import nts.uk.ctx.basic.infra.entity.organization.employment.CmnmtEmp;
import nts.uk.ctx.basic.infra.entity.organization.employment.CmnmtEmpPK;
@Stateless
public class JpaEmploymentRepository extends JpaRepository implements EmploymentRepository{
	
	private final String SELECT_ALL_EMP_BY_COMPANY = "SELECT c FROM CmnmtEmp c "
			+ "WHERE c.cmnmtEmpPk.companyCode = :companyCode";
	private final String SELECT_EMP_BY_DISPLAY_FLG = SELECT_ALL_EMP_BY_COMPANY
			+ " AND c.displayFlg = 1";
	/**
	 * insert Employment
	 */
	@Override
	public void add(Employment employment) {
		this.commandProxy().insert(toEntity(employment));
	}

	/**
	 *  update Employment
	 */
	@Override
	public void update(Employment employment) {
		this.commandProxy().update(toEntity(employment));
		
	}

	private CmnmtEmp toEntity(Employment employment) {
		val entity = new CmnmtEmp();
		entity.cmnmtEmpPk = new CmnmtEmpPK();
		entity.cmnmtEmpPk.companyCode = employment.getCompanyCode();
		entity.cmnmtEmpPk.employmentCode = employment.getEmploymentCode().v();
		entity.employmentName = employment.getEmploymentName().v();
		entity.closeDateNo = employment.getCloseDateNo().v();
		entity.memo = employment.getMemo().v();
		entity.processingNo = employment.getProcessingNo().v();
		entity.statutoryHolidayAtr = employment.getStatutoryHolidayAtr().value;
		entity.employementOutCd = employment.getEmployementOutCd().v();
		entity.displayFlg = employment.getDisplayFlg().value;
		return entity;
	}
	/**
	 * remove employment by company code and employment code
	 */
	@Override
	public void remove(String companyCode, String employmentCode) {
		val objectKey = new CmnmtEmpPK();
		objectKey.companyCode = companyCode;
		objectKey.employmentCode = employmentCode;
		this.commandProxy().remove(CmnmtEmp.class, objectKey);
	}
	/**
	 * find employment by company code and employment code
	 */
	@Override
	public Optional<Employment> findEmployment(String companyCode, String employmentCode) {
		CmnmtEmpPK primaryKey = new CmnmtEmpPK(companyCode, employmentCode);
		return  this.queryProxy().find(primaryKey, CmnmtEmp.class)
				.map(x->toDomain(x));
	}
	/**
	 * find all employments by company code
	 */
	@Override
	public List<Employment> findAllEmployment(String companyCode) {
		List<Employment> test = this.queryProxy().query(SELECT_ALL_EMP_BY_COMPANY + " ORDER by c.cmnmtEmpPk.employmentCode", CmnmtEmp.class)
				.setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
		return test;
	}
	private static Employment toDomain(CmnmtEmp entity) {
		val domain = Employment.createFromJavaType(entity.cmnmtEmpPk.companyCode,
				entity.cmnmtEmpPk.employmentCode,
				entity.employmentName,
				entity.closeDateNo, 
				entity.memo, 
				entity.processingNo,
				entity.statutoryHolidayAtr,
				entity.employementOutCd,
				entity.displayFlg);
		return domain;
		
	}
	/**
	 * find employment by company code and display flag = 1
	 */
	@Override
	public Optional<Employment> findEmploymnetByDisplayFlg(String companyCode) {		
		Optional<Employment> lstEmployment= this.queryProxy().query(SELECT_EMP_BY_DISPLAY_FLG, CmnmtEmp.class)
				.setParameter("companyCode", companyCode)
				.getSingle(c -> toDomain(c));
		return lstEmployment;
			
	}

}
