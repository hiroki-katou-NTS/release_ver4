package nts.uk.shr.sample.domevent;

import javax.ejb.Stateless;

import nts.arc.layer.dom.event.DomainEventSubscriber;

@Stateless
public class SampleDomainEventSubscriber implements DomainEventSubscriber<SampleDomainEvent> {

	@Override
	public Class<SampleDomainEvent> subscribedToEventType() {
		return SampleDomainEvent.class;
	}

	@Override
	public void handle(SampleDomainEvent domainEvent) {
		domainEvent.getParameter();
		
		/* do something */
	}

}
