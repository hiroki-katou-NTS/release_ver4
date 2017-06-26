package nts.uk.shr.sample.domevent;

import lombok.val;
import nts.arc.layer.dom.DomainObject;

public class SomeDomain extends DomainObject {

	public void doSomethingWithEvent(boolean flag) {
		
		if (flag) {
			val sampleEvent = new SampleDomainEvent("hello");
			sampleEvent.toBePublished();
		}
	}
}
