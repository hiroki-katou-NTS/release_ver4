package nts.uk.ctx.pr.core.app.command.insurance.social;

import lombok.Data;
import nts.uk.ctx.pr.core.app.find.insurance.social.dto.SocialInsuranceOfficeDto;

@Data
public class RegisterSocialOfficeCommand {

	private SocialInsuranceOfficeDto SIODto;
}
