package nts.uk.shr.com.primitive;

import nts.arc.primitive.StringPrimitiveValue;
import nts.gul.text.IdentifierUtil;

public class PersonId extends StringPrimitiveValue<PersonId>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	public PersonId(String rawValue) { 
		super(rawValue);
	}
	
	/**
     * create new UserId instance
     * 
     * @return new UserId instance
     */
    public static PersonId newPersonId() {
        return new PersonId(IdentifierUtil.randomUniqueId());
    }
}
