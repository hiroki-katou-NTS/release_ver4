package nts.uk.shr.sample.domevent;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.event.DomainEvent;

@Value
@EqualsAndHashCode(callSuper = false)
public class SampleDomainEvent extends DomainEvent {
	private String parameter;
}
