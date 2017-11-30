package find.person.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Getter
@Setter
@AllArgsConstructor
public class PersonDto extends PeregDomainDto{
	/** The Birthday */
	// 生年月日
	@PeregItem("IS00017")
	private GeneralDate birthDate;

	/** The BloodType */
	// 血液型
	@PeregItem("IS00019")
	private int bloodType;

	/** The Gender - 性別 */
	@PeregItem("IS00018")
	private int gender;

	/** The PersonNameGroup - 個人名グループ*/
	@PeregItem("IS00003")
	private String personName;

	@PeregItem("IS00004")
	private String personNameKana;

	@PeregItem("IS00009")
	private String businessName;
	
	@PeregItem("IS00010")
	private String businessNameKana;

	@PeregItem("IS00011")
	private String businessEnglishName;

	@PeregItem("IS00012")
	private String businessOtherName;

	@PeregItem("IS00005")
	private String personRomanji;
	
	@PeregItem("IS00006")
	private String personRomanjiKana;

	@PeregItem("IS00013")
	private String oldName;
	
	@PeregItem("IS00014")
	private String oldNameKana;

	@PeregItem("IS00015")
	private String todokedeFullName;
	
	@PeregItem("IS00016")
	private String todokedeFullNameKana;

	@PeregItem("IS00007")
	private String PersonalNameMultilingual;
	
	@PeregItem("IS00008")
	private String PersonalNameMultilingualKana;
	
	public static PersonDto createFromDomain(Person person){
		boolean hasBusinessNameKana = person.getPersonNameGroup().getBusinessNameKana().v() != null;
		boolean hasBusinessOtherName = person.getPersonNameGroup().getBusinessOtherName().v() != null;
		boolean hasBusinessEnglishName = person.getPersonNameGroup().getBusinessEnglishName().v() != null;
		boolean hasPerRomanjiFullName = person.getPersonNameGroup().getPersonRomanji().getFullName().v() != null;
		boolean hasPerRomanjiFullNameKana = person.getPersonNameGroup().getPersonRomanji().getFullNameKana().v() != null;
		boolean hasTodokedeFullName = person.getPersonNameGroup().getTodokedeFullName().getFullName().v() != null;
		boolean hasTodokedeFullNameFullName = person.getPersonNameGroup().getTodokedeFullName().getFullNameKana().v() != null;
		boolean hasOldNameFullName = person.getPersonNameGroup().getOldName().getFullName().v() != null;
		boolean hasOldNameFullNameKana = person.getPersonNameGroup().getOldName().getFullNameKana().v() != null;
		boolean hasPerNameMultilLang = person.getPersonNameGroup().getPersonalNameMultilingual().getFullName().v() != null;
		boolean hasPerNameMultilLangKana = person.getPersonNameGroup().getPersonalNameMultilingual().getFullNameKana().v() != null;

		return new PersonDto(person.getBirthDate(), 
				person.getBloodType().value, 
				person.getGender().value, 
				person.getPersonNameGroup().getPersonName().getFullName().v(),
				person.getPersonNameGroup().getPersonName().getFullNameKana().v(), 
				person.getPersonNameGroup().getBusinessName().v(), 
				hasBusinessNameKana ? person.getPersonNameGroup().getBusinessNameKana().v() : "", 
				hasBusinessEnglishName ? person.getPersonNameGroup().getBusinessEnglishName().v() : "", 
				hasBusinessOtherName ? person.getPersonNameGroup().getBusinessOtherName().v() : "", 
				hasPerRomanjiFullName ? person.getPersonNameGroup().getPersonRomanji().getFullName().v() : "", 
				hasPerRomanjiFullNameKana ? person.getPersonNameGroup().getPersonRomanji().getFullNameKana().v() : "", 
				hasOldNameFullName ? person.getPersonNameGroup().getOldName().getFullName().v() : "", 
				hasOldNameFullNameKana ? person.getPersonNameGroup().getOldName().getFullNameKana().v() : "", 
				hasTodokedeFullName ? person.getPersonNameGroup().getTodokedeFullName().getFullName().v() : "", 
				hasTodokedeFullNameFullName ? person.getPersonNameGroup().getTodokedeFullName().getFullNameKana().v() : "", 
				hasPerNameMultilLang ? person.getPersonNameGroup().getPersonalNameMultilingual().getFullName().v() : "", 
				hasPerNameMultilLangKana ? person.getPersonNameGroup().getPersonalNameMultilingual().getFullNameKana().v() : "");
	}
	
}
