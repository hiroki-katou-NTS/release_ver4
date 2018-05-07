module cps001.d.vm {
    import text = nts.uk.resource.getText;
    import alert = nts.uk.ui.dialog.alert;
    import alertError = nts.uk.ui.dialog.alertError;
    import confirm = nts.uk.ui.dialog.confirm;
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import permision = service.getCurrentEmpPermision;

    let __viewContext: any = window['__viewContext'] || {},
        block = window["nts"]["uk"]["ui"]["block"]["grayout"],
        unblock = window["nts"]["uk"]["ui"]["block"]["clear"],
        invisible = window["nts"]["uk"]["ui"]["block"]["invisible"];

    export class ViewModel {
        empFileMn: KnockoutObservable<IEmpFileMn> = ko.observable(<IEmpFileMn>{});
        oldEmpFileMn = {};
        isChange: KnockoutObservable<boolean> = ko.observable(false);
        enaBtnSave: KnockoutObservable<boolean> = ko.observable(true);
        isInit = true;

        constructor() {
            let self = this;
        }
        start() {
            let self = this,
                params: IEmpFileMn = getShared("CPS001D_PARAMS");
            $('input[type=checkbox]').prop('checked', false);
            $(".comfirm-checkbox").hide();

            self.empFileMn().employeeId = params.employeeId;
            //get employee file management domain by employeeId
            block();
            service.getFullAvatar(self.empFileMn().employeeId).done(function(data) {
                if (data.fileId != null) {
                    self.empFileMn().fileId = data.fileId;
                    self.empFileMn().fileType = 0;
                    if (self.empFileMn().fileId != "" && self.empFileMn().fileId != undefined)
                        self.getImage();
                    else self.isChange(true);
                    self.oldEmpFileMn = { employeeId: self.empFileMn().employeeId, fileId: self.empFileMn().fileId, fileType: self.empFileMn().fileType };
                } else {
                    unblock();
                    self.isChange(true);
                    $(".checkbox-holder").hide();
                }
                $("#test").bind("imgloaded", function(evt, query?: SrcChangeQuery) {
                    // update height 
                    var currentDialog = nts.uk.ui.windows.getSelf();
                    currentDialog.setHeight(840);
                    $(".checkbox-holder").show();
                    $('input[type=checkbox]').prop('checked', true);
                    if (!self.isInit) {
                        self.isChange(true);
                        unblock();
                        // check size if req
                        
                       /*  if (query.size > 10485760) { // 10485760 = 10MB
                            self.hasError = true;
                            alertError({ messageId: "Msg_77" });
                        } */
                        
                        return;
                    }
                    self.isInit = false;
                    unblock();
                });

            }).fail((mes) => {
                unblock();
            });

            permision().done((data: IPersonAuth) => {
                if (data) {
                    if (data.allowAvatarUpload != 1) {
                        self.enaBtnSave(false);
                        $(".upload-btn").attr('disabled', 'disabled');
                        $('input[type=checkbox]').prop('disabled', true);
                    }
                }
            });

            $('.upload-btn').focus();
        }

        upload() {
            let self = this;
            nts.uk.ui.block.grayout();

            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            let isImageLoaded = $("#test").ntsImageEditor("getImgStatus");

            if (isImageLoaded.imgOnView) {

                if ($("#test").data("cropper") == undefined) {
                    self.close();
                    return;
                }
                if ($("#test").data("cropper").cropped)
                    self.isChange(true);

                if (self.isChange()) {
                    $("#test").ntsImageEditor("upload", { stereoType: "avatarfile" }).done(function(data1) {

                        self.empFileMn().fileId = data1.id;

                        $("#test").ntsImageEditor("uploadOriginal", { stereoType: "avatarfile" }).done(function(data2) {

                            let emp = { employeeId: self.empFileMn().employeeId, fileId: data1.id, fileType: 0, fileIdnew: data2.id, isAvatar: true };
                            self.updateImage(emp);
                        });

                    });
                } else self.close();
            } else {
                alertError({ messageId: "Msg_617" });
                nts.uk.ui.block.clear();
            }
        }

        updateImage(emp) {
            let self = this;
            service.checkEmpFileMnExist(emp.employeeId).done(function(isExist) {
                if (isExist) {
                    confirm({ messageId: "Msg_386", messageParams: [nts.uk.resource.getText("CPS001_68")] }).ifYes(() => {
                        //insert employee file management
                        block();
                        service.removeAvaOrMap(emp).done(function() {
                            service.insertAvaOrMap(emp).done(function() {
                                setShared("CPS001D_VALUES", ko.unwrap(self.empFileMn));
                                unblock();
                                self.close();
                            }).always(function() { nts.uk.ui.block.clear(); });
                        }).fail((mes) => {
                            unblock();
                        });
                    }).ifNo(() => {
                        nts.uk.ui.block.clear();
                    });

                } else {
                    //insert employee file management
                    block();
                    service.insertAvaOrMap(emp).done(function() {
                        setShared("CPS001D_VALUES", ko.unwrap(self.empFileMn));
                        unblock();
                        self.close();
                    }).fail((mes) => {
                        unblock();
                    }).always(function() {
                        nts.uk.ui.block.clear();
                    });
                }
            });
        }

        getImage() {
            let self = this;
            let id = self.empFileMn().fileId;
            try {
                 $("#test").ntsImageEditor("selectByFileId", {fileId: id, actionOnClose: function(){
                     close();   
                }});
            } catch (Error) {
                self.isChange(true);
            }
        }

        close() {
            nts.uk.ui.block.clear();
            close();
        }
    }

    interface IPersonAuth {
        roleId: string;
        allowMapUpload: number;
        allowMapBrowse: number;
        allowDocRef: number;
        allowDocUpload: number;
        allowAvatarUpload: number;
        allowAvatarRef: number;
    }


    interface IEmpFileMn {
        employeeId: string;
        fileId?: string;
        fileType?: number;
        isAvatar: boolean;
    }
}