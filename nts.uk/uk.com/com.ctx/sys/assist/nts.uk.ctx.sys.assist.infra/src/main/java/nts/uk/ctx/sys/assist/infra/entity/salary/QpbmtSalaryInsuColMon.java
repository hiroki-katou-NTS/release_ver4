package nts.uk.ctx.sys.assist.infra.entity.salary;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.sys.assist.dom.salary.SalaryInsuColMon;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
* 給与社会保険徴収月
*/
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_SALARY_INSU_COL_MON")
public class QpbmtSalaryInsuColMon extends UkJpaEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
    * ID
    */
    @EmbeddedId
    public QpbmtSalaryInsuColMonPk salaryInsuColMonPk;
    
    /**
    * 徴収月
    */
    @Basic(optional = false)
    @Column(name = "MONTH_COLLECTED")
    public int monthCollected;
    
    @Override
    protected Object getKey()
    {
        return salaryInsuColMonPk;
    }

    public SalaryInsuColMon toDomain() {
        return new SalaryInsuColMon(this.salaryInsuColMonPk.processCateNo, this.salaryInsuColMonPk.cid, this.monthCollected);
    }
    public static QpbmtSalaryInsuColMon toEntity(SalaryInsuColMon domain) {
        return new QpbmtSalaryInsuColMon(new QpbmtSalaryInsuColMonPk(domain.getProcessCateNo(), domain.getCid()), domain.getMonthCollected().value);
    }

}
