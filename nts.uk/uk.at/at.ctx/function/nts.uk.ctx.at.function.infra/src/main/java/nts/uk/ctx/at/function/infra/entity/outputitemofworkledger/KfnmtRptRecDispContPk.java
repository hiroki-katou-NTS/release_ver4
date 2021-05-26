package nts.uk.ctx.at.function.infra.entity.outputitemofworkledger;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KfnmtRptRecDispContPk implements Serializable {

    public  static final long serialVersionUID = 1l;

    // ID->勤務台帳の出力項目.ID
    @Column(name = "LAYOUT_ID")
    public String iD;

    //	勤怠項目ID->印刷する勤怠項目.勤怠項目ID
    @Column(name = "ATD_ITEM_ID")
    public int attendanceItemId;
}
