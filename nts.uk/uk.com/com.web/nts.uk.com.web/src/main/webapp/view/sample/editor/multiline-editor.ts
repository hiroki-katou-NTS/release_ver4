__viewContext.ready(function() {
    class ScreenModel {
        simpleValue: KnockoutObservable<string>;
        multilineeditor: any;

        constructor() {
            var self = this;
            self.simpleValue = ko.observable("123");
            // MultilineEditor
            self.multilineeditor = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                    resizeable: true,
                    placeholder: "Placeholder for text editor",
                    width: "",
                    textalign: "left"
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
        }
    }

    var viewmodel = new ScreenModel();
    this.bind(viewmodel);
});