module nts.uk.at.view.kal003.share {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import model = nts.uk.at.view.kal003.share.model;

    
    
    export module kal003utils {
        /**
         * initial default value for Condition Object
         * @param itemcheck
         */
        export function getDefaultCondition(No): model.ErAlAtdItemCondition {
            return new model.ErAlAtdItemCondition(No, null);
        }

        /**
         * initial default value for GroupConditio Object
         * @param conditions
         */
        export function getDefaultGroupCondition(conditions: Array<model.ErAlAtdItemCondition>): model.ErAlConditionsAttendanceItem {
            return new model.ErAlConditionsAttendanceItem({
                atdItemConGroupId: ''
                , conditionOperator: 0
                , lstErAlAtdItemCon: conditions || ([])
            });
        }

        export function getDefaultAlCheckTargetCondition(): model.AlCheckTargetCondition {
            return new model.AlCheckTargetCondition({
                filterByBusinessType: false,
                filterByJobTitle: false,
                filterByEmployment: false,
                filterByClassification: false,
                lstBusinessTypeCode: [],
                lstJobTitleId: [],
                lstEmploymentCode: [],
                lstClassificationCode: []
            });
        }
        /**
         * initial default value for CompoundConditio Object
         * @param itemcheck
         */
        export function getDefaultAttendanceItemCondition(): model.AttendanceItemCondition {
            let conditions1: Array<model.ErAlAtdItemCondition> = [getDefaultCondition(0), getDefaultCondition(1), getDefaultCondition(2)];
            let conditions2: Array<model.ErAlAtdItemCondition> = [getDefaultCondition(0), getDefaultCondition(1), getDefaultCondition(2)];
            return new model.AttendanceItemCondition({
                group1: getDefaultGroupCondition(conditions1)
                , group2UseAtr: false
                , group2: getDefaultGroupCondition(conditions2)
                , operatorBetweenGroups: 0
            });
        }
         /**
         * get default group1 when checktype = 4 5 6 7
         * 
         */
        export function getDefaultAttdItemGroup1Item(): model.AttendanceItemCondition {
            let conditions1: Array<model.ErAlAtdItemCondition> = [getDefaultCondition(0)];
            return getDefaultGroupCondition(conditions1);
        }
        export function getDefaultAttdItemGroup3Item(): model.AttendanceItemCondition {
            let conditions1: Array<model.ErAlAtdItemCondition> = [getDefaultCondition(0), getDefaultCondition(1), getDefaultCondition(2)];
            return getDefaultGroupCondition(conditions1);
        }
       
        export function getDefaultWorkTypeCondition(): model.WorkTypeCondition {
            return new model.WorkTypeCondition({
                useAtr: false,
                comparePlanAndActual: 0,
                planFilterAtr: false,
                planLstWorkType: [],
                actualFilterAtr: false,
                actualLstWorkType: []
            });
        }
        export function getDefaultWorkTimeCondition(): model.WorkTimeCondition {
            return new model.WorkTimeCondition({
                useAtr: false,
                comparePlanAndActual: 0,
                planFilterAtr: false,
                planLstWorkTime: [],
                actualFilterAtr: false,
                actualLstWorkTime: []

            });
        }

        export function getDefaultErrorAlarmCondition(): model.ErrorAlarmCondition {
            return new model.ErrorAlarmCondition({
                errorAlarmCheckID: '',
                displayMessage: '',
                alCheckTargetCondition: getDefaultAlCheckTargetCondition(),
                workTypeCondition: getDefaultWorkTypeCondition(),
                workTimeCondition: getDefaultWorkTimeCondition(),
                atdItemCondition: getDefaultAttendanceItemCondition(),
                continuousPeriod: 0
            });
        }
        
