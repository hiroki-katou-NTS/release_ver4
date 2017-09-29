/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace.config;

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

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigHistory;
import nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfig;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfigPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWkpConfig_;

/**
 * The Class JpaWorkplaceConfigRepository.
 */
@Stateless
public class JpaWorkplaceConfigRepository extends JpaRepository implements WorkplaceConfigRepository {

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#add
     * (nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig)
     */
    @Override
    public void add(WorkplaceConfig workplaceConfig) {
        this.commandProxy().insertAll(this.toEntity(workplaceConfig));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * update(nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfig)
     */
    @Override
    public void update(WorkplaceConfig wkpConfig) {
        this.commandProxy().updateAll(this.toEntity(wkpConfig));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * findAllByCompanyId(java.lang.String)
     */
    @Override
    public WorkplaceConfig findAllByCompanyId(String companyId) {
        List<WorkplaceConfigHistory> lstWorkplaceConfigHistory = new ArrayList<>();
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWkpConfig> cq = criteriaBuilder.createQuery(BsymtWkpConfig.class);
        Root<BsymtWkpConfig> root = cq.from(BsymtWkpConfig.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWkpConfig_.bsymtWkpConfigPK).get(BsymtWkpConfigPK_.cid), companyId));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        cq.orderBy(criteriaBuilder.desc(root.get(BsymtWkpConfig_.strD)),
                criteriaBuilder.desc(root.get(BsymtWkpConfig_.endD)));
        // exclude select
        lstWorkplaceConfigHistory = em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
                .collect(Collectors.toList());
        return new WorkplaceConfig(new JpaWorkplaceConfigGetMemento(companyId, lstWorkplaceConfigHistory));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * findByHistId(java.lang.String, java.lang.String)
     */
    @Override
    public Optional<WorkplaceConfig> findByHistId(String companyId, String prevHistId) {
        List<WorkplaceConfigHistory> lstWorkplaceConfigHistory = new ArrayList<>();
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWkpConfig> cq = criteriaBuilder.createQuery(BsymtWkpConfig.class);
        Root<BsymtWkpConfig> root = cq.from(BsymtWkpConfig.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWkpConfig_.bsymtWkpConfigPK).get(BsymtWkpConfigPK_.cid), companyId));

        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWkpConfig_.bsymtWkpConfigPK).get(BsymtWkpConfigPK_.historyId), prevHistId));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

        // exclude select
        lstWorkplaceConfigHistory = em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
                .collect(Collectors.toList());
        return Optional.of(new WorkplaceConfig(new JpaWorkplaceConfigGetMemento(companyId, lstWorkplaceConfigHistory)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * findLatestByCompanyId(java.lang.String)
     */
    @Override
    public Optional<WorkplaceConfig> findWorkplaceByCompanyId(String companyId) {
        List<WorkplaceConfigHistory> lstWorkplaceConfigHistory = new ArrayList<>();
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWkpConfig> cq = criteriaBuilder.createQuery(BsymtWkpConfig.class);
        Root<BsymtWkpConfig> root = cq.from(BsymtWkpConfig.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWkpConfig_.bsymtWkpConfigPK).get(BsymtWkpConfigPK_.cid), companyId));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        cq.orderBy(criteriaBuilder.desc(root.get(BsymtWkpConfig_.strD)));

        lstWorkplaceConfigHistory = em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
                .collect(Collectors.toList());
        return Optional.of(new WorkplaceConfig(new JpaWorkplaceConfigGetMemento(companyId, lstWorkplaceConfigHistory)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * findByStartDate(java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public Optional<WorkplaceConfig> findByStartDate(String companyId, GeneralDate startDate) {
        List<WorkplaceConfigHistory> lstWorkplaceConfigHistory = new ArrayList<>();
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWkpConfig> cq = criteriaBuilder.createQuery(BsymtWkpConfig.class);
        Root<BsymtWkpConfig> root = cq.from(BsymtWkpConfig.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWkpConfig_.bsymtWkpConfigPK).get(BsymtWkpConfigPK_.cid), companyId));

        lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWkpConfig_.strD), startDate));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

        // exclude select
        lstWorkplaceConfigHistory = em.createQuery(cq).getResultList().stream().map(item -> this.toDomain(item))
                .collect(Collectors.toList());
        return Optional.of(new WorkplaceConfig(new JpaWorkplaceConfigGetMemento(companyId, lstWorkplaceConfigHistory)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.config.WorkplaceConfigRepository#
     * removeWkpConfigHist(java.lang.String, java.lang.String)
     */
    @Override
    public void removeWkpConfigHist(String companyId, String historyId) {
        this.commandProxy().remove(BsymtWkpConfig.class, new BsymtWkpConfigPK(companyId, historyId));
    }

    /**
     * To entity.
     *
     * @param workplaceConfig
     *            the workplace config
     * @return the list
     */
    private List<BsymtWkpConfig> toEntity(WorkplaceConfig workplaceConfig) {
        String companyId = workplaceConfig.getCompanyId();

        List<BsymtWkpConfig> lstEntity = new ArrayList<>();

        for (WorkplaceConfigHistory wkpConfigHist : workplaceConfig.getWkpConfigHistory()) {
            BsymtWkpConfigPK pk = new BsymtWkpConfigPK(companyId, wkpConfigHist.getHistoryId());

            Optional<BsymtWkpConfig> optional = this.queryProxy().find(pk, BsymtWkpConfig.class);

            BsymtWkpConfig entity = null;

            if (!optional.isPresent()) {
                entity = new BsymtWkpConfig();
                entity.setBsymtWkpConfigPK(pk);
            } else {
                entity = optional.get();
            }
            lstEntity.add(entity);
        }

        JpaWorkplaceConfigSetMemento memento = new JpaWorkplaceConfigSetMemento(lstEntity);
        workplaceConfig.saveToMemento(memento);

        return lstEntity;
    }

    /**
     * To domain.
     *
     * @param item
     *            the item
     * @return the workplace config history
     */
    private WorkplaceConfigHistory toDomain(BsymtWkpConfig item) {
        return new WorkplaceConfigHistory(new JpaWorkplaceConfigHistoryGetMemento(item));
    }

}
