package nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.otworkapply;

import java.util.Optional;

public interface OtWorkAppReflectRepository {
    Optional<OtWorkAppReflect> findReflectByCompanyId(String companyId);
}
