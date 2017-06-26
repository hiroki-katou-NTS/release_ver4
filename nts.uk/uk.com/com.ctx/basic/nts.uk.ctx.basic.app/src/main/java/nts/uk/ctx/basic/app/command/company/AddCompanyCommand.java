package nts.uk.ctx.basic.app.command.company;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.basic.dom.company.Company;
/**
 * 
 * @author lanlt
 *
 */
@Setter
@Getter
public class AddCompanyCommand {
    private String companyCode;
    private String companyName;
    private String companyNameGlobal;
    private String address1;
    private String address2;
    private String addressKana1;
    private String addressKana2;
    private String companyNameAbb;
    private String companyNameKana;
    private String corporateMyNumber;
    private int depWorkPlaceSet;
    private int displayAttribute;
    private String faxNo;
    private String postal;
    private String presidentName;
    private String presidentJobTitle;
    private String telephoneNo;
    private int termBeginMon;
	private int use_Gr_Set;
	private int use_Kt_Set;
	private int use_Qy_Set;
	private int use_Jj_Set;
	private int use_Ac_Set;
	private int use_Gw_Set;
	private int use_Hc_Set;
	private int use_Lc_Set;
	private int use_Bi_Set;
	private int use_Rs01_Set;
    private int use_Rs02_Set;
	private int use_Rs03_Set;
	private int use_Rs04_Set;
	private int use_Rs05_Set;
	private int use_Rs06_Set;
	private int use_Rs07_Set;
	private int use_Rs08_Set;
	private int use_Rs09_Set;
	private int use_Rs10_Set;
	public Company toDomain(){
		return Company.createFromJavaType(companyCode, companyName,companyNameGlobal, companyNameAbb, 
				 companyNameKana, corporateMyNumber, faxNo, postal, 
				 presidentName,presidentJobTitle, telephoneNo,
				 depWorkPlaceSet,displayAttribute, address1, address2, addressKana1, addressKana2, termBeginMon,
				 use_Gr_Set,use_Kt_Set, use_Qy_Set, use_Jj_Set, use_Ac_Set, use_Gw_Set,use_Hc_Set,use_Lc_Set,
				 use_Bi_Set,use_Rs01_Set, use_Rs02_Set, use_Rs03_Set,use_Rs04_Set, use_Rs05_Set,
				 use_Rs06_Set,use_Rs07_Set,use_Rs08_Set,use_Rs09_Set,use_Rs10_Set);
	}
}
