package nts.uk.ctx.sys.log.app.find.reference;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.uk.ctx.sys.log.dom.reference.LogDisplaySetting;
import nts.uk.ctx.sys.log.dom.reference.LogDisplaySettingRepository;
import nts.uk.ctx.sys.log.dom.reference.RecordTypeEnum;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/*
 * author : thuong.tv
 */

@Stateless
public class LogDisplaySettingFinder {

    @Inject
    private LogDisplaySettingRepository logDisplaySettingRepository;
    
    public LogDisplaySettingDto getLogDisplaySettingByCode(String code){
    	 // get login info
        LoginUserContext loginUserContext = AppContexts.user();
         // get company id
        String cid = loginUserContext .companyId();
        Optional<LogDisplaySetting> optLogDisplaySetting = this.logDisplaySettingRepository.getLogDisplaySettingByCodeAndCid(code, cid);
		if (optLogDisplaySetting.isPresent()) {
			return LogDisplaySettingDto.fromDomain(optLogDisplaySetting.get());
		}
		return null;
    }
    
    public LogDisplaySettingDto getLogDisplaySettingByCodeAndFlag(String code){
   	 // get login info
       LoginUserContext loginUserContext = AppContexts.user();
        // get company id
       String cid = loginUserContext .companyId();
       Optional<LogDisplaySetting> optLogDisplaySetting = this.logDisplaySettingRepository.getLogDisplaySettingByCodeAndCidAndIsUseFlg(code, cid);
		if (optLogDisplaySetting.isPresent()) {
			return LogDisplaySettingDto.fromDomain(optLogDisplaySetting.get());
		}
		 
		return null;
   }
    
    
    public List<LogDisplaySettingDto> getAllLogDisplaySet(){
   	 // get login info
       LoginUserContext loginUserContext = AppContexts.user();
        // get company id
       String cid = loginUserContext .companyId();
       return this.logDisplaySettingRepository.getAllLogDisplaySet(cid)
    		   .stream().map(s -> LogDisplaySettingDto.fromDomain(s))
    		   .collect(Collectors.toList());
   }
    
    
    public List<LogDisplaySettingDto> getLogDisplaySettingByRecordType(String logSetRecordType,String targetDataType){
    	 // get login info
        LoginUserContext loginUserContext = AppContexts.user();
         // get company id
        String cid = loginUserContext .companyId();
        if(!Objects.isNull(targetDataType) && Integer.valueOf(logSetRecordType).intValue()==RecordTypeEnum.DATA_CORRECT.code){
        	 return this.logDisplaySettingRepository.getLogDisplaySettingByRecordType(logSetRecordType, targetDataType, cid)
             		   .stream().map(s -> LogDisplaySettingDto.fromDomainNotLogSetOutputItems(s))
             		   .collect(Collectors.toList());
        }else{
        	 return this.logDisplaySettingRepository.getLogDisplaySettingByRecordType(logSetRecordType,cid)
             		   .stream().map(s -> LogDisplaySettingDto.fromDomainNotLogSetOutputItems(s))
             		   .collect(Collectors.toList());
        }
         
      }
    
}
