package nts.uk.ctx.hr.develop.infra.repository.sysoperationset.eventmanage;

import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.hr.develop.dom.sysoperationset.eventmanage.EventOperation;
import nts.uk.ctx.hr.develop.dom.sysoperationset.eventmanage.EventOperationRepository;
import nts.uk.ctx.hr.develop.infra.entity.sysoperationset.eventmanage.JcmstEventOperation;
import nts.uk.ctx.hr.develop.infra.entity.sysoperationset.eventmanage.JcmstEventOperationPK;
@Stateless
public class JpaEventOperationRepository extends JpaRepository implements EventOperationRepository{
	
	/**
	 * find item by key
	 * @author yennth
	 */
	@Override
	public Optional<EventOperation> findByKey(String companyId, int eventId) {
		Optional<EventOperation> result = this.queryProxy().find(new JcmstEventOperationPK(companyId, eventId), JcmstEventOperation.class)
											.map(x -> convertToDomain(x));
		return result;
	}
	
	/**
	 * insert a item
	 * @author yennth
	 */
	@Override
	public void add(EventOperation eventOperation) {
		this.commandProxy().insert(convertToEntity(eventOperation));
	}
	
	/**
	 * update a item
	 */
	@Override
	public void update(EventOperation eventOperation) {
		this.commandProxy().update(convertToEntity(eventOperation));
	}
	
	/**
	 * convert data from entity to domain
	 * @param x
	 * @return
	 * @author yennth
	 */
	private EventOperation convertToDomain(JcmstEventOperation x){
		return EventOperation.createFromJavaType(x.jcmstEventOperationPK.eventId, x.useEvent, x.jcmstEventOperationPK.companyId, x.ccd);
	}
	
	/**
	 * convert data from entity to domain
	 * @param x
	 * @return
	 * @author yennth
	 */
	private JcmstEventOperation convertToEntity(EventOperation x){
		val entity = new JcmstEventOperation();
		entity.jcmstEventOperationPK = new JcmstEventOperationPK(x.getCompanyId(), x.getEventId().value);
		entity.useEvent = x.getUseEvent().value;
		entity.ccd = x.getCcd();
		return entity;
	}
}
