package nts.uk.ctx.at.aggregation.infra.repository.schedulecounter.personalcounter;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.PersonalCounter;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.PersonalCounterCategory;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.PersonalCounterRepo;
import nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.personalcounter.KscmtTallyByPerson;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaPersonalCounterRepo extends JpaRepository implements PersonalCounterRepo {

    @Inject
    I18NResourcesForUK ukResource;

    private static final String SELECT;

    private static final String FIND_BY_CID;

    static {
        StringBuilder builderString = new StringBuilder();
        builderString.append(" SELECT a ");
        builderString.append(" FROM KscmtTallyByPerson a ");
        SELECT = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.pk.companyId = :companyId ");
        FIND_BY_CID = builderString.toString();
    }

    @Override
    public void insert(String companyId, PersonalCounter domain) {
        List<EnumConstant> listEnum = EnumAdaptor.convertToValueNameList(PersonalCounterCategory.class, ukResource);
        commandProxy().insertAll(KscmtTallyByPerson.toEntity(companyId,domain,listEnum));
    }

    @Override
    public void update(String companyId, PersonalCounter domain) {
        List<EnumConstant> listEnum = EnumAdaptor.convertToValueNameList(PersonalCounterCategory.class, ukResource);
        commandProxy().updateAll(KscmtTallyByPerson.toEntity(companyId,domain,listEnum));
    }

    @Override
    public Optional<PersonalCounter> get(String companyId) {
        List<KscmtTallyByPerson> result = this.queryProxy().query(FIND_BY_CID, KscmtTallyByPerson.class)
            .setParameter("companyId", companyId)
            .getList();
        return result.size() > 0 ? Optional.of(KscmtTallyByPerson.toDomain(result)) : Optional.empty();
    }

    @Override
    public boolean exists(String companyId) {
        List<KscmtTallyByPerson> result = this.queryProxy().query(FIND_BY_CID, KscmtTallyByPerson.class)
            .setParameter("companyId", companyId)
            .getList();
        return result.size() > 0;
    }
}
