package nts.uk.ctx.at.shared.pubimp.specialholiday.event;

import javax.ejb.Stateless;

import nts.arc.layer.dom.event.DomainEventSubscriber;
import nts.uk.ctx.at.shared.dom.specialholiday.event.SpecialHolidayEvent;
import nts.uk.ctx.at.shared.pub.specialholiday.SphdHolidayEventPub;

@Stateless
public class SphdEventPublisher implements DomainEventSubscriber<SpecialHolidayEvent> {

	@Override
	public Class<SpecialHolidayEvent> subscribedToEventType() {
		return SpecialHolidayEvent.class;
	}

	@Override
	public void handle(SpecialHolidayEvent domainEvent) {
		SphdHolidayEventPub event = new SphdHolidayEventPub(domainEvent.isEffective(), domainEvent.getSpecialHolidayCode().v(), domainEvent.getSpecialHolidayName().v());
		event.toBePublished();
	}

}
