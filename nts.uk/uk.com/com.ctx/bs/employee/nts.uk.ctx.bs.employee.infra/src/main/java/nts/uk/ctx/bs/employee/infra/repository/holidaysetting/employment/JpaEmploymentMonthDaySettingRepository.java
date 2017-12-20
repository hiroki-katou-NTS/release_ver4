package nts.uk.ctx.bs.employee.infra.repository.holidaysetting.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySetting;
import nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employment.KshmtEmpMonthDaySet;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employment.KshmtEmpMonthDaySetPK_;
import nts.uk.ctx.bs.employee.infra.entity.holidaysetting.employment.KshmtEmpMonthDaySet_;

/**
 * The Class JpaEmploymentMonthDaySettingRepository.
 */
@Stateless
public class JpaEmploymentMonthDaySettingRepository extends JpaRepository implements EmploymentMonthDaySettingRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository#findByYear(nts.uk.ctx.bs.employee.dom.common.CompanyId, java.lang.String, nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public Optional<EmploymentMonthDaySetting> findByYear(CompanyId companyId, String empCd, Year year) {
		List<KshmtEmpMonthDaySet> result = this.findBy(companyId, empCd, year, null);
		
		// Check exist
		if (CollectionUtil.isEmpty(result)) {
			return Optional.empty();
		}
		
		EmploymentMonthDaySetting domain = new EmploymentMonthDaySetting(new JpaEmploymentMonthDaySettingGetMemento(result));
			
		return Optional.of(domain);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository#add(nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySetting)
	 */
	@Override
	public void add(EmploymentMonthDaySetting domain) {
		List<KshmtEmpMonthDaySet> entities = new ArrayList<>();
		domain.saveToMemento(new JpaEmploymentMonthDaySettingSetMemento(entities));
		this.commandProxy().insertAll(entities);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository#update(nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySetting)
	 */
	@Override
	public void update(EmploymentMonthDaySetting domain) {
		List<KshmtEmpMonthDaySet> entities = this.findBy(domain.getCompanyId(), domain.getEmploymentCode(),
				domain.getManagementYear(), null);
		domain.saveToMemento(new JpaEmploymentMonthDaySettingSetMemento(entities));
		this.commandProxy().updateAll(entities);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.holidaysetting.employment.EmploymentMonthDaySettingRepository#remove(nts.uk.ctx.bs.employee.dom.common.CompanyId, java.lang.String, nts.uk.ctx.bs.employee.dom.holidaysetting.common.Year)
	 */
	@Override
	public void remove(CompanyId companyId, String empCd, Year year) {
		List<KshmtEmpMonthDaySet> result = this.findBy(companyId, empCd, year, null);
		this.commandProxy().removeAll(result);
	}
	
	/**
	 * Find by.
	 *
	 * @param companyId the company id
	 * @param empCd the emp cd
	 * @param year the year
	 * @param month the month
	 * @return the list
	 */
	private List<KshmtEmpMonthDaySet> findBy(CompanyId companyId, String empCd, Year year, Integer month) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<KshmtEmpMonthDaySet> cq = criteriaBuilder.createQuery(KshmtEmpMonthDaySet.class);

		// root data
		Root<KshmtEmpMonthDaySet> root = cq.from(KshmtEmpMonthDaySet.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// eq company id
		if (companyId != null) {
			lstpredicateWhere
					.add(criteriaBuilder.equal(root.get(KshmtEmpMonthDaySet_.kshmtEmpMonthDaySetPK).get(KshmtEmpMonthDaySetPK_.cid), companyId));
		}
		
		if (empCd != null) {
			lstpredicateWhere.add(criteriaBuilder.equal(
					root.get(KshmtEmpMonthDaySet_.kshmtEmpMonthDaySetPK).get(KshmtEmpMonthDaySetPK_.empCd),
					empCd));
		}

		if (year != null) {
			lstpredicateWhere.add(criteriaBuilder.equal(
					root.get(KshmtEmpMonthDaySet_.kshmtEmpMonthDaySetPK).get(KshmtEmpMonthDaySetPK_.manageYear),
					year));
		}

		if (month != null) {
			lstpredicateWhere
					.add(criteriaBuilder.equal(root.get(KshmtEmpMonthDaySet_.kshmtEmpMonthDaySetPK)
							.get(KshmtEmpMonthDaySetPK_.month), month));
		}

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// creat query
		TypedQuery<KshmtEmpMonthDaySet> query = em.createQuery(cq);

		return query.getResultList();
	}
}
