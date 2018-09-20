package nts.uk.ctx.pr.core.infra.repository.wageprovision.statementitem;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemName;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemNameRepository;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.statementitem.QpbmtStatementItemName;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.statementitem.QpbmtStatementItemNamePk;

@Stateless
public class JpaStatementItemNameRepository extends JpaRepository implements StatementItemNameRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM QpbmtStatementItemName f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.statementItemNamePk.cid =:cid AND  f.statementItemNamePk.salaryItemId =:salaryItemId ";

	@Override
	public List<StatementItemName> getAllStatementItemName() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, QpbmtStatementItemName.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<StatementItemName> getStatementItemNameById(String cid, String salaryItemId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtStatementItemName.class).setParameter("cid", cid)
				.setParameter("salaryItemId", salaryItemId).getSingle(c -> c.toDomain());
	}

	@Override
	public void add(StatementItemName domain) {
		this.commandProxy().insert(QpbmtStatementItemName.toEntity(domain));
	}

	@Override
	public void update(StatementItemName domain) {
		this.commandProxy().update(QpbmtStatementItemName.toEntity(domain));
	}

	@Override
	public void remove(String cid, String salaryItemId) {
		if (this.getStatementItemNameById(cid, salaryItemId).isPresent()) {
			this.commandProxy().remove(QpbmtStatementItemName.class, new QpbmtStatementItemNamePk(cid, salaryItemId));
		}
	}
}
