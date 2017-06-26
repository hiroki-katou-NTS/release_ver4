package nts.uk.ctx.pr.core.infra.repository.rule.employment.layout;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutLines;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutLinesPK;

@Stateless

public class JpaLayoutLineRepository extends JpaRepository implements LayoutMasterLineRepository {

	private final String SELECT_NO_WHERE = "SELECT c FROM QstmtStmtLayoutLines c";
	private final String SELECT_ALL_DETAILS = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutLinesPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm";
	private final String SELECT_ALL_BY_HISTORY_ID = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutLinesPk.historyId = :historyId";

	private final String SELECT_ALL_LINES = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutLinesPk.historyId = :historyId"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd" + " AND c.qstmtStmtLayoutLinesPk.ctgAtr = :ctgAtr"
			+ " c.qstmtStmtLayoutLinesPk.companyCd = :companyCd";

	private final String SELECT_ALL_LINE_BY_CATEGORY = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutLinesPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd" + " AND c.strYm = :strYm"
			+ " AND c.qstmtStmtLayoutLinesPk.ctgAtr = :ctgAtr";

	private final String SELECT_ALL_DETAILS_BEFORE = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutLinesPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd" + " AND c.endYm = :endYm";
	private final String SELECT_ALL_DETAILS1 = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutLinesPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd";

	private final String SELECT_ALL_DETAILS_BEFORE1 = SELECT_NO_WHERE
			+ " WHERE c.qstmtStmtLayoutLinesPk.companyCd = :companyCd"
			+ " AND c.qstmtStmtLayoutLinesPk.stmtCd = :stmtCd" + " AND c.qstmtStmtLayoutLinesPk.historyId = :historyId";

