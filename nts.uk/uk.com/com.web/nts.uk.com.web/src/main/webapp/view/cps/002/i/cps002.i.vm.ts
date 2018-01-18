module cps002.i.vm {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import close = nts.uk.ui.windows.close;
    import modal = nts.uk.ui.windows.sub.modal;
    import alert = nts.uk.ui.dialog.alert;
    import alertError = nts.uk.ui.dialog.alertError;
    import getText = nts.uk.resource.getText;


    export class ViewModel {
        imageId: KnockoutObservable<IImageId> = ko.observable(<IImageId>{});
        isChange: KnockoutObservable<boolean> = ko.observable(false);
        isInit = true;
        isEdit: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this;



            self.imageId.subscribe((newId) => {
                if (newId) {
                    $(".checkbox-holder").show();
                } else {
                    $(".checkbox-holder").hide();
                }
            });

            $("#test").bind("imgloaded", function(evt, query?: SrcChangeQuery) {
                $(".checkbox-holder").show();
            });
        }
        start() {
            let self = this;
            let dImageId = getShared("CPS002A");

            if (dImageId != "" && dImageId != undefined) {
                self.imageId().defaultImgId = dImageId;
                self.getImage();
                $("#test").bind("imgloaded", function(evt, query?: SrcChangeQuery) {
                    if (!self.isInit) {
                        self.isChange(true);
                        return;
                    }
                    self.isInit = false;
                });
            } else self.isChange(true);
            $(".upload-btn").focus();
        }
        upload() {
            let self = this;
            nts.uk.ui.block.grayout();
            let isImageLoaded = $("#test").ntsImageEditor("getImgStatus");
            if ($("#test").data("cropper") == undefined) {
                self.close();
                return;
            }
            if ($("#test").data("cropper").cropped)
                self.isChange(true);
            if (isImageLoaded.imgOnView) {
                if (self.isChange()) {
                    $("#test").ntsImageEditor("upload", { stereoType: "image" }).done(function(data) {
                        self.imageId().cropImgId = data.id;
                        $("#test").ntsImageEditor("uploadOriginal", { stereoType: "original-img" }).done(function(data2) {
                            self.imageId().defaultImgId = data2.id;
                            nts.uk.ui.block.clear();

                            self.close();
                        });


                    });
                } else self.close();
            } else self.close();
        }
        getImage() {
            let self = this;
            let id = self.imageId().defaultImgId;
            $("#test").ntsImageEditor("selectByFileId", id);
        }
        close() {
            let self = this;
            nts.uk.ui.block.clear();
            let result = self.imageId().cropImgId ? self.imageId() : undefined;

            setShared("imageId", result);
            close();
        }

    }

    export interface IImageId {
        defaultImgId: string;
        cropImgId: string;
    }
}