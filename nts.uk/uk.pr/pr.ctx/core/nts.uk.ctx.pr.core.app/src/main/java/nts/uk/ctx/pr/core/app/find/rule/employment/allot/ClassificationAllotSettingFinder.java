package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import javax.ejb.Stateless;
@Stateless
public class ClassificationAllotSettingFinder {
//	
//	@Inject
//	private ClassificationAdaptor classAdaptor;
//
//	@Inject
//	private ClassificationAllotSettingRespository classificationAllotSettingRespository;
//	
//	public List<ClassificationAllotSettingDto> getAllClassificationAllotSetting(String companyCode) {
//		//get paymentcode bonuscode
//		List<ClassificationAllotSetting> classAllots = this.classificationAllotSettingRespository.findAll(companyCode);
//		
//		List<String> classCodes = classAllots.stream().map(c -> c.getClassificationCode().v()).collect(Collectors.toList());
//		//get classificationCode classificationName
//		Map<String, ClassificationDto> classes = this.classAdaptor.getCodeName(classCodes, companyCode).stream().collect(
//				Collectors.toMap(c -> c.getClassificationCode(), c -> c));
//		
//		return classAllots.stream()
//				.map(m -> {
//					ClassificationAllotSettingDto classAllot = ClassificationAllotSettingDto.fromDomain(m);
//					
//					ClassificationDto classDto = classes.remove(classAllot.getClassificationCode());
//					classAllot.setClassificationName(classDto.getClassificationName());
//					return classAllot;
//				})
//				.collect(Collectors.toList());
//	}
//	
//	
//	public List<ClassificationAllotSettingDto> getHistoy(String historyId) {
//		String companyCode = AppContexts.user().companyCode();
//		List<ClassificationAllotSetting> classAllots = this.classificationAllotSettingRespository.findbyHistoryId(companyCode , historyId);
//		
//		List<String> classCodes = classAllots.stream().map(c -> c.getClassificationCode().v()).collect(Collectors.toList());
//		//get classificationCode classificationName
//		if(classCodes == null || classCodes.isEmpty()){
//			return new ArrayList<>();
//		}
//		Map<String, ClassificationDto> classes = this.classAdaptor.getCodeName(classCodes, companyCode).stream().collect(
//				Collectors.toMap(c -> c.getClassificationCode(), c -> c));
//		
//		return classAllots.stream()
//				.map(m -> {
//					ClassificationAllotSettingDto classAllot = ClassificationAllotSettingDto.fromDomain(m);
//					
//					ClassificationDto classDto = classes.remove(classAllot.getClassificationCode());
//					if(classDto != null){
//						classAllot.setClassificationName(classDto.getClassificationName());
//					}
//					return classAllot;
//				})
//				.collect(Collectors.toList());
//
//	}
}