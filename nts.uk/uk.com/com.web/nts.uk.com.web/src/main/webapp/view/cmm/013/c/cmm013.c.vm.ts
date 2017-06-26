module cmm013.c.viewmodel {

    export class ScreenModel {
        label_002: KnockoutObservable<Labels>;
        inp_003: KnockoutObservable<string>;
        historyId: KnockoutObservable<string>;
        startDateLast: KnockoutObservable<string>;
        //C_SEL_001
        itemList: KnockoutObservableArray<any>;
        selectedId: KnockoutObservable<number>;
        enable: KnockoutObservable<boolean>;
        res: KnockoutObservableArray<string>;
        yearmonthdateeditor: any;

        constructor() {
            var self = this;
            self.label_002 = ko.observable(new Labels());
            self.inp_003 = ko.observable("");
            self.historyId = ko.observable(null);
            self.startDateLast = ko.observable('');
            //C_SEL_001
            self.selectedId = ko.observable(1);
            self.enable = ko.observable(true);
            self.yearmonthdateeditor = {
                option: ko.mapping.fromJS(new nts.uk.ui.option.TimeEditorOption({
                    inputFormat: 'date'
                })),
            };
        }
        /**
         * Start page
         * get start date last from screen A
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.historyId(nts.uk.ui.windows.getShared('CMM013_historyId'));
            self.startDateLast(nts.uk.ui.windows.getShared('CMM013_startDateLast'));
           
            self.selectedId = ko.observable(0);
    //!nts.uk.text.isNullOrEmpty(self.historyId()) && !nts.uk.text.isNullOrEmpty(self.startDateLast()) && 
            
            if (self.startDateLast()) {
                self.itemList = ko.observableArray([
                    new BoxModel(0, '最新の履歴（' + self.startDateLast() + '）から引き継ぐ  '),
                    new BoxModel(1, '初めから作成する')
                   
                ]);
                 self.selectedId = ko.observable(1);
                self.enable(true);
            } else {
                self.enable(false);
                 self.setValueForRadio();
            }
            dfd.resolve();
            return dfd.promise();
        }
        /**
         * decision add history
         * set start date new and send to screen A(main)
         * then close screen C
         */
        setValueForRadio(): any {
            var self = this;
            self.itemList = ko.observableArray([
                new BoxModel(0, ' 初めから作成する '),
                new BoxModel(1, ' 初めから作成する')
            ]);
            self.selectedId = ko.observable(0);
        }

        closeDialog() {

            nts.uk.ui.windows.close();


        }
        add() {
            var self = this;
            if (self.checkTypeInput() == false) {
                return;
            } else
                if (self.checkValueInput(self.inp_003()) == false) {
                    return;
                }
                else {
                    if (self.startDateLast() != '' && self.startDateLast() != null) {
                        var check = self.selectedId();
                    } else {
                        var check = 2;
                    }
                    var date = new Date(self.inp_003());
                    let dateNew = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();
                    if (date.getMonth() < 9 && date.getDate() < 10) {
                        dateNew = date.getFullYear() + '/' + 0 + (date.getMonth() + 1) + '/' + 0 + date.getDate();
                    } else {
                        if (date.getDate() < 10) {
                            dateNew = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + 0 + date.getDate();
                        }
                        if (date.getMonth() < 9) {
                            dateNew = date.getFullYear() + '/' + 0 + (date.getMonth() + 1) + '/' + date.getDate();
                        }
                    }
                    if (self.checkValueInput(dateNew) == false) {
                        return;
                    }

                    nts.uk.ui.windows.setShared('cmm013C_startDateNew', dateNew, true);
                    nts.uk.ui.windows.setShared('cmm013Copy', check == 0 ? true : false, true);
                    nts.uk.ui.windows.setShared('cmm013Insert', true, true);
                    nts.uk.ui.windows.close();
                }
        }
        checkTypeInput(): boolean {
            var self = this;
            var date = new Date(self.inp_003());

            if (date.toDateString() == 'Invalid Date') {
                alert("Input by YYYY/MM/DD");
                return false;
            } else {
                return true;
            }
        }
        checkValueInput(value: string): boolean {
            var self = this;
            if (value <= self.startDateLast()) {
                alert("履歴の期間が正しくありません。");
                return false;
            } else {
                return true;
            }
        }
    }

    export class Labels {
        constraint: string = 'LayoutCode';
        inline: KnockoutObservable<boolean>;
        required: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.inline = ko.observable(true);
            self.required = ko.observable(true);
            self.enable = ko.observable(true);
        }
    }

    export class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }

}
