/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem.applicable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpCon;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpConPK_;
import nts.uk.ctx.at.record.infra.entity.optitem.applicable.KrcstApplEmpCon_;

/**
 * The Class JpaEmpConditionRepository.
 */
@Stateless
public class JpaEmpConditionRepository extends JpaRepository implements EmpConditionRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository#update
	 * (nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition)
	 */
	@Override
	public void update(EmpCondition dom) {
		List<KrcstApplEmpCon> entities = this.findByItemNo(dom.getCompanyId().v(), dom.getOptItemNo().v());
		JpaEmpConditionSetMemento memento = new JpaEmpConditionSetMemento(entities);
		dom.saveToMemento(memento);

		this.commandProxy().updateAll(memento.getTypeValues());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository#find(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public EmpCondition find(String companyId, Integer optionalItemNo) {

		List<KrcstApplEmpCon> entityEmpCons = this.findByItemNo(companyId, optionalItemNo);

		return new EmpCondition(new JpaEmpConditionGetMemento(companyId, optionalItemNo, entityEmpCons));
	}

	/**
	 * Find by item no.
	 *
	 * @param companyId the company id
	 * @param optionalItemNo the optional item no
	 * @return the list
	 */
	private List<KrcstApplEmpCon> findByItemNo(String companyId, Integer optionalItemNo) {
		// Get entity manager
		EntityManager em = this.getEntityManager();

		// Create builder
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// Create query
		CriteriaQuery<KrcstApplEmpCon> cq = builder.createQuery(KrcstApplEmpCon.class);

		// From table
		Root<KrcstApplEmpCon> root = cq.from(KrcstApplEmpCon.class);

		List<Predicate> predicateList = new ArrayList<Predicate>();

		// Add where condition
		predicateList.add(
				builder.equal(root.get(KrcstApplEmpCon_.krcstApplEmpConPK).get(KrcstApplEmpConPK_.cid), companyId));
		predicateList.add(builder.equal(
				root.get(KrcstApplEmpCon_.krcstApplEmpConPK).get(KrcstApplEmpConPK_.optionalItemNo), optionalItemNo));
		cq.where(predicateList.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList();
	}

	
	/*
	 * 会社IDのみで全て取得
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.applicable.EmpConditionRepository#find(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<EmpCondition> findAll(String companyId, List<Integer> optionalItemNoList) {
		
		if(CollectionUtil.isEmpty(optionalItemNoList)) {
			return Collections.emptyList();
		}
		
		List<KrcstApplEmpCon> result = new ArrayList<>();
		CollectionUtil.split(optionalItemNoList, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			
			String sql = "select * from KRCST_APPL_EMP_CON"
					+ " where CID = ?"
					+ " and OPTIONAL_ITEM_NO in (" + NtsStatement.In.createParamsString(subList) + ")";
			
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				stmt.setString(1, companyId);
				for (int i = 0; i < subList.size(); i++) {
					stmt.setInt(2 + i, subList.get(i));
				}
				
				List<KrcstApplEmpCon> subResult = new NtsResultSet(stmt.executeQuery())
						.getList(rec -> KrcstApplEmpCon.MAPPER.toEntity(rec));
				result.addAll(subResult);
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		});
		
		// Group by item NO
		Map<Integer, List<KrcstApplEmpCon>> mapEmpCondition = result.stream().collect(
				Collectors.groupingBy(item -> item.getKrcstApplEmpConPK().getOptionalItemNo()));

		// Return
		return mapEmpCondition.entrySet().stream()
				.map(item -> new EmpCondition(
						new JpaEmpConditionGetMemento(companyId, item.getKey(), item.getValue())))
				.collect(Collectors.toList());
	}
	
}
