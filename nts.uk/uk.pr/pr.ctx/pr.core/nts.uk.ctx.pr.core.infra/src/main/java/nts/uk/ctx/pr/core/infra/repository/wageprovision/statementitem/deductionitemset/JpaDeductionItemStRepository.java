package nts.uk.ctx.pr.core.infra.repository.wageprovision.statementitem.deductionitemset;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.deductionitemset.DeductionItemSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.deductionitemset.DeductionItemStRepository;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.statementitem.deductionitemset.QpbmtDeductionItemSt;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.statementitem.deductionitemset.QpbmtDeductionItemStPk;

@Stateless
public class JpaDeductionItemStRepository extends JpaRepository implements DeductionItemStRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM QpbmtDeductionItemSt f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.deductionItemStPk.cid =:cid AND  f.deductionItemStPk.salaryItemId =:salaryItemId ";

	@Override
	public List<DeductionItemSet> getAllDeductionItemSt() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, QpbmtDeductionItemSt.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public Optional<DeductionItemSet> getDeductionItemStById(String cid, String salaryItemId) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtDeductionItemSt.class).setParameter("cid", cid)
				.setParameter("salaryItemId", salaryItemId).getSingle(c -> c.toDomain());
	}

	@Override
	public void add(DeductionItemSet domain) {
		this.commandProxy().insert(QpbmtDeductionItemSt.toEntity(domain));
	}

	@Override
	public void update(DeductionItemSet domain) {
		this.commandProxy().update(QpbmtDeductionItemSt.toEntity(domain));
	}

	@Override
	public void remove(String cid, String salaryItemId) {
		this.commandProxy().remove(QpbmtDeductionItemSt.class, new QpbmtDeductionItemStPk(cid, salaryItemId));
	}
}
