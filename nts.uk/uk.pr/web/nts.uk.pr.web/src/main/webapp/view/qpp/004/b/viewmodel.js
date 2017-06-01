var qpp004;
(function (qpp004) {
    var b;
    (function (b) {
        var viewmodel;
        (function (viewmodel) {
            var Step = (function () {
                function Step(id, content) {
                    var self = this;
                    this.id = ko.observable(id);
                    this.content = ko.observable(content);
                }
                return Step;
            }());
            viewmodel.Step = Step;
            var ItemModel = (function () {
                function ItemModel(id, code, name) {
                    var self = this;
                    this.id = id;
                    this.code = code;
                    this.name = name;
                }
                return ItemModel;
            }());
            viewmodel.ItemModel = ItemModel;
            var Listbox = (function () {
                function Listbox() {
                    var self = this;
                    self.itemList = ko.observableArray([
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000001', 'A00000000001', '譌･騾壹��遉ｾ蜩｡�ｼ�'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000002', 'A00000000002', '譌･騾壹��遉ｾ蜩｡2'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000003', 'A00000000003', '譌･騾壹��遉ｾ蜩｡3'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000004', 'A00000000004', '譌･騾壹��遉ｾ蜩｡4'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000005', 'A00000000005', '譌･騾壹��遉ｾ蜩｡5'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000006', 'A00000000006', '譌･騾壹��遉ｾ蜩｡6'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000007', 'A00000000007', '譌･騾壹��遉ｾ蜩｡7'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000008', 'A00000000008', '譌･騾壹��遉ｾ蜩｡8'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000009', 'A00000000009', '譌･騾壹��遉ｾ蜩｡9'),
                        new qpp004.b.viewmodel.ItemModel('A00000000000000000000000000000000010', 'A00000000010', '譌･騾壹��遉ｾ蜩｡�ｼ�0'),
                    ]);
                    self.itemName = ko.observable('');
                    self.currentCode = ko.observable(3);
                    self.selectedCode = ko.observable(null);
                    self.isEnable = ko.observable(true);
                    self.selectedCodes = ko.observableArray([]);
                }
                return Listbox;
            }());
            viewmodel.Listbox = Listbox;
            var Wizard = (function () {
                function Wizard() {
                    var self = this;
                    self.stepList = ko.observableArray([
                        new qpp004.b.viewmodel.Step('step-1', '.step-1'),
                        new qpp004.b.viewmodel.Step('step-2', '.step-2'),
                        new qpp004.b.viewmodel.Step('step-3', '.step-3'),
                    ]);
                    self.stepSelected = ko.observable(new Step('step-2', '.step-2'));
                }
                Wizard.prototype.begin = function () {
                    $('#wizard').begin();
                };
                Wizard.prototype.end = function () {
                    $('#wizard').end();
                };
                Wizard.prototype.next = function () {
                    var currStep = $('#wizard').steps('getCurrentIndex');
                    if (currStep == 1) {
                        var valid = $('#list-box').ntsListBox('validate');
                        console.log(valid);
                        if (valid) {
                            $('#wizard').steps('next');
                        }
                    }
                    else {
                        $('#wizard').steps('next');
                    }
                };
                Wizard.prototype.previous = function () {
                    $('#wizard').steps('previous');
                };
                Wizard.prototype.step1 = function () {
                    $('#wizard').setStep(0);
                };
                Wizard.prototype.step2 = function () {
                    $('#wizard').setStep(1);
                };
                Wizard.prototype.step3 = function () {
                    $('#wizard').setStep(2);
                };
                return Wizard;
            }());
            viewmodel.Wizard = Wizard;
            var ScreenModel = (function () {
                function ScreenModel(data) {
                    var self = this;
                    self.wizard = new qpp004.b.viewmodel.Wizard();
                    self.listbox = new qpp004.b.viewmodel.Listbox();
                    self.currentProcessingYearMonth = ko.observable(data.displayCurrentProcessingYm);
                    self.processingNo = ko.observable(data.processingNo);
                    self.processingYM = ko.observable(data.currentProcessingYm);
                }
                ScreenModel.prototype.processCreateData = function () {
                    var self = this;
                    var result = [];
                    _.forEach(self.listbox.itemList(), function (item) {
                        if (self.listbox.selectedCodes().indexOf(item.id) >= 0) {
                            result.push(item);
                        }
                    });
                    var data = {
                        personIdList: result,
                        processingNo: self.processingNo(),
                        processingYearMonth: self.processingYM()
                    };
                    nts.uk.ui.windows.setShared("data", data);
                    nts.uk.ui.windows.sub.modal("/view/qpp/004/l/index.xhtml", { title: "邨ｦ荳弱ョ繝ｼ繧ｿ縺ｮ菴懈��", dialogClass: "no-close" });
                };
                ScreenModel.prototype.backqpp004a = function () {
                    nts.uk.request.jump("/view/qpp/004/a/index.xhtml", {});
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
    })(b = qpp004.b || (qpp004.b = {}));
})(qpp004 || (qpp004 = {}));
//# sourceMappingURL=viewmodel.js.map