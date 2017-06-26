import editorOption = nts.uk.ui.option;

__viewContext.ready(function() {
    class ScreenModel {
        value: KnockoutObservable<any>;
        constraint: any;
        option: editorOption.NumberEditorOption;
        enable: KnockoutObservable<Boolean>;
        readonly: KnockoutObservable<Boolean>;
        dirty: nts.uk.ui.DirtyChecker;

        constructor() {
            var self = this;
            self.value = ko.observable(12);
            self.constraint = '';
            self.option = ko.mapping.fromJS(new editorOption.NumberEditorOption({ grouplength: 3, decimallength: 2 }));
            self.enable = ko.observable(true);
            self.readonly = ko.observable(false);
            self.dirty = new nts.uk.ui.DirtyChecker(self.value)
        }
        reset() {
            var self = this;
            self.dirty.reset();
        };
        checkDirty() {
            var self = this;
            if (self.dirty.isDirty()) {
                alert("Data is changed.");
            } else {
                alert("Data isn't changed.");
            }
        };
        reload() {
            var self = this;
            location.reload(true);
        }
    }

    var viewmodel = new ScreenModel();
    nts.uk.ui.confirmSave(viewmodel.dirty);
    this.bind(viewmodel);
});