package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IdentityProcessUseSetPub {
	private String cid;
	private boolean useConfirmByYourself;
	private boolean useIdentityOfMonth;
	private Optional<Integer> yourSelfConfirmError;
}
