package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingHeader;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotCp;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotEm;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotEmPK;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotHEm;

@Stateless
public class JpaEmployeeAllotSettingRepository extends JpaRepository implements EmployeeAllotSettingRepository {
	private final String SEL_1 = "SELECT c FROM QstmtStmtAllotEm c"
			+ " WHERE c.qstmtStmtAllotEmPK.companyCode = :companyCode"
			+ " AND c.qstmtStmtAllotEmPK.employeeCd = :employeeCd" + " AND c.qstmtStmtAllotEmPK.histId = :histId"
			+ " AND ( c.paymentDetailCode != '00'" + " OR c.bonusDetailCode != '00')";

	private final String SEL_2 = "SELECT c FROM QstmtStmtAllotEm c "
			+ " WHERE c.qstmtStmtAllotEmPK.companyCode = :companyCode" + " AND c.qstmtStmtAllotEmPK.histId = :histId";

	private final String ALL_EMPLOYEE_SETTING = "SELECT e.cmnmtEmpPk.companyCode, a.QstmtStmtAllotEmPK.histId,  e.cmnmtEmpPk.employmentCode, e.employmentName, q.qstmtStmtLayoutHeadPK.stmtCd, q.stmtName"
			+ " FROM CmnmtEmp e, QstmtStmtAllotEm a "
//			+ " ON e.cmnmtEmpPk.companyCode = a.qstmtStmtAllotEmPK.companyCode AND e.cmnmtEmpPk.employmentCode = a.qstmtStmtAllotEmPK.employeeCd"
			+ " , QstmtStmtLayoutHead q"
//			+ " ON a.QstmtStmtAllotEmPK.companyCode = q.qstmtStmtLayoutHeadPK.companyCd AND"
//			+ " a.paymentDetailCode = q.qstmtStmtLayoutHeadPK.stmtCd AND a.bonusDetailCode = q.qstmtStmtLayoutHeadPK.stmtCd"
			+ " WHERE e.cmnmtEmpPk.companyCode = a.QstmtStmtAllotEmPK.companyCode AND e.cmnmtEmpPk.employmentCode = a.QstmtStmtAllotEmPK.employeeCd AND "
			+ " a.QstmtStmtAllotEmPK.companyCode = q.qstmtStmtLayoutHeadPK.companyCd AND "
			+ " (a.paymentDetailCode = q.qstmtStmtLayoutHeadPK.stmtCd OR a.bonusDetailCode = q.qstmtStmtLayoutHeadPK.stmtCd) AND"
			+ " a.QstmtStmtAllotEmPK.companyCode = :companyCode AND a.QstmtStmtAllotEmPK.histId = :histId";
//	private final String MAX_END = "SELECT c FROM QstmtStmtAllotHEm c"
//			+ " WHERE c.QstmtStmtAllotHEmPK.companyCode = :companyCode "
//			+ " AND c.endDate = (SELECT MAX(d.endDate) FROM QstmtStmtAllotHEm d)";
	private final String EMP_SET_SEL = "SELECT a "
									+ " FROM QstmtStmtAllotEm a WHERE a.QstmtStmtAllotEmPK.histId = :histId ";
	
	private final String EMP_SEL = "SELECT e.cmnmtEmpPk.employmentCode, e.employmentName, e.cmnmtEmpPk.companyCode  FROM CmnmtEmp e WHERE e.cmnmtEmpPk.companyCode = :companyCode ";
	 
	private final String LAYOUT_HEAD = "SELECT q.qstmtStmtLayoutHeadPK.companyCd, q.qstmtStmtLayoutHeadPK.stmtCd, q.stmtName, a.QstmtStmtAllotEmPK.histId FROM QstmtStmtLayoutHead q WHERE q.qstmtStmtLayoutHeadPK.stmtCd, = :stmtCd";
	@Override
	public Optional<EmployeeAllotSetting> find(String companyCode, String historyID, String employeeCd) {
		Optional<QstmtStmtAllotEm> empAllot = this.queryProxy().query(SEL_1, QstmtStmtAllotEm.class)
				.setParameter("companyCode", companyCode).setParameter("employeeCd", employeeCd)
				.setParameter("histId", historyID).getSingle();

		if (!empAllot.isPresent()) {
			return Optional.empty();
		}

		return Optional.of(toDomain(empAllot.get()));
	}

	private EmployeeAllotSetting toDomain(QstmtStmtAllotEm empAllot) {
		val domain = EmployeeAllotSetting.createFromJavaType(empAllot.QstmtStmtAllotEmPK.companyCode,
				empAllot.QstmtStmtAllotEmPK.histId, empAllot.QstmtStmtAllotEmPK.employeeCd, empAllot.paymentDetailCode,
				empAllot.bonusDetailCode);
		return domain;
	}
//	private static QstmtStmtAllotEm toEntity(QstmtStmtAllotEm domain){
//		val entity = new QstmtStmtAllotEm();
//		entity.QstmtStmtAllotEmPK = new QstmtStmtAllotEmPK(domain.);
//	}
	
	private final EmployeeAllotSettingHeader toDomain(QstmtStmtAllotHEm entity) {
		// return null;
		val domain = EmployeeAllotSettingHeader.createFromJavaType(entity.QstmtStmtAllotHEmPK.companyCode,
				entity.QstmtStmtAllotHEmPK.histId, entity.startYm, entity.endYm);
		return domain;
	}

	@Override
	public List<EmployeeAllotSetting> findAll(String companyCode, String historyID) {
		return this.queryProxy().query(SEL_2, QstmtStmtAllotEm.class).setParameter("companyCode", companyCode)
				.setParameter("histId", historyID).getList(c -> toDomain(c));
	}

	@Override
	public List<EmployeeAllSetting> findAllEm(String companyCode, String historyID) {
		 List<EmployeeAllSetting> result = this.queryProxy().query(ALL_EMPLOYEE_SETTING, Object[].class).setParameter("companyCode", companyCode)
				.setParameter("histId", historyID)//.getList();
				.getList(s -> EmployeeAllSetting.createFromJavaType(companyCode, s[1].toString(), s[2].toString(),
						s[3].toString(), s[4].toString(), s[5].toString(), s[4].toString(), s[5].toString()));
		return result;
	}

	@Override
	public List<EmployeeAllotSetting> findEmpDetail(String companyCode, String historyID) {
		List<EmployeeAllotSetting> result = this.queryProxy().query(EMP_SET_SEL, QstmtStmtAllotEm.class)
				.setParameter("histId", historyID)
				.getList(a -> toDomain(a));
		return result;
	}

	
	
	

//	@Override
//	public List<Employment> findAllEmName() {
//		return this.queryProxy().query(SEL_EMNAME, CmnmtEmp.class).getList(c-> toDomain(c));
//	}

//	@Override
//	public Optional<EmployeeAllotSettingHeader> findMaxEnd(String companyCode) {
//		return this.queryProxy().query(MAX_END, QstmtStmtAllotHEm.class).setParameter("companyCode", companyCode)
//				.getSingle(c-> toDomain(c));
//	}

	

}
