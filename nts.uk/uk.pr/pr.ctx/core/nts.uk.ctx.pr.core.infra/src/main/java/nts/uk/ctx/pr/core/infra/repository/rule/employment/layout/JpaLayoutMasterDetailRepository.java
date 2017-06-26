package nts.uk.ctx.pr.core.infra.repository.rule.employment.layout;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutDetail;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutDetailPK;

@Stateless
public class JpaLayoutMasterDetailRepository extends JpaRepository implements LayoutMasterDetailRepository {
	private final String SELECT_NO_WHERE = "SELECT c FROM QstmtStmtLayoutDetail c";
	private final String SELECT_NO_WHERE_JOIN = "SELECT i.itemAbName, c FROM QstmtStmtLayoutDetail c";
	private final String SELECT_ALL_DETAILS = SELECT_NO_WHERE_JOIN + " INNER JOIN QcamtItem i" + " ON ("
			+ " i.qcamtItemPK.ccd = c.qstmtStmtLayoutDetailPk.companyCd"
			+ " AND i.qcamtItemPK.itemCd = c.qstmtStmtLayoutDetailPk.itemCd"
			+ " AND i.qcamtItemPK.ctgAtr = c.qstmtStmtLayoutDetailPk.ctgAtr)"
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm";
	private final String SELECT_ALL_BY_HISTORY_ID = SELECT_NO_WHERE_JOIN + " INNER JOIN QcamtItem i" + " ON ("
			+ " i.qcamtItemPK.ccd = c.qstmtStmtLayoutDetailPk.companyCd"
			+ " AND i.qcamtItemPK.itemCd = c.qstmtStmtLayoutDetailPk.itemCd"
			+ " AND i.qcamtItemPK.ctgAtr = c.qstmtStmtLayoutDetailPk.ctgAtr)"
			+ " WHERE c.qstmtStmtLayoutDetailPk.historyId = :historyId";
	
	private final String SELECT_DETAIL = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm"
			+ " AND c.qstmtStmtLayoutDetailPk.itemCd = :itemCd";
	private final String SELECT_ALL_DETAILS_BY_CATEGORY = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm"
			+ " AND c.qstmtStmtLayoutDetailPk.ctgAtr = :ctgAtr";
	private final String SELECT_ALL_DETAILS_BY_CATEGORY1 = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutDetailPk.historyId = :historyId"
			+ " AND c.qstmtStmtLayoutDetailPk.ctgAtr = :ctgAtr";
	private final String SELECT_ALL_DETAILS_BY_LINE = SELECT_NO_WHERE + " WHERE c.autoLineId = :autoLineId";
	private final String SELECT_DETAILS_WITH_SUMSCOPEATR = SELECT_ALL_DETAILS
			+ " AND c.qstmtStmtLayoutDetailPk.ctgAtr = :ctgAtr" + " AND c.sumScopeAtr = :sumScopeAtr";
	private final String SELECT_ALL_DETAILS_BEFORE = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.endYm = :endYm";
	private final String SELECT_ALL_DETAILS_BEFORE1 = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.qstmtStmtLayoutDetailPk.historyId = :historyId";
	private final String SELECT_ALL_DETAILS_BEFORE2 = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" ;

	private final String FIND_ONLY_ALL = "SELECT c FROM QstmtStmtLayoutDetail c"
			+ " WHERE c.qstmtStmtLayoutDetailPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutDetailPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm";

