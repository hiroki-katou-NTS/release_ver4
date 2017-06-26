module kml001.shr.vmbase {
    export class GridPersonCostCalculation {
        dateRange: string;  
        constructor(dateRange: string) {
            var self = this;
            self.dateRange = dateRange;
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
    }

    export class PersonCostCalculation {
        companyID: KnockoutObservable<string>;
        historyID: KnockoutObservable<string>;
        startDate: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;
        unitPrice: KnockoutObservable<number>;
        memo: KnockoutObservable<string>;
        premiumSets : KnockoutObservableArray<PremiumSetting>;
        constructor(companyID: string, historyID: string, startDate: string, endDate: string, unitPrice: number, memo: string, premiumSets: Array<PremiumSettingInterface>) {
            var self = this;
            self.companyID = ko.observable(companyID);
            self.historyID = ko.observable(historyID);
            self.startDate = ko.observable(startDate);
            self.endDate = ko.observable(endDate);
            self.unitPrice = ko.observable(unitPrice);
            self.memo = ko.observable(memo);
            let koPremiumSets = [];
            premiumSets.forEach(function(premiumSet){koPremiumSets.push(ProcessHandler.fromObjectPremiumSet(premiumSet));});
            self.premiumSets = ko.observableArray(koPremiumSets);
        }
    }
    
    export interface PremiumSettingInterface {
        companyID: string;
        historyID: string;
        premiumID: number;
        rate: number;
        attendanceID: number;
        name: string;
        displayNumber: number;
        useAtr: number;
        attendanceItems: Array<AttendanceItem>;
    }
    
    export class PremiumSetting {
        companyID: KnockoutObservable<string>;
        historyID: KnockoutObservable<string>;
        premiumID: KnockoutObservable<number>; 
        rate: KnockoutObservable<number>;
        attendanceID: KnockoutObservable<number>;
        name: KnockoutObservable<string>;
        displayNumber: KnockoutObservable<number>;
        useAtr: KnockoutObservable<number>;
        attendanceItems: KnockoutObservableArray<AttendanceItem>;
        constructor(companyID: string, historyID: string, premiumID: number, rate: number, attendanceID: number, 
            name: string, displayNumber: number, useAtr: number, attendanceItems: Array<AttendanceItem>) {
            var self = this;
            self.companyID = ko.observable(companyID);
            self.historyID = ko.observable(historyID);
            self.premiumID = ko.observable(premiumID);
            self.rate = ko.observable(rate);
            self.attendanceID = ko.observable(attendanceID);
            self.name = ko.observable(name);
            self.displayNumber = ko.observable(displayNumber);
            self.useAtr = ko.observable(useAtr);
            let koAttendanceItems = [];
            attendanceItems.forEach(function(item){
                koAttendanceItems.push(new vmbase.AttendanceItem(item.shortAttendanceID, item.name));
            });
            self.attendanceItems = ko.observableArray(koAttendanceItems);
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
        iD: KnockoutObservable<number>; 
        attendanceID: KnockoutObservable<number>;
        name: KnockoutObservable<string>;
        displayNumber: KnockoutObservable<number>;
        useAtr: KnockoutObservable<number>;
        constructor(companyID: string, iD: number, attendanceID: number, name: string, displayNumber: number, useAtr: number) {
            var self = this;
            self.companyID = ko.observable(companyID);
            self.iD = ko.observable(iD);
            self.attendanceID = ko.observable(attendanceID);
            self.name = ko.observable(name);
            self.displayNumber = ko.observable(displayNumber);
            self.useAtr = ko.observable(useAtr);
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
                object.premiumSets);
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
                premiumSets : premiumSets     
            };    
        }
        
        /**
         * convert PremiumSetting JS object to PremiumSetting knockoutJS object 
         */
        static fromObjectPremiumSet(object: PremiumSettingInterface): PremiumSetting {
            return new PremiumSetting(
                object.companyID,
                object.historyID,
                object.premiumID,
                object.rate,
                object.attendanceID,
                object.name,
                object.displayNumber,
                object.useAtr,
                object.attendanceItems);
        }
        
        /**
         * convert PremiumSetting knockoutJS object to PremiumSetting JS object
         */
        static toObjectPremiumSet(koObject: PremiumSetting): PremiumSettingInterface {
            return {
                companyID: koObject.companyID(),
                historyID: koObject.historyID(),
                premiumID: koObject.premiumID(), 
                rate: koObject.rate(),
                attendanceID: koObject.attendanceID(),
                name: koObject.name(),
                displayNumber: koObject.displayNumber(),
                useAtr: koObject.useAtr(),
                attendanceItems: _.map(koObject.attendanceItems() , function(item){ return {shortAttendanceID: item.shortAttendanceID, name: item.name}})   
            };    
        }
        
        /**
         * get one day before input date as string format
         */
        static getOneDayBefore(date: string) {
            let numberDate = Date.parse(date);
            // get before day by subtraction one day milliseconds
            let dayBefore = new Date(numberDate - 24 * 60 * 60 * 1000);
            return dayBefore.getFullYear() + '/' + (dayBefore.getMonth() + 1) + '/' + dayBefore.getDate();
        }
        
        /**
         * get one day after input date as string format
         */
        static getOneDayAfter(date: string) {
            let numberDate = Date.parse(date);
            // get after day by addition one day milliseconds
            let dayBefore = new Date(numberDate + 24 * 60 * 60 * 1000);
            return dayBefore.getFullYear() + '/' + (dayBefore.getMonth() + 1) + '/' + dayBefore.getDate();
        }
        
        /**
         * check input date in range, if date in range return true
         */
        static validateDateRange(inputDate: string, startDate: string, endDate: string){
            return (Date.parse(startDate) <= Date.parse(inputDate))&&(Date.parse(inputDate)<=Date.parse(endDate));    
        }
        
        /**
         * check input date before or equal date
         */
        static validateDateInput(inputDate: string, date: string){
            return (Date.parse(inputDate) <= Date.parse(date));
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