	@Override
	public List<LayoutMasterLine> getLines(String companyCd, String stmtCd, int strYm) {
		return this.queryProxy().query(SELECT_ALL_DETAILS, QstmtStmtLayoutLines.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("strYm", strYm)
				.getList(c -> toDomain(c));

	}

	private static LayoutMasterLine toDomain(QstmtStmtLayoutLines entity) {
		val domain = LayoutMasterLine.createFromJavaType(entity.qstmtStmtLayoutLinesPk.companyCd,
				entity.qstmtStmtLayoutLinesPk.stmtCd, entity.qstmtStmtLayoutLinesPk.autoLineId, entity.lineDispAtr,
				entity.linePos, entity.qstmtStmtLayoutLinesPk.ctgAtr, entity.qstmtStmtLayoutLinesPk.historyId);

		return domain;
	}

	private QstmtStmtLayoutLinesPK toEntityPk(LayoutMasterLine domain) {
		val entityPk = new QstmtStmtLayoutLinesPK();
		entityPk.companyCd = domain.getCompanyCode().v();
		entityPk.stmtCd = domain.getStmtCode().v();
		entityPk.historyId = domain.getHistoryId();
		entityPk.ctgAtr = domain.getCategoryAtr().value;
		entityPk.autoLineId = domain.getAutoLineId().v();

		return entityPk;
	}

	private QstmtStmtLayoutLines toEntity(LayoutMasterLine domain) {
		val entity = new QstmtStmtLayoutLines();
		entity.qstmtStmtLayoutLinesPk = new QstmtStmtLayoutLinesPK();
		entity.qstmtStmtLayoutLinesPk.companyCd = domain.getCompanyCode().v();
		entity.qstmtStmtLayoutLinesPk.stmtCd = domain.getStmtCode().v();
		// entity.strYm = domain.getStartYM().v(); Lanlt cmt
		entity.qstmtStmtLayoutLinesPk.ctgAtr = domain.getCategoryAtr().value;
		entity.qstmtStmtLayoutLinesPk.autoLineId = domain.getAutoLineId().v();
		// entity.endYm = domain.getEndYM().v(); Lanlt cmt
		entity.linePos = domain.getLinePosition().v();
		entity.lineDispAtr = domain.getLineDisplayAttribute().value;
		entity.qstmtStmtLayoutLinesPk.historyId = domain.getHistoryId();

		return entity;
	}

	@Override
	public void add(LayoutMasterLine layoutMasterLine) {
		this.commandProxy().insert(toEntity(layoutMasterLine));
	}

	@Override
	public void update(LayoutMasterLine layoutMasterLine) {
		this.commandProxy().update(toEntity(layoutMasterLine));
	}

	@Override
	public void remove(String companyCode, String historyId, String autoLineID, int categoryAtr, String stmtCode) {
		val objectKey = new QstmtStmtLayoutLinesPK();
		objectKey.companyCd = companyCode;
		objectKey.historyId = historyId;
		objectKey.autoLineId = autoLineID;
		objectKey.ctgAtr = categoryAtr;
		objectKey.stmtCd = stmtCode;
		this.commandProxy().remove(QstmtStmtLayoutLines.class, objectKey);
	}

	@Override
	public Optional<LayoutMasterLine> getLine(String companyCd, String stmtCd, int strYm, String autoLineId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(List<LayoutMasterLine> lines) {
		for (LayoutMasterLine line : lines) {
			this.commandProxy().insert(toEntity(line));
		}

	}

	@Override
	public void update(List<LayoutMasterLine> lines) {
		for (LayoutMasterLine line : lines) {
			this.commandProxy().update(toEntity(line));
		}

	}

	@Override
	public void remove(List<LayoutMasterLine> lines) {

		List<QstmtStmtLayoutLinesPK> linesEntityPk = lines.stream().map(line -> {
			return this.toEntityPk(line);
		}).collect(Collectors.toList());

		this.commandProxy().removeAll(QstmtStmtLayoutLines.class, linesEntityPk);

	}

	@Override
	public List<LayoutMasterLine> getLines(String companyCd, String stmtCd, int strYm, int categoryAtr) {
		return this.queryProxy().query(SELECT_ALL_LINE_BY_CATEGORY, QstmtStmtLayoutLines.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("strYm", strYm)
				.setParameter("ctgAtr", categoryAtr).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterLine> getLinesBefore(String companyCd, String stmtCd, int endYm) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE, QstmtStmtLayoutLines.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd).setParameter("endYm", endYm)
				.getList(c -> toDomain(c));
	}

	@Override
	public Optional<LayoutMasterLine> find(String companyCode, String stmtCode, String historyId, int categoryAtr,
			String autoLineId) {
		QstmtStmtLayoutLinesPK primaryKey = new QstmtStmtLayoutLinesPK(companyCode, stmtCode, historyId, categoryAtr,
				autoLineId);
		return this.queryProxy().find(primaryKey, QstmtStmtLayoutLines.class).map(x -> toDomain(x));
	}

	@Override
	public List<LayoutMasterLine> getLines(String historyId) {
		return this.queryProxy().query(SELECT_ALL_BY_HISTORY_ID, QstmtStmtLayoutLines.class)
				.setParameter("historyId", historyId).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterLine> getLines(String companyCd, String stmtCd, String historyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LayoutMasterLine> getLines(String companyCd, String stmtCd, String historyId, int categoryAtr) {

		return this.queryProxy().query(SELECT_ALL_LINES, QstmtStmtLayoutLines.class)
				.setParameter("historyId", historyId).setParameter("stmtCd", stmtCd)
				.setParameter("categoryAtr", categoryAtr).setParameter("companyCd", companyCd)
				.getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterLine> getLinesBefore(String companyCd, String stmtCd, String historyId) {
		return this.queryProxy().query(SELECT_ALL_DETAILS_BEFORE, QstmtStmtLayoutLines.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd)
				.setParameter("historyId", historyId).getList(c -> toDomain(c));
	}

	@Override
	public List<LayoutMasterLine> getLinesBefore(String companyCd, String stmtCd) {
		
		return this.queryProxy().query(SELECT_ALL_DETAILS1, QstmtStmtLayoutLines.class)
				.setParameter("companyCd", companyCd).setParameter("stmtCd", stmtCd)
				.getList(c -> toDomain(c));
	}
}
