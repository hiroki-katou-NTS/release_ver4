package nts.uk.ctx.pr.core.app.command.paymentdata.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author vunv
 *
 */
@Getter
@Setter
public abstract class PaymentDataCommandBase {

	private PaymentHeaderCommandBase paymentHeader;

	private List<CategoryCommandBase> categories;
}
