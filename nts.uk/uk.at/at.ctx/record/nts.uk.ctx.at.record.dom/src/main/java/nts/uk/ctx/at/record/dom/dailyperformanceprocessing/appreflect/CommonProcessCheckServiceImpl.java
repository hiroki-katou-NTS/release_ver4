package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class CommonProcessCheckServiceImpl implements CommonProcessCheckService{

	@Override
	public boolean commonProcessCheck(CommonCheckParameter para) {
		ReflectedStateRecord state;
		if(para.getExecutiontype() == ExecutionType.RETURN) {
			return true;
		}
		//実績反映状態
		if(para.getDegressAtr() == DegreeReflectionAtr.RECORD) {
			state = para.getStateReflectionReal();
		} else {
			state = para.getStateReflection();
		}
		if(state == ReflectedStateRecord.WAITREFLECTION) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<Integer> lstScheWorkItem() {
		List<Integer> lstItem = new ArrayList<>();
		lstItem.add(1);
		lstItem.add(2);
		lstItem.add(3);
		lstItem.add(4);
		lstItem.add(5);
		lstItem.add(6);
		lstItem.add(7);
		lstItem.add(8);
		lstItem.add(9);
		lstItem.add(10);
		lstItem.add(11);
		lstItem.add(12);
		lstItem.add(13);
		lstItem.add(14);
		lstItem.add(15);
		lstItem.add(16);
		lstItem.add(17);
		lstItem.add(18);
		lstItem.add(19);
		lstItem.add(20);
		lstItem.add(21);
		lstItem.add(22);
		lstItem.add(23);
		lstItem.add(24);
		lstItem.add(25);
		lstItem.add(26);
		lstItem.add(27);
		
		return lstItem;
	}

}
