package nts.uk.ctx.pr.core.dom.allot;

import java.util.Optional;

public interface PersonalAllotSettingRepository {

	Optional<PersonalAllotSetting> find(String companyCode, String personId, int baseYM);	

}
