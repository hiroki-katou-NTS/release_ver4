/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.workplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.workplace.Workplace;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceHistory;
import nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHistPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHistPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist_;

/**
 * The Class JpaBSWorkplaceRepository.
 */
@Stateless
public class JpaBSWorkplaceRepository extends JpaRepository implements WorkplaceRepository {

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#add(nts.uk.ctx.
     * bs.employee.dom.workplace.Workplace)
     */
    @Override
    public void add(Workplace wkp) {
        this.commandProxy().insertAll(this.toEntity(wkp));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#update(nts.uk.
     * ctx.bs.employee.dom.workplace.Workplace)
     */
    @Override
    public void update(Workplace wkp) {
        this.commandProxy().updateAll(this.toEntity(wkp));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#removeByWkpId(
     * java.lang.String, java.lang.String)
     */
    @Override
    public void removeByWkpId(String companyId, String workplaceId) {
        Optional<Workplace> optional = this.findByWorkplaceId(companyId, workplaceId);
        if (!optional.isPresent()) {
            throw new RuntimeException(String.format("Workplace %s doesn't exited.", workplaceId));
        }
        // find list entity need to remove
        List<BsymtWorkplaceHistPK> lstPrimaryKey = optional.get().getWorkplaceHistory().stream()
                .map(item -> {
                    BsymtWorkplaceHistPK pk = new BsymtWorkplaceHistPK();
                    pk.setCid(companyId);
                    pk.setWkpid(workplaceId);
                    pk.setHistoryId(item.getHistoryId().v());
                    return pk;
                }).collect(Collectors.toList());
        this.commandProxy().removeAll(BsymtWorkplaceHist.class, lstPrimaryKey);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#findByStartDate(
     * java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public Optional<Workplace> findByStartDate(String companyId, GeneralDate date) {
     // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceHist> cq = criteriaBuilder.createQuery(BsymtWorkplaceHist.class);
        Root<BsymtWorkplaceHist> root = cq.from(BsymtWorkplaceHist.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWorkplaceHist_.strD), date));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        
        List<BsymtWorkplaceHist> lstBsymtWorkplaceHist = em.createQuery(cq).getResultList();
        // check empty
        if (CollectionUtil.isEmpty(lstBsymtWorkplaceHist)) {
            return Optional.empty();
        }
        return Optional.of(new Workplace(new JpaWorkplaceGetMemento(lstBsymtWorkplaceHist)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#findByWkpIds(
     * java.util.List)
     */
    @Override
    public List<Workplace> findByWkpIds(List<String> workplaceIds) {
        if (CollectionUtil.isEmpty(workplaceIds)) {
            return null;
        }
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceHist> cq = criteriaBuilder.createQuery(BsymtWorkplaceHist.class);
        Root<BsymtWorkplaceHist> root = cq.from(BsymtWorkplaceHist.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> predicateList = new ArrayList<>();
        
        Expression<String> exp = root.get(BsymtWorkplaceHist_.bsymtWorkplaceHistPK).get(BsymtWorkplaceHistPK_.wkpid);
        predicateList.add(exp.in(workplaceIds));
        
        cq.where(predicateList.toArray(new Predicate[] {}));
        cq.orderBy(criteriaBuilder.desc(root.get(BsymtWorkplaceHist_.strD)));
        
        List<BsymtWorkplaceHist> lstBsymtWorkplaceHist = em.createQuery(cq).getResultList();
        // check empty
        if (CollectionUtil.isEmpty(lstBsymtWorkplaceHist)) {
            return null;
        }
        return workplaceIds.stream().map(wkpId -> {
            List<BsymtWorkplaceHist> subListEntity = lstBsymtWorkplaceHist.stream()
                    .filter(entity -> entity.getBsymtWorkplaceHistPK().getWkpid().equals(wkpId))
                    .collect(Collectors.toList());
            return new Workplace(new JpaWorkplaceGetMemento(subListEntity));
        }).collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.bs.employee.dom.workplace.WorkplaceRepository#
     * findByWorkplaceId(java.lang.String, java.lang.String)
     */
    @Override
    public Optional<Workplace> findByWorkplaceId(String companyId, String workplaceId) {
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceHist> cq = criteriaBuilder.createQuery(BsymtWorkplaceHist.class);
        Root<BsymtWorkplaceHist> root = cq.from(BsymtWorkplaceHist.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceHist_.bsymtWorkplaceHistPK).get(BsymtWorkplaceHistPK_.cid), companyId));
        lstpredicateWhere.add(criteriaBuilder.equal(
                root.get(BsymtWorkplaceHist_.bsymtWorkplaceHistPK).get(BsymtWorkplaceHistPK_.wkpid), workplaceId));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        cq.orderBy(criteriaBuilder.desc(root.get(BsymtWorkplaceHist_.strD)));
        
        List<BsymtWorkplaceHist> lstBsymtWorkplaceHist = em.createQuery(cq).getResultList();
        // check empty
        if (CollectionUtil.isEmpty(lstBsymtWorkplaceHist)) {
            return Optional.empty();
        }
        return Optional.of(new Workplace(new JpaWorkplaceGetMemento(lstBsymtWorkplaceHist)));
    }

    /**
     * To entity.
     *
     * @param workplace the workplace
     * @return the list
     */
    private List<BsymtWorkplaceHist> toEntity(Workplace workplace) {
        String companyId = workplace.getCompanyId();
        String workplaceId = workplace.getWorkplaceId().v();

        List<BsymtWorkplaceHist> lstEntity = new ArrayList<>();

        for (WorkplaceHistory wkpHistory : workplace.getWorkplaceHistory()) {
            BsymtWorkplaceHistPK pk = new BsymtWorkplaceHistPK(companyId, workplaceId, wkpHistory.getHistoryId().v());
            BsymtWorkplaceHist entity = this.queryProxy().find(pk, BsymtWorkplaceHist.class)
                    .orElse(new BsymtWorkplaceHist());
            entity.setBsymtWorkplaceHistPK(pk);
            lstEntity.add(entity);
        }
        JpaWorkplaceSetMemento memento = new JpaWorkplaceSetMemento(lstEntity);
        workplace.saveToMemento(memento);

        return lstEntity;
    }

}
