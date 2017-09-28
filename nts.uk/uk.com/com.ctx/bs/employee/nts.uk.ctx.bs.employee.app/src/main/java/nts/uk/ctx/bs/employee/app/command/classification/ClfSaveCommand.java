package nts.uk.ctx.bs.employee.app.command.classification;

import lombok.Data;
import nts.uk.ctx.bs.employee.dom.classification.ClassificationCode;
import nts.uk.ctx.bs.employee.dom.classification.ClassificationGetMemento;
import nts.uk.ctx.bs.employee.dom.classification.ClassificationName;
import nts.uk.ctx.bs.employee.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * Instantiates a new clf save command.
 */
@Data
public class ClfSaveCommand implements ClassificationGetMemento {
	
	/** The classification code. */
	private String classificationCode;
	
	/** The classification name. */
	private String classificationName;
	
	/** The memo. */
	private String memo;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.classification.ClassificationGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(AppContexts.user().companyId());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.classification.ClassificationGetMemento#getClassificationCode()
	 */
	@Override
	public ClassificationCode getClassificationCode() {
		return new ClassificationCode(this.classificationCode);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.bs.employee.dom.classification.ClassificationGetMemento#getClassificationName()
	 */
	@Override
	public ClassificationName getClassificationName() {
		return new ClassificationName(this.classificationName);
	}
}
