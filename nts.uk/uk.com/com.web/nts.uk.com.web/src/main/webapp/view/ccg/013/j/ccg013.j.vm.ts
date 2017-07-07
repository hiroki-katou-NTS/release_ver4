module nts.uk.sys.view.ccg013.j.viewmodel {
    import windows = nts.uk.ui.windows;

    export class ScreenModel {
        //Text edittor
        nameTitleBar: KnockoutObservable<string>;
        //colorpicker
        letterColor: KnockoutObservable<string>;
        backgroundColor: KnockoutObservable<string>;
        //file Upload
        filename: KnockoutObservable<string>;
        imageName: KnockoutObservable<string>;
        imageSize: KnockoutObservable<string>;
        textId: KnockoutObservable<string>;
        accept: KnockoutObservableArray<string>;
        fileID: KnockoutObservable<string>;
        //delete Button 
        isDelete: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.nameTitleBar = ko.observable("");
            //color picker
            self.letterColor = ko.observable('');
            self.backgroundColor = ko.observable('');
            //delete button 
            self.isDelete = ko.observable(false);
            //image upload
            self.filename = ko.observable(""); 
            //file name
            self.imageName = ko.observable("");
            self.imageSize = ko.observable(nts.uk.text.format(resource.getText('CCG013_99'),0));
            self.accept = ko.observableArray([".png"]);
            //supported extension
            self.textId = ko.observable("");
            // file browser button text id
            self.fileID = ko.observable('');
            var liveviewcontainer = $("#liveview");
            var setShareTitleMenu = nts.uk.ui.windows.getShared("CCG013A_ToChild_TitleBar");
            if(setShareTitleMenu !== undefined){
                self.fileID(setShareTitleMenu.imageFile);
                self.nameTitleBar(setShareTitleMenu.titleMenuName);
                self.letterColor(setShareTitleMenu.textColor);
                self.backgroundColor(setShareTitleMenu.backgroundColor);
                liveviewcontainer.html("");
                liveviewcontainer.append($("<img/>").attr("src", nts.uk.request.resolvePath("/webapi/shr/infra/file/storage/liveview/" + setShareTitleMenu.imageId)));
            }
            self.fileID.subscribe(function(id) {
                if (id) {
                    liveviewcontainer.html("");
                    liveviewcontainer.append($("<img/>").attr("src", nts.uk.request.resolvePath("/webapi/shr/infra/file/storage/liveview/" + id)));
                }
            });
        }

        /** Upload File */
        uploadFile(): void {
            var self = this;
            self.uploadFileProcess();
        }

        private uploadFileProcess(): void {
            var self = this;
            var option = {
                stereoType: "titleBar",
                onSuccess: function() { alert('upload Success'); },
                onFail: function() { alert('upload Fails') }
            }
            $("#file_upload").ntsFileUpload({ stereoType: "titleBar" }).done(function(res) {
                self.fileID(res[0].id);
                self.filename('');
                self.imageName(res[0].originalName);
                self.imageSize(nts.uk.text.format(resource.getText('CCG013_99'),res[0].originalSize));
                self.isDelete(true);
            }).fail(function(err) {
                nts.uk.ui.dialog.alertError(err.message);
            });
        }

        private deleteFile(): void {
            var self = this;
            self.imageName('');
            self.imageSize(nts.uk.text.format(resource.getText('CCG013_99'),0));
            $("#liveview").html('');
            self.isDelete(false);
        }

        cancel_Dialog(): any {
            nts.uk.ui.windows.close();
        }

        submit() {
            var self = this;
            console.time('タイトルバー編集');
            var titleBar = new TitleBar(self.nameTitleBar(), self.letterColor(), self.backgroundColor(), self.fileID());
            windows.setShared("CCG013J_ToMain_TitleBar", titleBar);
            console.timeEnd('タイトルバー編集');
            self.cancel_Dialog();
        }
    }

    class TitleBar {
        nameTitleBar: string;
        letterColor: string;
        backgroundColor: string;
        imageId: string;
        constructor(nameTitleBar: string, letterColor: string, backgroundColor: string, imageId: string) {
            this.nameTitleBar = nameTitleBar;
            this.letterColor = letterColor;
            this.backgroundColor = backgroundColor;
            this.imageId = imageId;
            }
    }
    
}