        export function getDefaultExtraResultMonthly(data : any):model.ExtraResultMonthly{
            
            
            let extraResultMonthly = new model.ExtraResultMonthly({
                errorAlarmCheckID: data.errorAlarmCheckID || '',
                sortBy : data.sortBy || 0,
                nameAlarmExtraCon : data.nameAlarmExtraCon || '',
                useAtr : data.useAtr ||  false,
                typeCheckItem : data.typeCheckItem ||0,
                messageBold: data.messageBold || false,
                messageColor: data.messageColor || '',
                displayMessage : data.displayMessage || '',
                rowId: data.rowId ||  0,
                checkConMonthly : nts.uk.util.isNullOrUndefined(data.checkConMonthly) ? getDefaultAttendanceItemCondition() : data.checkConMonthly,
                specHolidayCheckCon :nts.uk.util.isNullOrUndefined(data.specHolidayCheckCon) ? getDefaultSpecHolidayCheckCon():data.specHolidayCheckCon,
                checkRemainNumberMon :nts.uk.util.isNullOrUndefined(data.checkRemainNumberMon) ? getDefaultCheckRemainNumberMon(): data.checkRemainNumberMon,
                agreementCheckCon36 :nts.uk.util.isNullOrUndefined(data.agreementCheckCon36) ? getDefaultAgreementCheckCon36():data.agreementCheckCon36
                
                
            });
            return extraResultMonthly;
        }
        
        export function getDefaultSpecHolidayCheckCon(): model.SpecHolidayCheckCon {
            return new model.SpecHolidayCheckCon({
                errorAlarmCheckID: '',
                compareOperator: 0,
                numberDayDiffHoliday1: 0,
                numberDayDiffHoliday2: null
            });
        }
        
        export function getDefaultAgreementCheckCon36(): model.AgreementCheckCon36 {
            return new model.AgreementCheckCon36({
                errorAlarmCheckID: '',
                classification: 0,
                compareOperator: 0,
                eralBeforeTime: 0
            });
        }
        
        export function getDefaultCheckRemainNumberMon(): model.CheckRemainNumberMon {
            return new model.CheckRemainNumberMon({
                errorAlarmCheckID: '',
                checkVacation: 0,
                checkOperatorType: 0,
                compareRangeEx: getDefaultCompareRangeImport(),
                compareSingleValueEx : getDefaultCompareSingleValueImport(),
                listItemID: []
            });
        }
        
        
        export function getDefaultCompareRangeImport(): model.CompareRangeImport {
            return new model.CompareRangeImport({
                compareOperator : 0,
                startValue: getDefaultCheckConValueRemainNumberImport(),
                endValue: getDefaultCheckConValueRemainNumberImport()
            });
        }
        
        export function getDefaultCompareSingleValueImport(): model.CompareSingleValueImport {
            return new model.CompareSingleValueImport({
                compareOperator : 0,
                value: getDefaultCheckConValueRemainNumberImport()
            });
        }
        
         export function getDefaultCheckConValueRemainNumberImport(): model.CheckConValueRemainNumberImport {
            return new model.CheckConValueRemainNumberImport({
                daysValue : '',
                timeValue: ''
            });
        }
        //MinhVV MulMonCheckCondSet
        export function getDefaultMulMonCheckCondSet(typeCheckItem: number): model.MulMonCheckCondSet {
            let mulMonCheckCondSet = new model.MulMonCheckCondSet({
                errorAlarmCheckID: '',
                nameAlarmMulMon : '',
                useAtr : false,
                typeCheckItem: typeCheckItem|| 0,
                messageBold: false,
                messageColor: '',
                displayMessage : '',
                erAlAtdItem: null,
                continuousMonths : 0,
                times : 0,
                compareOperator: 0, 
                rowId : 0
            });
            return mulMonCheckCondSet;
        }
        
        export function convertTransferDataToMulMonCheckCondSet(mulMonCheckCondSet): model.MulMonCheckCondSet {
            let convertMulMonCheckCondSet = new model.MulMonCheckCondSet(mulMonCheckCondSet);
            //ErAlAtdItemCondition
            convertMulMonCheckCondSet.erAlAtdItem(new model.ErAlAtdItemCondition(0, convertMulMonCheckCondSet.erAlAtdItem()));
            return convertMulMonCheckCondSet;
        }


        export function convertArrayOfMulMonCheckCondSetToJS(dataJS: any, mulMonCheckCondSet: model.MulMonCheckCondSet): any {
            let erAlAtdItemMulMon = mulMonCheckCondSet.erAlAtdItem();
            dataJS.erAlAtdItem = ko.toJS(erAlAtdItemMulMon);
            dataJS.erAlAtdItem.countableAddAtdItems = _.values(erAlAtdItemMulMon.countableAddAtdItems());
            dataJS.erAlAtdItem.countableSubAtdItems = _.values(erAlAtdItemMulMon.countableSubAtdItems());
            return dataJS;
        }
        //MinhVV End


