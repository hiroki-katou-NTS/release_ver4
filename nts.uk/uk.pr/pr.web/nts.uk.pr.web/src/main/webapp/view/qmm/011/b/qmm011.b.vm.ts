module nts.uk.pr.view.qmm011.b.viewmodel {
    import getText = nts.uk.resource.getText;
    import dialog = nts.uk.ui.dialog;
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import model = qmm011.share.model;
    import error = nts.uk.ui.errors;
    import modal = nts.uk.ui.windows.sub.modal;
    import service = nts.uk.pr.view.qmm011.b.service;
    export class ScreenModel {

        listPerFracClass: KnockoutObservableArray<model.ItemModel> = ko.observableArray(getListPerFracClass());
        listEmpInsHis: KnockoutObservableArray<IEmplInsurHis> = ko.observableArray([]);
        listEmpInsurPreRate: KnockoutObservableArray<EmpInsurPreRate> = ko.observableArray([]);
        selectedEmpInsHis: KnockoutObservable<IEmplInsurHis> = ko.observable();
        hisId: KnockoutObservable<string> = ko.observable('');
        index: KnockoutObservable<number> = ko.observable(0);
        selectedEmpInsHisId: KnockoutObservable<string> = ko.observable('');
        monthlyCalendar: KnockoutObservable<string> = ko.observable('');
        startYearMonth: KnockoutObservable<number> = ko.observable();
        endYearMonth: KnockoutObservable<number> = ko.observable();
        isNewMode: KnockoutObservable<number> = ko.observable(2);
        transferMethod: KnockoutObservable<number> = ko.observable();
        constructor() {
            let self = this;
            self.initScreen(null);
            self.selectedEmpInsHisId.subscribe((data) => {
                let self = this;
                if (data == HIS_ID_TEMP) {
                    self.isNewMode(MODE.NEW);
                }
                self.selectedEmpInsHis(self.listEmpInsHis()[self.getIndex(data)]);
                self.setEmplInsurHis(self.selectedEmpInsHis());
                self.getEmpInsurPreRate();
            });
        }


        // 初期データ取得処理
        initScreen(hisId: string) {
            let self = this;
            block.invisible();
            service.getEmpInsHis().done((listEmpInsHis: Array<IEmplInsurHis>) => {
                if (listEmpInsHis && listEmpInsHis.length > 0) {
                    self.listEmpInsHis(EmplInsurHis.convertToDisplayHis(listEmpInsHis));
                    self.index(0);
                    if (hisId != null) {
                        self.index(self.getIndex(hisId));
                    }
                    self.selectedEmpInsHisId(self.listEmpInsHis()[self.index()].hisId);
                    self.selectedEmpInsHis(self.listEmpInsHis()[self.index()]);
                    self.setEmplInsurHis(self.selectedEmpInsHis());
                    
                } else {
                    self.listEmpInsurPreRate(self.addEmpInsurPreRate());
                }
            }).always(() => {
                block.clear();
            });
        }
        
        getEmpInsurPreRate() {
            let self = this;
            let hisId :string = self.selectedEmpInsHisId();
            if (self.transferMethod() == TRANSFER_MOTHOD.TRANSFER && self.listEmpInsHis().length > 1 && self.isNewMode == MODE.NEW) {
                hisId = self.listEmpInsHis()[1].hisId;
            }
            service.getEmpInsurPreRate(hisId).done((listEmpInsurPreRate: Array<IEmpInsurPreRate>) => {
                if (listEmpInsurPreRate && listEmpInsurPreRate.length > 0) {
                    self.listEmpInsurPreRate(EmpInsurPreRate.fromApp(listEmpInsurPreRate));
                    self.isNewMode(MODE.UPDATE);
                    if (self.selectedEmpInsHisId() == HIS_ID_TEMP) {
                        self.isNewMode(MODE.NEW);
                    }
                } else {
                    self.listEmpInsurPreRate(self.addEmpInsurPreRate());
                }
             });
        }
        
        addEmpInsurPreRate(){
            let self = this;
            let listEmpInsurPreRate: Array<EmpInsurPreRate> = [];
            let empInsurPreRate: EmpInsurPreRate;
            for(let i = 0; i< 3; i++) {
                empInsurPreRate = new EmpInsurPreRate();
                empInsurPreRate.hisId = self.hisId();
                empInsurPreRate.empPreRateId = i;
                empInsurPreRate.indBdRatio = ko.observable();
                empInsurPreRate.empContrRatio = ko.observable();
                empInsurPreRate.perFracClass = ko.observable(1);
                empInsurPreRate.busiOwFracClass = ko.observable(1);
                listEmpInsurPreRate.push(empInsurPreRate);
            }
            return listEmpInsurPreRate;
        }
        
        convertToCommand(dto :Array<EmpInsurPreRate>){
            let listEmpInsurPreRate :Array <EmpInsurPreRate> = [];
            _.each(dto, function(item: EmpInsurPreRate) {
                let temp = new EmpInsurPreRate();
                temp.hisId = item.hisId;
                temp.empPreRateId = item.empPreRateId;
                temp.indBdRatio = Number(item.indBdRatio());
                temp.empContrRatio = Number(item.empContrRatio());
                temp.perFracClass = item.perFracClass();
                temp.busiOwFracClass = item.busiOwFracClass();
                listEmpInsurPreRate.push(temp);
            })
            return listEmpInsurPreRate;
        }

        getIndex(hisId: string) {
            let self = this;
            let temp = _.findIndex(self.listEmpInsHis(), function(x) {
                return x.hisId == hisId;
            });
            if (temp) {
                return temp;
            }
            return 0;
        }

        openEscreen() {
            let self = this;
            setShared('QMM011_E_PARAMS_INPUT', {
                startYearMonth: self.startYearMonth()
            });

            modal("/view/qmm/011/e/index.xhtml").onClosed(() =>{
                let params = getShared('QMM011_E_PARAMS_OUTPUT');
                if (params) {
                    self.isNewMode(MODE.NEW);
                    if (params) {
                        self.startYearMonth(params.startYearMonth);
                        self.isNewMode(MODE.NEW);
                        self.transferMethod(params.transferMethod);
                        self.listEmpInsHis(self.addEmplInsurHis(self.startYearMonth(), self.listEmpInsHis()));
                        self.selectedEmpInsHisId(self.listEmpInsHis()[FIRST].hisId);
                    }
                }
            });
        }

        register() {
            let self = this;
            if (self.isNewMode() == MODE.NO) {
                return;
            }
            let isNewMode = self.isNewMode() == MODE.NEW;
            if(self.validate()){
                return;
            }
            let data: any = {
                listEmpInsurPreRate: self.convertToCommand(self.listEmpInsurPreRate()),
                isNewMode: isNewMode,
                hisId: self.hisId(),
                startYearMonth: self.convertStringToYearMonth(self.startYearMonth()),
                endYearMonth:  self.convertStringToYearMonth(self.endYearMonth())
            }
            block.invisible();
            service.register(data).done(() => {
                dialog.info({ messageId: "Msg_15" }).then(() => {
                    self.isNewMode(MODE.UPDATE);
                    self.transferMethod(null);
                    self.initScreen(null);
                });
            }).fail(function(res: any) {
                if (res)
                    dialog.alertError(res);
            }).always(() => {
                block.clear();
            });
        }
        
        addEmplInsurHis(start: number, list: Array<EmplInsurHis>){
            let listEmpInsHis: Array<EmplInsurHis> = []; 
            let self = this;
            let emplInsurHis = new EmplInsurHis();
            emplInsurHis.hisId = '00000';
            emplInsurHis.startYearMonth = start.toString();
            emplInsurHis.endYearMonth = '999912';
            emplInsurHis.display = self.convertMonthYearToString(emplInsurHis.startYearMonth) + " ~ " + self.convertMonthYearToString(emplInsurHis.endYearMonth);
            if (list && list.length > 0) {
                list[FIRST].display = self.convertMonthYearToString(list[FIRST].startYearMonth) + " ~ " + self.convertMonthYearToString((start - 1).toString());
            }
            listEmpInsHis.push(emplInsurHis);
            _.each(list, (item) => {
                listEmpInsHis.push(item);
            })
            return listEmpInsHis;
            
        }
        
        setEmplInsurHis(emplInsurHis: EmplInsurHis) {
            let self = this;
            let year, month: number;
            self.hisId(emplInsurHis.hisId);
            self.startYearMonth(self.convertMonthYearToString(emplInsurHis.startYearMonth));
            self.endYearMonth(self.convertMonthYearToString(emplInsurHis.endYearMonth));
            year = self.startYearMonth().slice(0, 4);
            month = self.startYearMonth().slice(5, 7);
            self.monthlyCalendar( "(" + nts.uk.time.yearInJapanEmpire(year).toString() + month +"月)" );
        }
        
        validate(){
            $(".B3_7").trigger("validate");
            $(".B3_11").trigger("validate");
            return error.hasError();
        }
        
        enableRegis(){
            return this.isNewMode() == MODE.NO;
        }
        
        enableNew(){
            return this.isNewMode() == MODE.NEW;
        }
        
        enableUpdate(){
            return this.isNewMode() == MODE.UPDATE;
        }
        
        openFscreen() {
            let self = this;
            let laststartYearMonth: number = 0;
            if (self.listEmpInsHis() && self.listEmpInsHis().length != self.index() + 1) {
                laststartYearMonth = self.listEmpInsHis().length > 1 ? self.listEmpInsHis()[self.index() + 1].startYearMonth : 0;
            }
            let canDelete: boolean = false;
            if (self.listEmpInsHis().length > 2 && self.hisId() == self.listEmpInsHis()[FIRST].hisId) {
                canDelete = true;
            }
            
            setShared('QMM011_F_PARAMS_INPUT', {
                startYearMonth: self.convertStringToYearMonth(self.startYearMonth()),
                endYearMonth: self.convertStringToYearMonth(self.endYearMonth()),
                insurrance: INSURRANCE.EMPLOYMENT_INSURRANCE_RATE,
                hisId: self.hisId(),
                laststartYearMonth: laststartYearMonth,
                canDelete: canDelete
            });
            modal("/view/qmm/011/f/index.xhtml").onClosed(function() {
                let params = getShared('QMM011_F_PARAMS_OUTPUT');
                if(params && params.methodEditing == 1) {
                    self.initScreen(self.selectedEmpInsHisId());
                }
                if(params && params.methodEditing == 0) {
                    self.initScreen(null);
                }
                
            });
        }
        
        convertMonthYearToString(yearMonth: any) {
            let self = this;
            let year: string, month: string;
            yearMonth = yearMonth.toString();
            year = yearMonth.slice(0, 4);
            month = yearMonth.slice(4, 6);
            return year + "/" + month;
        }
        
        convertStringToYearMonth(yearMonth: any){
            let self = this;
            let year: string, month: string;
            yearMonth = yearMonth.slice(0, 4) + yearMonth.slice(5, 7);
            return yearMonth;
        }
    }
    

    class IEmplInsurHis {
        hisId: string
        startYearMonth: string;
        endYearMonth: string;
        between: string;
    }

    class EmplInsurHis {
        hisId: string
        startYearMonth: string;
        endYearMonth: string;
        display: string;
        constructor() {
            
        }
        
        static convertMonthYearToString(yearMonth: any) {
            let self = this;
            let year: string, month: string;
            yearMonth = yearMonth.toString();
            year = yearMonth.slice(0, 4);
            month = yearMonth.slice(4, 6);
            return year + "/" + month;
        }
        static convertToDisplayHis(app) {
            let listEmp = [];
            _.each(app, (item) => {
                let dto: EmplInsurHis = new EmplInsurHis();
                dto.hisId = item.hisId;
                dto.startYearMonth = item.startYearMonth;
                dto.endYearMonth = item.endYearMonth;
                dto.display = this.convertMonthYearToString(item.startYearMonth) + " ~ " + this.convertMonthYearToString(item.endYearMonth);
                listEmp.push(dto);
            })
            return listEmp;
        }

    }

    class IEmpInsurPreRate {
        hisId: string;
        empPreRateId: number;
        indBdRatio: number;
        empContrRatio: number;
        perFracClass: number;
        busiOwFracClass: number;
    }

    class EmpInsurPreRate {
        hisId: string;
        empPreRateId: number;
        indBdRatio: number;
        empContrRatio: number;
        perFracClass: KnockoutObservable<number>;
        busiOwFracClass: number;
        constructor() {
        }

        static fromApp(app) {
            let listEmp = [];
            _.each(app, (item) => {
                let dto: EmpInsurPreRate = new EmpInsurPreRate();
                dto.hisId = item.hisId;
                dto.empPreRateId = item.empPreRateId;
                dto.indBdRatio = ko.observable(item.indBdRatio);
                dto.empContrRatio = ko.observable(item.empContrRatio);
                dto.perFracClass = ko.observable(item.perFracClass);
                dto.busiOwFracClass = ko.observable(item.busiOwFracClass);
                listEmp.push(dto);
            })
            return listEmp;
        }
    }
    
    export function getListPerFracClass(): Array<model.ItemModel> {
        return [
            new model.ItemModel('0', getText('CMF002_358')),
            new model.ItemModel('1', getText('CMF002_359')),
            new model.ItemModel('2', getText('CMF002_360')),
            new model.ItemModel('3', getText('CMF002_361')),
            new model.ItemModel('4', getText('CMF002_362'))
        ];
    }
    
    export enum INSURRANCE {
        EMPLOYMENT_INSURRANCE_RATE = 1,
        ACCIDENT_INSURRANCE_RATE = 0
    }
    
    export enum TRANSFER_MOTHOD {
        CREATE_NEW = 1,
        TRANSFER = 0
    }
    
    export enum MODE {
        NEW = 0,
        UPDATE = 1,
        NO = 2
    }
    
    export const HIS_ID_TEMP = "00000"

    export const FIRST = 0;
}