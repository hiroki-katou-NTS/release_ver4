module nts.uk.pr.view.base.postcode {
    export module service {

        var pathService: any = {
            findPostCodeZipCode: "ctx/pr/core/postcode/find"
        };

        export function findPostCodeZipCode(zipCode: string): JQueryPromise<model.PostCode[]> {
            return nts.uk.request.ajax(pathService.findPostCodeZipCode + '/' + zipCode);
        }

        export function findPostCodeZipCodeToRespone(zipCode: string): JQueryPromise<model.PostCodeRespone> {
            var dfd = $.Deferred<model.PostCodeRespone>();
            var postCodeRespone: model.PostCodeRespone;
            postCodeRespone = new model.PostCodeRespone('0', '', null);
            if (zipCode && zipCode != '') {
                service.findPostCodeZipCode(zipCode).done(data => {
                    var datalength: number = 0;
                    if (data != null) {
                        datalength = data.length;
                    }
                    if (datalength == 0) {
                        postCodeRespone = new model.PostCodeRespone('0', 'ER010', null);
                        dfd.resolve(postCodeRespone);
                    }
                    if (datalength == 1) {
                        postCodeRespone = new model.PostCodeRespone('1', '', data[0]);
                        dfd.resolve(postCodeRespone);
                    }
                    if (datalength >= 2) {
                        postCodeRespone = new model.PostCodeRespone('2', '', null);
                        dfd.resolve(postCodeRespone);
                    }
                }).fail(function(error) {
                    postCodeRespone = new model.PostCodeRespone('0', error.messageId, null);
                    dfd.resolve(postCodeRespone);
                });
            }
            else {
                postCodeRespone = new model.PostCodeRespone('0', 'ER001', null);
                dfd.resolve(postCodeRespone);
            }
            return dfd.promise();
        }

        export function findPostCodeZipCodeSelection(zipCode: string): JQueryPromise<model.PostCodeRespone> {
            var dfd = $.Deferred<model.PostCodeRespone>();
            var postCodeRespone: model.PostCodeRespone;
            postCodeRespone = new model.PostCodeRespone('0', '', null);

            nts.uk.ui.windows.setShared('zipCode', zipCode);
            nts.uk.ui.windows.sub.modal("/view/base/postcode/index.xhtml", { height: 400, width: 530, title: "郵便番号" }).onClosed(() => {
                var zipCodeRes: model.PostCode = nts.uk.ui.windows.getShared('zipCodeRes');
                if (zipCodeRes) {
                    postCodeRespone = new model.PostCodeRespone('1', '', zipCodeRes);
                    dfd.resolve(postCodeRespone);
                } else {
                    postCodeRespone = new model.PostCodeRespone('0', 'ER010', null);
                    dfd.resolve(postCodeRespone);
                }
            });
            return dfd.promise();
        }
        
        //to address => UI
        export function toAddress(postCode: model.PostCode): string {
            return postCode.prefectureName + ' ' + postCode.municipalityName + ' ' + postCode.townName;
        }
        
        //to kana => UI
        export function toKana(postCode: model.PostCode): string {
            return postCode.prefectureNameKn + ' ' + postCode.municipalityNameKn + ' ' + postCode.townNameKn;
        }
        export module model {

            export class PostCode {
                /** The id. */
                id: string;

                /** The local gov code. */
                localGovCode: string;

                /** The postcode. */
                postcode: string;

                /** The prefecture name kn. */
                prefectureNameKn: string;

                /** The municipality name kn. */
                municipalityNameKn: string;

                /** The town name kn. */
                townNameKn: string;

                /** The prefecture name. */
                prefectureName: string;

                /** The municipality name. */
                municipalityName: string;

                /** The town name. */
                townName: string;

            }

            export class PostCodeRespone {

                errorCode: string; //0 error 010 // error 
                message: string; // message (001)
                postcode: PostCode; // error =1 (size 1)
                constructor(errorCode: string, message: string, postcode: PostCode) {
                    this.errorCode = errorCode;
                    this.message = message;
                    this.postcode = postcode;
                }
            }
        }
    }
}