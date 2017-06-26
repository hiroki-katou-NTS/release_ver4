package nts.uk.ctx.basic.dom.company.address;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
/**
 * 
 * @author lanlt
 *
 */
@Getter
public class Address  extends DomainObject{
	
	private Address1 address1;
	
	private Address2 address2;
	
	private AddressKana1 addressKana1;
	
	private AddressKana2 addressKana2;

	public Address(Address1 address1, Address2 address2, AddressKana1 addressKana1, AddressKana2 addressKana2) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.addressKana1 = addressKana1;
		this.addressKana2 = addressKana2;
	}
	
	
}
