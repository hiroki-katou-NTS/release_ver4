// TreeGrid Node
var qpp014;
(function (qpp014) {
    var h;
    (function (h) {
        var ScreenModel = (function () {
            function ScreenModel() {
                var self = this;
                //DatePicker
                self.date_H_INP_001 = ko.observable(new Date('2016/12/01'));
                //gridview
                self.items_H_LST_001 = ko.observableArray([]);
                for (var i_1 = 1; i_1 < 100; i_1++) {
                    self.items_H_LST_001.push(new ItemModel_H_LST_001('00' + i_1, '基本給', "description " + i_1, "other" + i_1));
                }
                self.columns_H_LST_001 = ko.observableArray([
                    { headerText: '�R�[�h', prop: 'code', width: 100 },
                    { headerText: '����', prop: 'name', width: 150 },
                    { headerText: '����', prop: 'description', width: 200 }
                ]);
                self.currentCode_H_LST_001 = ko.observable();
            }
            return ScreenModel;
        }());
        h.ScreenModel = ScreenModel;
        var ItemModel_H_LST_001 = (function () {
            function ItemModel_H_LST_001(code, name, description) {
                this.code = code;
                this.name = name;
                this.description = description;
            }
            return ItemModel_H_LST_001;
        }());
        h.ItemModel_H_LST_001 = ItemModel_H_LST_001;
    })(h = qpp014.h || (qpp014.h = {}));
})(qpp014 || (qpp014 = {}));
;
