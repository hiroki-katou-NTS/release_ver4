__viewContext.ready(function () {
    var ScreenModel = (function () {
        function ScreenModel() {
            var self = this;
            self.value = ko.observable("123");
            // TextEditor
            self.texteditor = {
                value: ko.observable(''),
                valueKana: ko.observable(''),
                valueUan: ko.observable(''),
                valueHalfInt: ko.observable(''),
                defaultValue: ko.observable(''),
                constraint: 'WLAggregateItemCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "Placeholder for text editor",
                    width: "",
                    textalign: "left"
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false),
                clear: function () {
                    $("#text-1").ntsError("clear");
                },
                setDefault: function () {
                    var self = this;
                    nts.uk.util.value.reset($("#text-1"), self.defaultValue() !== '' ? self.defaultValue() : undefined);
                }
            };
            // EmployeeCodeEditor
            self.employeeeditor = {
                value: ko.observable('19'),
                constraint: 'EmployeeCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    filldirection: "right",
                    fillcharacter: "0"
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
        }
        return ScreenModel;
    }());
    var viewmodel = new ScreenModel();
    this.bind(viewmodel);
});
//# sourceMappingURL=text-editor.js.map