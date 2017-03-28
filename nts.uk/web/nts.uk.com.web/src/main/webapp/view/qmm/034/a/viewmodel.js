<<<<<<< HEAD
var qmm034;
(function (qmm034) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    this.constraint = 'LayoutCode';
                    this.isUpdate = ko.observable(true);
                    var self = this;
                    /*gridList*/
                    self.items = ko.observableArray([]);
                    self.columns = ko.observableArray([
                        { headerText: '元号', prop: 'code', width: 50 },
                        { headerText: '記号', prop: 'name', width: 50 },
                        { headerText: '開始年月日', prop: 'startDateText', width: 80 },
                    ]);
                    self.currentCodeList = ko.observableArray([]);
                    //Tim object dau tien
                    self.currentEra = ko.observable(null);
                    self.currentCode = ko.observable();
                    self.currentCode.subscribe(function (codeChanged) {
                        self.currentEra(self.getEra(codeChanged));
                        self.date(self.currentEra().startDate);
                    });
                    /*datePicker*/
                    // var datePicker = self.currentEra();
                    self.date = ko.observable(new Date());
                    self.dateTime = ko.observable(nts.uk.time.yearInJapanEmpire(self.date()).toString());
                    self.eras = ko.observableArray([]);
                    console.log(self.items());
                    /*selected row*/
                    self.findIndex = ko.observable(0);
                    self.countItems = ko.observable(0);
                    self.isSelectdFirstRow = ko.observable(true);
                    self.isDeleteEnable = ko.observable(true);
                }
                //        register() {
                //            let self = this;
                //            if (self.isUpdate() === false) {
                //                self.insertData();
                //            } else {
                //                self.update();
                //            }
                //        }
                ScreenModel.prototype.refreshLayout = function () {
                    var self = this;
                    self.currentEra(new EraModel('', '', ''));
                    self.isUpdate = ko.observable(false);
                };
                ScreenModel.prototype.insertData = function () {
                    var self = this;
                    //let newData = self.currentEra();
                    //let newEradata = self;
                    // var x = self.items();
                    //x.push(newData);
                    //            if (self.isUpdate() === false) {
                    //                self.items.push(newData);
                    //                self.isUpdate = ko.observable(true);
                    //            }
                    var eraName;
                    eraName = $('#A_INP_001').val();
                    var eraMark;
                    eraMark = $('#A_INP_002').val();
                    var startDate = self.date();
                    var endDate;
                    var fixAttribute;
                    var dfd = $.Deferred();
                    var node;
                    node = new qmm034.a.service.model.EraDto(eraName, eraMark, startDate.toDateString(), endDate, fixAttribute);
                    qmm034.a.service.addData(self.isUpdate(false), node).done(function (result) {
                        self.reload().done(function () {
                            dfd.resolve();
                        });
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.alertDelete = function () {
                    var self = this;
                    if (confirm("do you wanna delete") === true) {
                        self.deleteData();
                    }
                    else {
                        alert("you didnt delete!");
                    }
                };
                ScreenModel.prototype.selectedItem = function (item) {
                    var self = this;
                    self.currentCode(item.code);
                    return new EraModel(item.code, item.name, item.startDate);
                };
                ScreenModel.prototype.reload = function () {
                    var dfd = $.Deferred();
                    var self = this;
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        self.buildGridDataSource(data);
                        self.countItems(data.length);
                        if (data.length > 0) {
                            if (self.isSelectdFirstRow()) {
                                self.currentEra(self.selectedItem(data[0]));
                                self.isSelectdFirstRow(false);
                            }
                            self.items(data);
                        }
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.deleteData = function () {
                    var self = this;
                    //            let newDel = self.currentEra();
                    //            self.items.splice(self.items().indexOf(newDel), 1);
                    var eraName;
                    eraName = $('#A_INP_001').val();
                    var eraMark;
                    eraMark = $('#A_INP_002').val();
                    var startDate = self.date();
                    var endDate;
                    var fixAttribute;
                    var dfd = $.Deferred();
                    var node;
                    node = new qmm034.a.service.model.EraDtoDelete(startDate);
                    //            qmm034.a.service.deleteData(node)
                    //                .done(function() {
                    //                    qmm034.a.service.getAllEras();
                    //                }).fail(function(error) {
                    //                    alert(error.message);
                    //                });
                    qmm034.a.service.deleteData(node).done(function (result) {
                        self.reload().done(function () {
                            dfd.resolve();
                        });
                    }).fail(function (error) {
                        alert(error.message);
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.getEra = function (codeNew) {
                    var self = this;
                    var era = _.find(self.items(), function (item) {
                        return item.code === codeNew;
                    });
                    return _.cloneDeep(era);
                };
                ScreenModel.prototype.update = function () {
                    var self = this;
                    //            if (self.currentCode() !== undefined && self.currentCode() !== null) {
                    //                var newCurrentEra = _.findIndex(self.items(), function(item) {
                    //                    return item.code === self.currentCode();
                    //                });
                    //                self.items.splice(newCurrentEra, 1, _.cloneDeep(self.currentEra()));
                    //                self.items.valueHasMutated();
                    //            }
                    //            qmm034.a.service.updateData().done(function() {
                    //                        self.start();
                    //                        //console.log(self.items());
                    //                    });
                };
                ScreenModel.prototype.selectSomeItems = function () {
                    this.currentCode('150');
                    this.currentCodeList.removeAll();
                    this.currentCodeList.push('001');
                    this.currentCodeList.push('ABC');
                };
                ScreenModel.prototype.deselectAll = function () {
                    this.currentCode(null);
                    this.currentCodeList.removeAll();
                };
                ScreenModel.prototype.start = function () {
                    var self = this;
                    // Page load dfd.
                    var dfd = $.Deferred();
                    // Resolve start page dfd after load all data.
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    // Page load dfd.
                    var dfd = $.Deferred();
                    // Resolve start page dfd after load all data.
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        self.buildGridDataSource(data);
                        self.currentEra = ko.observable(_.cloneDeep(_.first(self.items())));
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.buildGridDataSource = function (items) {
                    var self = this;
                    self.items([]);
                    _.forEach(items, function (obj) {
                        self.items.push(new EraModel(obj.eraName, obj.eraMark, obj.startDate));
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            //    class Era{
            //        eraName: KnockoutObservable<string>;
            //        eraMark: KnockoutObservable<string>;
            //        startDateEra: KnockoutObservable<Date>;    
            //        
            //        constructor(eraName: string, eraMark: string, startDateEra: Date){
            //                this.eraName = ko.observable(eraName);
            //                this.eraMark = ko.observable(eraMark);
            //                this.startDateEra = ko.observable(startDateEra);
            //        }
            //    }
            var EraModel = (function () {
                function EraModel(code, name, startDate) {
                    this.code = code;
                    this.name = name;
                    if (startDate !== "") {
                        this.startDate = new Date(startDate);
                        this.startDateText = startDate;
                    }
                    else {
                        this.startDate = new Date();
                        this.startDateText = this.startDate.toDateString();
                    }
                }
                return EraModel;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm034.a || (qmm034.a = {}));
})(qmm034 || (qmm034 = {}));
=======
var qmm034;
(function (qmm034) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    this.constraint = 'LayoutCode';
                    this.isUpdate = ko.observable(true);
                    var self = this;
                    self.items = ko.observableArray([]);
                    self.columns = ko.observableArray([
                        { headerText: '元号', prop: 'code', width: 50 },
                        { headerText: '記号', prop: 'name', width: 50 },
                        { headerText: '開始年月日', prop: 'startDateText', width: 80 },
                    ]);
                    self.currentCodeList = ko.observableArray([]);
                    self.currentEra = ko.observable(null);
                    self.currentCode = ko.observable();
                    self.currentCode.subscribe(function (codeChanged) {
                        self.currentEra(self.getEra(codeChanged));
                        self.date(self.currentEra().startDate);
                    });
                    self.date = ko.observable(new Date());
                    self.dateTime = ko.observable(nts.uk.time.yearInJapanEmpire(self.date()).toString());
                    self.eras = ko.observableArray([]);
                    console.log(self.items());
                    self.findIndex = ko.observable(0);
                    self.countItems = ko.observable(0);
                    self.isSelectdFirstRow = ko.observable(true);
                    self.isDeleteEnable = ko.observable(true);
                }
                ScreenModel.prototype.refreshLayout = function () {
                    var self = this;
                    self.currentEra(new EraModel('', '', ''));
                    self.isUpdate = ko.observable(false);
                };
                ScreenModel.prototype.insertData = function () {
                    var self = this;
                    var eraName;
                    eraName = $('#A_INP_001').val();
                    var eraMark;
                    eraMark = $('#A_INP_002').val();
                    var startDate = self.date();
                    var endDate;
                    var fixAttribute;
                    var dfd = $.Deferred();
                    var node;
                    node = new qmm034.a.service.model.EraDto(eraName, eraMark, startDate.toDateString(), endDate, fixAttribute);
                    qmm034.a.service.addData(self.isUpdate(false), node).done(function (result) {
                        self.reload().done(function () {
                            dfd.resolve();
                        });
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.alertDelete = function () {
                    var self = this;
                    if (confirm("do you wanna delete") === true) {
                        self.deleteData();
                    }
                    else {
                        alert("you didnt delete!");
                    }
                };
                ScreenModel.prototype.selectedItem = function (item) {
                    var self = this;
                    self.currentCode(item.code);
                    return new EraModel(item.code, item.name, item.startDate);
                };
                ScreenModel.prototype.reload = function () {
                    var dfd = $.Deferred();
                    var self = this;
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        self.buildGridDataSource(data);
                        self.countItems(data.length);
                        if (data.length > 0) {
                            if (self.isSelectdFirstRow()) {
                                self.currentEra(self.selectedItem(data[0]));
                                self.isSelectdFirstRow(false);
                            }
                            self.items(data);
                        }
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.deleteData = function () {
                    var self = this;
                    var eraName;
                    eraName = $('#A_INP_001').val();
                    var eraMark;
                    eraMark = $('#A_INP_002').val();
                    var startDate = self.date();
                    var endDate;
                    var fixAttribute;
                    var dfd = $.Deferred();
                    var node;
                    node = new qmm034.a.service.model.EraDtoDelete(startDate);
                    qmm034.a.service.deleteData(node).done(function (result) {
                        self.reload().done(function () {
                            dfd.resolve();
                        });
                    }).fail(function (error) {
                        alert(error.message);
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.getEra = function (codeNew) {
                    var self = this;
                    var era = _.find(self.items(), function (item) {
                        return item.code === codeNew;
                    });
                    return _.cloneDeep(era);
                };
                ScreenModel.prototype.update = function () {
                    var self = this;
                };
                ScreenModel.prototype.selectSomeItems = function () {
                    this.currentCode('150');
                    this.currentCodeList.removeAll();
                    this.currentCodeList.push('001');
                    this.currentCodeList.push('ABC');
                };
                ScreenModel.prototype.deselectAll = function () {
                    this.currentCode(null);
                    this.currentCodeList.removeAll();
                };
                ScreenModel.prototype.start = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    $.when(qmm034.a.service.getAllEras()).done(function (data) {
                        self.buildGridDataSource(data);
                        self.currentEra = ko.observable(_.cloneDeep(_.first(self.items())));
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.buildGridDataSource = function (items) {
                    var self = this;
                    self.items([]);
                    _.forEach(items, function (obj) {
                        self.items.push(new EraModel(obj.eraName, obj.eraMark, obj.startDate));
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var EraModel = (function () {
                function EraModel(code, name, startDate) {
                    this.code = code;
                    this.name = name;
                    if (startDate !== "") {
                        this.startDate = new Date(startDate);
                        this.startDateText = startDate;
                    }
                    else {
                        this.startDate = new Date();
                        this.startDateText = this.startDate.toDateString();
                    }
                }
                return EraModel;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm034.a || (qmm034.a = {}));
})(qmm034 || (qmm034 = {}));
>>>>>>> basic/develop
//# sourceMappingURL=viewmodel.js.map