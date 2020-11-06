package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.alarmlist.daily;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.daily.FixedCheckDayItems;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.daily.FixedExtractionDayCon;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.AggregateTableEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity: アラームリスト（職場）日別の固定抽出条件
 *
 * @author Thanh.LNP
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_WKP_FXEX_DAY_CON")
public class KrcmtWkpFxexDayCon extends AggregateTableEntity {

    @EmbeddedId
    public KrcmtWkpFxexDayConPK pk;

    /* 契約コード */
    @Column(name = "CONTRACT_CD")
    public String contractCd;

    /* 表示するメッセージ */
    @Column(name = "MESSAGE_DISPLAY")
    public String messageDisp;

    @Override
    protected Object getKey() {
        return pk;
    }

    public static KrcmtWkpFxexDayCon fromDomain(FixedExtractionDayCon domain) {
        KrcmtWkpFxexDayCon entity = new KrcmtWkpFxexDayCon();

        entity.pk = KrcmtWkpFxexDayConPK.fromDomain(domain);
        entity.contractCd = AppContexts.user().contractCode();
        entity.messageDisp = domain.getMessageDisp().v();

        return entity;
    }

    public FixedExtractionDayCon toDomain() {
        return FixedExtractionDayCon.create(
                this.pk.errorAlarmWorkplaceId,
                EnumAdaptor.valueOf(this.pk.fixedCheckDayItems, FixedCheckDayItems.class),
                new DisplayMessage(this.messageDisp)
        );
    }
}
