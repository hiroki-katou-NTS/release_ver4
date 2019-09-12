package nts.uk.ctx.sys.auth.app.find.role.workplace;

import java.io.Serializable;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
public class WorkplaceParam implements Serializable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The base date. */
	private GeneralDate baseDate;
	
	/** The reference range. */
	private Integer referenceRange;
	
	private Optional<Boolean> isWkplManager;
}
