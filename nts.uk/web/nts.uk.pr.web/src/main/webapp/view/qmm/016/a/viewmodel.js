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
                                    self.head = new WageTableHeadViewModel();
                                    self.selectedTab = ko.observable('tab-1');
                                    self.tabs = ko.observableArray([
                                        { id: 'tab-1', title: '基本情報', content: '#tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                                        { id: 'tab-2', title: '賃金テーブルの情報', content: '#tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
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
                                    dfd.resolve();
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
                            var WageTableHeadViewModel = (function () {
                                function WageTableHeadViewModel() {
                                    var self = this;
                                    self.code = ko.observable(undefined);
                                    self.name = ko.observable(undefined);
                                    self.demensionSet = ko.observable(undefined);
                                    self.memo = ko.observable(undefined);
                                    self.reset();
                                }
                                WageTableHeadViewModel.prototype.reset = function () {
                                    var self = this;
                                    self.code('');
                                    self.name('');
                                    self.demensionSet(qmm016.model.allDemension[0].code);
                                    self.memo('');
                                };
                                WageTableHeadViewModel.prototype.resetBy = function (head) {
                                    var self = this;
                                    self.code(head.code);
                                    self.name(head.name);
                                    self.demensionSet(head.demensionSet);
                                    self.memo(head.memo);
                                };
                                return WageTableHeadViewModel;
                            }());
                            viewmodel.WageTableHeadViewModel = WageTableHeadViewModel;
                        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
                    })(a = qmm016.a || (qmm016.a = {}));
                })(qmm016 = view.qmm016 || (view.qmm016 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=viewmodel.js.map