        /**
        * initial default value for WorkRecordExtractingCondition Object
        * @param checkItem
        */
        export function getDefaultWorkRecordExtractingCondition(checkItem: number): model.WorkRecordExtractingCondition {
            let workRecordExtractingCondition = new model.WorkRecordExtractingCondition({
                errorAlarmCheckID: '',
                checkItem: checkItem || 0,
                messageBold: false,
                messageColor: '',
                sortOrderBy: 0,
                useAtr: false,
                nameWKRecord: '',
                errorAlarmCondition: getDefaultErrorAlarmCondition(),
                rowId: 0
            });
            return workRecordExtractingCondition;
        }
        //monthly
        export function convertTransferDataToExtraResultMonthly(vmodel: model.ExtraResultMonthly, sortBy: number): any {
            let convertExtraResultMonthly = {};
            convertExtraResultMonthly["errorAlarmCheckID"] = vmodel.errorAlarmCheckID();
            convertExtraResultMonthly["sortBy"] = sortBy;
            convertExtraResultMonthly["nameAlarmExtraCon"] = vmodel.nameAlarmExtraCon();
            convertExtraResultMonthly["useAtr"] = vmodel.useAtr();
            convertExtraResultMonthly["typeCheckItem"] = vmodel.typeCheckItem();
            convertExtraResultMonthly["messageBold"] = vmodel.messageBold();
            convertExtraResultMonthly["messageColor"] = vmodel.messageColor();
            convertExtraResultMonthly["displayMessage"] = vmodel.displayMessage();
            _.forEach(vmodel.currentConditions(), (con: model.ExtractCondition) => {
                if(con.typeCheckItem() === 0){
                    convertExtraResultMonthly["specHolidayCheckCon"] = {
                        errorAlarmCheckID: vmodel.errorAlarmCheckID(),
                        compareOperator: con.extractType(),
                        numberDayDiffHoliday1: con.inputs()[0].value(),
                        numberDayDiffHoliday2: con.inputs()[1].value()
                    };
                    convertExtraResultMonthly["agreementCheckCon36"] = null;
                    convertExtraResultMonthly["checkConMonthly"] = null;
                    convertExtraResultMonthly["checkRemainNumberMon"] = null;
                } else if (con.typeCheckItem() === 3){
                    let compareRangeExValue = {};
                    let compareSingleValueExValue = {};
                    if(con.extractType()>5){
                        compareRangeExValue["compareOperator"] = con.extractType();
                        compareRangeExValue["startValue"] = mapCheckConValueRemain(con.inputs()[0].value(),con.inputs()[1].value());
                        compareRangeExValue["endValue"] = mapCheckConValueRemain(con.inputs()[2].value(),con.inputs()[3].value());
                        compareSingleValueExValue = null;
                    }else{
                        compareSingleValueExValue["compareOperator"] = con.extractType();
                        compareSingleValueExValue["value"] = mapCheckConValueRemain(con.inputs()[0].value(),con.inputs()[1].value());
                        compareRangeExValue = null;
                    }
                    let  listItemID = [];
                    listItemID.push(con.listItemID()[0] != undefined?con.listItemID()[0]:con.listItemID());
                    convertExtraResultMonthly["specHolidayCheckCon"] = null;
                    convertExtraResultMonthly["agreementCheckCon36"] = null;
                    convertExtraResultMonthly["checkConMonthly"] = null;
                    convertExtraResultMonthly["checkRemainNumberMon"] = {
                        errorAlarmCheckID: vmodel.errorAlarmCheckID(),
                        checkVacation: con.checkVacation(),
                        checkOperatorType: con.checkOperatorType(),
                        compareRangeEx: compareRangeExValue,
                        compareSingleValueEx: compareSingleValueExValue,
                        listItemID: listItemID
                    };                        
                } else if (con.typeCheckItem() === 1 || con.typeCheckItem() === 2){
                    convertExtraResultMonthly["agreementCheckCon36"] = {
                        errorAlarmCheckID: vmodel.errorAlarmCheckID(),
                        compareOperator: con.extractType(),
                        classification: con.classification(),
                        eralBeforeTime: con.eralBeforeTime()
                    };
                        
                    convertExtraResultMonthly["specHolidayCheckCon"] = null;
                    convertExtraResultMonthly["checkConMonthly"] = null;
                    convertExtraResultMonthly["checkRemainNumberMon"] = null;
                } else {
                    let checkConMonthly = {};
                    if (con.typeCheckItem() === 4||con.typeCheckItem() === 5 || con.typeCheckItem() === 6 || con.typeCheckItem() === 7){
                       if(typeof con.group1 === "function"){
                        con.group1().lstErAlAtdItemCon()[0].compareStartValue(
                            con.inputs()[0].value()
                            );
                        con.group1().lstErAlAtdItemCon()[0].compareEndValue(
                            con.inputs()[1].value()
                            );
                        con.group1().lstErAlAtdItemCon()[0].compareOperator(con.extractType());  
                       }else{
                        con.group1.lstErAlAtdItemCon()[0].compareStartValue(
                            con.inputs()[0].value()
                            );
                        con.group1.lstErAlAtdItemCon()[0].compareEndValue(
                            con.inputs()[1].value()
                            );
                        con.group1.lstErAlAtdItemCon()[0].compareOperator(con.extractType());
                       }
                      
                    }
                    
                    checkConMonthly["group1"] = mapGroup(ko.toJS(con.group1));
                    if (con.typeCheckItem() === 8){
                        checkConMonthly["operatorBetweenGroups"] = con.operator();
                        
                        checkConMonthly["group2"] = mapGroup(ko.toJS(con.group2));
                        checkConMonthly["group2UseAtr"] = con.group2UseAtr();
                    }else{
                        checkConMonthly["operatorBetweenGroups"] = 0;
                        checkConMonthly["group2"] = null;
                        checkConMonthly["group2UseAtr"] = false;    
                    } 
                    convertExtraResultMonthly["checkConMonthly"] = checkConMonthly;
                    convertExtraResultMonthly["specHolidayCheckCon"] = null;
                    convertExtraResultMonthly["agreementCheckCon36"] = null;
                    convertExtraResultMonthly["checkRemainNumberMon"] = null;
                }
            });
            
            return convertExtraResultMonthly;
        }
        
