/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.infra.repository.optitem.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtCalcItemSelection;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtFormulaRounding;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtFormulaSetting;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtFormulaSettingPK;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormula;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormulaPK;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormulaPK_;
import nts.uk.ctx.at.record.infra.entity.optitem.calculation.KrcmtOptItemFormula_;

/**
 * The Class JpaFormulaRepository.
 */
@Stateless
public class JpaFormulaRepository extends JpaRepository implements FormulaRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#create(
	 * java.util.List)
	 */
	@Override
	public void create(List<Formula> domains) {
		this.commandProxy().insertAll(domains.stream().map(domain -> {

			// Save to memento
			JpaFormulaSetMemento memento = new JpaFormulaSetMemento();
			domain.saveToMemento(memento);

			// Get entity from memento
			return memento.getEntity();

		}).collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#remove(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void remove(String comId, Integer optItemNo) {
		this.commandProxy().removeAll(this.findEntity(comId, optItemNo));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#
	 * findByOptItemNo(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Formula> findByOptItemNo(String companyId, Integer optItemNo) {

		List<KrcmtOptItemFormula> results = this.findEntity(companyId, optItemNo);

		// Return
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		return results.stream().map(item -> new Formula(new JpaFormulaGetMemento(item))).collect(Collectors.toList());
	}

	/**
	 * Find entity.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	private List<KrcmtOptItemFormula> findEntity(String companyId, Integer optItemNo) {
		// get entity manager
		EntityManager em = this.getEntityManager();

		// create query
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<KrcmtOptItemFormula> cq = criteriaBuilder.createQuery(KrcmtOptItemFormula.class);

		// select from table
		Root<KrcmtOptItemFormula> root = cq.from(KrcmtOptItemFormula.class);
		cq.select(root);

		// add conditions
		List<Predicate> lstpredicateWhere = new ArrayList<>();
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtOptItemFormula_.krcmtOptItemFormulaPK).get(KrcmtOptItemFormulaPK_.cid), companyId));
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KrcmtOptItemFormula_.krcmtOptItemFormulaPK).get(KrcmtOptItemFormulaPK_.optionalItemNo),
				optItemNo));
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// Get results
		return em.createQuery(cq).getResultList();

	}
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#
	 * find(java.lang.String)
	 */
	public List<Formula> find(String companyId) {
		List<KrcmtOptItemFormula> results = this.findEntityByCompanyId(companyId);

		// Return
		if (CollectionUtil.isEmpty(results)) {
			return Collections.emptyList();
		}

		return results.stream().map(item -> new Formula(new JpaFormulaGetMemento(item))).collect(Collectors.toList());
	}
	
	/**
	 * Find entity.
	 *
	 * @param companyId the company id
	 * @param optItemNo the opt item no
	 * @return the list
	 */
	private List<KrcmtOptItemFormula> findEntityByCompanyId(String companyId) {
		List<KrcmtOptItemFormula> result = new ArrayList<>();
		List<KrcmtFormulaRounding> krcmtFormulaRoundings = new ArrayList<>();
		List<KrcmtCalcItemSelection> krcmtCalcItemSelections = new ArrayList<>();
		List<KrcmtFormulaSetting> krcmtFormulaSetting = new ArrayList<>();
		
		String sql = "SELECT * FROM KRCMT_OPT_ITEM_FORMULA WHERE CID = @companyID";
		result.addAll(new NtsStatement(sql, this.jdbcProxy())
				.paramString("companyID", companyId)
				.getList(rec -> KrcmtOptItemFormula.MAPPER.toEntity(rec))
			);
		
		List<Integer> optionalItemNo = new ArrayList<>();
		List<String> formulaId = new ArrayList<>();
		for (KrcmtOptItemFormula item : result) {
			optionalItemNo.add(item.getKrcmtOptItemFormulaPK().getOptionalItemNo());
			formulaId.add(item.getKrcmtOptItemFormulaPK().getFormulaId());
		}
		optionalItemNo = optionalItemNo.stream().distinct().collect(Collectors.toList());
		formulaId = formulaId.stream().distinct().collect(Collectors.toList());
		List<List<Integer>> optionalItemNoSub = new ArrayList<>();
		CollectionUtil.split(optionalItemNo, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			optionalItemNoSub.add(subList);
		});
		for (List<Integer> subList : optionalItemNoSub) {
			CollectionUtil.split(formulaId, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList2 -> {
				String sqlKrcmtFormulaRoundings = "SELECT * FROM KRCMT_FORMULA_ROUNDING "
						+ "WHERE CID = @companyID "
						+ "AND OPTIONAL_ITEM_NO IN @optionalItemNo "
						+ "AND FORMULA_ID IN @formulaId ";
				krcmtFormulaRoundings.addAll(new NtsStatement(sqlKrcmtFormulaRoundings, this.jdbcProxy())
						.paramString("companyID", companyId)
						.paramInt("optionalItemNo", subList)
						.paramString("formulaId", subList2)
						.getList(rec -> KrcmtFormulaRounding.MAPPER.toEntity(rec))
					);
				String sqlKrcmtCalcItemSelections = "SELECT * FROM KRCMT_CALC_ITEM_SELECTION "
						+ "WHERE CID = @companyID "
						+ "AND OPTIONAL_ITEM_NO IN @optionalItemNo "
						+ "AND FORMULA_ID IN @formulaId ";
				krcmtCalcItemSelections.addAll(new NtsStatement(sqlKrcmtCalcItemSelections, this.jdbcProxy())
						.paramString("companyID", companyId)
						.paramInt("optionalItemNo", subList)
						.paramString("formulaId", subList2)
						.getList(rec -> KrcmtCalcItemSelection.MAPPER.toEntity(rec))
					);
				String sqlKrcmtFormulaSetting = "SELECT * FROM KRCMT_FORMULA_SETTING "
						+ "WHERE CID = @companyID "
						+ "AND OPTIONAL_ITEM_NO IN @optionalItemNo "
						+ "AND FORMULA_ID IN @formulaId ";
				krcmtFormulaSetting.addAll(new NtsStatement(sqlKrcmtFormulaSetting, this.jdbcProxy())
						.paramString("companyID", companyId)
						.paramInt("optionalItemNo", subList)
						.paramString("formulaId", subList2)
						.getList(rec -> {
							return new KrcmtFormulaSetting(
									new KrcmtFormulaSettingPK(rec.getString("CID"), rec.getInt("OPTIONAL_ITEM_NO"), rec.getString("FORMULA_ID")), 
									rec.getInt("MINUS_SEGMENT"), 
									rec.getInt("OPERATOR"), 
									rec.getInt("LEFT_SET_METHOD"), 
									rec.getBigDecimal("LEFT_INPUT_VAL"),
									rec.getString("LEFT_FORMULA_ITEM_ID"), 
									rec.getInt("LEFT_SET_METHOD"), 
									rec.getBigDecimal("RIGHT_INPUT_VAL"), 
									rec.getString("RIGHT_FORMULA_ITEM_ID"));
						}));
			});
		}	
		
		for (KrcmtOptItemFormula item : result) {
			List<KrcmtFormulaRounding> formulaRounding = krcmtFormulaRoundings.stream().filter(c->{
				return c.getKrcmtFormulaRoundingPK().getFormulaId().equals(item.getKrcmtOptItemFormulaPK().getFormulaId()) && c.getKrcmtFormulaRoundingPK().getOptionalItemNo() == item.getKrcmtOptItemFormulaPK().getOptionalItemNo(); 
			}).collect(Collectors.toList());
			
			List<KrcmtCalcItemSelection> calcItemSelections = krcmtCalcItemSelections.stream().filter(c->{
				return c.getKrcmtCalcItemSelectionPK().getFormulaId().equals(item.getKrcmtOptItemFormulaPK().getFormulaId()) && c.getKrcmtCalcItemSelectionPK().getOptionalItemNo() == item.getKrcmtOptItemFormulaPK().getOptionalItemNo(); 
			}).collect(Collectors.toList());
			
			Optional<KrcmtFormulaSetting> formulaSetting = krcmtFormulaSetting.stream().filter(c->{
				return c.getKrcmtFormulaSettingPK().getFormulaId().equals(item.getKrcmtOptItemFormulaPK().getFormulaId()) && c.getKrcmtFormulaSettingPK().getOptionalItemNo() == item.getKrcmtOptItemFormulaPK().getOptionalItemNo(); 
			}).findFirst();
			
			item.setKrcmtFormulaRoundings(formulaRounding);
			item.setKrcmtCalcItemSelections(calcItemSelections);
			if(formulaSetting.isPresent()) {
				item.setKrcmtFormulaSetting(formulaSetting.get());
			}
		}
		
		return result;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.optitem.calculation.FormulaRepository#findById(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Formula findById(String companyId, Integer optItemNo, String formulaId) {
		KrcmtOptItemFormula entity = this.queryProxy()
				.find(new KrcmtOptItemFormulaPK(companyId, optItemNo, formulaId), KrcmtOptItemFormula.class).get();
		return new Formula(new JpaFormulaGetMemento(entity));
	}

}
