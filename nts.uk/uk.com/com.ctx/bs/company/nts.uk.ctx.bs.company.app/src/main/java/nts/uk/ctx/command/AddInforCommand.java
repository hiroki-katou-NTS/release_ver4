package nts.uk.ctx.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.bs.company.dom.company.AddInfor;

/**
 * 
 * @author yennth
 *
 */
@Data
@AllArgsConstructor
public class AddInforCommand {
	
	/** 会社コード */
	private String companyCode;
	
	/** FAX番号 **/
	private String faxNum;
	/** 住所１ **/
	private String add_1;
	/** 住所２ **/
	private String add_2;
	/** 住所カナ１ **/
	private String addKana_1;
	/** 住所カナ２ **/
	private String addKana_2;
	/** 郵便番号 **/
	private String postCd;
	/** 電話番号 **/
	private String phoneNum;
	
	public AddInfor toDomainAdd(String contractCd, String companyId, String companyCode){
		return AddInfor.createFromJavaType(companyCode, contractCd, 
											faxNum, add_1, add_2, 
											addKana_1, addKana_2, postCd, phoneNum);
	}
}
