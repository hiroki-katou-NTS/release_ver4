module nts.uk.at.view.kdw006 {
    export module viewmodel {
        export class ScreenModel {
            constructor() {
            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                dfd.resolve();
                return dfd.promise();
            }

            opendScreenA() {
                nts.uk.request.jump("/view/kdw/002/d/index.xhtml");
            }

            opendScreenB() {
                nts.uk.request.jump("/view/kdw/002/a/index.xhtml");
            }
            
            opendScreenC() {
                nts.uk.request.jump("/view/kdw/007/a/index.xhtml");
            }
            
            opendScreenD() {
                nts.uk.request.jump("/view/kdw/008/b/index.xhtml");
            }
            
            opendOperationSetting() {
                nts.uk.request.jump("/view/kdw/006/b/index.xhtml");
            }
        }
    }
}
