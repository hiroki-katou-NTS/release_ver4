package nts.uk.ctx.at.function.infra.entity.outputitemofworkledger;


import lombok.AllArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * entity: 勤務台帳の設定
 */
@Entity
@Table(name = "KFNMT_RPT_REC_SETTING")
@AllArgsConstructor
public class KfnmtRptRecSetting extends UkJpaEntity implements Serializable {
    public static long serialVersionUID = 1L;

    @EmbeddedId
    public KfnmtRptRecSettingPk pk;
    //	契約コード
    @Column(name = "CONTRACT_CD")
    public String contractCode;

    // 	会社ID
    @Column(name = "CID")
    public String companyID;

    //	表示コード -> 勤務台帳の出力項目.コード
    @Column(name = "DISPLAY_CODE")
    public int displayCode;

    //	名称 -> 勤務台帳の出力項目.名称
    @Column(name = "NAME")
    public String name;

    // 	社員ID-> 勤務台帳の出力項目.社員ID
    @Column(name = "EMPLOYEE_ID")
    public String employeeId;

    // 	定型自由区分	-> 勤務台帳の出力項目.定型自由区分
    @Column(name = "SETTING_TYPE")
    public int settingType;

    @Override
    protected Object getKey() {
        return pk;
    }
}
