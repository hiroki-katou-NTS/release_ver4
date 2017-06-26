/**
 * 
 */
package nts.uk.ctx.basic.infra.entity.company;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * @author lanlt
 *
 */
@Entity
@Table(name="CMNMT_COMPANY")
@AllArgsConstructor
@NoArgsConstructor
public class CmnmtCompany extends UkJpaEntity implements Serializable{
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public CmnmtCompanyPK cmnmtCompanyPk;
	
	@Basic(optional = false)
	@Column(name ="CNAME")
	public String cName;
	
	@Column(name ="CNAME_E")
	public String cName_E;
	
	@Column(name="CNAME_KANA")
	public String cName_Kana;
	
	@Column(name="CNAME_ABB")
	public String cName_Abb;
	
	@Basic(optional = false)
	@Column(name="DISP_ATR")
	public int disp_Atr;
	
	@Basic(optional = false)
	@Column(name="CMYNO")
	public String cmyNo;
	
	@Column(name="PRESIDENT_NAME")
	public String president_Name;
	
	@Column(name="PRESIDENT_JOB_TITLE")
	public String president_Job_Title;
	
	@Column(name="POSTAL")
	public String postal;
	
	@Basic(optional = false)
	@Column(name="ADDRESS1")
	public String address1;
	
	@Column(name="ADDRESS2")
	public String address2;
	
	@Column(name="KN_ADDRESS1")
	public String kn_Address1;

	@Column(name="KN_ADDRESS2")
	public String kn_Address2;
	
	@Column(name="TEL_NO")
	public String tel_No;

	@Column(name="FAX_NO")
	public String fax_No;
	
	@Basic(optional = false)
	@Column(name="DEP_WORK_PLACE_SET")
	public int dep_Work_Place_Set;
	
	@Basic(optional = false)
	@Column(name="TERM_BEGIN_MON")
	public int term_Begin_Mon;
	
	@Basic(optional = false)
	@Column(name="USE_GR_SET")
	public int use_Gr_Set;
	
	@Basic(optional = false)
	@Column(name="USE_KT_SET")
	public int use_Kt_Set;
	
	@Basic(optional = false)
	@Column(name="USE_QY_SET")
	public int use_Qy_Set;
	
	@Basic(optional = false)
	@Column(name="USE_JJ_SET")
	public int use_Jj_Set;
	
	@Basic(optional = false)
	@Column(name="USE_AC_SET")
	public int use_Ac_Set;
	
	@Basic(optional = false)
	@Column(name= "USE_GW_SET")
	public int use_Gw_Set;
	
	@Basic(optional = false)
	@Column(name="USE_HC_SET")
	public int use_Hc_Set;
	
	@Basic(optional = false)
	@Column(name="USE_LC_SET")
	public int use_Lc_Set;
	
	@Basic(optional = false)
	@Column(name="USE_BI_SET")
	public int use_Bi_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS01_SET")
	public int use_Rs01_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS02_SET")
	public int use_Rs02_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS03_SET")
	public int use_Rs03_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS04_SET")
	public int use_Rs04_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS05_SET")
	public int use_Rs05_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS06_SET")
	public int use_Rs06_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS07_SET")
	public int use_Rs07_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS08_SET")
	public int use_Rs08_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS09_SET")
	public int use_Rs09_Set;
	
	@Basic(optional = false)
	@Column(name="USE_RS10_SET")
	public int use_Rs10_Set;

	@Override
	protected Object getKey() {
		return this.cmnmtCompanyPk;
	}
}
