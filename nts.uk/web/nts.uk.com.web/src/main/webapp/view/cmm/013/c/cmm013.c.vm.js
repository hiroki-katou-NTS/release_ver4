var cmm013;
(function (cmm013) {
    var c;
    (function (c) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
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
                startPage() {
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
                    }
                    else {
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
                setValueForRadio() {
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
                    }
                    else if (self.checkValueInput(self.inp_003()) == false) {
                        return;
                    }
                    else {
                        if (self.startDateLast() != '' && self.startDateLast() != null) {
                            var check = self.selectedId();
                        }
                        else {
                            var check = 2;
                        }
                        var date = new Date(self.inp_003());
                        let dateNew = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();
                        if (date.getMonth() < 9 && date.getDate() < 10) {
                            dateNew = date.getFullYear() + '/' + 0 + (date.getMonth() + 1) + '/' + 0 + date.getDate();
                        }
                        else {
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
                checkTypeInput() {
                    var self = this;
                    var date = new Date(self.inp_003());
                    if (date.toDateString() == 'Invalid Date') {
                        alert("Input by YYYY/MM/DD");
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                checkValueInput(value) {
                    var self = this;
                    if (value <= self.startDateLast()) {
                        alert("履歴の期間が正しくありません。");
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            }
            viewmodel.ScreenModel = ScreenModel;
            class Labels {
                constructor() {
                    this.constraint = 'LayoutCode';
                    var self = this;
                    self.inline = ko.observable(true);
                    self.required = ko.observable(true);
                    self.enable = ko.observable(true);
                }
            }
            viewmodel.Labels = Labels;
            class BoxModel {
                constructor(id, name) {
                    var self = this;
                    self.id = id;
                    self.name = name;
                }
            }
            viewmodel.BoxModel = BoxModel;
        })(viewmodel = c.viewmodel || (c.viewmodel = {}));
    })(c = cmm013.c || (cmm013.c = {}));
})(cmm013 || (cmm013 = {}));
