package nts.uk.ctx.at.record.infra.repository.workrecord.erroralarm.alarmlist.schedule;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.schedule.ExtractionScheduleCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.schedule.ExtractionScheduleConRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.alarmlistworkplace.schedule.KrcmtWkpSchedaiExCon;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareRange;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareSingle;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlSingleFixed;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaExtractionScheduleConRepository extends JpaRepository implements ExtractionScheduleConRepository {

    private static final String SELECT_COMPARE_SINGLE =
        " SELECT a FROM KrcstErAlCompareSingle a " +
            " JOIN KrcmtWkpMonExtracCon b ON a.krcstEralCompareSinglePK.conditionGroupId = b.errorAlarmCheckID " +
            " WHERE a.krcstEralCompareSinglePK.atdItemConNo = 0 AND a.conditionType = 0 ";

    private static final String SELECT_COMPARE_RANGE =
        " SELECT a FROM KrcstErAlCompareRange a " +
            " JOIN KrcmtWkpMonExtracCon b ON a.krcstEralCompareRangePK.conditionGroupId = b.errorAlarmCheckID " +
            " WHERE a.krcstEralCompareRangePK.atdItemConNo = 0 ";

    private static final String SELECT_SINGLE_FIXED =
        " SELECT a FROM KrcstErAlSingleFixed a " +
            " JOIN KrcmtWkpMonExtracCon b ON a.krcstEralSingleFixedPK.conditionGroupId = b.errorAlarmCheckID " +
            " WHERE a.krcstEralSingleFixedPK.atdItemConNo = 0 ";

    private static final String SELECT;

    private static final String FIND_BY_IDS_AND_USEATR;

    public static final String GET_BY_IDS;

    static {
        StringBuilder builderString = new StringBuilder();
        builderString.append(" SELECT a FROM KrcmtWkpSchedaiExCon a ");
        SELECT = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.errorAlarmWorkplaceId in :ids AND a.useAtr = :useAtr ");
        FIND_BY_IDS_AND_USEATR = builderString.toString();

        builderString = new StringBuilder();
        builderString.append(SELECT);
        builderString.append(" WHERE a.errorAlarmWorkplaceId in :ids ");
        GET_BY_IDS = builderString.toString();

    }

    @Override
    public List<ExtractionScheduleCon> getBy(List<String> ids, boolean useAtr) {
        if (CollectionUtil.isEmpty(ids)) return new ArrayList<>();

        Optional<KrcstErAlCompareSingle> krcstErAlCompareSingle = this.queryProxy().query(SELECT_COMPARE_SINGLE, KrcstErAlCompareSingle.class).getSingle();
        Optional<KrcstErAlCompareRange> krcstErAlCompareRange = this.queryProxy().query(SELECT_COMPARE_RANGE, KrcstErAlCompareRange.class).getSingle();
        Optional<KrcstErAlSingleFixed> krcstErAlSingleFixed = this.queryProxy().query(SELECT_SINGLE_FIXED, KrcstErAlSingleFixed.class).getSingle();

        return this.queryProxy().query(FIND_BY_IDS_AND_USEATR, KrcmtWkpSchedaiExCon.class)
            .setParameter("ids", ids)
            .setParameter("useAtr", useAtr)
            .getList(x -> x.toDomain(krcstErAlCompareSingle, krcstErAlCompareRange, krcstErAlSingleFixed));
    }

    @Override
    public List<ExtractionScheduleCon> getByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) return new ArrayList<>();

        Optional<KrcstErAlCompareSingle> krcstErAlCompareSingle = this.queryProxy().query(SELECT_COMPARE_SINGLE, KrcstErAlCompareSingle.class).getSingle();
        Optional<KrcstErAlCompareRange> krcstErAlCompareRange = this.queryProxy().query(SELECT_COMPARE_RANGE, KrcstErAlCompareRange.class).getSingle();
        Optional<KrcstErAlSingleFixed> krcstErAlSingleFixed = this.queryProxy().query(SELECT_SINGLE_FIXED, KrcstErAlSingleFixed.class).getSingle();

        return this.queryProxy().query(GET_BY_IDS, KrcmtWkpSchedaiExCon.class)
                .setParameter("ids", ids)
                .getList(x -> x.toDomain(krcstErAlCompareSingle, krcstErAlCompareRange, krcstErAlSingleFixed));
    }

}
