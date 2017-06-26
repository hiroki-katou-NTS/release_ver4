package nts.uk.ctx.pr.core.dom.allot;

import java.util.Optional;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;

public interface CompanyAllotSettingRepository {
	Optional<CompanyAllotSetting> find(String companyCode);	
}