        function mapGroup(groupX: model.IErAlConditionsAttendanceItem): any{
            let group = {};
            group["atdItemConGroupId"] = groupX.atdItemConGroupId;
            group["conditionOperator"] = groupX.conditionOperator;
            group["lstErAlAtdItemCon"] = mapEralConAttd(groupX.lstErAlAtdItemCon);
            return group;
            
        }
        
        function mapEralConAttd(lstErAlAtdItemCon: Array<any>): any{
            let lstErAlAtdItemConNew = [];
            for(let i = 0;i<3;i++){
                let value = lstErAlAtdItemCon[i];
                if(_.isNil(value)){
                    value = getDefaultAttdItemGroup1Item()[0];                        
                }
                let erAlAtdItemCon = {};
                erAlAtdItemCon["targetNO"] = i;
                erAlAtdItemCon["conditionAtr"] = value.conditionAtr; 
                erAlAtdItemCon["useAtr"] = value.useAtr; 
                erAlAtdItemCon["uncountableAtdItem"] = value.uncountableAtdItem; 
                erAlAtdItemCon["countableAddAtdItems"] = value.countableAddAtdItems; 
                erAlAtdItemCon["countableSubAtdItems"] = value.countableSubAtdItems; 
                erAlAtdItemCon["conditionType"] = value.conditionType; 
                erAlAtdItemCon["compareOperator"] = value.compareOperator; 
                erAlAtdItemCon["singleAtdItem"] = value.singleAtdItem; 
                erAlAtdItemCon["compareStartValue"] = value.compareStartValue; 
                erAlAtdItemCon["compareEndValue"] = value.compareEndValue; 
                
                lstErAlAtdItemConNew.push(erAlAtdItemCon);
            }
            return lstErAlAtdItemConNew;
            
        }
        
        
        function mapCheckConValueRemain( daysValue : number,timeValue : number) : any{
            let checkConValueRemainValue = {}; 
            checkConValueRemainValue["daysValue"] = daysValue;
            checkConValueRemainValue["timeValue"] = timeValue ;
            
            return checkConValueRemainValue;
        }

