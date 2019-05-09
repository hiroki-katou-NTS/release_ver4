module nts.uk.at.view.kal001.a.service {
    import format = nts.uk.text.format;
    import model = nts.uk.at.view.kal001.a.model;
        var paths = {
            getAlarmByUser: "at/function/alarm/kal/001/pattern/setting",
            getCheckConditionTime: "at/function/alarm/kal/001/check/condition/time",
            extractAlarm: "at/function/alarm/kal/001/extract/alarm",
            isExtracting: "at/function/alarm/kal/001/isextracting",
            extractStarting: "at/function/alarm/kal/001/extractstarting",
            extractFinished: "at/function/alarm/kal/001/extractfinished"
        }
        
        export function getAlarmByUser(): JQueryPromise<Array<any>> {
            return nts.uk.request.ajax("at", paths.getAlarmByUser);
        }        

        export function getCheckConditionTime(alarmCode: string): JQueryPromise<Array<CheckConditionTimeDto>> {                              
              return nts.uk.request.ajax("at", paths.getCheckConditionTime, alarmCode);
        }
        
        export function extractAlarm(listEmployee: Array<model.UnitModel>, alarmCode: string, listPeriodByCategory: Array<model.PeriodByCategory>): JQueryPromise<ExtractedAlarmDto>{
            let command = new ExtractAlarmCommand(listEmployee, alarmCode, 
                                                   _.map(listPeriodByCategory, (item) =>{ return new PeriodByCategoryCommand(item);}));
            
            let def = $.Deferred(), toStopForWriteData = ko.observable(false);
            
            nts.uk.request.ajax("at", paths.extractAlarm, command).done(function(task){
                nts.uk.deferred.repeat(conf => conf.task(() => {
                    return nts.uk.request.asyncTask.getInfo(task.id, !toStopForWriteData()).done(function(res: any) {
                        let dataWriting = _.find(res.taskDatas, function(t){ return t.key === "dataWriting"; });
                        if(dataWriting !== null && dataWriting !== undefined && dataWriting.valueAsBoolean === true) {
                            toStopForWriteData(true);
                        }
                        if(res.succeeded){
                            let data = {};
                            let sorted = _.sortBy(res.taskDatas, function(t){ return parseInt(t.key.replace("dataNo", "")) });
                            let dataX = []; 
                            _.forEach(sorted, item => {
                                if(item.key === "extracting" ){
                                    data["extracting"] = item.valueAsBoolean;
                                } else if(item.key === "nullData"){
                                    data["nullData"] = item.valueAsBoolean;
                                } else {
                                    dataX.push(JSON.parse(item.valueAsString));     
                                }
                            });
                            data["extractedAlarmData"] = dataX;
                            def.resolve(data);
                        } else if(res.failed){
                            def.reject(res.error);
                        }
                    });
                }).while(infor => {
                    return (infor.pending || infor.running) && infor.status != "REQUESTED_CANCEL";
                }).pause(1000));
            });
            
            return def.promise();
        }
    
        export function isExtracting(): JQueryPromise<boolean> {
            return nts.uk.request.ajax("at", paths.isExtracting);
        }        

        export function extractStarting(): JQueryPromise<string> {
            return nts.uk.request.ajax("at", paths.extractStarting);
        }
        
        export function extractFinished(statusId): JQueryPromise<void>{
            
            return nts.uk.request.ajax("at", paths.extractFinished, { processStatusId: statusId });
        }
    
        export interface CheckConditionTimeDto{
            category : number;
            categoryName: string;
            tabOrder: number;
            startDate :   string;
            endDate: string;
            startMonth: string;
            endMonth: string;  
            year: number;
        }
        export class PeriodByCategoryCommand{
                category: number;  
                name: string;                
                startDate: string;                
                endDate : string; 
                constructor(p: model.PeriodByCategory){
                    
                    this.category = p.category;
                    this.name = p.categoryName;     
                                        
                    if(p.category==2|| p.category==5){
                        this.startDate =nts.uk.time.parseMoment(p.dateValue().startDate).momentObject.toISOString() ;
                        this.endDate = nts.uk.time.parseMoment(p.dateValue().endDate).momentObject.toISOString() ;
                        
                    }else if(p.category ==7 || p.category == 9){
//                        this.startDate = null ;
//                        this.endDate = null;
                        let sDate =p.dateValue().startDate + '/01';
                            let eDate = p.dateValue().endDate;

                            let lastDay = new Date(Number(eDate.slice(0, 4)), Number(eDate.slice(5, 7)), 0);
                            eDate = eDate + "/"  +(lastDay.getDate() <10? "0" + lastDay.getDate() : lastDay.getDate());
                                                        
                            this.startDate =nts.uk.time.parseMoment(sDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(eDate).momentObject.toISOString() ;   
                        
                    }else if(p.category==12){
                        this.name=nts.uk.resource.getText("KAL010_208");
                        if(p.categoryName =="36協定　1・2・4週間"){
                            this.startDate =nts.uk.time.parseMoment(p.dateValue().startDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(p.dateValue().endDate).momentObject.toISOString() ;
                                                        
                        } else if(p.categoryName=="36協定　年間"){
                            let sDate =p.year() +'/' + p.dateValue().startDate.slice(5, 7) +"/01";
                            let eDate =(parseInt(p.year()) + 1) +'/' + p.dateValue().endDate.slice(5,7) ;
                            
                            let lastDay = new Date(Number(eDate.slice(0, 4)), Number(eDate.slice(5, 7)), 0);
                            eDate = eDate + "/"  +(lastDay.getDate() <10? "0" + lastDay.getDate() : lastDay.getDate());                            
                            
                            this.startDate =nts.uk.time.parseMoment(sDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(eDate).momentObject.toISOString() ;    
                                                    
                        } else{
                            let sDate =p.dateValue().startDate + '/01';
                            let eDate = p.dateValue().endDate;

                            let lastDay = new Date(Number(eDate.slice(0, 4)), Number(eDate.slice(5, 7)), 0);
                            eDate = eDate + "/"  +(lastDay.getDate() <10? "0" + lastDay.getDate() : lastDay.getDate());
                                                        
                            this.startDate =nts.uk.time.parseMoment(sDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(eDate).momentObject.toISOString() ;                                 
                        }
                        
                    }
                       
                }   
        }
        export class ExtractAlarmCommand{
            listEmployee: Array<model.UnitModel>;
            alarmCode: string;
            listPeriodByCategory: Array<PeriodByCategoryCommand>;
            
            constructor(listEmployee: Array<model.UnitModel>,  alarmCode: string, listPeriodByCategory: Array<PeriodByCategoryCommand>){
                this.listEmployee = listEmployee;
                this.alarmCode = alarmCode;
                this.listPeriodByCategory = _.uniqWith(listPeriodByCategory, _.isEqual);
            }
        }
        
        export interface ValueExtractAlarmDto{
            guid : string;
            workplaceID : string;
            hierarchyCd : string;
            workplaceName : string;
            employeeID : string;
            employeeCode : string;
            employeeName : string;
            alarmValueDate : string;
            category : number;
            categoryName: string;
            alarmItem : string;
            alarmValueMessage : string;
            comment : string;     
            checkedValue : string;       
        }
        
        export interface ExtractedAlarmDto{
            extractedAlarmData: Array<ValueExtractAlarmDto>;
            extracting: boolean;
            nullData: boolean;
        }
    
}
