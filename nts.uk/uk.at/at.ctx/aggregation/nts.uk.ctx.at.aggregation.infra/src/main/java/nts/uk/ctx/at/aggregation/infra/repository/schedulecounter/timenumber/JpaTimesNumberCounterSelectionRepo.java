package nts.uk.ctx.at.aggregation.infra.repository.schedulecounter.timenumber;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.tally.timescounting.TimesNumberCounterSelection;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.tally.timescounting.TimesNumberCounterSelectionRepo;
import nts.uk.ctx.at.aggregation.dom.schedulecounter.tally.timescounting.TimesNumberCounterType;
import nts.uk.ctx.at.aggregation.infra.entity.schedulecounter.timenumber.KscmtTallyTotalTime;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaTimesNumberCounterSelectionRepo extends JpaRepository implements TimesNumberCounterSelectionRepo {


    private static final String SELECT;

    private static final String FIND_BY_CID;

    private static final String FIND_BY_TYPE;

    private static final String FIND_BY_CID_AND_TYPE;

    static {
        StringBuilder builderString = new StringBuilder();
        builderString.append(" SELECT a ");
        builderString.append(" FROM KscmtTallyTotalTime a ");
        SELECT = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.pk.companyId = :companyId ");
        FIND_BY_CID = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.pk.companyId = :companyId ");
        builderString.append(" AND a.pk.countType = :countType ");
        FIND_BY_TYPE = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" JOIN KshmtTotalTimes b  ");
        builderString.append(" ON a.pk.timeNo = b.kshstTotalTimesPK.totalTimesNo AND a.pk.companyId = b.kshstTotalTimesPK.cid ");
        builderString.append(" WHERE a.pk.companyId = :companyId ");
        builderString.append(" AND a.pk.countType = :type AND b.useAtr = 1 ");
        FIND_BY_CID_AND_TYPE = builderString.toString();
    }

    @Override
    public void insert(String companyId, TimesNumberCounterSelection domain) {
        KscmtTallyTotalTime.toEntity(companyId,domain).forEach(x -> {
            commandProxy().insert(x);
            this.getEntityManager().flush();
        });
    }

    @Override
    public void update(String companyId, TimesNumberCounterSelection domain) {
        List<KscmtTallyTotalTime> result = this.queryProxy().query(FIND_BY_TYPE, KscmtTallyTotalTime.class)
            .setParameter("companyId", companyId)
            .setParameter("countType", domain.getType().value)
            .getList();
        commandProxy().removeAll(result);
        this.getEntityManager().flush();
        KscmtTallyTotalTime.toEntity(companyId,domain).forEach(x -> {
            commandProxy().insert(x);
            this.getEntityManager().flush();
        });
    }

    @Override
    public Optional<TimesNumberCounterSelection> get(String companyId, TimesNumberCounterType type) {
        List<KscmtTallyTotalTime> result = this.queryProxy().query(FIND_BY_CID_AND_TYPE, KscmtTallyTotalTime.class)
            .setParameter("companyId", companyId)
            .setParameter("type", type.value)
            .getList();
        return result.size() > 0 ? Optional.ofNullable(KscmtTallyTotalTime.toDomain(result)) : Optional.empty();
    }

    @Override
    public boolean exists(String companyId, TimesNumberCounterType type) {
        List<KscmtTallyTotalTime> result = this.queryProxy().query(FIND_BY_CID, KscmtTallyTotalTime.class)
            .setParameter("companyId", companyId)
            .getList();
        return result.size() > 0;
    }
}
