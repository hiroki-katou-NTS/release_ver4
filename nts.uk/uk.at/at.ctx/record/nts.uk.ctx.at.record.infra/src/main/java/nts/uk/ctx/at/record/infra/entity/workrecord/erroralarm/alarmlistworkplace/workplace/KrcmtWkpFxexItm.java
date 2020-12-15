package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.alarmlistworkplace.workplace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.workplace.AlarmFixedExtractionItem;
import nts.uk.shr.infra.data.entity.ContractCompanyUkJpaEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "KRCMT_WKP_FXEX_ITM")
public class KrcmtWkpFxexItm extends ContractCompanyUkJpaEntity implements Serializable {

    @Id
    @Column(name="NO")
    public Integer no;

    @Column(name="WORKPLACE_CHECK_NAME")
    public String name;

    @Column(name="ALARM_CHK_ATR")
    public Integer checkAtr;

    @Column(name="FIRST_MESSAGE_DIS")
    public String message;

    @Column(name="MESSAGE_BOLD")
    public Boolean bold;

    @Column(name="MESSAGE_COLOR")
    public String color;

    @Override
    protected Object getKey() {
        return this.no;
    }

    public AlarmFixedExtractionItem toDomain() {
        return new AlarmFixedExtractionItem(
            this.no,
            this.checkAtr,
            this.bold,
            this.message,
            this.name,
            this.color
        );
    }
}
