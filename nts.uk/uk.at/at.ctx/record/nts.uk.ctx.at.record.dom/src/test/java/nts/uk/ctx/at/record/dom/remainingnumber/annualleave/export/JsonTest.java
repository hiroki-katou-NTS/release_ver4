package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import nts.arc.layer.ws.json.serializer.EnumDeserializer;
import nts.arc.layer.ws.json.serializer.EnumSerializer;
import nts.arc.layer.ws.json.serializer.GeneralDateSerializer;
import nts.arc.layer.ws.json.serializer.GeneralDateTimeDeserializer;
import nts.arc.layer.ws.json.serializer.GeneralDateTimeSerializer;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.gul.serialize.binary.ObjectBinaryFile;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.annotation.*;

import nts.gul.serialize.json.JsonMapping;

import lombok.val;
import lombok.experimental.var;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveConditionInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveRemainingHistoryTest;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.YearDayNumber;


public class JsonTest {
	
	@Test
	public void testToJson() throws IOException{
		
//		try{
//			ToBinaryMapClass aToBinaryMapClass = new ToBinaryMapClass();
//			aToBinaryMapClass.toBinaryMap = new HashMap<String,ArrayList<AnnualLeaveRemainingHistoryTest>>();
////			Map<String,ArrayList<AnnualLeaveRemainingHistoryTest>> toBinaryMap 
////				= new HashMap<String,ArrayList<AnnualLeaveRemainingHistoryTest>>();
//			
//		//	// ????????????????????????????????????????????????
//		//	AnnualLeaveRemainHistRepository annualLeaveRemainHistRepo
//		//		= new JpaAnnualLeaveRemainHistRepository();	
//			
//			ArrayList<AnnualLeaveRemainingHistoryTest> remainHistList 
//				= new ArrayList<AnnualLeaveRemainingHistoryTest>();
//			
//			AnnualLeaveRemainingHistoryTest aAnnualLeaveRemainingHistoryTest
//				= new AnnualLeaveRemainingHistoryTest(
//						"cID", "employeeId", 201909, 1, 20, true,
//						GeneralDate.ymd(2019, 3, 30),
//						GeneralDate.ymd(2019, 3, 30),
//						1, 0, 1.5, 10, 1.0, 30, 20.5, 4.5, 30,
//						4.5, 4.5, 4.5, 4.5);
//			
//			AnnualLeaveConditionInfo aAnnualLeaveConditionInfo = new AnnualLeaveConditionInfo();
//			aAnnualLeaveConditionInfo.setPrescribedDays(new YearDayNumber(1.1));
//			aAnnualLeaveRemainingHistoryTest.setAnnualLeaveConditionInfo(
//					Optional.of(aAnnualLeaveConditionInfo));
//					
//			remainHistList.add(aAnnualLeaveRemainingHistoryTest);
//			remainHistList.add(aAnnualLeaveRemainingHistoryTest);
//			
//			
//			
//			ArrayList<AnnualLeaveRemainingHistoryTest> remainHistList2 
//			= new ArrayList<AnnualLeaveRemainingHistoryTest>();
//			
//			aToBinaryMapClass.toBinaryMap.put("2", remainHistList);
//			aToBinaryMapClass.toBinaryMap.put("3", remainHistList2);
//			aToBinaryMapClass.toBinaryMap.put("4", remainHistList);
//			
//			
//			
//			SimpleModule module = new SimpleModule();
//	        module.addSerializer(Enum.class, new EnumSerializer());
//	        module.addDeserializer(Enum.class, new EnumDeserializer());
//	        module.addSerializer(GeneralDate.class, new GeneralDateSerializer());
//	        module.addDeserializer(GeneralDate.class, new GeneralDateDeserializer());
//	        module.addSerializer(GeneralDateTime.class, new GeneralDateTimeSerializer());
//	        module.addDeserializer(GeneralDateTime.class, new GeneralDateTimeDeserializer());
//	        module.addSerializer(YearMonth.class, new YearMonthSerializer());
//	        module.addDeserializer(YearMonth.class, new YearMonthDeserializer());
//	        JsonMapping.MAPPER.registerModule(module);
//	        
//	        
//	        //JSON??????????????????
//	        String json = JsonMapping.toJson(aToBinaryMapClass);
//	            
//	            //System.out.println(json);
//	            
//	//		// JSON?????????????????????
//	//        ObjectMapper mapper = new ObjectMapper();
//	// 
//	        System.out.println(json);
//			
//	        
//	        //????????????????????????
//	//        val file = Paths.get("c:\\jinno\\binarytest.csv");
//	//		ObjectBinaryFile.write(json, file);
//			
//	        String fileName = "c:\\jinno\\jsontest.csv";
//			try {
//	            FileWriter fw = new FileWriter(fileName);
//	            fw.write(json);
//	            fw.close();
//	        } catch (IOException ex) {
//	            ex.printStackTrace();
//	        }
//	        
//			// ????????????????????????
//			//FileReader fileReader = new FileReader(fileName);
//			String text = readAll(fileName);
//			
//			
//			ToBinaryMapClass aToBinaryMapClass9 = null;
//			
//			// json??????????????????????????????
//			System.out.println("1");
//			try{
//				aToBinaryMapClass9
//					= JsonMapping.parse(text, ToBinaryMapClass.class);
//				
//			}catch(Exception e2){
//				e2.printStackTrace();
//			}
//			
//			System.out.println("2");
//			
//			
//			ArrayList<AnnualLeaveRemainingHistoryTest> remainHistList9
//				= aToBinaryMapClass9.toBinaryMap.get("4");
//			
//			System.out.println("3");
//			System.out.println("?????????????????????" + String.valueOf(remainHistList9.size()));
//			
//			Object val9 = remainHistList9.get(0);
//			System.out.println(val9.getClass().toString());
//			
//			
//			// ???????????????
//			AnnualLeaveRemainingHistoryTest aAnnualLeaveRemainingHistoryTest9
//				= remainHistList9.get(0);
//			
//			
//			
//			System.out.println("4");
//			
//			if ( aAnnualLeaveRemainingHistoryTest9.getAnnualLeaveConditionInfo().isPresent()){
//				Double d = aAnnualLeaveRemainingHistoryTest9.getAnnualLeaveConditionInfo().get().getPrescribedDays().v();
//				System.out.println( "getPrescribedDays().v() = " + d.toString() );
//			} else {
//				System.out.println( "aAnnualLeaveRemainingHistoryTest9.getAnnualLeaveConditionInfo() = Empty" );
//			}
//			
//			System.out.println("5");
//			
//	
//			// ????????????????????????????????????????????????????????????
//			String json2 = JsonMapping.toJson(aToBinaryMapClass9);
//			System.out.println(json2);
//			
//			String fileName2 = "c:\\jinno\\jsontest2.csv";
//			try {
//	            FileWriter fw2 = new FileWriter(fileName2);
//	            fw2.write(json2);
//	            fw2.close();
//	        } catch (IOException ex) {
//	            ex.printStackTrace();
//	        }
//		} catch (Exception ex9) {
//            ex9.printStackTrace();
//        }
	}
	
	// ???????????????????????? ??????????????????
	public static String readAll(String path) throws IOException {
	    StringBuilder builder = new StringBuilder();

	    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
	        String string = reader.readLine();
	        while (string != null){
	            builder.append(string + System.getProperty("line.separator"));
	            string = reader.readLine();
	        }
	    }

	    return builder.toString();
	}
	
	
}


