package nts.uk.ctx.at.function.infra.entity.outputitemsofannualworkledger;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.AnnualWorkLedgerOutputSetting;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.DailyOutputItemsAnnualWorkLedger;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItem;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity: 	年間勤務台帳の項目
 *
 * @author :chinh.hm
 */
@Entity
@Table(name = "KFNMT_RPT_YR_REC_ITEM")
@NoArgsConstructor
@AllArgsConstructor
public class KfnmtRptYrRecItem extends UkJpaEntity implements Serializable {
    @EmbeddedId
    public KfnmtRptYrRecItemPk pk;
    //	契約コード
    @Column(name = "CONTRACT_CD")
    public String contractCode;

    //	会社ID
    @Column(name = "CID")
    public String cid;

    //	出力名称->出力項目.名称
    @Column(name = "ITEM_NAME")
    public String itemName;

    //	印刷対象フラグ		->出力項目.印刷対象フラグ
    @Column(name = "ITEM_IS_PRINTED")
    public boolean itemIsPrintEd;

    //	単独計算区分->出力項目.単独計算区分
    @Column(name = "ITEM_CALCULATOR_TYPE")
    public int itemCalculatorType;

    //	日次月次区分->出力項目.日次月次区分
    @Column(name = "ITEM_ATTENDANCE_TYPE")
    public int itemAttendanceType;

    //	属性->出力項目.属性
    @Column(name = "ITEM_ATTRIBUTE")
    public int itemAttribute;

    @Override
    protected Object getKey() {
        return pk;
    }

    public static List<KfnmtRptYrRecItem> fromDomain(AnnualWorkLedgerOutputSetting outputSetting,
                                                     List<DailyOutputItemsAnnualWorkLedger> outputItemsOfTheDayList,
                                                     List<OutputItem> outputItemList){
        return outputItemList.stream().map(e->new KfnmtRptYrRecItem(
                new KfnmtRptYrRecItemPk(Integer.parseInt(outputSetting.getID()),e.getRank()),
                AppContexts.user().contractCode(),
                AppContexts.user().companyId(),
                e.getName().v(),
                e.isPrintTargetFlag(),
                e.getIndependentCalculaClassification().value,
                e.getDailyMonthlyClassification().value,
                e.getItemDetailAttributes().value
        ) ).collect(Collectors.toList());
    }
}
