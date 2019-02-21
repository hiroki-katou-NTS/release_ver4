package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

@AllArgsConstructor
@NoArgsConstructor
public class ResultReturnDCUpdateData extends EmpAndDate{
	
	@Getter
	private Map<Integer, List<DPItemValue>> resultError  = new HashMap<>();
	
	
	public boolean hasError(){
		return !resultError.isEmpty() && !resultError.values().isEmpty();
	}

	public ResultReturnDCUpdateData(String employeeId, GeneralDate date, Map<Integer, List<DPItemValue>> resultError) {
		super(employeeId, date);
		this.resultError = resultError;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resultError == null) ? 0 : resultError.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultReturnDCUpdateData other = (ResultReturnDCUpdateData) obj;
		if (resultError == null) {
			if (other.resultError != null)
				return false;
		} 
//		else if (!resultError.equals(other.resultError))
//			return false;
		return true;
	}	
	
	
}