        export function convertTransferDataToWorkRecordExtractingCondition(
            workRecordExtractingCondition): model.WorkRecordExtractingCondition {
            let convertWorkRecordExtractingCondition = new model.WorkRecordExtractingCondition(workRecordExtractingCondition);

            convertWorkRecordExtractingCondition.errorAlarmCondition(new model.ErrorAlarmCondition(workRecordExtractingCondition.errorAlarmCondition));
            convertWorkRecordExtractingCondition.errorAlarmCondition()
                .alCheckTargetCondition(new model.AlCheckTargetCondition(workRecordExtractingCondition.errorAlarmCondition.alCheckTargetCondition));

            convertWorkRecordExtractingCondition.errorAlarmCondition()
                .workTypeCondition(new model.WorkTypeCondition(workRecordExtractingCondition.errorAlarmCondition.workTypeCondition));
            convertWorkRecordExtractingCondition.errorAlarmCondition()
                .workTimeCondition(new model.WorkTimeCondition(workRecordExtractingCondition.errorAlarmCondition.workTimeCondition));
            convertWorkRecordExtractingCondition.errorAlarmCondition()
                .atdItemCondition(new model.AttendanceItemCondition(workRecordExtractingCondition.errorAlarmCondition.atdItemCondition));
            //group 1
            convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition()
                .group1(new model.ErAlConditionsAttendanceItem(workRecordExtractingCondition.errorAlarmCondition.atdItemCondition.group1));
            //ErAlAtdItemCondition
            let lstErAlAtdItemCon1 = workRecordExtractingCondition.errorAlarmCondition.atdItemCondition.group1.lstErAlAtdItemCon;
            convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon = ko.observableArray([]);
            if (lstErAlAtdItemCon1) {
                for (var i = 0; i < lstErAlAtdItemCon1.length; i++) {
                    var erAlAtdItemCondition1 = new model.ErAlAtdItemCondition(lstErAlAtdItemCon1[i].targetNO, lstErAlAtdItemCon1[i]);
                    convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon().push(erAlAtdItemCondition1);
                }


            }

            //group 2
            convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition()
                .group2(new model.ErAlConditionsAttendanceItem(workRecordExtractingCondition.errorAlarmCondition.atdItemCondition.group2));
            //ErAlAtdItemCondition
            let lstErAlAtdItemCon2 = workRecordExtractingCondition.errorAlarmCondition.atdItemCondition.group2.lstErAlAtdItemCon;
            convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group2().lstErAlAtdItemCon = ko.observableArray([]);

            if (lstErAlAtdItemCon2) {
                for (var i = 0; i < lstErAlAtdItemCon2.length; i++) {
                    var erAlAtdItemCondition2 = new model.ErAlAtdItemCondition(lstErAlAtdItemCon2[i].targetNO, lstErAlAtdItemCon2[i]);
                    convertWorkRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group2().lstErAlAtdItemCon().push(erAlAtdItemCondition2);
                }
            }
            return convertWorkRecordExtractingCondition;
        }
        //monthly
         export function convertArrayOfExtraResultMonthlyToJS(dataJS: any,
            extraResultMonthly :model.ExtraResultMonthly):any {
             dataJS.checkConMonthly = convertArrayOfAttendanceItemCondition(dataJS.checkConMonthly, extraResultMonthly.checkConMonthly());
             return dataJS;
         }

