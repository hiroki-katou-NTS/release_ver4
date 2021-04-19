package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.alarmlistworkplace.monthly;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.monthly.FixedExtractionMonthlyItems;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity: アラームリスト（職場）月次の固定抽出項目
 *
 * @author Thanh.LNP
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_WKP_MON_FXEX_ITM")
public class KrcmtWkpMonFxexItm extends UkJpaEntity implements Serializable {

    /* No */
    @Id
    @Column(name = "NO")
    public int no;

    /* 月次チェック名称 */
    @Column(name = "MON_CHKNAME")
    public String monthlyCheckName;

    /* アラームチェック区分 */
    @Column(name = "ALARM_CHK_ATR")
    public int alarmCheckCls;

    /* 最初表示するメッセージ */
    @Column(name = "FIRST_MESSAGE_DIS")
    public String firstMessageDisp;

    /* メッセージを太字にする */
    @Column(name = "MESSAGE_BOLD")
    public boolean boldAtr;

    /* メッセージの色 */
    @Column(name = "MESSAGE_COLOR")
    public String messageColor;

    @Override
    protected Object getKey() {
        return no;
    }

    public static KrcmtWkpMonFxexItm fromDomain(FixedExtractionMonthlyItems domain) {
        KrcmtWkpMonFxexItm entity = new KrcmtWkpMonFxexItm();

        entity.no = domain.getNo().value;
        entity.monthlyCheckName = domain.getMonthlyCheckName();
        entity.alarmCheckCls = domain.getAlarmCheckCls().value;
        entity.firstMessageDisp = domain.getFirstMessageDisp().v();
        entity.boldAtr = domain.isBoldAtr();
        entity.messageColor = domain.getMessageColor().map(i -> i.v()).orElse(null);

        return entity;
    }

    public FixedExtractionMonthlyItems toDomain() {
        return FixedExtractionMonthlyItems.create(
                this.no,
                this.alarmCheckCls,
                this.monthlyCheckName,
                this.boldAtr,
                this.firstMessageDisp,
                this.messageColor
        );
    }
}
