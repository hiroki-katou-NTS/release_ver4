package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm.alarmlist.daily;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.FixedExtractionDayCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily.FixedExtractionDayConRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.alarmlistworkplace.daily.KrcmtWkpFxexDayCon;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaFixedExtractionDayConRepository extends JpaRepository implements FixedExtractionDayConRepository {

    private static final String SELECT_ALL_FXEX_DAY_CON = "SELECT a FROM KrcmtWkpFxexDayCon a ";

    private static final String SELECT_RANGE_FXEX_DAY_CON_BY_ID = SELECT_ALL_FXEX_DAY_CON
            + " WHERE a.errorAlarmWorkplaceId IN :chkId ";

    @Override
    public List<FixedExtractionDayCon> getRange(List<String> errorAlarmWorkplaceId) {
        if (errorAlarmWorkplaceId.isEmpty()) {
            return new ArrayList<>();
        }

        return this.queryProxy().query(SELECT_RANGE_FXEX_DAY_CON_BY_ID, KrcmtWkpFxexDayCon.class)
                .setParameter("chkId", errorAlarmWorkplaceId)
                .getList(KrcmtWkpFxexDayCon::toDomain);
    }

    @Override
    public void register(List<FixedExtractionDayCon> domain) {
        this.commandProxy().insertAll(domain.stream().map(KrcmtWkpFxexDayCon::fromDomain).collect(Collectors.toList()));
    }

    @Override
    public void delete(List<String> ids) {
        this.commandProxy().removeAll(KrcmtWkpFxexDayCon.class, ids);
    }

}
