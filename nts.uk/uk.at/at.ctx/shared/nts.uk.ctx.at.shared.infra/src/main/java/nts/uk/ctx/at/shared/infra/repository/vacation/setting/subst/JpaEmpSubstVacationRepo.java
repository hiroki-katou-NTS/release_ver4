/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.subst;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.subst.KsvstEmpSubstVacation;
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
		Optional<KsvstEmpSubstVacation> optEntity = this.queryProxy().find(setting.getCompanyId(),
				KsvstEmpSubstVacation.class);

		KsvstEmpSubstVacation entity = optEntity.get();

		setting.saveToMemento(new JpaEmpSubstVacationSetMemento(entity));

		this.commandProxy().update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.vacation.setting.subst.
	 * EmpSubstVacationRepository#findById(java.lang.String)
	 */
	@Override
	public Optional<EmpSubstVacation> findById(String companyId, String contractTypeCode) {
		EntityManager em = this.getEntityManager();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<KsvstEmpSubstVacation> cq = builder.createQuery(KsvstEmpSubstVacation.class);
		Root<KsvstEmpSubstVacation> root = cq.from(KsvstEmpSubstVacation.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		predicateList.add(builder.equal(root.get(KsvstEmpSubstVacation_.kclstEmpSubstVacationPK)
				.get(KsvstEmpSubstVacationPK_.cid), companyId));
		predicateList.add(builder.equal(root.get(KsvstEmpSubstVacation_.kclstEmpSubstVacationPK)
				.get(KsvstEmpSubstVacationPK_.contractTypeCd), contractTypeCode));

		cq.where(predicateList.toArray(new Predicate[] {}));

		List<KsvstEmpSubstVacation> results = em.createQuery(cq).getResultList();

		if (CollectionUtil.isEmpty(results)) {
			return Optional.empty();
		}

		return Optional.of(new EmpSubstVacation(new JpaEmpSubstVacationGetMemento(results.get(0))));
	}

}
