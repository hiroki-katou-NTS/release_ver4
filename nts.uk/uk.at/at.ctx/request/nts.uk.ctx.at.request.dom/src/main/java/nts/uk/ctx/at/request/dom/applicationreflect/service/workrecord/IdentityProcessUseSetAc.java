package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
public class IdentityProcessUseSetAc {
	private String cid;
	private boolean useConfirmByYourself;
	private boolean useIdentityOfMonth;
	private Optional<Integer> yourSelfConfirmError;
}
