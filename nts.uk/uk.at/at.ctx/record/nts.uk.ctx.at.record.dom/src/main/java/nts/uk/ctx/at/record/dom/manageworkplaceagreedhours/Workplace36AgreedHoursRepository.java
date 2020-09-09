package nts.uk.ctx.at.record.dom.manageworkplaceagreedhours;

import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfWorkPlace;

import java.util.List;
import java.util.Optional;

/**
 * 	Repository	 職場３６協定時間
 */
public interface Workplace36AgreedHoursRepository {
    void insert(AgreementTimeOfWorkPlace domain);
    void update(AgreementTimeOfWorkPlace domain);
    void delete(AgreementTimeOfWorkPlace domain);
    List<AgreementTimeOfWorkPlace> getByListWorkplaceId(List<String> listWorkplaceId);
    Optional<AgreementTimeOfWorkPlace> getByWorkplaceId(String workplaceId);
}