        export function convertArrayOfWorkRecordExtractingConditionToJS(dataJS: any,
            workRecordExtractingCondition: model.WorkRecordExtractingCondition): any {
            let errorAlarmCondition = workRecordExtractingCondition.errorAlarmCondition();
            dataJS.errorAlarmCondition.workTypeCondition.planLstWorkType = _.values(errorAlarmCondition.workTypeCondition().planLstWorkType() || []);
            dataJS.errorAlarmCondition.workTypeCondition.actualLstWorkType = _.values(errorAlarmCondition.workTypeCondition().actualLstWorkType || []);

            dataJS.errorAlarmCondition.workTimeCondition.planLstWorkTime = _.values(errorAlarmCondition.workTimeCondition().planLstWorkTime() || []);
            dataJS.errorAlarmCondition.workTimeCondition.actualLstWorkTime = _.values(errorAlarmCondition.workTimeCondition().actualLstWorkTime || []);

            dataJS.errorAlarmCondition.atdItemCondition =
                convertArrayOfAttendanceItemCondition(dataJS.errorAlarmCondition.atdItemCondition, errorAlarmCondition.atdItemCondition());
            return dataJS;
        }
        /**
         * Covert all data array to array data of JSon
         */
        export function convertArrayOfAttendanceItemCondition(dataAttItemJS, attItemCondition: model.AttendanceItemCondition): any {
            let lstErAlAtdItemCon1 = attItemCondition.group1().lstErAlAtdItemCon();
            dataAttItemJS.group1.lstErAlAtdItemCon = _.values(lstErAlAtdItemCon1);
            if (lstErAlAtdItemCon1) {
                for (var i = 0; i < lstErAlAtdItemCon1.length; i++) {
                    dataAttItemJS.group1.lstErAlAtdItemCon[i] = ko.toJS(lstErAlAtdItemCon1[i])
                    dataAttItemJS.group1.lstErAlAtdItemCon[i].countableAddAtdItems = _.values(lstErAlAtdItemCon1[i].countableAddAtdItems());
                    dataAttItemJS.group1.lstErAlAtdItemCon[i].countableSubAtdItems = _.values(lstErAlAtdItemCon1[i].countableSubAtdItems());
                }
            }

            let lstErAlAtdItemCon2 = attItemCondition.group2().lstErAlAtdItemCon();
            dataAttItemJS.group2.lstErAlAtdItemCon = _.values(lstErAlAtdItemCon2);

            if (lstErAlAtdItemCon2) {
                for (var i = 0; i < lstErAlAtdItemCon2.length; i++) {
                    dataAttItemJS.group2.lstErAlAtdItemCon[i] = ko.toJS(lstErAlAtdItemCon2[i])
                    dataAttItemJS.group2.lstErAlAtdItemCon[i].countableAddAtdItems = _.values(lstErAlAtdItemCon2[i].countableAddAtdItems());
                    dataAttItemJS.group2.lstErAlAtdItemCon[i].countableSubAtdItems = _.values(lstErAlAtdItemCon2[i].countableSubAtdItems());
                }
            }
            return dataAttItemJS;

        }
        
        export function convertArrayOfAttdItemConCommon(dataAttItemJS, attItemCondition: model.AttdItemConCommon): any {
            let lstErAlAtdItemCon1 = attItemCondition.group1().lstErAlAtdItemCon();
            dataAttItemJS.group1.lstErAlAtdItemCon = _.values(lstErAlAtdItemCon1);
            if (lstErAlAtdItemCon1) {
                for (var i = 0; i < lstErAlAtdItemCon1.length; i++) {
                    dataAttItemJS.group1.lstErAlAtdItemCon[i] = ko.toJS(lstErAlAtdItemCon1[i])
                    dataAttItemJS.group1.lstErAlAtdItemCon[i].countableAddAtdItems = _.values(lstErAlAtdItemCon1[i].countableAddAtdItems());
                    dataAttItemJS.group1.lstErAlAtdItemCon[i].countableSubAtdItems = _.values(lstErAlAtdItemCon1[i].countableSubAtdItems());
                }
            }

            let lstErAlAtdItemCon2 = attItemCondition.group2().lstErAlAtdItemCon();
            dataAttItemJS.group2.lstErAlAtdItemCon = _.values(lstErAlAtdItemCon2);

            if (lstErAlAtdItemCon2) {
                for (var i = 0; i < lstErAlAtdItemCon2.length; i++) {
                    dataAttItemJS.group2.lstErAlAtdItemCon[i] = ko.toJS(lstErAlAtdItemCon2[i])
                    dataAttItemJS.group2.lstErAlAtdItemCon[i].countableAddAtdItems = _.values(lstErAlAtdItemCon2[i].countableAddAtdItems());
                    dataAttItemJS.group2.lstErAlAtdItemCon[i].countableSubAtdItems = _.values(lstErAlAtdItemCon2[i].countableSubAtdItems());
                }
            }
            return dataAttItemJS;

        }

        export function getDefaultAgreementHour(): model.AgreeCondOt {
            let agreeCondOt = new model.AgreeCondOt({
                category: 0,
                code: '',
                id: '',
                no: 0,
                ot36: 0,
                excessNum: 0,
                messageDisp: '',
            });
            return agreeCondOt;
        }
    }
}