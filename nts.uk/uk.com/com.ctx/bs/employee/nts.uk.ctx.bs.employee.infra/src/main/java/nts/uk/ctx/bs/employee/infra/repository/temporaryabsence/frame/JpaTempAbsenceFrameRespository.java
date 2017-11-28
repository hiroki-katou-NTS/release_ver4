/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.bs.employee.infra.repository.temporaryabsence.frame;

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
import nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceFrame;
import nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame;
import nts.uk.ctx.bs.employee.infra.entity.temporaryabsence.frame.BsystTempAbsenceFrame;
import nts.uk.ctx.bs.employee.infra.entity.temporaryabsence.frame.BsystTempAbsenceFramePK;
import nts.uk.ctx.bs.employee.infra.entity.temporaryabsence.frame.BsystTempAbsenceFramePK_;
import nts.uk.ctx.bs.employee.infra.entity.temporaryabsence.frame.BsystTempAbsenceFrame_;

/**
 * The Class JpaTempAbsenceFrameRespository.
 */
@Stateless
public class JpaTempAbsenceFrameRespository extends JpaRepository implements TempAbsenceRepositoryFrame{

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame#add(nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceFrame)
	 */
	@Override
	public void add(TempAbsenceFrame tempAbsenceFrame) {
		this.commandProxy().insert(this.toEntity(tempAbsenceFrame));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame#udpate(nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceFrame)
	 */
	@Override
	public void udpate(TempAbsenceFrame tempAbsenceFrame) {
		 this.commandProxy().update(this.toEntity(tempAbsenceFrame));
	}

	/**
	 * To entity.
	 *
	 * @param tempAbsenceFrame the temp absence frame
	 * @return the bsyst temp absence frame
	 */
	private BsystTempAbsenceFrame toEntity(TempAbsenceFrame tempAbsenceFrame) {
		Optional<BsystTempAbsenceFrame> optional = this.queryProxy()
				.find(new BsystTempAbsenceFramePK(tempAbsenceFrame.getCompanyId(), 
													tempAbsenceFrame.getTempAbsenceFrNo().v().shortValue()), 
						BsystTempAbsenceFrame.class);
		BsystTempAbsenceFrame entity = new BsystTempAbsenceFrame();
		if (optional.isPresent()) {
			entity = optional.get();
		}
		JpaTempAbsenceFrameSetMemento memento = new JpaTempAbsenceFrameSetMemento(entity);
		tempAbsenceFrame.saveToMemento(memento);
		return entity;
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame#findByTAFPk(java.lang.String, short)
	 */
	@Override
	public TempAbsenceFrame findByTAFPk(String cId, short tempAbsenceFrameNo) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<BsystTempAbsenceFrame> cq = criteriaBuilder.createQuery(BsystTempAbsenceFrame.class);
		Root<BsystTempAbsenceFrame> root = cq.from(BsystTempAbsenceFrame.class);
		
		// select root
		cq.select(root);
		
		// add where
		List<Predicate> lstPredicateWhere = new ArrayList<>();
		lstPredicateWhere.add(root.get(BsystTempAbsenceFrame_.bsystTempAbsenceFramePK)
				.get(BsystTempAbsenceFramePK_.cid).in(cId));
		
		lstPredicateWhere.add(root.get(BsystTempAbsenceFrame_.bsystTempAbsenceFramePK)
				.get(BsystTempAbsenceFramePK_.tempAbsenceFrNo).in(tempAbsenceFrameNo));
		
		cq.where(lstPredicateWhere.toArray(new Predicate[] {}));
		
		return new TempAbsenceFrame(new JpaTempAbsenceFrameGetMemento(em.createQuery(cq).getSingleResult()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.temporaryabsence.frame.TempAbsenceRepositoryFrame#findByCid(java.lang.String)
	 */
	@Override
	public List<TempAbsenceFrame> findByCid(String cId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<BsystTempAbsenceFrame> cq = criteriaBuilder.createQuery(BsystTempAbsenceFrame.class);
		Root<BsystTempAbsenceFrame> root = cq.from(BsystTempAbsenceFrame.class);
		
		// select root
		cq.select(root);
		
		// add where
		List<Predicate> lstPredicateWhere = new ArrayList<>();
		lstPredicateWhere.add(root.get(BsystTempAbsenceFrame_.bsystTempAbsenceFramePK)
				.get(BsystTempAbsenceFramePK_.cid).in(cId));
		
		cq.where(lstPredicateWhere.toArray(new Predicate[] {}));
		
		return em.createQuery(cq).getResultList().stream()
				.map(item -> new TempAbsenceFrame(new JpaTempAbsenceFrameGetMemento(item)))
				.collect(Collectors.toList());
	}

}
