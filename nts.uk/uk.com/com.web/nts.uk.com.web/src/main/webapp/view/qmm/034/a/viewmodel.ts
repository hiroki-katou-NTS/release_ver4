module qmm034.a.viewmodel {
    export class ScreenModel {
        //chua du lieu cua gridlist
        items: KnockoutObservableArray<any>;
        // cau truc cot cua gridList era
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        // là era code hien thời đang được select trên grid list era
        currentCode: KnockoutObservable<any>;
        // dùng để lưu trữ đối tượng era hiện thời mà đang làm việc trên màn hình(dùng trong insert, update, delete)
        currentEra: KnockoutObservable<EraModel>;
        // là cờ để phân biệt xem đang insert hay đang update
        isUpdate: KnockoutObservable<boolean>;
        // là giá trị để binding với nts datePicker
        date: KnockoutObservable<any>;
        startDate: KnockoutObservable<string>;
        dateTime: KnockoutObservable<string>;
        // selected row
        isDeleteEnable: KnockoutObservable<boolean>;
        // binding with textEditor as a observable object
        isEnableCode: KnockoutObservable<boolean>;
        dirtyObject: nts.uk.ui.DirtyChecker;
        countStartDateChange: number = 1; //Biến này để tránh việc chạy hàm startDate.subscribe 2 lần
        previousCurrentCode: string = null; //lưu giá trị của currentCode trước khi nó bị thay đổi

        constructor() {
            let self = this;
            self.init();
            self.date = ko.observable("");
            self.startDate = ko.observable(moment().format("YYYY/MM/DD"));

            self.startDate.subscribe(function(dateChange) {
                if (self.countStartDateChange === 1) {
                    // event datePicker onchange
                    if ($('#txtStartDate').ntsError("hasError")) {
                        $("#txtStartDate").ntsError('clear');
                    }
                } else {
                    self.countStartDateChange = 1;
                }
                self.currentEra().startDate(dateChange);
                self.dateTime(nts.uk.time.yearInJapanEmpire(moment(dateChange, "YYYY/MM/DD").format()).toString());
            })
            self.currentCode.subscribe(function(codeChanged) {
                if (!nts.uk.text.isNullOrEmpty(codeChanged) && self.currentCode() !== self.previousCurrentCode) {
                    if (self.dirtyObject.isDirty()) {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。?").ifYes(function() {
                            self.processWhenCurrentCodeChange(codeChanged);
                        }).ifCancel(function() {
                            self.currentCode(self.previousCurrentCode);
                        })
                    } else {
                        self.processWhenCurrentCodeChange(codeChanged);    
                    }
                }
            });
            //convert to Japan Emprise year
            self.dateTime = ko.observable(nts.uk.time.yearInJapanEmpire(self.currentEra().startDate()).toString());
        }

        processWhenCurrentCodeChange(codeChanged: any) {
            let self = this;
            self.clearError();
            self.countStartDateChange += 1;
            self.currentEra(self.getEra(codeChanged));
            self.date(self.currentEra().startDate().toString());
            self.startDate(self.currentEra().startDate());
            self.isDeleteEnable(true);
            self.isEnableCode(false);
            self.isUpdate(true);
            qmm034.a.service.getFixAttribute(self.currentEra().eraHist()).done(function(data) {
                if (data === 0) {
                    self.isEnableCode(true);
                }
            });    
            if (self.dirtyObject !== undefined)
                self.dirtyObject.reset();
            self.previousCurrentCode = codeChanged;
        }
        
        init(): void {
            let self = this;
            self.items = ko.observableArray([]);
            self.columns = ko.observableArray([
                { headerText: 'KEY', key: 'eraHist', width: 50, hidden: true },
                { headerText: '元号', key: 'eraName', width: 50 },
                { headerText: '記号', key: 'eraMark', width: 50 },
                { headerText: '開始年月日', key: 'startDate', width: 80, isDateColumn: true, format: 'YYYY/MM/DD' },
            ]);
            self.currentEra = ko.observable((new EraModel('', '', moment.utc().toISOString(), 1, '', moment.utc().toISOString())));
            self.currentCode = ko.observable(null);
            self.date = ko.observable('');
            self.dateTime = ko.observable('');
            self.isDeleteEnable = ko.observable(false);
            self.isEnableCode = ko.observable(false);
            self.isUpdate = ko.observable(false);
        }
        
        validateData() : boolean {
            $(".nts-editor").ntsEditor("validate");
            $("#txtStartDate").ntsEditor("validate");
            
            if ($(".nts-editor").ntsError('hasError') || $("#txtStartDate").ntsError('hasError')) {
                return false;    
            }
            return true;
        }

        insertData(): any {
            let self = this;
            let eraName: string;
            eraName = $('#txtEraName').val();
            let eraMark: string;
            eraMark = $('#txtEraMark').val();
            let startDate = self.startDate();
            let endDate: Date;
            let eraHist = self.currentEra().eraHist();
            let fixAttribute: number;
            let dfd = $.Deferred<any>();
            let node: qmm034.a.service.model.EraDto;
            node = new qmm034.a.service.model.EraDto(
                eraName, eraMark, startDate, endDate, fixAttribute, eraHist
            );
            if (!self.validateData()) {
                return;    
            }

            qmm034.a.service.addData(self.isUpdate(), node).done(function(result: service.model.EraDto) {
                self.dirtyObject.reset();
                self.reload().done(function() {
                    self.currentCode(result === undefined ? self.currentEra().eraHist() : result.eraHist);
                    self.isDeleteEnable = ko.observable(false);
                    self.isEnableCode = ko.observable(false);
                    self.isUpdate = ko.observable(true);
                    let lastStartDate = _.maxBy(self.items(), function(o) {
                        return o.startDate;
                    });
                    dfd.resolve();
                });
            }).fail(function(res) {
                $("#txtStartDate").ntsError("set", res.message);
            });
            return dfd.promise();

        }
        alertDelete() {
            let self = this;
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                self.deleteData();
            })
        }

        reload(): JQueryPromise<any> {
            var dfd = $.Deferred();
            var self = this;

            $.when(qmm034.a.service.getAllEras()).done(function(data) {
                self.items([]);
                if (data.length > 0) {
                    self.items(ko.toJS(data));
                    //self.date(self.currentEra().startDate().toString());
                    //self.currentCode(self.currentEra().eraHist());
                    self.isDeleteEnable(true);
                }
                dfd.resolve(data);
            }).fail(function(res) {

            });
            return dfd.promise();
        }

        deleteData() {
            let self = this;
            let eraName: string;
            eraName = $('#txtEraName').val();
            let eraMark: string;
            eraMark = $('#txtEraMark').val();
            let eraHist: string = self.currentEra().eraHist();
            let startDate = self.startDate();
            let dfd = $.Deferred<any>();
            let node: qmm034.a.service.model.EraDtoDelete;
            node = new qmm034.a.service.model.EraDtoDelete(eraHist);
            let rowIndex = _.findIndex(self.items(), function(item) {
                return item.eraName == self.currentEra().eraName();
            })
            qmm034.a.service.deleteData(node).done(function(result) {
                self.reload().done(function(data) {
                    if (self.items().length === 0) {
                        self.refreshLayout();
                    } else if (self.items().length === rowIndex) {
                        self.currentCode(self.items()[rowIndex - 1].eraHist);
                    } else if (self.items().length < rowIndex) {
                        self.currentCode(self.items()[0].eraHist);
                    } else {
                        self.currentCode(self.items()[rowIndex].eraHist);
                    }
                });
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            });
            return dfd.promise();
        }

        getEra(codeNew: any): EraModel {
            let self = this;
            let era = _.find(self.items(), function(item) {
                return item.eraHist === codeNew;
            });
            if (era) {
                return new EraModel(era.eraName, era.eraMark, era.startDate, era.fixAttribute, era.eraHist, era.endDate);
            } else {
                return new EraModel("", "", moment.utc().toISOString(), 0, "", moment.utc().toISOString());
            }
        }

        startPage(): JQueryPromise<any> {
            var self = this;

            // Page load dfd.
            var dfd = $.Deferred();
            // Resolve start page dfd after load all data.
            $.when(qmm034.a.service.getAllEras()).done(function(data: Array<EraModel>) {
                if (data.length > 0) {
                    self.items(ko.toJS(data));
                    self.currentEra(self.items()[0]);
                    self.dirtyObject = new nts.uk.ui.DirtyChecker(self.currentEra);
                    self.currentCode(self.currentEra().eraHist);
                    //self.processWhenCurrentCodeChange(self.currentCode());
                } else {
                    self.dirtyObject = new nts.uk.ui.DirtyChecker(self.currentEra);
                    self.startWithEmptyData();
                }

                dfd.resolve();
            }).fail(function(res) {
                $("#txtEraName").ntsError("set", res.message);
            });

            return dfd.promise();
        }

        refreshLayout(): void {
            let self = this;
            self.clearError();
            self.currentEra(new EraModel('', '', moment.utc().toISOString(), 1, '', moment.utc().toISOString()));
            self.startDate(self.currentEra().startDate());
            self.currentCode(null);
            self.isDeleteEnable(false);
            self.isEnableCode(true);
            self.isUpdate(false);
            if (self.dirtyObject !== undefined)
                self.dirtyObject.reset();
            $("#txtEraName").focus();
        }
        
        startWithEmptyData() {
            let self = this;
            self.isDeleteEnable(false);
            self.isEnableCode(true);
            self.isUpdate(false);
            self.dirtyObject.reset();
        }
        
        clearError() {
            if ($(".nts-editor").ntsError('hasError'))
                $(".nts-editor").ntsError('clear');
            if ($("#txtStartDate").ntsError('hasError'))
                $("#txtStartDate").ntsError('clear');
        }
    }


    export class EraModel {
        eraName: KnockoutObservable<string>;
        eraMark: KnockoutObservable<string>;
        startDate: KnockoutObservable<string>;
        fixAttribute: KnockoutObservable<number>;
        eraHist: KnockoutObservable<string>;
        endDate: KnockoutObservable<string>;

        constructor(eraName: string, eraMark: string, startDate: string, fixAttribute: number, eraHist: string, endDate: string) {
            this.eraName = ko.observable(eraName);
            this.eraMark = ko.observable(eraMark);
            this.startDate = ko.observable(moment(startDate, "YYYY/MM/DD").format());
            this.endDate = ko.observable(moment(endDate, "YYYY/MM/DD").format());
            this.fixAttribute = ko.observable(fixAttribute);
            this.eraHist = ko.observable(eraHist);
        }
    }



}