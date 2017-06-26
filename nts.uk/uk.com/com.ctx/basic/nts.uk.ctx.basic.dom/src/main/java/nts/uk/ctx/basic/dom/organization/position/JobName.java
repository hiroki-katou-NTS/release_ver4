package nts.uk.ctx.basic.dom.organization.position;
import nts.arc.primitive.constraint.StringMaxLength;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.primitive.StringPrimitiveValue;


@StringMaxLength(20)
public class JobName extends StringPrimitiveValue<PrimitiveValue<String>>{
	/**
	 * JOBNAME
	 */
	private static final long serialVersionUID = 1L;
	public JobName(String rawValue) {
		super(rawValue);
	}

}