package nts.uk.ctx.at.shared.app.command.specialholiday;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.app.find.specialholiday.GrantDatePerSetDto;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDatePer;
import nts.uk.ctx.at.shared.dom.specialholiday.grantdate.GrantDatePerSet;
import nts.uk.shr.com.context.AppContexts;

@Data
@AllArgsConstructor
public class GrantDatePerCommand {
	/*会社ID*/
	private String companyId;

	/*付与日のID*/
	private String specialHolidayCode;

	/*特別休暇コード*/
	private String personalGrantDateCode;

	/*特別休暇名称*/
	private String personalGrantDateName;

	/*一律基準日*/
	private int grantDate;
 
	/*付与基準日*/
	private int grantDateAtr;
	
	private List<GrantDatePerSetDto> grantDatePerSet;
	
	public GrantDatePer toDomain() {
		String companyId = AppContexts.user().companyId();
		
		List<GrantDatePerSet> grantDatePerSet = this.grantDatePerSet.stream().map(x-> {
			return GrantDatePerSet.createSimpleFromJavaType(companyId, 
					x.getSpecialHolidayCode(), 
					x.getPersonalGrantDateCode(),
					x.getGrantDateNo(),
					x.getGrantDateMonth(),
					x.getGrantDateYear());
		}).collect(Collectors.toList());
		
		return  GrantDatePer.createSimpleFromJavaType(companyId, specialHolidayCode, personalGrantDateCode, personalGrantDateName, grantDate, grantDateAtr, grantDatePerSet);
	}
}
