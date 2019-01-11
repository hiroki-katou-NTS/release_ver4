package nts.uk.ctx.pr.core.infra.repository.wageprovision.processdatecls;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.processdatecls.CurrProcessDate;
import nts.uk.ctx.pr.core.dom.wageprovision.processdatecls.CurrProcessDateRepository;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.processdatecls.QpbmtCurrProcessDate;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.processdatecls.QpbmtCurrProcessDatePk;

@Stateless
public class JpaCurrProcessDateRepository extends JpaRepository implements CurrProcessDateRepository {

	private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM QpbmtCurrProcessDate f";
	private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING
			+ " WHERE  f.currProcessDatePk.cid =:cid AND  f.currProcessDatePk.processCateNo =:processCateNo ";

	@Override
	public List<CurrProcessDate> getAllCurrProcessDate() {
		return this.queryProxy().query(SELECT_ALL_QUERY_STRING, QpbmtCurrProcessDate.class)
				.getList(item -> item.toDomain());
	}

	@Override
	public List<CurrProcessDate> getCurrProcessDateById(String cid, int processCateNo) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtCurrProcessDate.class).setParameter("cid", cid)
				.setParameter("processCateNo", processCateNo).getList(c -> c.toDomain());
	}

	@Override
	public Optional<CurrProcessDate> getCurrProcessDateByIdAndProcessCateNo(String cid, int processCateNo) {
		return this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtCurrProcessDate.class)
				.setParameter("cid", cid)
				.setParameter("processCateNo", processCateNo)
				.getSingle(c -> c.toDomain());
	}

	@Override
	public void add(CurrProcessDate domain) {
		this.commandProxy().insert(QpbmtCurrProcessDate.toEntity(domain));
	}

	@Override
	public void update(CurrProcessDate domain) {
		this.commandProxy().update(QpbmtCurrProcessDate.toEntity(domain));
	}

	@Override
	public void remove(String cid, int processCateNo) {
		this.commandProxy().remove(QpbmtCurrProcessDate.class, new QpbmtCurrProcessDatePk(cid, processCateNo));
	}

	@Override
	public Optional<CurrProcessDate> getCurrProcessDateByKey(String cid, int processCateNo) {
		Optional<QpbmtCurrProcessDate> entity = this.queryProxy().find(new QpbmtCurrProcessDatePk(cid, processCateNo),
				QpbmtCurrProcessDate.class);
		if (entity.isPresent())
			return Optional.of(entity.get().toDomain());
		else
			return Optional.empty();
	}

	@Override
	public Optional<CurrProcessDate> getByIds(String cid, int processCateNo) {
		return Optional.ofNullable(this.getEntityManager().find(QpbmtCurrProcessDate.class,new QpbmtCurrProcessDatePk(cid,processCateNo)).toDomain());
	}

}
