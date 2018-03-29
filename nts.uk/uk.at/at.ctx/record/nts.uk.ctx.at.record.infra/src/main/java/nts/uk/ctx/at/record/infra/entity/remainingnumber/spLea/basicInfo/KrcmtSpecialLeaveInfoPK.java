package nts.uk.ctx.at.record.infra.entity.remainingnumber.spLea.basicInfo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcmtSpecialLeaveInfoPK {
    @Column(name = "SID")
    public String employeeId;
    
    @Column(name = "SPECIAL_LEAVE_CD")
    public int spLeaveCD;
}
