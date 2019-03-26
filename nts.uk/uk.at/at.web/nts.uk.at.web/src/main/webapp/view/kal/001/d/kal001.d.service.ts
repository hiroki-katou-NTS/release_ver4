module nts.uk.at.view.kal001.d.service {
    import format = nts.uk.text.format;
    import model = nts.uk.at.view.kal001.d.viewmodel;
        var paths = {
            
            
            extractAlarm: "at/function/alarm/kal/001/extract/alarm",
            extractResult: "at/function/alarm/kal/001/extract/result/",
            isExtracting: "at/function/alarm/kal/001/isextracting",
            extractStarting: "at/function/alarm/kal/001/extractstarting",
            extractFinished: "at/function/alarm/kal/001/extractfinished"
        }
        
        
        export function extractAlarm(taskId :any ,numberEmpSuccess: any, statusId:string ,listEmployee: Array<model.UnitModel>, alarmCode: string, listPeriodByCategory: Array<model.PeriodByCategory>): JQueryPromise<ExtractedAlarmDto>{
            let command = new ExtractAlarmCommand(listEmployee, alarmCode, 
                                                   _.map(listPeriodByCategory, (item) =>{ return new PeriodByCategoryCommand(item);}),statusId);
            
            let def = $.Deferred(), toStopForWriteData = ko.observable(false), secondForLoop = listEmployee.length > 50 ? 5000 : 1000;
            
            nts.uk.request.ajax("at", paths.extractAlarm, command).done(function(task){
                taskId(task.id);
                nts.uk.deferred.repeat(conf => conf.task(() => {
                    return nts.uk.request.asyncTask.getInfo(task.id, !toStopForWriteData()).done(function(res: any) {
                        let taskData = res.taskDatas;
                        _.forEach(taskData, itemCount => {
                            if (itemCount.key === "empCount") {
                                numberEmpSuccess(itemCount.valueAsNumber);
                            }
                        });
                        let dataWriting = _.find(res.taskDatas, function(t){ return t.key === "dataWriting"; });
                        if(dataWriting !== null && dataWriting !== undefined && dataWriting.valueAsBoolean === true) {
                            toStopForWriteData(true);
                        }
                        if(res.succeeded){
                            let data = {};
                            let nullData = _.find(res.taskDatas, (t) => t.key.indexOf("nullData") >= 0);
                            data["nullData"] = nullData.valueAsBoolean;
                            let eralRecord = _.find(res.taskDatas, (t) => t.key.indexOf("eralRecord") >= 0);
                            data["eralRecord"] = eralRecord.valueAsNumber;
                            if(nullData.valueAsBoolean === false){

                                nts.uk.request.ajax("at", paths.extractResult + command.statusProcessId).done(function(result){
                                    let empMap = _.groupBy(_.map(result.empInfos, (empData) => {
                                        return {
                                            employeeCode: empData[0],
                                            employeeID: empData[1],
                                            employeeName: empData[2],
                                            workplaceName: empData[3],
                                            start: moment(empData[4]),
                                            end: moment(empData[5])
                                        }
                                    }), "employeeID");
                                    
                                    let dataX = []; 
                                    _.forEach(result.empEralDatas, item => {
                                        //let eralData = JSON.parse(item.valueAsString);
                                        let empInfo = _.find(empMap[item[0]], (e) => {
                                            if(item[2].indexOf("～") > 0) {
                                                let parts = item[2].split("～");
                                                let start = moment(parts[0].trim()), end = moment(parts[1].trim());
                                                return e.start.isSameOrBefore(start) && e.end.isSameOrAfter(end);
                                            } else {
                                                let start = moment(item[2]);
                                                return e.start.isSameOrBefore(start) && e.end.isSameOrAfter(start);
                                            }    
                                        });
                                        if(!_.isNil(empInfo)) {
                                            dataX.push(_.merge({
                                                guid: item[1],
                                                alarmValueDate: item[2],
                                                categoryName: item[3],
                                                alarmItem: item[4],
                                                alarmValueMessage: item[5],
                                                comment: item[6]
                                            }, empInfo));    
                                        }
                                        
                                    });
                                    data["extractedAlarmData"] = dataX;
                                    def.resolve(data); 
                                });    
                            } else {
                                def.resolve(data); 
                            }
                        } else if(res.failed){
                            def.reject(res.error);
                        }
                    });
                }).while(infor => {
                    return (infor.pending || infor.running) && infor.status != "REQUESTED_CANCEL";
                }).pause(secondForLoop));
            });
            
            return def.promise();
        }
    
        export function isExtracting(): JQueryPromise<boolean> {
            return nts.uk.request.ajax("at", paths.isExtracting);
        }        

        export function extractStarting(): JQueryPromise<string> {
            return nts.uk.request.ajax("at", paths.extractStarting);
        }
        
        export function extractFinished(extraParam): JQueryPromise<void>{
            
            return nts.uk.request.ajax("at", paths.extractFinished, extraParam);
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
                period36Agreement : numer; 
                constructor(p: model.PeriodByCategory){
                    
                    this.category = p.category;
                    this.name = p.categoryName;     
                    this.period36Agreement = p.period36Agreement;     
                                        
                    if(p.category==2|| p.category==5){
                        this.startDate =nts.uk.time.parseMoment(p.startDate).momentObject.toISOString() ;
                        this.endDate = nts.uk.time.parseMoment(p.endDate).momentObject.toISOString() ;
                        
                    }else if(p.category ==7 || p.category ==9){
//                        this.startDate = null ;
//                        this.endDate = null;
                        let sDate =p.startDate + '/01';
                            let eDate = p.endDate;

                            let lastDay = new Date(Number(eDate.slice(0, 4)), Number(eDate.slice(5, 7)), 0);
                            eDate = eDate + "/"  +(lastDay.getDate() <10? "0" + lastDay.getDate() : lastDay.getDate());
                                                        
                            this.startDate =nts.uk.time.parseMoment(sDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(eDate).momentObject.toISOString() ;   
                        
                    }else if(p.category==12){
                        this.name=nts.uk.resource.getText("KAL010_208");
                        if(p.categoryName =="36協定　1・2・4週間"){
                            this.startDate =nts.uk.time.parseMoment(p.startDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(p.endDate).momentObject.toISOString() ;
                                                        
                        } else if(p.categoryName=="36協定　年間"){
                            let sMonth = p.startDate.slice(5, 7);
                            let sDate = p.year +'/' + sMonth +"/01";
                            let eDate = "" ;
                            if (sMonth != 1) {
                                eDate = (parseInt(p.year) + 1) +'/' + p.endDate.slice(5, 7);
                            } else {
                                eDate = p.year + '/' + p.endDate.slice(5, 7);
                            }
                            let lastDay = new Date(Number(eDate.slice(0, 4)), Number(eDate.slice(5, 7)), 0);
                            eDate = eDate + "/"  +(lastDay.getDate() <10? "0" + lastDay.getDate() : lastDay.getDate());                            
                            
                            this.startDate =nts.uk.time.parseMoment(sDate).momentObject.toISOString() ;
                            this.endDate = nts.uk.time.parseMoment(eDate).momentObject.toISOString() ;    
                                                    
                        } else{
                            let sDate =p.startDate + '/01';
                            let eDate = p.endDate;

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
            statusProcessId :string;
            constructor(listEmployee: Array<model.UnitModel>,  alarmCode: string, listPeriodByCategory: Array<PeriodByCategoryCommand>, statusProcessId : string){
                this.listEmployee = listEmployee;
                this.alarmCode = alarmCode;
                this.listPeriodByCategory = _.uniqWith(listPeriodByCategory, _.isEqual);
                this.statusProcessId = statusProcessId;
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
        }
        
        export interface ExtractedAlarmDto{
            extractedAlarmData: Array<ValueExtractAlarmDto>;
            extracting: boolean;
            nullData: boolean;
        }
        export enum AlarmExtraStatus {
            END_NORMAL = 0,   /**正常終了*/
            END_ABNORMAL = 1, /**異常終了*/
            PROCESSING = 2,   /**処理中*/
            INTERRUPT = 3,    /**中断*/
        }
}
