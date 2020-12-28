module nts.uk.at.view.kml001.shr {
    export module vmbase {
        export class GridPersonCostCalculation {
            dateRange: string;  
            companyId?: string;
            historyId?: string;
            startDate?: string;
            endDate?: string;
            constructor(dateRange: string, companyId?: string, historyId?: string, startDate?: string, endDate?: string) {
                var self = this;
                self.dateRange = dateRange; //display on the list
                self.companyId = companyId;
                self.historyId = historyId;                
                self.startDate = startDate;
                self.endDate   = endDate;
            }  
        }
    
        export interface PersonCostCalculationInterface {
            companyID: string;
            historyID: string;
            startDate: string;
            endDate: string;
            unitPrice: number;
            memo: string;
            premiumSets: Array<PremiumSettingInterface>;
            calculationSetting: number;
            roundingUnitPrice: number;
            roundingAmount: number;
            inUnits: number;
            workingHour: number;
        }
    
        export class PersonCostCalculation {
            companyID: KnockoutObservable<string>;
            historyID: KnockoutObservable<string>;
            startDate: KnockoutObservable<string>;
            endDate: KnockoutObservable<string>;
            unitPrice: KnockoutObservable<number> =ko.observable(0);
            memo: KnockoutObservable<string>;
            premiumSets : KnockoutObservableArray<PremiumSetting>;
            //ver9
            calculationSetting: KnockoutObservable<number> =ko.observable(1);
            roundingUnitPrice: KnockoutObservable<number>;
            roundingAmount: KnockoutObservable<number>;
            inUnits: KnockoutObservable<number>;
            workingHour: KnockoutObservable<number>;
            constructor(
                companyID: string, historyID: string, 
                startDate: string, endDate: string, 
                unitPrice: number, memo: string, 
                premiumSets: Array<PremiumSettingInterface>,
                calculationSetting: number = 1, roundingUnitPrice: number = 0, 
                roundingAmount: number = 1, inUnits: number = 0, workingHour: number
                ) {
                var self = this;
                self.companyID = ko.observable(companyID);
                self.historyID = ko.observable(historyID);
                self.startDate = ko.observable(startDate);
                self.endDate = ko.observable(endDate);
                self.unitPrice = ko.observable(unitPrice);
                self.memo = ko.observable(memo);
                self.premiumSets = ko.observableArray(_.map(premiumSets, premiumSet => vmbase.ProcessHandler.fromObjectPremiumSet(premiumSet)));
                //ver9
                self.calculationSetting = ko.observable(calculationSetting);
                self.roundingUnitPrice = ko.observable(roundingUnitPrice);
                self.roundingAmount = ko.observable(roundingAmount);
                self.inUnits = ko.observable(inUnits);
                self.workingHour = ko.observable(workingHour);
            }


            public updateData(data: PersonCostCalculationInterface) {
                var self = this;
                self.companyID(data.companyID);
                self.historyID(data.historyID);
                self.startDate(data.startDate);
                self.endDate(data.endDate);
                self.unitPrice(data.unitPrice);
                self.memo(data.memo);
                self.premiumSets(_.map(data.premiumSets, premiumSet => vmbase.ProcessHandler.fromObjectPremiumSet(premiumSet)));
                //ver9
                self.calculationSetting(data.calculationSetting);
                self.roundingUnitPrice(data.roundingUnitPrice);
                self.roundingAmount(data.roundingAmount);
                self.inUnits(data.inUnits);
                self.workingHour(data.workingHour);
            }
        }
        
        export interface PremiumSettingInterface {
            companyID: string;
            historyID: string;
            displayNumber: number;
            rate: number;
            name: string;
            useAtr: number;
            attendanceItems: Array<AttendanceItem>;
            unitPrice: number;
        }
        
        export class PremiumSetting {
            companyID: KnockoutObservable<string>;
            historyID: KnockoutObservable<string>;
            displayNumber: KnockoutObservable<number>;
            rate: KnockoutObservable<number>;
            name: KnockoutObservable<string>;
            useAtr: KnockoutObservable<number>;
            attendanceItems: KnockoutObservableArray<AttendanceItem>;
            unitPrice: KnockoutObservable<number>;

            constructor(companyID: string, historyID: string, displayNumber: number, rate: number,
                name: string, useAtr: number, attendanceItems: Array<AttendanceItem>, unitPrice: number) {
                var self = this;
                self.companyID = ko.observable(companyID);
                self.historyID = ko.observable(historyID);
                self.displayNumber = ko.observable(displayNumber);
                self.rate = ko.observable(rate);
                self.name = ko.observable(name);
                self.useAtr = ko.observable(useAtr);
                let koAttendanceItems = [];
                attendanceItems.forEach(function(item){
                    koAttendanceItems.push(new vmbase.AttendanceItem(item.shortAttendanceID, item.name));
                });
                self.attendanceItems = ko.observableArray(koAttendanceItems);
                self.unitPrice = ko.observable(unitPrice);
            }
            
        }
        
        export class AttendanceItem {
            shortAttendanceID: number;
            name: string;
            constructor(shortAttendanceID: number, name: string) {
                var self = this;
                self.shortAttendanceID = shortAttendanceID;
                self.name = name;    
            }    
        }
        
        export class PremiumItem {
            companyID: KnockoutObservable<string>;
            displayNumber: KnockoutObservable<number>; 
            name: KnockoutObservable<string>;
            useAtr: KnockoutObservable<number>;
            isChange: KnockoutObservable<boolean>;
            unitPrice: KnockoutObservable<number>;
            constructor(companyID: string, displayNumber: number, name: string, useAtr: number, isChange: boolean, unitPrice: number) {
                var self = this;
                self.companyID = ko.observable(companyID);
                self.displayNumber = ko.observable(displayNumber);
                self.name = ko.observable(name);
                self.useAtr = ko.observable(useAtr);
                self.isChange = ko.observable(isChange);
                self.unitPrice = ko.observable(unitPrice);
            }
        }
        
        export class PremiumItemLanguage {
            companyID: KnockoutObservable<string>;
            displayNumber: KnockoutObservable<number>; 
            langID: KnockoutObservable<string>;
            name: KnockoutObservable<string>;
            constructor(companyID: string, displayNumber: number,langID : string, name: string) {
                var self = this;
                self.companyID = ko.observable(companyID);
                self.displayNumber = ko.observable(displayNumber);
                self.langID = ko.observable(langID);
                self.name = ko.observable(name);
            }
        }
        
        export class ProcessHandler {
            
            /**
             * convert PersonCostCalculation JS object to PersonCostCalculation knockoutJS object 
             */
            static fromObjectPerconCost(object: PersonCostCalculationInterface): PersonCostCalculation {
                return new PersonCostCalculation(
                    object.companyID, 
                    object.historyID, 
                    object.startDate, 
                    object.endDate,
                    object.unitPrice,
                    object.memo,
                    object.premiumSets,
                    object.calculationSetting,
                    object.roundingUnitPrice,
                    object.roundingAmount,
                    object.inUnits,
                    object.workingHour
                );
            }
            
            /**
             * convert PersonCostCalculation knockoutJS object to PersonCostCalculation JS object
             */
            static toObjectPersonCost(koObject: PersonCostCalculation): PersonCostCalculationInterface {
                let premiumSets: Array<PremiumSettingInterface> = [];
                koObject.premiumSets().forEach(function(koPremiumSet){premiumSets.push(ProcessHandler.toObjectPremiumSet(koPremiumSet));});
                return {
                    companyID: koObject.companyID(),
                    historyID: koObject.historyID(),
                    startDate: koObject.startDate(),
                    endDate: koObject.endDate(),
                    unitPrice: koObject.unitPrice(),
                    memo: koObject.memo(),
                    premiumSets : premiumSets,
                    calculationSetting: koObject.calculationSetting(),
                    roundingUnitPrice: koObject.roundingUnitPrice(),
                    roundingAmount: koObject.roundingAmount(),
                    inUnits: koObject.inUnits(),
                    workingHour: koObject.workingHour()
                };    
            }
            
            /**
             * convert PremiumSetting JS object to PremiumSetting knockoutJS object 
             */
            static fromObjectPremiumSet(object: PremiumSettingInterface): PremiumSetting {
                return new PremiumSetting(
                    object.companyID,
                    object.historyID,
                    object.displayNumber,
                    object.rate,
                    object.name,
                    object.useAtr,
                    object.attendanceItems,
                    object.unitPrice
                );
            }
            
            /**
             * convert PremiumSetting knockoutJS object to PremiumSetting JS object
             */
            static toObjectPremiumSet(koObject: PremiumSetting): PremiumSettingInterface {
                return {
                    companyID: koObject.companyID(),
                    historyID: koObject.historyID(),
                    displayNumber: koObject.displayNumber(), 
                    rate: koObject.rate(),
                    name: koObject.name(),
                    useAtr: koObject.useAtr(),
                    attendanceItems: _.map(koObject.attendanceItems() , function(item){ return {shortAttendanceID: item.shortAttendanceID, name: item.name}}),
                    unitPrice: koObject.unitPrice()
                };    
            }
            
            static createPersonCostCalFromValue(objectPersonCostCalculation: PersonCostCalculationInterface, premiumItems: Array<PremiumItem>): PersonCostCalculation {
                let personCostCalculation = new PersonCostCalculation("","","","",0,"",[], 1, 0, 0, 1, 0);
                var self = this;
                personCostCalculation.companyID(objectPersonCostCalculation.companyID);
                personCostCalculation.historyID(objectPersonCostCalculation.historyID);
                personCostCalculation.startDate(objectPersonCostCalculation.startDate);
                personCostCalculation.endDate(objectPersonCostCalculation.endDate);
                personCostCalculation.unitPrice(objectPersonCostCalculation.unitPrice);
                personCostCalculation.memo(objectPersonCostCalculation.memo);
                let koPremiumSets = [];
                premiumItems.forEach(function(premiumItem, index){
                    if(premiumItem.useAtr()){
                        let premiumSet = _.find(objectPersonCostCalculation.premiumSets, function(o) { 
                            return o.displayNumber == index+1; 
                        })
                        if(premiumSet) {
                            koPremiumSets.push(ProcessHandler.fromObjectPremiumSet(premiumSet));        
                        } else {
                            koPremiumSets.push(
                                new vmbase.PremiumSetting(
                                    "", "", premiumItem.displayNumber(), 
                                    100, premiumItem.name(), premiumItem.useAtr(), 
                                    [], premiumItem.unitPrice()
                                )
                            );    
                        }
                    }
                });
                personCostCalculation.premiumSets(koPremiumSets);
                return personCostCalculation;
            }
            
            /**
             * get one day before input date as string format
             */
            static getOneDayBefore(date: string) {
                return moment(date).add(-1,'days').format("YYYY/MM/DD");
            }
            
            /**
             * get one day after input date as string format
             */
            static getOneDayAfter(date: string) {
                return moment(date).add(1,'days').format("YYYY/MM/DD");
            }
            
            /**
             * check input date in range, if date in range return true
             */
            static validateDateRange(inputDate: string, startDate: string, endDate: string){
                return moment(inputDate).isBetween(moment(this.getOneDayBefore(startDate)), moment(this.getOneDayAfter(endDate)));
            }
            
            /**
             * check input date before or equal date
             */
            static validateDateInput(inputDate: string, date: string){
                return moment(inputDate).isSameOrAfter(moment(date));
            }
        }
    
        export enum UseAtr {
            NotUse = 0,
            Use = 1
        }
        
        export enum UnitPrice {
            Price_1 = 0,
            Price_2 = 1, 
            Price_3 = 2,
            Standard = 3,
            Contract = 4
        }
    
        export enum MSG {
            MSG015 = <any>"登録しました。",
            MSG018 = <any>"選択中のデータを削除しますか？",
            MSG065 = <any>"最新の履歴の有効開始日より以前の有効開始日を登録できません。",
            MSG066 = <any>"割増項目が設定されてません。",
            MSG102 = <any>"最新の履歴開始日以前に履歴を追加することはできません。",
            MSG128 = <any>"最後の履歴を削除することができません。"
        }
    }
}