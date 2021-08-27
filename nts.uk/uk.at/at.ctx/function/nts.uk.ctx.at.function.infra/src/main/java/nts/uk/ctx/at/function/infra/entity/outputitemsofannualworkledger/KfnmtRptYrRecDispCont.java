package nts.uk.ctx.at.function.infra.entity.outputitemsofannualworkledger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.val;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.AnnualWorkLedgerOutputSetting;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.DailyOutputItemsAnnualWorkLedger;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItemWorkLedger;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * Entity: 	年間勤務台帳の表示内容
 */
@Entity
@Table(name = "KFNMT_RPT_YR_REC_DISP_CONT")
@NoArgsConstructor
@AllArgsConstructor
public class KfnmtRptYrRecDispCont extends UkJpaEntity implements Serializable {

    private static long serialVersionUID = 1L;

    @EmbeddedId
    public KfnmtRptYrRecDispContPk pk;
    //	契約コード
    @Column(name = "CONTRACT_CD")
    public String contractCode;

    //	会社ID
    @Column(name = "CID")
    public String companyId;

    //		演算子->出力項目詳細の選択勤怠項目.演算子
    @Column(name = "OPERATOR")
    public int operator;

    @Override
    protected Object getKey() {
        return pk;
    }

    public static List<KfnmtRptYrRecDispCont> fromDomain(AnnualWorkLedgerOutputSetting outputSetting){

        val rs = new ArrayList<KfnmtRptYrRecDispCont>();
        for (OutputItemWorkLedger i:outputSetting.getMonthlyOutputItemList() ) {
            rs.addAll(i.getSelectedAttendanceItemList().stream().map(e->new KfnmtRptYrRecDispCont(
                    new KfnmtRptYrRecDispContPk((outputSetting.getID()),i.getRank(),e.getAttendanceItemId()),
                    AppContexts.user().contractCode(),
                    AppContexts.user().companyId(),
                    e.getOperator().value
            ) ).collect(Collectors.toList()));
        }
        for (DailyOutputItemsAnnualWorkLedger i:outputSetting.getDailyOutputItemList() ) {
            rs.addAll(i.getSelectedAttendanceItemList().stream().map(e->new KfnmtRptYrRecDispCont(
                    new KfnmtRptYrRecDispContPk((outputSetting.getID()),i.getRank(),e.getAttendanceItemId()),
                    AppContexts.user().contractCode(),
                    AppContexts.user().companyId(),
                    e.getOperator().value
            ) ).collect(Collectors.toList()));
        }
        return rs;


    }
}
