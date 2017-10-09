package nts.uk.ctx.at.schedule.infra.repository.shift.team.teamsetting;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.shift.team.TeamCode;
import nts.uk.ctx.at.schedule.dom.shift.team.teamsetting.TeamSet;
import nts.uk.ctx.at.schedule.dom.shift.team.teamsetting.TeamSetRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.team.teamsetting.KscstTeamSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.team.teamsetting.KscstTeamSetPK;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;

/**
 * teamset repository implement
 * 
 * @author Trung Tran
 *
 */
@Stateless
public class JpaTeamSetRepository extends JpaRepository implements TeamSetRepository {
	/**
	 * get all team set
	 */
	private static final String GET_ALL_DEFAULT = "Select c from KscstTeamSet c";
	/**
	 * remove team set
	 */
	private static final String REMOVE_TEAM_SET = "DELETE FROM KscstTeamSet c ";

	private static final String BY_CODE = "WHERE c.ksctTeamSetPk.workPlaceId = :workPlaceId AND c.teamCode = :teamCode";

	@Override
	public List<TeamSet> getAllTeamSet() {
		return this.queryProxy().query(GET_ALL_DEFAULT, KscstTeamSet.class).getList(t -> toDomain(t));
	}

	/**
	 * convert entity obj to domain obj
	 * 
	 * @param entity
	 * @return domain object
	 */
	private TeamSet toDomain(KscstTeamSet entity) {
		return new TeamSet(new TeamCode(entity.teamCode), entity.ksctTeamSetPk.sId,
				new WorkplaceId(entity.ksctTeamSetPk.workPlaceId));
	}

	private KscstTeamSet toEntity(TeamSet domain) {
		KscstTeamSetPK pk = new KscstTeamSetPK(domain.getSId(), domain.getWorkPlaceId().v());
		return new KscstTeamSet(pk, domain.getTeamCode().v());
	}

	/**
	 * add teamSet
	 */
	@Override
	public void addTeamSet(TeamSet domain) {
		KscstTeamSet entity = toEntity(domain);
		this.commandProxy().insert(entity);
	}

	@Override
	public void removeTeamSetByTeamCode(String workPlaceId, String teamCode) {
		this.getEntityManager().createQuery(REMOVE_TEAM_SET + BY_CODE).setParameter("workPlaceId", workPlaceId)
				.setParameter("teamCode", teamCode).executeUpdate();
	}

	@Override
	public void removeListTeamSet(List<String> employees, String workPlaceId) {
		employees.stream().forEach(sId -> {
			KscstTeamSetPK pk = new KscstTeamSetPK(sId, workPlaceId);
			this.commandProxy().remove(KscstTeamSet.class, pk);
		});
	}

	@Override
	public void removeTeamSet(String sId, String workPlace) {
		KscstTeamSetPK pk = new KscstTeamSetPK(sId, workPlace);
		this.commandProxy().remove(KscstTeamSet.class, pk);
	}

}
