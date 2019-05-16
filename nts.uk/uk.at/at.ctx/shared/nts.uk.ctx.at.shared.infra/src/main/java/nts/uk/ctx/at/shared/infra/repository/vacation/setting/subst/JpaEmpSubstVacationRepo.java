/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.subst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacation;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacationPK;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacationPK_;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacation_;

/**
 * The Class JpaEmpSubstVacationRepo.
 */
@Stateless
public class JpaEmpSubstVacationRepo extends JpaRepository implements EmpSubstVacationRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationRepository#insert(nts.uk.ctx.at.shared.dom.vacation.
	 * setting.subst.EmpSubstVacation)
	 */
	@Override
	public void insert(EmpSubstVacation setting) {
		KsvstEmpSubstVacation entity = new KsvstEmpSubstVacation();

		setting.saveToMemento(new JpaEmpSubstVacationSetMemento(entity));

		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * ComSubstVacationRepository#update(nts.uk.ctx.at.shared.dom.vacation.
	 * setting.subst.ComSubstVacation)
	 */
	@Override
	public void update(EmpSubstVacation setting) {
		Optional<KsvstEmpSubstVacation> optEntity = this.queryProxy()
				.find(new KsvstEmpSubstVacationPK(setting.getCompanyId(),
						setting.getEmpContractTypeCode()), KsvstEmpSubstVacation.class);

		KsvstEmpSubstVacation entity = optEntity.get();

		setting.saveToMemento(new JpaEmpSubstVacationSetMemento(entity));

		this.commandProxy().update(entity);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * ComSubstVacationRepository#delete(nts.uk.ctx.at.shared.dom.vacation.
	 * setting.subst.ComSubstVacation)
	 */
	@Override
	public void delete(String companyId, String contractTypeCode) {
		KsvstEmpSubstVacationPK key = new KsvstEmpSubstVacationPK(companyId, contractTypeCode);
		this.commandProxy().remove(KsvstEmpSubstVacation.class, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationRepository#findById(java.lang.String)
	 */
	@Override
	@SneakyThrows
	public Optional<EmpSubstVacation> findById(String companyId, String contractTypeCode) {
		
		String sql = "select * from KSVST_EMP_SUBST_VACATION"
    			+ " where CID = ?"
    			+ " and EMPCD = ?";
    	try (val stmt = this.connection().prepareStatement(sql)) {
	    	stmt.setString(1, companyId);
	    	stmt.setString(2, contractTypeCode);
	    	
	    	val entity = new NtsResultSet(stmt.executeQuery())
	    			.getSingle(rec -> KsvstEmpSubstVacation.MAPPER.toEntity(rec));
	
	    	return entity.map(e -> new EmpSubstVacation(new JpaEmpSubstVacationGetMemento(e)));
    	}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationRepository#findAll(java.lang.String)
	 */
	@Override
	public List<EmpSubstVacation> findAll(String companyId) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<KsvstEmpSubstVacation> cq = builder.createQuery(KsvstEmpSubstVacation.class);
		Root<KsvstEmpSubstVacation> root = cq.from(KsvstEmpSubstVacation.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		predicateList.add(builder.equal(root.get(KsvstEmpSubstVacation_.kclstEmpSubstVacationPK)
				.get(KsvstEmpSubstVacationPK_.cid), companyId));

		cq.where(predicateList.toArray(new Predicate[] {}));
		return em.createQuery(cq).getResultList().stream()
				.map(entity -> new EmpSubstVacation(new JpaEmpSubstVacationGetMemento(entity)))
				.collect(Collectors.toList());
	}

}
