package nts.uk.ctx.at.schedule.infra.repository.shift.rank.ranksetting;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.shift.rank.ranksetting.RankSet;
import nts.uk.ctx.at.schedule.dom.shift.rank.ranksetting.RankSetRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.rank.ranksetting.KscstRankSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.rank.ranksetting.KscstRankSetPk;

/**
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaRankSetRepository extends JpaRepository implements RankSetRepository {
	private static final String SELECT_NO_WHERE = "SELECT k FROM KscstRankSet k ";
	private static final String SELECT_BY_LIST_EMPLOYEEID = SELECT_NO_WHERE + "WHERE  k.kscstRankSetPk.sId IN :sIds";
	private static final String DELETE_BY_SID = "DELETE from KscstRankSet k WHERE k.kscstRankSetPk.sId = :sId ";

	@Override
	public List<RankSet> getListRankSet(List<String> employeeIds) {
		return this.queryProxy().query(SELECT_BY_LIST_EMPLOYEEID, KscstRankSet.class).setParameter("sIds", employeeIds)
				.getList(x -> toDomain(x));
	}

	@Override
	public void removeRankSet(String sId) {
		this.getEntityManager().createQuery(DELETE_BY_SID).setParameter("sId", sId).executeUpdate();
	}

	@Override
	public void updateRankSet(RankSet rankSet) {
		KscstRankSetPk kscstRankSetPk = new KscstRankSetPk(rankSet.getRankCode().v(), rankSet.getSId());
		KscstRankSet entity = new KscstRankSet(kscstRankSetPk);
        //this.getEntityManager().merge(entity);
		this.commandProxy().update(entity);  
	}

	@Override
	public void insetRankSet(RankSet rankSet) {
		this.commandProxy().insert(toEntity(rankSet));
	}

	private RankSet toDomain(KscstRankSet entity) {
		RankSet domain = RankSet.createFromJavaType(entity.kscstRankSetPk.rankCode, entity.kscstRankSetPk.sId);
		return domain;
	}

	private KscstRankSet toEntity(RankSet domain) {
		val entity = new KscstRankSet();
		KscstRankSetPk kscstRankSetPk = new KscstRankSetPk(domain.getRankCode().v(), domain.getSId());
		entity.kscstRankSetPk = kscstRankSetPk;
		return entity;
	}

}
