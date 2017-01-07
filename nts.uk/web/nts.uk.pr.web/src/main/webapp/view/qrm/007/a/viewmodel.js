var qrm007;
(function (qrm007) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.paymentDateProcessingList = ko.observableArray([]);
                    self.selectedPaymentDate = ko.observable(null);
                    self.items = ko.observableArray([
                        new ItemModel('001', 'name1', "name1"),
                        new ItemModel('002', 'name2', "name2"),
                    ]);
                    self.columns = ko.observableArray([
                        { headerText: 'コード', prop: 'code', width: 40 },
                        { headerText: '名称', prop: 'name', width: 130 },
                        { headerText: '印刷用名称', prop: 'description', width: 160 }
                    ]);
                    self.currentCode = ko.observable(_.first(self.items()).code);
                    self.currentItem = ko.observable(_.first(self.items()));
                    self.currentCode.subscribe(function (newValue) {
                        self.currentItem(_.find(self.items(), function (item) { return item.code === newValue; }));
                    });
                    self.texteditor = {
                        value: ko.computed(function () { return self.currentItem().name; }),
                        constraint: 'ResidenceCode',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            width: "200px",
                            textalign: "left"
                        })),
                        required: ko.observable(true),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                    self.multilineeditor = {
                        value: ko.observable(''),
                        constraint: 'ResidenceCode',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                            resizeable: true,
                            placeholder: "",
                            width: "",
                            textalign: "left"
                        })),
                        required: ko.observable(true),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                }
                /*
                getItem(newValue): ItemModel {
                    let self = this;
                    let item: ItemModel = _.find(self.items(), function(item) {
                        return item.code === newValue;
                    });
                    return item;
                }
                */
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qrm007.a.service.getPaymentDateProcessingList().done(function (data) {
                        self.paymentDateProcessingList(data);
                        dfd.resolve();
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var ItemModel = (function () {
                function ItemModel(code, name, description) {
                    this.code = code;
                    this.name = name;
                    this.description = description;
                }
                return ItemModel;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qrm007.a || (qrm007.a = {}));
})(qrm007 || (qrm007 = {}));
