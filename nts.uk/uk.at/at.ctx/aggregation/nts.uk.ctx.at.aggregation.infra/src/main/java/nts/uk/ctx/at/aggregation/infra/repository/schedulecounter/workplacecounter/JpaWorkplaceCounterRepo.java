package nts.uk.ctx.at.aggregation.infra.repository.schedulecounter.workplacecounter;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.WorkplaceCounter;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.WorkplaceCounterCategory;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.WorkplaceCounterRepo;
import nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.workplacecounter.KscmtTallyByWkp;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaWorkplaceCounterRepo extends JpaRepository implements WorkplaceCounterRepo {

    @Inject
    I18NResourcesForUK ukResource;

    private static final String SELECT;

    private static final String FIND_BY_CID;

    static {
        StringBuilder builderString = new StringBuilder();
        builderString.append(" SELECT a ");
        builderString.append(" FROM KscmtTallyByWkp a ");
        SELECT = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.pk.companyId = :companyId ");
        FIND_BY_CID = builderString.toString();
    }

    @Override
    public void insert(String companyId, WorkplaceCounter domain) {
        List<EnumConstant> listEnum = EnumAdaptor.convertToValueNameList(WorkplaceCounterCategory.class, ukResource);
        commandProxy().insertAll(KscmtTallyByWkp.toEntity(companyId,domain,listEnum));
    }

    @Override
    public void update(String companyId, WorkplaceCounter domain) {
        List<EnumConstant> listEnum = EnumAdaptor.convertToValueNameList(WorkplaceCounterCategory.class, ukResource);
        commandProxy().updateAll(KscmtTallyByWkp.toEntity(companyId,domain,listEnum));
    }

    @Override
    public Optional<WorkplaceCounter> get(String companyId) {
        List<KscmtTallyByWkp> result = this.queryProxy().query(FIND_BY_CID, KscmtTallyByWkp.class)
            .setParameter("companyId", companyId)
            .getList();
        return result.size() > 0 ? Optional.of(KscmtTallyByWkp.toDomain(result)) : Optional.empty();
    }

    @Override
    public boolean exists(String companyId) {
        List<KscmtTallyByWkp> result = this.queryProxy().query(FIND_BY_CID, KscmtTallyByWkp.class)
            .setParameter("companyId", companyId)
            .getList();
        return result.size() > 0;
    }
}
