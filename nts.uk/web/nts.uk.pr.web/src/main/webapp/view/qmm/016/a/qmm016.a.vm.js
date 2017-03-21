var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qmm016;
                (function (qmm016) {
                    var a;
                    (function (a) {
                        var viewmodel;
                        (function (viewmodel) {
                            var ScreenModel = (function (_super) {
                                __extends(ScreenModel, _super);
                                function ScreenModel() {
                                    _super.call(this, {
                                        functionName: '賃金テープル',
                                        service: qmm016.service.instance,
                                        removeMasterOnLastHistoryRemove: true });
                                    var self = this;
                                    self.head = new HeadViewModel();
                                    self.history = new HistoryViewModel();
                                    self.selectedTab = ko.observable('tab-1');
                                    self.tabs = ko.observableArray([
                                        {
                                            id: 'tab-1',
                                            title: '基本情報',
                                            content: '#tab-content-1',
                                            enable: ko.observable(true),
                                            visible: ko.observable(true) },
                                        {
                                            id: 'tab-2',
                                            title: '賃金テーブルの情報',
                                            content: '#tab-content-2',
                                            enable: ko.computed(function () {
                                                return !self.isNewMode();
                                            }),
                                            visible: ko.observable(true) }
                                    ]);
                                    self.generalTableTypes = ko.observableArray(qmm016.model.normalDemension);
                                    self.specialTableTypes = ko.observableArray(qmm016.model.specialDemension);
                                }
                                ScreenModel.prototype.onSelectHistory = function (id) {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    qmm016.service.instance.loadHistoryByUuid(id).done(function (model) {
                                        self.head.resetBy(model.head);
                                    });
                                    dfd.resolve();
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.onSave = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    if (self.isNewMode) {
                                        var wagetableDto = self.head.getWageTableDto();
                                        qmm016.service.instance.initWageTable({
                                            wageTableHeadDto: wagetableDto,
                                            startMonth: self.history.startYearMonth()
                                        }).done(function (res) {
                                            dfd.resolve(res.uuid);
                                        });
                                    }
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.onRegistNew = function () {
                                    var self = this;
                                    $('.save-error').ntsError('clear');
                                    self.head.reset();
                                };
                                return ScreenModel;
                            }(view.base.simplehistory.viewmodel.ScreenBaseModel));
                            viewmodel.ScreenModel = ScreenModel;
                            var HeadViewModel = (function () {
                                function HeadViewModel() {
                                    var self = this;
                                    self.code = ko.observable(undefined);
                                    self.name = ko.observable(undefined);
                                    self.demensionSet = ko.observable(undefined);
                                    self.memo = ko.observable(undefined);
                                    self.demensionType = ko.computed(function () {
                                        return qmm016.model.demensionMap[self.demensionSet()];
                                    });
                                    self.demensionItemList = ko.observableArray([]);
                                    self.demensionSet.subscribe(function (val) {
                                        if (!self.isNewMode()) {
                                            return;
                                        }
                                        self.demensionItemList(self.getDemensionItemListByType(val));
                                    });
                                    self.isNewMode = ko.observable(true);
                                }
                                HeadViewModel.prototype.reset = function () {
                                    var self = this;
                                    self.isNewMode(true);
                                    self.code('');
                                    self.name('');
                                    self.demensionSet(qmm016.model.allDemension[0].code);
                                    self.demensionItemList([]);
                                    self.memo('');
                                };
                                HeadViewModel.prototype.getWageTableDto = function () {
                                    var self = this;
                                    var dto = {};
                                    dto.code = self.code();
                                    dto.name = self.name();
                                    dto.memo = self.memo();
                                    dto.mode = self.demensionSet();
                                    dto.elements = _.map(self.demensionItemList(), function (item) {
                                        var elementDto = {};
                                        elementDto.demensionNo = item.demensionNo();
                                        elementDto.type = item.elementType();
                                        elementDto.referenceCode = item.elementCode();
                                        return elementDto;
                                    });
                                    return dto;
                                };
                                HeadViewModel.prototype.getDemensionItemListByType = function (typeCode) {
                                    var newDemensionItemList = new Array();
                                    switch (typeCode) {
                                        case 0:
                                            newDemensionItemList.push(new DemensionItemViewModel(1));
                                            break;
                                        case 1:
                                            newDemensionItemList.push(new DemensionItemViewModel(1));
                                            newDemensionItemList.push(new DemensionItemViewModel(2));
                                            break;
                                        case 2:
                                            newDemensionItemList.push(new DemensionItemViewModel(1));
                                            newDemensionItemList.push(new DemensionItemViewModel(2));
                                            newDemensionItemList.push(new DemensionItemViewModel(3));
                                            break;
                                        case 3:
                                            {
                                                var cert = new DemensionItemViewModel(1);
                                                cert.elementType(6);
                                                cert.elementName('資格名称');
                                                newDemensionItemList.push(cert);
                                            }
                                            break;
                                        case 4:
                                            {
                                                var workDay = new DemensionItemViewModel(1);
                                                workDay.elementType(7);
                                                workDay.elementName('欠勤日数');
                                                var late = new DemensionItemViewModel(2);
                                                late.elementType(8);
                                                late.elementName('遅刻・早退回数');
                                                var level = new DemensionItemViewModel(2);
                                                level.elementType(8);
                                                level.elementName('レベル');
                                                newDemensionItemList.push(workDay);
                                                newDemensionItemList.push(late);
                                                newDemensionItemList.push(level);
                                            }
                                            break;
                                    }
                                    return newDemensionItemList;
                                };
                                HeadViewModel.prototype.resetBy = function (head) {
                                    var self = this;
                                    self.isNewMode(false);
                                    self.code(head.code);
                                    self.name(head.name);
                                    self.demensionSet(head.mode);
                                    self.memo(head.memo);
                                };
                                HeadViewModel.prototype.onSelectDemensionBtnClick = function (demension) {
                                    var self = this;
                                    var dlgOptions = {
                                        onSelectItem: function (data) {
                                            demension.elementType(data.demension.type);
                                            demension.elementCode(data.demension.code);
                                            demension.elementName(data.demension.name);
                                        }
                                    };
                                    nts.uk.ui.windows.setShared('options', dlgOptions);
                                    var ntsDialogOptions = { title: '要素の選択', dialogClass: 'no-close' };
                                    nts.uk.ui.windows.sub.modal('/view/qmm/016/k/index.xhtml', ntsDialogOptions);
                                };
                                return HeadViewModel;
                            }());
                            viewmodel.HeadViewModel = HeadViewModel;
                            var DemensionItemViewModel = (function () {
                                function DemensionItemViewModel(demensionNo) {
                                    var self = this;
                                    self.demensionNo = ko.observable(demensionNo);
                                    self.elementType = ko.observable(0);
                                    self.elementCode = ko.observable('');
                                    self.elementName = ko.observable('');
                                }
                                return DemensionItemViewModel;
                            }());
                            viewmodel.DemensionItemViewModel = DemensionItemViewModel;
                            var HistoryViewModel = (function () {
                                function HistoryViewModel() {
                                    var self = this;
                                    self.startYearMonth = ko.observable(parseInt(nts.uk.time.formatDate(new Date(), 'yyyyMM')));
                                    self.endYearMonth = ko.observable(99999);
                                    self.startYearMonthJpText = ko.computed(function () {
                                        return nts.uk.text.format('（{0}）', nts.uk.time.yearmonthInJapanEmpire(self.startYearMonth()).toString());
                                    });
                                }
                                return HistoryViewModel;
                            }());
                            viewmodel.HistoryViewModel = HistoryViewModel;
                        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
                    })(a = qmm016.a || (qmm016.a = {}));
                })(qmm016 = view.qmm016 || (view.qmm016 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=qmm016.a.vm.js.map