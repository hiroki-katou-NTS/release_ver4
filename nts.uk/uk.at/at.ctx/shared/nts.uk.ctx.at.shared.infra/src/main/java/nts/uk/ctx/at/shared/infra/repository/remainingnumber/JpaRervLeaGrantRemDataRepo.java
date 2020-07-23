package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.RervLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveNumberInfo;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.resvlea.KrcmtReverseLeaRemain;

@Stateless
public class JpaRervLeaGrantRemDataRepo extends JpaRepository implements RervLeaGrantRemDataRepository {

	private static final String QUERY_WITH_EMP_ID = "SELECT a FROM KrcmtReverseLeaRemain a WHERE a.sid = :employeeId ORDER BY a.grantDate desc";

	private static final String QUERY_WITH_EMP_ID_NOT_EXP = "SELECT * FROM KRCMT_RVSLEA_REMAIN WHERE SID = @employeeId AND EXP_STATUS = 1 ORDER BY GRANT_DATE DESC";
	
	private static final String DELETE_AFTER_QUERY = "DELETE FROM KrcmtReverseLeaRemain a WHERE a.sid = :employeeId and a.grantDate > :startDate";
	
	private static final String SEL_REM_BY_SID_AND_GRANT_DATE = "SELECT a FROM KrcmtReverseLeaRemain a WHERE a.sid = :employeeId AND a.grantDate = :grantDate AND a.rvsLeaId !=:rvsLeaId";

	private static final JpaEntityMapper<KrcmtReverseLeaRemain> MAPPER = new JpaEntityMapper<>(KrcmtReverseLeaRemain.class);
	
	@Override
	public List<ReserveLeaveGrantRemainingData> find(String employeeId, String cId) {
		List<KrcmtReverseLeaRemain> entities = this.queryProxy().query(QUERY_WITH_EMP_ID, KrcmtReverseLeaRemain.class)
				.setParameter("employeeId", employeeId).getList();
		return entities.stream().map(ent -> ent.toDomain()).collect(Collectors.toList());
	}

	@Override
	public List<ReserveLeaveGrantRemainingData> findNotExp(String employeeId, String cId) {
		return new NtsStatement(QUERY_WITH_EMP_ID_NOT_EXP, this.jdbcProxy())
				.paramString("employeeId", employeeId)
				.getList(rec -> MAPPER.toEntity(rec).toDomain());
	}

	@Override
	public void add(ReserveLeaveGrantRemainingData data, String cId) {
		KrcmtReverseLeaRemain e = new KrcmtReverseLeaRemain();
		e.cid = cId;
		e.rvsLeaId = data.getRsvLeaID();
		updateDetail(e, data);
		this.commandProxy().insert(e);
	}
	
	private void updateDetail(KrcmtReverseLeaRemain e, ReserveLeaveGrantRemainingData data) {
		e.sid = data.getEmployeeId();
		e.grantDate = data.getGrantDate();
		e.deadline = data.getDeadline();
		e.expStatus = data.getExpirationStatus().value;
		e.registerType = data.getRegisterType().value;
		ReserveLeaveNumberInfo details = data.getDetails();
		e.grantDays = details.getGrantNumber().v();
		e.usedDays = details.getUsedNumber().getDays().v();
		e.overLimitDays = details.getUsedNumber().getOverLimitDays().isPresent()
				? details.getUsedNumber().getOverLimitDays().get().v() : 0;
		e.remainingDays = details.getRemainingNumber().v();
	}

	@Override
	public void update(ReserveLeaveGrantRemainingData data) {
		Optional<KrcmtReverseLeaRemain> entityOpt = this.queryProxy().find(data.getRsvLeaID(),
				KrcmtReverseLeaRemain.class);
		if (entityOpt.isPresent()) {
			KrcmtReverseLeaRemain entity = entityOpt.get();
			updateDetail(entity, data);
			this.commandProxy().update(entity);
		}
	}

	@Override
	public void delete(String rsvLeaId) {
		Optional<KrcmtReverseLeaRemain> entityOpt = this.queryProxy().find(rsvLeaId, KrcmtReverseLeaRemain.class);
		if (entityOpt.isPresent()) {
			this.commandProxy().remove(entityOpt.get());
		}
	}

	@Override
	public Optional<ReserveLeaveGrantRemainingData> getById(String id) {
		Optional<KrcmtReverseLeaRemain> entityOpt = this.queryProxy().find(id, KrcmtReverseLeaRemain.class);
		if (entityOpt.isPresent()) {
			KrcmtReverseLeaRemain entity = entityOpt.get();
			return Optional.of(ReserveLeaveGrantRemainingData.createFromJavaType(id, entity.sid, entity.grantDate,
					entity.deadline, entity.expStatus, entity.registerType, entity.grantDays, entity.usedDays,
					entity.overLimitDays, entity.remainingDays));
		}
		return Optional.empty();
	}

	@Override
	public List<ReserveLeaveGrantRemainingData> find(String employeeId, GeneralDate grantDate) {
		String sql = "SELECT a FROM KrcmtReverseLeaRemain a WHERE a.sid = :employeeId AND a.grantDate = :grantDate";
		return this.queryProxy().query(sql, KrcmtReverseLeaRemain.class).setParameter("employeeId", employeeId)
				.setParameter("grantDate", grantDate).getList(e -> e.toDomain());
	}

	@Override
	public void deleteAfterDate(String employeeId, GeneralDate date) {
		this.getEntityManager().createQuery(DELETE_AFTER_QUERY).setParameter("employeeId", employeeId)
				.setParameter("startDate", date);
	}

	@Override
	public boolean checkValidateGrantDay(String sid, String rvsLeaId, GeneralDate grantDate) {
		//SEL_REM_BY_SID_AND_GRANT_DATE 	
		List<KrcmtReverseLeaRemain> remainData = this.queryProxy().query(SEL_REM_BY_SID_AND_GRANT_DATE, KrcmtReverseLeaRemain.class).setParameter("employeeId", sid)
		.setParameter("grantDate", grantDate)
		.setParameter("rvsLeaId", rvsLeaId)
		.getList();
		if(remainData.size() > 0) {
			return true;
		}
		return false;
	}
}
