__viewContext.ready(function () {
    class ScreenModel {
        constructor() {
            var self = this;
        }
    }
    this.bind(new ScreenModel());
});

function OpenModalSubWindow(){
    nts.uk.ui.windows.sub.modal("/view/sample/subwindow/subwindow.xhtml");
}

function OpenModelessSubWindow(){
    nts.uk.ui.windows.sub.modeless("/view/sample/subwindow/subwindow.xhtml");
}

function openDialog() {
    nts.uk.ui.windows.sub.modeless("/view/sample/subwindow/dialog.xhtml");
}

function openDialogB() {
    nts.uk.ui.windows.sub.modeless("/view/sample/subwindow/dialogb.xhtml");
}