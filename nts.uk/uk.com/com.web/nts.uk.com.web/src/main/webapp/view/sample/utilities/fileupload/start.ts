__viewContext.ready(function() {

    $("#button").click(function() {
        $("#custom-upload").ntsFileUpload({ stereoType: "flowmenu" }).done(function(res) {
            nts.uk.ui.dialog.info("Upload successfully!");
        }).fail(function(err) {
            nts.uk.ui.dialog.alertError(err);
        });
    });

    class ScreenModel {
        fileId: KnockoutObservable<string>;
        filename: KnockoutObservable<string>;
        fileInfo: KnockoutObservable<any>;
        textId: KnockoutObservable<string>;
        accept: KnockoutObservableArray<string>;
        enable: KnockoutObservable<boolean>;
        onchange: (filename) => void;
        onfilenameclick: (filename) => void;
        
        constructor() {
            this.fileId = ko.observable("");
            this.filename = ko.observable("");
            this.fileInfo = ko.observable(null);
            this.accept = ko.observableArray([".png", '.gif', '.jpg', '.jpeg']);
            this.textId = ko.observable("KMF004_106");
            this.enable = ko.observable(true);
            this.onchange = (filename) => {
                console.log(filename);
            };
            this.onfilenameclick = (filename) => {
                alert(filename);
            };
        }

        upload() {
            var self = this;
            $("#file-upload").ntsFileUpload({ stereoType: "flowmenu" }).done(function(res) {
                self.fileId(res[0].id);
            }).fail(function(err) {
                nts.uk.ui.dialog.alertError(err);
            });
        }

        download() {
            console.log(nts.uk.request.specials.donwloadFile(this.fileId()));
        }
        
        preview() {
            var self = this;
            var liveviewcontainer = $("#file-review");
            liveviewcontainer.html("");
            liveviewcontainer.append($("<img/>").attr("src", nts.uk.request.liveView(self.fileId())));
            liveviewcontainer.append($("<iframe/>").attr("src", nts.uk.request.liveView(self.fileId())));
        }
        
        getInfo() {
            var self = this;
            nts.uk.request.ajax("/shr/infra/file/storage/infor/" + this.fileId()).done(function(res) {
                self.fileInfo(res);
            });
        }

    }

    this.bind(new ScreenModel());

});