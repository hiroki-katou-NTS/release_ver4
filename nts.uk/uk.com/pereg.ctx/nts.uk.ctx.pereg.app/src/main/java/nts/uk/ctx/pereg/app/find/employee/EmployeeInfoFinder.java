package nts.uk.ctx.pereg.app.find.employee;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.employee.dom.employeeinfo.service.EmployeeBusiness;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmployeeInfoFinder {

	@Inject
	private EmployeeDataMngInfoRepository empDataRepo;

	@Inject
	private EmployeeBusiness employeeBusiness;

	@Inject
	private UserRepository userRepo;

	@Inject
	private EmployeeDataMngInfoRepository empDataMngInfoRepo;

	@Inject
	private PersonRepository personRepository;

	private static final String JP_SPACE = "　";
	
	public String getGenerateEmplCodeAndComId(String startLetters) {
		String ComId = AppContexts.user().companyId();
		String EmpCode = employeeBusiness.generateEmplCode(startLetters);
		return EmpCode == "" ? "" : ComId + EmpCode;
	}

	public void validateEmpInfo(EmpInfoDto empInfo) {
		Boolean isDuplicateEmpCode = !CollectionUtil.isEmpty(this.empDataRepo
				.getEmployeeNotDeleteInCompany(AppContexts.user().companyId(), empInfo.getEmployeeCode()));

		if (isDuplicateEmpCode) {
			throw new BusinessException("Msg_345");
		}
		
		String employeeName = empInfo.getEmployeeName();
		if (employeeName.startsWith(JP_SPACE) || employeeName.endsWith(JP_SPACE)
				|| !employeeName.contains(JP_SPACE)){
			throw new BusinessException("Msg_924");
		}
		// Boolean isDuplicateCardNo =
		// this.employeeRepository.isDuplicateCardNo(AppContexts.user().companyId(),
		// empInfo.getCardNo());
		//
		// if (isDuplicateCardNo) {
		// throw new BusinessException(new RawErrorMessage("Msg_346"));
		// }
		Boolean isDuplicateLoginId = !CollectionUtil.isEmpty(this.userRepo.getByLoginId(empInfo.getLoginId()));

		if (isDuplicateLoginId) {

			throw new BusinessException("Msg_757");

		}

	}

	public String generateEmplCode(String startLetters) {
		int maxLength = 6;
		String companyId = AppContexts.user().companyId();
		String lastEmployeeCode = empDataMngInfoRepo.findLastEml(companyId, startLetters);
		while (lastEmployeeCode.length() < maxLength) {
			lastEmployeeCode += " ";
		}
		return generateCode(lastEmployeeCode);
	}

	public String generateCardNo(String startLetters) {
		String companyId = AppContexts.user().companyId();
		String lastCardNo = personRepository.getLastCardNo(companyId, startLetters);
		return generateCode(lastCardNo);
	}

	private String generateCode(String value) {
		String returnString = "";
		if (value.equals("")) {
			throw new BusinessException("Msg_505");
		}
		
		// neeus chuỗi toàn là z hoặc Z
		if (value.matches("[zZ]+")) {
			throw new BusinessException("Msg_505");
		}
			
		int length = value.length();
		// nếu chuỗi toàn là 9
		if (value.matches("[9]+")) {
			String standard = "A";
			for (int i = 1; i < length - 1; i++)
				standard += "0";
			standard += "1";
			returnString = standard;
		} else {
			char increaseLetter = ' ';
			char[] arr = value.toCharArray();
			char[] resultArr = new char[arr.length];
			boolean con = true;
			boolean beforeLast = false;
			for (int i = arr.length - 1; i >= 0; i--) {
				char curChar = arr[i];
				if (con) {
					con = false;
					switch (curChar) {
					case ' ':
						increaseLetter = ' ';
						con = true;
						break;
					case '9':
						increaseLetter = '0';
						beforeLast = true;
						con = true;
						break;
					// nếu ký tự tăng là z
					case 'z':
						increaseLetter = 'a';
						beforeLast = true;
						con = true;
						break;
					// nếu ký tự tăng là Z
					case 'Z':
						increaseLetter = 'A';
						beforeLast = true;
						con = true;
						break;
					default:
						increaseLetter = (char) ((int) curChar + 1);
						break;
					}
					
					if (i != 0) {
						if (beforeLast && increaseLetter == ' ') {
							increaseLetter = '1';
							beforeLast = false;
							con = false;
						}
						resultArr[i] = increaseLetter;
					} else {
						if (con) {
							resultArr[i] = increaseLetter;
							for (int j = resultArr.length - 2; j > 0; j--) {
								char c = resultArr[j - 1];
								resultArr[j] = c;
							}
							if (beforeLast)
								resultArr[0] = '1';
						} else
							resultArr[i] = increaseLetter;
					}
				} else {
					resultArr[i] = arr[i];
				}

			}
			returnString = String.valueOf(resultArr);
		}
		return returnString;
	}
}
