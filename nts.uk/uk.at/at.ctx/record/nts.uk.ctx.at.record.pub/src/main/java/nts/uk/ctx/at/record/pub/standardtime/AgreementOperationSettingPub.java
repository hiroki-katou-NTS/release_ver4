package nts.uk.ctx.at.record.pub.standardtime;

import java.util.Optional;

import nts.uk.ctx.at.shared.dom.standardtime.AgreementOperationSetting;

public interface AgreementOperationSettingPub {
	Optional<AgreementOperationSetting> find(String cid);
}
