/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.outsideot;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.outsideot.UseClassification;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.BreakdownItemNo;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.OvertimeNo;
import nts.uk.ctx.at.shared.infra.entity.outsideot.KshstOutsideOtSet;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.KshstOutsideOtBrd;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.KshstOutsideOtBrdPK;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.KshstOutsideOtBrdPK_;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.KshstOutsideOtBrd_;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.attendance.KshstOutsideOtBrdAten;
import nts.uk.ctx.at.shared.infra.entity.outsideot.breakdown.attendance.KshstOutsideOtBrdAtenPK;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTime;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTimePK;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTimePK_;
import nts.uk.ctx.at.shared.infra.entity.outsideot.overtime.KshstOverTime_;
import nts.uk.ctx.at.shared.infra.repository.outsideot.breakdown.JpaOutsideOTBRDItemGetMemento;
import nts.uk.ctx.at.shared.infra.repository.outsideot.breakdown.JpaOutsideOTBRDItemSetMemento;
import nts.uk.ctx.at.shared.infra.repository.outsideot.overtime.JpaOvertimeGetMemento;
import nts.uk.ctx.at.shared.infra.repository.outsideot.overtime.JpaOvertimeSetMemento;

/**
 * The Class JpaOutsideOTSettingRepository.
 */
@Stateless
public class JpaOutsideOTSettingRepository extends JpaRepository
		implements OutsideOTSettingRepository {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingRepository#findById(java
	 * .lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@SneakyThrows
	@Override
	public Optional<OutsideOTSetting> findById(String companyId) {
		
		String sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_SET KOOS "
				+ "WHERE KOOS.CID = ?";

		try (PreparedStatement stmt = this.connection().prepareStatement(sqlJdbc)) {

			stmt.setString(1, companyId);

			Optional<KshstOutsideOtSet> kshstOutsideOtSet = new NtsResultSet(stmt.executeQuery())
					.getSingle(rec -> {
						KshstOutsideOtSet entity = new KshstOutsideOtSet();
						entity.setCid(rec.getString("CID"));
						entity.setNote(rec.getString("NOTE"));
						entity.setCalculationMethod(rec.getInt("CALCULATION_METHOD"));
						return entity;
					});
			
			sqlJdbc = "SELECT * FROM KSHST_OVER_TIME KOT "
					+ "WHERE KOT.CID = ? AND KOT.USE_ATR = ? ORDER BY KOT.OVER_TIME_NO ASC";
			List<KshstOverTime> entityOvertime = new ArrayList<>();
			try (PreparedStatement stmt1 = this.connection().prepareStatement(sqlJdbc)) {

				stmt1.setString(1, companyId);
				stmt1.setInt(2, UseClassification.UseClass_Use.value);

				entityOvertime = new NtsResultSet(stmt1.executeQuery())
						.getList(rec1 -> {
							KshstOverTimePK kshstOverTimePK = new KshstOverTimePK();
							kshstOverTimePK.setCid(rec1.getString("CID"));
							kshstOverTimePK.setOverTimeNo(rec1.getInt("OVER_TIME_NO"));

							KshstOverTime entity = new KshstOverTime();
							entity.setKshstOverTimePK(kshstOverTimePK);
							entity.setIs60hSuperHd(rec1.getInt("IS_60H_SUPER_HD"));
							entity.setUseAtr(rec1.getInt("USE_ATR"));
							entity.setName(rec1.getString("NAME"));
							entity.setOverTime(rec1.getInt("OVER_TIME"));

							return entity;
						});
			}
			
			sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_BRD KOOB "
					+ "WHERE KOOB.CID = ? AND KOOB.USE_ATR = ? ORDER BY KOOB.BRD_ITEM_NO ASC";
			List<KshstOutsideOtBrd> entityOvertimeBRDItem = new ArrayList<>();
			try (PreparedStatement stmt2 = this.connection().prepareStatement(sqlJdbc)) {

				stmt2.setString(1, companyId);
				stmt2.setInt(2, UseClassification.UseClass_Use.value);

				entityOvertimeBRDItem = new NtsResultSet(stmt2.executeQuery())
						.getList(rec2 -> {
							KshstOutsideOtBrdPK kshstOutsideOtBrdPK = new KshstOutsideOtBrdPK();
							kshstOutsideOtBrdPK.setCid(rec2.getString("CID"));
							kshstOutsideOtBrdPK.setBrdItemNo(rec2.getInt("BRD_ITEM_NO"));

							KshstOutsideOtBrd entity = new KshstOutsideOtBrd();
							entity.setKshstOutsideOtBrdPK(kshstOutsideOtBrdPK);
							entity.setName(rec2.getString("NAME"));
							entity.setUseAtr(rec2.getInt("USE_ATR"));
							entity.setProductNumber(rec2.getInt("PRODUCT_NUMBER"));


							return entity;
						});
			}
			
			sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_BRD_ATEN KOOBA "
					+ "WHERE KOOBA.CID = ?";
			List<KshstOutsideOtBrdAten> lstOutsideOtBrdAten = new ArrayList<>();
			try (PreparedStatement stmt3 = this.connection().prepareStatement(sqlJdbc)) {

				stmt3.setString(1, companyId);

				lstOutsideOtBrdAten = new NtsResultSet(stmt3.executeQuery())
						.getList(rec3 -> {
							KshstOutsideOtBrdAtenPK kshstOutsideOtBrdAtenPK = new KshstOutsideOtBrdAtenPK();
							kshstOutsideOtBrdAtenPK.setCid(rec3.getString("CID"));
							kshstOutsideOtBrdAtenPK.setBrdItemNo(rec3.getInt("BRD_ITEM_NO"));
							kshstOutsideOtBrdAtenPK.setAttendanceItemId(rec3.getInt("ATTENDANCE_ITEM_ID"));

							KshstOutsideOtBrdAten entity = new KshstOutsideOtBrdAten();
							entity.setKshstOutsideOtBrdAtenPK(kshstOutsideOtBrdAtenPK);

							return entity;
						});
			}
			
			Map<Integer, List<KshstOutsideOtBrdAten>> lstOutsideOtBrdAtenMap = lstOutsideOtBrdAten
					.stream().collect(Collectors
							.groupingBy(item -> item.getKshstOutsideOtBrdAtenPK().getBrdItemNo()));
			entityOvertimeBRDItem.forEach(item -> {
				item.setLstOutsideOtBrdAten(lstOutsideOtBrdAtenMap.getOrDefault(
						item.getKshstOutsideOtBrdPK().getBrdItemNo(), Collections.emptyList()));
			});
			
			// check exist data
			if (kshstOutsideOtSet.isPresent()) {
				return Optional
						.ofNullable(this.toDomain(kshstOutsideOtSet.get(), entityOvertimeBRDItem, entityOvertime));
			}
			// default data
			return Optional.ofNullable(
					this.toDomain(new KshstOutsideOtSet(), entityOvertimeBRDItem, entityOvertime));
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@SneakyThrows
	@Override
	public Optional<OutsideOTSetting> findByIdV2(String companyId) {
		
		String sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_SET KOOS "
				+ "WHERE KOOS.CID = ?";

		try (PreparedStatement stmt = this.connection().prepareStatement(sqlJdbc)) {

			stmt.setString(1, companyId);

			Optional<KshstOutsideOtSet> kshstOutsideOtSet = new NtsResultSet(stmt.executeQuery())
					.getSingle(rec -> {
						KshstOutsideOtSet entity = new KshstOutsideOtSet();
						entity.setCid(rec.getString("CID"));
						entity.setNote(rec.getString("NOTE"));
						entity.setCalculationMethod(rec.getInt("CALCULATION_METHOD"));
						return entity;
					});
			
			sqlJdbc = "SELECT * FROM KSHST_OVER_TIME KOT "
					+ "WHERE KOT.CID = ? ORDER BY KOT.OVER_TIME_NO ASC";
			List<KshstOverTime> entityOvertime = new ArrayList<>();
			try (PreparedStatement stmt1 = this.connection().prepareStatement(sqlJdbc)) {

				stmt1.setString(1, companyId);

				entityOvertime = new NtsResultSet(stmt1.executeQuery())
						.getList(rec1 -> {
							KshstOverTimePK kshstOverTimePK = new KshstOverTimePK();
							kshstOverTimePK.setCid(rec1.getString("CID"));
							kshstOverTimePK.setOverTimeNo(rec1.getInt("OVER_TIME_NO"));

							KshstOverTime entity = new KshstOverTime();
							entity.setKshstOverTimePK(kshstOverTimePK);
							entity.setIs60hSuperHd(rec1.getInt("IS_60H_SUPER_HD"));
							entity.setUseAtr(rec1.getInt("USE_ATR"));
							entity.setName(rec1.getString("NAME"));
							entity.setOverTime(rec1.getInt("OVER_TIME"));

							return entity;
						});
			}
			
			sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_BRD KOOB "
					+ "WHERE KOOB.CID = ?  ORDER BY KOOB.BRD_ITEM_NO ASC";
			List<KshstOutsideOtBrd> entityOvertimeBRDItem = new ArrayList<>();
			try (PreparedStatement stmt2 = this.connection().prepareStatement(sqlJdbc)) {

				stmt2.setString(1, companyId);

				entityOvertimeBRDItem = new NtsResultSet(stmt2.executeQuery())
						.getList(rec2 -> {
							KshstOutsideOtBrdPK kshstOutsideOtBrdPK = new KshstOutsideOtBrdPK();
							kshstOutsideOtBrdPK.setCid(rec2.getString("CID"));
							kshstOutsideOtBrdPK.setBrdItemNo(rec2.getInt("BRD_ITEM_NO"));

							KshstOutsideOtBrd entity = new KshstOutsideOtBrd();
							entity.setKshstOutsideOtBrdPK(kshstOutsideOtBrdPK);
							entity.setName(rec2.getString("NAME"));
							entity.setUseAtr(rec2.getInt("USE_ATR"));
							entity.setProductNumber(rec2.getInt("PRODUCT_NUMBER"));


							return entity;
						});
			}
			
			sqlJdbc = "SELECT * FROM KSHST_OUTSIDE_OT_BRD_ATEN KOOBA "
					+ "WHERE KOOBA.CID = ?";
			List<KshstOutsideOtBrdAten> lstOutsideOtBrdAten = new ArrayList<>();
			try (PreparedStatement stmt3 = this.connection().prepareStatement(sqlJdbc)) {

				stmt3.setString(1, companyId);

				lstOutsideOtBrdAten = new NtsResultSet(stmt3.executeQuery())
						.getList(rec3 -> {
							KshstOutsideOtBrdAtenPK kshstOutsideOtBrdAtenPK = new KshstOutsideOtBrdAtenPK();
							kshstOutsideOtBrdAtenPK.setCid(rec3.getString("CID"));
							kshstOutsideOtBrdAtenPK.setBrdItemNo(rec3.getInt("BRD_ITEM_NO"));
							kshstOutsideOtBrdAtenPK.setAttendanceItemId(rec3.getInt("ATTENDANCE_ITEM_ID"));

							KshstOutsideOtBrdAten entity = new KshstOutsideOtBrdAten();
							entity.setKshstOutsideOtBrdAtenPK(kshstOutsideOtBrdAtenPK);

							return entity;
						});
			}
			
			Map<Integer, List<KshstOutsideOtBrdAten>> lstOutsideOtBrdAtenMap = lstOutsideOtBrdAten
					.stream().collect(Collectors
							.groupingBy(item -> item.getKshstOutsideOtBrdAtenPK().getBrdItemNo()));
			entityOvertimeBRDItem.forEach(item -> {
				item.setLstOutsideOtBrdAten(lstOutsideOtBrdAtenMap.getOrDefault(
						item.getKshstOutsideOtBrdPK().getBrdItemNo(), Collections.emptyList()));
			});
			
			// check exist data
			if (kshstOutsideOtSet.isPresent()) {
				return Optional
						.ofNullable(this.toDomain(kshstOutsideOtSet.get(), entityOvertimeBRDItem, entityOvertime));
			}
			// default data
			return Optional.ofNullable(
					this.toDomain(new KshstOutsideOtSet(), entityOvertimeBRDItem, entityOvertime));
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeSettingRepository#findById(java
	 * .lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<OutsideOTSetting> reportById(String companyId) {

		// call repository find entity setting
		Optional<KshstOutsideOtSet> entity = this.queryProxy().find(companyId,
				KshstOutsideOtSet.class);

		// call repository find all domain overtime
		List<Overtime> domainOvertime = this.findAllOvertime(companyId);

		// call repository find all domain overtime break down item
		List<OutsideOTBRDItem> domainOvertimeBrdItem = this.findAllBRDItem(companyId);

		// domain to entity
		List<KshstOverTime> entityOvertime = domainOvertime.stream().map(domain -> {
			KshstOverTime entityItem = new KshstOverTime();
			domain.saveToMemento(new JpaOvertimeSetMemento(entityItem, companyId));
			return entityItem;
		}).collect(Collectors.toList());

		// domain to entity
		List<KshstOutsideOtBrd> entityOvertimeBRDItem = domainOvertimeBrdItem.stream()
				.map(domain -> {
					KshstOutsideOtBrd entityItem = new KshstOutsideOtBrd();
					domain.saveToMemento(new JpaOutsideOTBRDItemSetMemento(entityItem, companyId));
					return entityItem;
				}).collect(Collectors.toList());

		// check exist data
		if (entity.isPresent()) {
			return Optional
					.ofNullable(this.toDomain(entity.get(), entityOvertimeBRDItem, entityOvertime));
		}
		// default data
		return Optional.ofNullable(
				this.toDomain(new KshstOutsideOtSet(), entityOvertimeBRDItem, entityOvertime));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSettingRepository#save(
	 * nts.uk.ctx.at.shared.dom.overtime.setting.OvertimeSetting)
	 */
	@Override
	public void save(OutsideOTSetting domain) {
		KshstOutsideOtSet entity = new KshstOutsideOtSet();
		// call repository find entity setting
		Optional<KshstOutsideOtSet> opEntity = this.queryProxy().find(domain.getCompanyId().v(),
				KshstOutsideOtSet.class);

		// check exist data
		if (opEntity.isPresent()) {
			entity = opEntity.get();
			entity = this.toEntity(domain);
			this.commandProxy().update(entity);
		}
		// insert data
		else {
			entity = this.toEntity(domain);
			this.commandProxy().insert(entity);
		}
		// save all overtime
		this.saveAllOvertime(domain.getOvertimes(), domain.getCompanyId().v());

		// save all over time breakdown item
		this.saveAllBRDItem(domain.getBreakdownItems(), domain.getCompanyId().v());
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @param entityOvertimeBRDItems the entity overtime BRD items
	 * @param entityOvertime the entity overtime
	 * @return the outside OT setting
	 */
	private OutsideOTSetting toDomain(KshstOutsideOtSet entity,
			List<KshstOutsideOtBrd> entityOvertimeBRDItems, List<KshstOverTime> entityOvertime) {
		return new OutsideOTSetting(
				new JpaOutsideOTSettingGetMemento(entity, entityOvertimeBRDItems, entityOvertime));
	}

	/**
	 * To entity.
	 *
	 * @param domain
	 *            the domain
	 * @return the kshst over time set
	 */
	private KshstOutsideOtSet toEntity(OutsideOTSetting domain) {
		KshstOutsideOtSet entity = new KshstOutsideOtSet();
		domain.saveToMemento(
				new JpaOutsideOTSettingSetMemento(new ArrayList<>(), new ArrayList<>(), entity));
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.breakdown.OvertimeBRDItemRepository#
	 * saveAll(java.util.List)
	 */
	@Override
	public void saveAllBRDItem(List<OutsideOTBRDItem> overtimeBreakdownItems, String companyId) {

		// to map over time break down item
		Map<BreakdownItemNo, OutsideOTBRDItem> mapOvertimeBRDItem = this.findAllBRDItem(companyId)
				.stream().collect(Collectors.toMap((overtime) -> {
					return overtime.getBreakdownItemNo();
				}, Function.identity()));

		// entity add all
		List<KshstOutsideOtBrd> entityAddAll = new ArrayList<>();

		// entity update all
		List<KshstOutsideOtBrd> entityUpdateAll = new ArrayList<>();

		// for each data overtime
		overtimeBreakdownItems.forEach(overtimeBRDItem -> {
			if (mapOvertimeBRDItem.containsKey(overtimeBRDItem.getBreakdownItemNo())) {
				entityUpdateAll.add(this.toEntity(overtimeBRDItem, companyId));
			} else {
				entityAddAll.add(this.toEntity(overtimeBRDItem, companyId));
			}
		});

		// insert all
		this.commandProxy().insertAll(entityAddAll);

		// update all
		this.commandProxy().updateAll(entityUpdateAll);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.breakdown.OvertimeBRDItemRepository#
	 * findAll(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<OutsideOTBRDItem> findAllBRDItem(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHST_OVER_TIME_BRD (KshstOverTimeBrd SQL)
		CriteriaQuery<KshstOutsideOtBrd> cq = criteriaBuilder.createQuery(KshstOutsideOtBrd.class);

		// root data
		Root<KshstOutsideOtBrd> root = cq.from(KshstOutsideOtBrd.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshstOutsideOtBrd_.kshstOutsideOtBrdPK).get(KshstOutsideOtBrdPK_.cid),
				companyId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by over time break down item no asc
		cq.orderBy(criteriaBuilder.asc(root.get(KshstOutsideOtBrd_.kshstOutsideOtBrdPK)
				.get(KshstOutsideOtBrdPK_.brdItemNo)));

		// create query
		TypedQuery<KshstOutsideOtBrd> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomain(entity))
				.collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.breakdown.OvertimeBRDItemRepository#
	 * findAllUse(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<OutsideOTBRDItem> findAllUseBRDItem(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHST_OVER_TIME_BRD (KshstOverTimeBrd SQL)
		CriteriaQuery<KshstOutsideOtBrd> cq = criteriaBuilder.createQuery(KshstOutsideOtBrd.class);

		// root data
		Root<KshstOutsideOtBrd> root = cq.from(KshstOutsideOtBrd.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshstOutsideOtBrd_.kshstOutsideOtBrdPK).get(KshstOutsideOtBrdPK_.cid),
				companyId));

		// equal use classification
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KshstOutsideOtBrd_.useAtr),
				UseClassification.UseClass_Use.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by over time break down item no asc
		cq.orderBy(criteriaBuilder.asc(root.get(KshstOutsideOtBrd_.kshstOutsideOtBrdPK)
				.get(KshstOutsideOtBrdPK_.brdItemNo)));

		// create query
		TypedQuery<KshstOutsideOtBrd> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomain(entity))
				.collect(Collectors.toList());
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the outside OTBRD item
	 */
	private OutsideOTBRDItem toDomain(KshstOutsideOtBrd entity) {
		return new OutsideOTBRDItem(new JpaOutsideOTBRDItemGetMemento(entity));
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @param companyId the company id
	 * @return the kshst outside ot brd
	 */
	private KshstOutsideOtBrd toEntity(OutsideOTBRDItem domain, String companyId) {
		KshstOutsideOtBrd entity = new KshstOutsideOtBrd();
		domain.saveToMemento(new JpaOutsideOTBRDItemSetMemento(entity, companyId));
		return entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.overtime.OvertimeRepository#findAll(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<Overtime> findAllOvertime(String companyId) {
		return this.findAllEntity(companyId).stream().map(item -> this.toDomain(item))
				.collect(Collectors.toList());
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.overtime.OvertimeRepository#saveAll(java.util.
	 * List, java.lang.String)
	 */
	@Override
	public void saveAllOvertime(List<Overtime> overtimes, String companyId) {
		
		// to map over time
		Map<OvertimeNo, Overtime> mapOvertime = this.findAllOvertime(companyId).stream()
				.collect(Collectors.toMap((overtime) -> {
					return overtime.getOvertimeNo();
				}, Function.identity()));
		
		// entity add all
		List<KshstOverTime> entityAddAll = new ArrayList<>();
		
		// entity update all
		List<KshstOverTime> entityUpdateAll = new ArrayList<>();
		
		
		// for each data overtime
		overtimes.forEach(overtime->{
			if (mapOvertime.containsKey(overtime.getOvertimeNo())) {
				entityUpdateAll.add(this.toEntity(overtime, companyId));
			} else {
				entityAddAll.add(this.toEntity(overtime, companyId));
			}
		});
		
		// insert all
		this.commandProxy().insertAll(entityAddAll);
		
		// update all
		this.commandProxy().updateAll(entityUpdateAll);
	}
	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the monthly pattern
	 */
	private Overtime toDomain(KshstOverTime entity){
		return new Overtime(new JpaOvertimeGetMemento(entity));
	}
	
	
	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kmpst month pattern
	 */
	private KshstOverTime toEntity(Overtime domain, String companyId) {
		KshstOverTime entity = new KshstOverTime();
		domain.saveToMemento(new JpaOvertimeSetMemento(entity, companyId));
		return entity;
	}
	
	/**
	 * Find all entity.
	 *
	 * @param companyId the company id
	 * @return the list
	 */
	private List<KshstOverTime> findAllEntity(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHST_OVER_TIME (KshstOverTime SQL)
		CriteriaQuery<KshstOverTime> cq = criteriaBuilder.createQuery(KshstOverTime.class);

		// root data
		Root<KshstOverTime> root = cq.from(KshstOverTime.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshstOverTime_.kshstOverTimePK).get(KshstOverTimePK_.cid), companyId));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by over time no asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KshstOverTime_.kshstOverTimePK).get(KshstOverTimePK_.overTimeNo)));

		// create query
		TypedQuery<KshstOverTime> query = em.createQuery(cq);

		// exclude select
		return query.getResultList();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository#
	 * findAllUseOvertime(java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<Overtime> findAllUseOvertime(String companyId) {
		// get entity manager
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		// call KSHST_OVER_TIME (KshstOverTime SQL)
		CriteriaQuery<KshstOverTime> cq = criteriaBuilder.createQuery(KshstOverTime.class);

		// root data
		Root<KshstOverTime> root = cq.from(KshstOverTime.class);

		// select root
		cq.select(root);

		// add where
		List<Predicate> lstpredicateWhere = new ArrayList<>();

		// equal company id
		lstpredicateWhere.add(criteriaBuilder.equal(
				root.get(KshstOverTime_.kshstOverTimePK).get(KshstOverTimePK_.cid), companyId));
		
		// equal Use Classification use
		lstpredicateWhere.add(criteriaBuilder.equal(root.get(KshstOverTime_.useAtr),
				UseClassification.UseClass_Use.value));

		// set where to SQL
		cq.where(lstpredicateWhere.toArray(new Predicate[] {}));

		// order by over time no asc
		cq.orderBy(criteriaBuilder
				.asc(root.get(KshstOverTime_.kshstOverTimePK).get(KshstOverTimePK_.overTimeNo)));

		// create query
		TypedQuery<KshstOverTime> query = em.createQuery(cq);

		// exclude select
		return query.getResultList().stream().map(entity -> this.toDomain(entity))
				.collect(Collectors.toList());
	}


}
