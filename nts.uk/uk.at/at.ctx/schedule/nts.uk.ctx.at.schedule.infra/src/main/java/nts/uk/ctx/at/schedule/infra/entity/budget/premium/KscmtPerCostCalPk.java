package nts.uk.ctx.at.schedule.infra.entity.budget.premium;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KscmtPerCostCalPk implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "CID")
    public String companyID;

    @Column(name = "HIS_ID")
    public String histID;

}
