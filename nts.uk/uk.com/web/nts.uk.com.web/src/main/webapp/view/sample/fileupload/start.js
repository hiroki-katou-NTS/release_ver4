__viewContext.ready(function () {
    $("#button").click(function () {
        $("#file_upload").ntsFileUpload({ stereoType: "flowmenu" }).done(function (res) {
            alert(res);
        }).fail(function (err) {
            nts.uk.ui.dialog.alertError(err);
        });
    });
    $("#button1").click(function () {
        $("#fileupload1").ntsFileUpload({ stereoType: "flowmenu" }).done(function (res) {
            alert(res);
        }).fail(function (err) {
            nts.uk.ui.dialog.alertError(err);
        });
    });
    $("#download").click(function () {
        nts.uk.request.specials.donwloadFile($("#fileid").val());
    });
    var ScreenModel = (function () {
        function ScreenModel() {
            filename = ko.observable("");
            accept = ko.observableArray([".txt", '.xlsx']);
            textId = ko.observable("KMF004_106");
        }
        return ScreenModel;
    }());
    this.bind(new ScreenModel());
});
//# sourceMappingURL=start.js.map