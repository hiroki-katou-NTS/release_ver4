package nts.uk.ctx.office.infra.repository.status;

import java.util.List;
import java.util.Optional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.office.dom.status.ActivityStatus;
import nts.uk.ctx.office.dom.status.ActivityStatusRepository;
import nts.uk.ctx.office.infra.entity.status.ActivityStatusEntity;

public class JpaActivityStatusRepository extends JpaRepository implements ActivityStatusRepository {

	private static final String SELECT_BY_DATE_AND_IDS = "SELECT a From ActivityStatusEntity a WHERE a.pk.sid IN :listId and a.date =:date";
	
	private static final String SELECT_BY_DATE_AND_ID = "SELECT a From ActivityStatusEntity a WHERE a.pk.sid = :sId and a.date = :date";
	
	@Override
	public void insert(ActivityStatus domain) {
		ActivityStatusEntity entity = new ActivityStatusEntity();
		domain.setMemento(entity);
		this.commandProxy().insert(entity);
	}

	@Override
	public void update(ActivityStatus domain) {
		ActivityStatusEntity entity = new ActivityStatusEntity();
        domain.setMemento(entity);
	    Optional<ActivityStatusEntity> oldEntity = this.queryProxy().find(entity.getPk(), ActivityStatusEntity.class);
	    if(oldEntity.isPresent()) {
	    	ActivityStatusEntity updateEntity = oldEntity.get();
	    	updateEntity.setActivity(entity.getActivity());
	    	updateEntity.setDate(entity.getDate());
	    	this.commandProxy().update(updateEntity);
	    }
	}

	@Override
	public void delete(ActivityStatus domain) {
		ActivityStatusEntity entity = new ActivityStatusEntity();
		domain.setMemento(entity);
		this.commandProxy().remove(entity);
	}

	@Override
	public List<ActivityStatus> getByListSidAndDate(List<String> sids, GeneralDate date) {
		return this.queryProxy().query(SELECT_BY_DATE_AND_IDS, ActivityStatusEntity.class)
				.setParameter("listId", sids)
				.setParameter("date", date)
				.getList(ActivityStatus::createFromMemento);
	}

	@Override
	public Optional<ActivityStatus> getBySidAndDate(String sid, GeneralDate date) {
		return this.queryProxy().query(SELECT_BY_DATE_AND_ID, ActivityStatusEntity.class)
				.setParameter("sId", sid)
				.setParameter("date", date)
				.getSingle(ActivityStatus::createFromMemento);
	}


}
