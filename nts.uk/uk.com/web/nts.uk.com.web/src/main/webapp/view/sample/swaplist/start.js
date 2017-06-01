__viewContext.ready(function () {
    var ScreenModel = (function () {
        function ScreenModel() {
            this.itemsSwap = ko.observableArray([]);
            var array = [];
            this.itemsSwap(array);
            this.columns = ko.observableArray([
                { headerText: 'コード', key: 'code', width: 100 },
                { headerText: '名称', key: 'name', width: 150 }
            ]);
            var x = [];
            //            x.push(_.cloneDeep(array[0]));
            //            x.push(_.cloneDeep(array[1]));
            //            x.push(_.cloneDeep(array[2]));
            this.currentCodeListSwap = ko.observableArray(x);
            this.currentCodeListSwap.subscribe(function (value) {
                console.log(value);
            });
            this.test = ko.observableArray([]);
        }
        ScreenModel.prototype.remove = function () {
            this.itemsSwap.shift();
        };
        ScreenModel.prototype.bindSource = function () {
            var self = this;
            var array = [];
            for (var i = 0; i < 10000; i++) {
                array.push(new ItemModel(i, '基本給', "description"));
            }
            self.itemsSwap(array);
        };
        return ScreenModel;
    }());
    var ItemModel = (function () {
        function ItemModel(code, name, description) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.deletable = code % 3 === 0;
        }
        return ItemModel;
    }());
    var screen;
    this.bind(screen = new ScreenModel());
    $("#check").on("click", function () {
        var a = screen.x;
    });
});
//# sourceMappingURL=start.js.map