	@Override
	public void add(LayoutMasterDetail domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	private QstmtStmtLayoutDetailPK toEntityPk(LayoutMasterDetail domain) {
		val entityPk = new QstmtStmtLayoutDetailPK();
		entityPk.companyCd = domain.getCompanyCode().v();
		entityPk.stmtCd = domain.getStmtCode().v();
		entityPk.historyId = domain.getHistoryId();
		entityPk.ctgAtr = domain.getCategoryAtr().value;
		entityPk.itemCd = domain.getItemCode().v();
		return entityPk;
	}

	private QstmtStmtLayoutDetail toEntity(LayoutMasterDetail domain) {
		val entity = new QstmtStmtLayoutDetail();
		entity.qstmtStmtLayoutDetailPk = new QstmtStmtLayoutDetailPK();
		entity.qstmtStmtLayoutDetailPk.companyCd = domain.getCompanyCode().v();
		entity.qstmtStmtLayoutDetailPk.stmtCd = domain.getStmtCode().v();
		entity.qstmtStmtLayoutDetailPk.historyId = domain.getHistoryId();
		// entity.strYm = domain.getStartYm().v(); lanlt remove
		entity.qstmtStmtLayoutDetailPk.ctgAtr = domain.getCategoryAtr().value;
		entity.qstmtStmtLayoutDetailPk.itemCd = domain.getItemCode().v();
		// entity.endYm = domain.getEndYm().v(); lanlt remove
		entity.autoLineId = domain.getAutoLineId().v();
		// xem lai voi qa 86
		entity.itemPosColumn = domain.getItemPosColumn().v();
		entity.dispAtr = domain.getDisplayAtr().value;
		entity.sumScopeAtr = domain.getSumScopeAtr().value;
		entity.calcMethod = domain.getCalculationMethod().value;
		entity.pWageCd = domain.getPersonalWageCode().v() == "" ? "00" : domain.getPersonalWageCode().v();
		// 今回、対応対象外 ↓
		entity.formulaCd = "000";
		entity.wageTableCd = "000";
		//Lanlt remove
		//entity.commonMny =0;
		 entity.commonMny = new BigDecimal('0');
		// 今回、対応対象外 ↑
		entity.setoffItemCd = domain.getSetOffItemCode().v();
		entity.commuteAtr = domain.getCommuteAtr().value;
		entity.distributeSet = domain.getDistribute().getSetting().value;
		entity.distributeWay = domain.getDistribute().getMethod().value;
		entity.errRangeLowAtr = domain.getError().getIsUseLow().value;
		entity.errRangeLow = domain.getError().getRange().min();
		entity.errRangeHighAtr = domain.getError().getIsUseHigh().value;
		entity.errRangeHigh = domain.getError().getRange().max();
		entity.alRangeLowAtr = domain.getAlarm().getIsUseLow().value;
		entity.alRangeLow = domain.getAlarm().getRange().min();
		entity.alRangeHighAtr = domain.getAlarm().getIsUseHigh().value;
		entity.alRangeHigh = domain.getAlarm().getRange().max();
		return entity;
	}

	@Override
	public void update(LayoutMasterDetail domain) {
		this.commandProxy().update(toEntity(domain));
	}

	/**
	 * find all layout master details
	 */
	@Override
	public List<LayoutMasterDetail> getDetails(String companyCd, String stmtCd, int startYm) {
		@SuppressWarnings("unchecked")
		List<Object[]> objects = this.getEntityManager().createQuery(SELECT_ALL_DETAILS)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("strYm", startYm)
				.getResultList();

		return objects.stream().map(c -> toDomainJoin(String.valueOf(c[0]), (QstmtStmtLayoutDetail) c[1]))
				.collect(Collectors.toList());
	}

	private static LayoutMasterDetail toDomainJoin(String itemAbName, QstmtStmtLayoutDetail entity) {
		val domain = toDomain(entity);
		domain.setItemAbName(itemAbName);

		return domain;
	}

	private static LayoutMasterDetail toDomain(QstmtStmtLayoutDetail entity) {
		val domain = LayoutMasterDetail.createFromJavaType(entity.qstmtStmtLayoutDetailPk.companyCd,
				entity.qstmtStmtLayoutDetailPk.stmtCd, entity.qstmtStmtLayoutDetailPk.ctgAtr,
				entity.qstmtStmtLayoutDetailPk.itemCd, entity.autoLineId, entity.dispAtr, entity.sumScopeAtr,
				entity.calcMethod, entity.distributeWay, entity.distributeSet, entity.formulaCd, entity.pWageCd,
				entity.wageTableCd, entity.commonMny, entity.setoffItemCd, entity.commuteAtr, entity.errRangeHighAtr,
				entity.errRangeHigh, entity.errRangeLowAtr, entity.errRangeLow, entity.alRangeHighAtr,
				entity.alRangeHigh, entity.alRangeLowAtr, entity.alRangeLow, entity.itemPosColumn,
				entity.qstmtStmtLayoutDetailPk.historyId);

		return domain;
	}

	@Override
	public Optional<LayoutMasterDetail> getDetail(String companyCd, String stmtCd, int startYm, int categoryAtr,
			String itemCd) {
		return this.queryProxy().query(SELECT_DETAIL, QstmtStmtLayoutDetail.class).setParameter("companyCd", companyCd)
				.setParameter("stmtCd", stmtCd).setParameter("strYm", startYm).setParameter("itemCd", itemCd)
				.getSingle(c -> toDomain(c));

	}

	@Override
	public void remove(String companyCode, String layoutCode, String historyId, int categoryAtr, String itemCode) {
		val objectKey = new QstmtStmtLayoutDetailPK();
		objectKey.companyCd = companyCode;
		objectKey.stmtCd = layoutCode;
		objectKey.historyId = historyId;
		objectKey.ctgAtr = categoryAtr;
		objectKey.itemCd = itemCode;
		this.commandProxy().remove(QstmtStmtLayoutDetail.class, objectKey);

	}

	@Override
	public void add(List<LayoutMasterDetail> details) {
		for (LayoutMasterDetail detail : details) {
			this.commandProxy().insert(toEntity(detail));
		}
	}

	@Override
	public void update(List<LayoutMasterDetail> details) {
		for (LayoutMasterDetail detail : details) {
			this.commandProxy().update(toEntity(detail));
		}
	}

	@Override
	public List<LayoutMasterDetail> getDetailsByCategory(String companyCd, String stmtCd, int startYm,
			int categoryAtr) {

		return this.queryProxy().query(SELECT_ALL_DETAILS_BY_CATEGORY, QstmtStmtLayoutDetail.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("strYm", startYm)
				.setParameter("ctgAtr", categoryAtr).getList(c -> toDomain(c));
	}

	@Override
	public void remove(List<LayoutMasterDetail> details) {
		List<QstmtStmtLayoutDetailPK> detailEntitiesPk = details.stream().map(detail -> {
			return this.toEntityPk(detail);
		}).collect(Collectors.toList());
		this.commandProxy().removeAll(QstmtStmtLayoutDetail.class, detailEntitiesPk);
	}

	@Override
	public List<LayoutMasterDetail> getDetailsByLine(String autoLineId) {

		return this.queryProxy().query(SELECT_ALL_DETAILS_BY_LINE, QstmtStmtLayoutDetail.class)
				.setParameter("autoLineId", autoLineId).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterDetail> getDetailsWithSumScopeAtr(String companyCode, String stmtCode, int startYearMonth,
			int categoryAttribute, int sumScopeAtr) {
		return this.queryProxy().query(SELECT_DETAILS_WITH_SUMSCOPEATR, QstmtStmtLayoutDetail.class)
				.setParameter("companyCd", companyCode).setParameter("stmtCd", stmtCode)
				.setParameter("strYm", startYearMonth).setParameter("ctgAtr", categoryAttribute)
				.setParameter("sumScopeAtr", sumScopeAtr).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterDetail> getDetailsBefore(String companyCd, String stmtCd, int endYm) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE, QstmtStmtLayoutDetail.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("endYm", endYm)
				.getList(c -> toDomain(c));
	}

	/**
	 * find all layout master details without item info
	 */
	@Override
	public List<LayoutMasterDetail> findAll(String companyCd, String stmtCd, int startYm) {
		return this.queryProxy().query(FIND_ONLY_ALL, QstmtStmtLayoutDetail.class).setParameter("companyCd", companyCd)
				.setParameter("stmtCd", stmtCd).setParameter("strYm", startYm).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterDetail> getDetails(String historyId) {
		@SuppressWarnings("unchecked")
		List<Object[]> objects = this.getEntityManager().createQuery(SELECT_ALL_BY_HISTORY_ID)
				.setParameter("historyId", historyId)
				.getResultList();

		return objects.stream().map(c -> toDomainJoin(String.valueOf(c[0]), (QstmtStmtLayoutDetail) c[1]))
				.collect(Collectors.toList());
	}

	@Override
	public List<LayoutMasterDetail> getDetailsByCategory(String historyId, int categoryAtr) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BY_CATEGORY1, QstmtStmtLayoutDetail.class)
			.setParameter("historyId", historyId)
		    .setParameter("ctgAtr", categoryAtr).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterDetail> getDetailsBefore(String companyCd, String stmtCd, String historyId) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE1, QstmtStmtLayoutDetail.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("historyId", historyId)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterDetail> getDetailsBefore(String companyCd, String stmtCd) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE2, QstmtStmtLayoutDetail.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd)
				.getList(c -> toDomain(c));
	}
}
