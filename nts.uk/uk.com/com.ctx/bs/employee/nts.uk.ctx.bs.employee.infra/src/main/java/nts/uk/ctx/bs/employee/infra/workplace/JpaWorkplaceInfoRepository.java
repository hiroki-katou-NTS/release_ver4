/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.workplace;

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
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo;
import nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceHist_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfoPK_;
import nts.uk.ctx.bs.employee.infra.entity.workplace.BsymtWorkplaceInfo_;

/**
 * The Class JpaWorkplaceInfoRepository.
 */
@Stateless
public class JpaWorkplaceInfoRepository extends JpaRepository implements WorkplaceInfoRepository {

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#find(
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Optional<WorkplaceInfo> find(String companyId, String wkpId, String historyId) {
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
        Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid), wkpId));
        lstpredicateWhere.add(criteriaBuilder.equal(
                root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.historyId), historyId));
        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        List<BsymtWorkplaceInfo> lst = em.createQuery(cq).getResultList();
        if (lst.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(lst.get(0))));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#update(
     * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfo)
     */
    @Override
    public void update(WorkplaceInfo workplaceInfo) {
        this.commandProxy().update(this.toEntity(workplaceInfo));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#remove(
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void remove(String companyId, String workplaceId, String historyId) {
        this.commandProxy().remove(BsymtWorkplaceInfo.class,
                new BsymtWorkplaceInfoPK(companyId, historyId, workplaceId));
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
     * findByWkpCd(java.lang.String, java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public List<WorkplaceInfo> findByWkpCd(String companyId, String wpkCode, GeneralDate baseDate) {
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
        Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.cid), companyId));
        lstpredicateWhere.add(criteriaBuilder.equal(root.get(BsymtWorkplaceInfo_.wkpcd), wpkCode));
        lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
                root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD), baseDate));
        lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD), baseDate));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        return em.createQuery(cq).getResultList().stream().map(item -> {
            return new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item));
        }).collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
     * findByWkpId(java.lang.String, nts.arc.time.GeneralDate)
     */
    @Override
    public Optional<WorkplaceInfo> findByWkpId(String wpkId, GeneralDate baseDate) {
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
        Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid), wpkId));
        lstpredicateWhere.add(criteriaBuilder.lessThanOrEqualTo(
                root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD), baseDate));
        lstpredicateWhere.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.endD), baseDate));

        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        List<WorkplaceInfo> lstWkpInfo = em.createQuery(cq).getResultList().stream().map(item -> {
            return new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item));
        }).collect(Collectors.toList());
        if (lstWkpInfo.isEmpty()) {
            return Optional.of(null);
        }
        return Optional.of(lstWkpInfo.get(0));
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.bs.employee.dom.workplace.info.WorkplaceInfoRepository#
     * findLatestByWorkplaceId(java.lang.String)
     */
    @Override
    public Optional<WorkplaceInfo> findLatestByWorkplaceId(String wkpId) {
        // get entity manager
        EntityManager em = this.getEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<BsymtWorkplaceInfo> cq = criteriaBuilder.createQuery(BsymtWorkplaceInfo.class);
        Root<BsymtWorkplaceInfo> root = cq.from(BsymtWorkplaceInfo.class);

        // select root
        cq.select(root);

        // add where
        List<Predicate> lstpredicateWhere = new ArrayList<>();
        lstpredicateWhere.add(criteriaBuilder
                .equal(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceInfoPK).get(BsymtWorkplaceInfoPK_.wkpid), wkpId));
        cq.orderBy(
                criteriaBuilder.desc(root.get(BsymtWorkplaceInfo_.bsymtWorkplaceHist).get(BsymtWorkplaceHist_.strD)));
        cq.where(lstpredicateWhere.toArray(new Predicate[] {}));
        List<WorkplaceInfo> lstWkpInfo = em.createQuery(cq).getResultList().stream().map(item -> {
            return new WorkplaceInfo(new JpaWorkplaceInfoGetMemento(item));
        }).collect(Collectors.toList());
        if (lstWkpInfo.isEmpty()) {
            return Optional.of(null);
        }
        return Optional.of(lstWkpInfo.get(0));
    }

    /**
     * To entity.
     *
     * @param workplaceInfo
     *            the workplace info
     * @return the bsymt workplace info
     */
    private BsymtWorkplaceInfo toEntity(WorkplaceInfo workplaceInfo) {
        Optional<BsymtWorkplaceInfo> optional = this.queryProxy()
                .find(new BsymtWorkplaceInfoPK(workplaceInfo.getCompanyId(), workplaceInfo.getHistoryId().v(),
                        workplaceInfo.getWorkplaceId().v()), BsymtWorkplaceInfo.class);
        BsymtWorkplaceInfo entity = new BsymtWorkplaceInfo();
        if (optional.isPresent()) {
            entity = optional.get();
        }
        JpaWorkplaceInfoSetMemento memento = new JpaWorkplaceInfoSetMemento(entity);
        workplaceInfo.saveToMemento(memento);
        return entity;
    }
}
