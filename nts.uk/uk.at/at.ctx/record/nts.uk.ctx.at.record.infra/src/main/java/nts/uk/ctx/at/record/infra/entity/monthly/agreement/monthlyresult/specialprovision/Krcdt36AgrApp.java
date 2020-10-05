package nts.uk.ctx.at.record.infra.entity.monthly.agreement.monthlyresult.specialprovision;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.*;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.AgreementOneMonthTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.OneMonthErrorAlarmTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.oneyear.AgreementOneYearTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.oneyear.OneYearErrorAlarmTime;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author quang.nh1
 */
@Entity
@Table(name = "KRCDT_36AGR_APP")
@AllArgsConstructor
@NoArgsConstructor
public class Krcdt36AgrApp extends UkJpaEntity implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "APP_ID")
    public String appID;

    @Column(name = "CID")
    public String companyID;

    @Column(name = "INP_SID")
    public String enteredPersonSID;

    @Column(name = "INPUT_DATE_TIME")
    public GeneralDate inputDate;

    @Column(name = "TGT_SID")
    public String applicantsSID;

    @Column(name = "APP_ATR")
    public int typeAgreement;

    @Column(name = "YM")
    public Integer yearMonth;

    @Column(name = "M_ER_TIME")
    public Integer monthErrorTime;

    @Column(name = "M_AL_TIME")
    public Integer monthAlarmTime;

    @Column(name = "YEAR")
    public Integer year;

    @Column(name = "Y_ER_TIME")
    public Integer yearErrorTime;

    @Column(name = "Y_AL_TIME")
    public Integer yearAlarmTime;

    @Column(name = "APP_REASON")
    public String reasonsForAgreement;

    @Column(name = "APPROVED_STATUS")
    public int approvalStatus;

    @Column(name = "APPROVED_SID")
    public String approveSID;

    @Column(name = "APPROVED_COMMENT")
    public String approvalComment;

    @Column(name = "APPROVED_YMD")
    public GeneralDate approvalDate;

    @Column(name = "APV_SID_1")
    public String approveSID1;

    @Column(name = "APV_SID_2")
    public String approveSID2;

    @Column(name = "APV_SID_3")
    public String approveSID3;

    @Column(name = "APV_SID_4")
    public String approveSID4;

    @Column(name = "APV_SID_5")
    public String approveSID5;

    @Column(name = "CFM_SID_1")
    public String confirmerSID1;

    @Column(name = "CFM_STATUS_1")
    public Integer confirmationStatus1;

    @Column(name = "CFM_YMD_1")
    public GeneralDate confirmDate1;

    @Column(name = "CFM_SID_2")
    public String confirmerSID2;

    @Column(name = "CFM_STATUS_2")
    public Integer confirmationStatus2;

    @Column(name = "CFM_YMD_2")
    public GeneralDate confirmDate2;

    @Column(name = "CFM_SID_3")
    public String confirmerSID3;

    @Column(name = "CFM_STATUS_3")
    public Integer confirmationStatus3;

    @Column(name = "CFM_YMD_3")
    public GeneralDate confirmDate3;

    @Column(name = "CFM_SID_4")
    public String confirmerSID4;

    @Column(name = "CFM_STATUS_4")
    public Integer confirmationStatus4;

    @Column(name = "CFM_YMD_4")
    public GeneralDate confirmDate4;

    @Column(name = "CFM_SID_5")
    public String confirmerSID5;

    @Column(name = "CFM_STATUS_5")
    public Integer confirmationStatus5;

    @Column(name = "CFM_YMD_5")
    public GeneralDate confirmDate5;

    @Override
    protected Object getKey() {
        return this.appID;
    }

    public static Krcdt36AgrApp fromDomain(SpecialProvisionsOfAgreement domain) {

        return new Krcdt36AgrApp(
                domain.getApplicationID(),
                AppContexts.user().companyId(),
                domain.getEnteredPersonSID(),
                domain.getInputDate(),
                domain.getApplicantsSID(),
                domain.getApplicationTime().getTypeAgreement().value,
                domain.getApplicationTime().getOneMonthTime().isPresent() ? domain.getApplicationTime().getOneMonthTime().get().getYearMonth().v() : null,
                domain.getApplicationTime().getOneMonthTime().isPresent() ? domain.getApplicationTime().getOneMonthTime().get().getErrorTimeInMonth().getError().v() : null,
                domain.getApplicationTime().getOneMonthTime().isPresent() ? domain.getApplicationTime().getOneMonthTime().get().getErrorTimeInMonth().getAlarm().v() : null,
                domain.getApplicationTime().getOneYearTime().isPresent() ? domain.getApplicationTime().getOneYearTime().get().getYear().v() : null,
                domain.getApplicationTime().getOneYearTime().isPresent() ? domain.getApplicationTime().getOneYearTime().get().getErrorTimeInYear().getError().v() : null,
                domain.getApplicationTime().getOneYearTime().isPresent() ? domain.getApplicationTime().getOneYearTime().get().getErrorTimeInYear().getAlarm().v() : null,
                domain.getReasonsForAgreement().v(),
                domain.getApprovalStatusDetails().getApprovalStatus().value,
                domain.getApprovalStatusDetails().getApproveSID().isPresent() ? domain.getApprovalStatusDetails().getApproveSID().get() : null,
                domain.getApprovalStatusDetails().getApprovalComment().isPresent() ? domain.getApprovalStatusDetails().getApprovalComment().get().v() : null,
                domain.getApprovalStatusDetails().getApprovalDate().isPresent() ? domain.getApprovalStatusDetails().getApprovalDate().get() : null,
                domain.getListApproverSID().size() > 0 ? domain.getListApproverSID().get(0) : null,
                domain.getListApproverSID().size() > 1 ? domain.getListApproverSID().get(1) : null,
                domain.getListApproverSID().size() > 2 ? domain.getListApproverSID().get(2) : null,
                domain.getListApproverSID().size() > 3 ? domain.getListApproverSID().get(3) : null,
                domain.getListApproverSID().size() > 5 ? domain.getListApproverSID().get(4) : null,
                domain.getConfirmationStatusDetails().size() > 0 ? domain.getConfirmationStatusDetails().get(0).getConfirmerSID() : null,
                domain.getConfirmationStatusDetails().size() > 0 ? domain.getConfirmationStatusDetails().get(0).getConfirmationStatus().value : null,
                domain.getConfirmationStatusDetails().size() > 0 ? domain.getConfirmationStatusDetails().get(0).getConfirmDate().get() : null,

                domain.getConfirmationStatusDetails().size() > 1 ? domain.getConfirmationStatusDetails().get(1).getConfirmerSID() : null,
                domain.getConfirmationStatusDetails().size() > 1 ? domain.getConfirmationStatusDetails().get(1).getConfirmationStatus().value : null,
                domain.getConfirmationStatusDetails().size() > 1 ? domain.getConfirmationStatusDetails().get(1).getConfirmDate().get() : null,

                domain.getConfirmationStatusDetails().size() > 2 ? domain.getConfirmationStatusDetails().get(2).getConfirmerSID() : null,
                domain.getConfirmationStatusDetails().size() > 2 ? domain.getConfirmationStatusDetails().get(2).getConfirmationStatus().value : null,
                domain.getConfirmationStatusDetails().size() > 2 ? domain.getConfirmationStatusDetails().get(2).getConfirmDate().get() : null,

                domain.getConfirmationStatusDetails().size() > 3 ? domain.getConfirmationStatusDetails().get(3).getConfirmerSID() : null,
                domain.getConfirmationStatusDetails().size() > 3 ? domain.getConfirmationStatusDetails().get(3).getConfirmationStatus().value : null,
                domain.getConfirmationStatusDetails().size() > 3 ? domain.getConfirmationStatusDetails().get(3).getConfirmDate().get() : null,

                domain.getConfirmationStatusDetails().size() > 4 ? domain.getConfirmationStatusDetails().get(4).getConfirmerSID() : null,
                domain.getConfirmationStatusDetails().size() > 4 ? domain.getConfirmationStatusDetails().get(4).getConfirmationStatus().value : null,
                domain.getConfirmationStatusDetails().size() > 4 ? domain.getConfirmationStatusDetails().get(4).getConfirmDate().get() : null
        );
    }

    public static SpecialProvisionsOfAgreement toDomain(Krcdt36AgrApp entity) {
        List<String> listApproveSID = new ArrayList<>();
        if (entity.approveSID1 != null) {
            listApproveSID.add(entity.approveSID1);
        }
        if (entity.approveSID2 != null) {
            listApproveSID.add(entity.approveSID2);
        }
        if (entity.approveSID3 != null) {
            listApproveSID.add(entity.approveSID3);
        }
        if (entity.approveSID4 != null) {
            listApproveSID.add(entity.approveSID4);
        }
        if (entity.approveSID5 != null) {
            listApproveSID.add(entity.approveSID5);
        }

        List<ConfirmationStatusDetails> confirmationStatusDetails = new ArrayList<>();
        if (entity.confirmationStatus1 != null){
            ConfirmationStatusDetails item1 = new ConfirmationStatusDetails(entity.confirmerSID1,EnumAdaptor.valueOf(entity.confirmationStatus1, ConfirmationStatus.class) ,entity.confirmDate1 == null ? Optional.empty() : Optional.of(entity.confirmDate1));
            confirmationStatusDetails.add(item1);
        }
        if (entity.confirmationStatus2 != null){
            ConfirmationStatusDetails item2 = new ConfirmationStatusDetails(entity.confirmerSID2,EnumAdaptor.valueOf(entity.confirmationStatus2, ConfirmationStatus.class),entity.confirmDate2 == null ? Optional.empty() : Optional.of(entity.confirmDate2));
            confirmationStatusDetails.add(item2);
        }
        if (entity.confirmationStatus3 != null){
            ConfirmationStatusDetails item3 = new ConfirmationStatusDetails(entity.confirmerSID3,EnumAdaptor.valueOf(entity.confirmationStatus3, ConfirmationStatus.class),entity.confirmDate3 == null ? Optional.empty() : Optional.of(entity.confirmDate3));
            confirmationStatusDetails.add(item3);
        }
        if (entity.confirmationStatus4 != null){
            ConfirmationStatusDetails item4 = new ConfirmationStatusDetails(entity.confirmerSID4,EnumAdaptor.valueOf(entity.confirmationStatus4, ConfirmationStatus.class),entity.confirmDate4 == null ? Optional.empty() : Optional.of(entity.confirmDate4));
            confirmationStatusDetails.add(item4);
        }
        if (entity.confirmationStatus5 != null){
            ConfirmationStatusDetails item5 = new ConfirmationStatusDetails(entity.confirmerSID5,EnumAdaptor.valueOf(entity.confirmationStatus5, ConfirmationStatus.class),entity.confirmDate5 == null ? Optional.empty() : Optional.of(entity.confirmDate5));
            confirmationStatusDetails.add(item5);
        }

        OneMonthTime oneMonthTime = new OneMonthTime(OneMonthErrorAlarmTime.of(new AgreementOneMonthTime(entity.monthErrorTime), new AgreementOneMonthTime(entity.monthAlarmTime)), new YearMonth(entity.yearMonth));
        OneYearTime oneYearTime = new OneYearTime(OneYearErrorAlarmTime.of(new AgreementOneYearTime(entity.yearErrorTime), new AgreementOneYearTime(entity.yearAlarmTime)), new Year(entity.year));
        ApplicationTime applicationTime = new ApplicationTime(EnumAdaptor.valueOf(entity.typeAgreement, TypeAgreementApplication.class), Optional.of(oneMonthTime), Optional.of(oneYearTime));
        return new SpecialProvisionsOfAgreement(
                entity.appID,
                entity.enteredPersonSID,
                entity.inputDate,
                entity.applicantsSID,
                applicationTime,
                new ReasonsForAgreement(entity.reasonsForAgreement),
                listApproveSID,
                ApprovalStatusDetails.create(EnumAdaptor.valueOf(entity.approvalStatus, ApprovalStatus.class),
                        entity.approveSID == null ? Optional.empty() :  Optional.of(entity.approveSID),
                        entity.approvalComment == null ? Optional.empty() : Optional.of(new AgreementApprovalComments(entity.approvalComment)),
                        entity.approvalDate == null ? Optional.empty() : Optional.of(entity.approvalDate)),
                confirmationStatusDetails,
                new ScreenDisplayInfo()
        );

    }

}
