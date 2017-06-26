module nts.uk.pr.view.base.postcode {

    import PostCode = service.model.PostCode;

    export module viewmodel {
        export class ScreenModel {

            postCodeList: KnockoutObservableArray<PostCodeModel>;
            addressList: KnockoutObservableArray<PostCode>;
            zipCode: KnockoutObservable<string>;
            columns: KnockoutObservableArray<any>;

            constructor() {
                var self = this;
                self.postCodeList = ko.observableArray<PostCodeModel>([]);
                self.addressList = ko.observableArray<PostCode>([]);
                self.zipCode = ko.observable('');
                var strZipCode: string = nts.uk.ui.windows.getShared('zipCode');
                self.columns = ko.observableArray([
                    { headerText: '郵便番号', prop: 'postcode', width: 100 },
                    { headerText: '住所', prop: 'address', width: 150 },
                    { headerText: 'ｶﾅ', prop: 'kana', width: 150 }
                ]);
                service.findPostCodeZipCode(strZipCode).done(data => {
                    var postcodeList: PostCodeModel[];
                    postcodeList = [];
                    for (var itemData of data) {
                        postcodeList.push(new PostCodeModel(itemData));
                    }
                    self.postCodeList(postcodeList);
                    self.addressList(data);
                }).fail(function(error) {
                    console.log(error)
                });

            }

            private chooseZipCode() {
                var self = this;
                for (var itemZipCode of self.addressList()) {
                    if (itemZipCode.postcode == self.zipCode()) {
                        nts.uk.ui.windows.setShared('zipCodeRes', itemZipCode);
                        break;
                    }
                }
                nts.uk.ui.windows.close();
            }

        }
        export class PostCodeModel {
            address: string;
            kana: string;
            id: string;
            postcode: string;
            constructor(postcode: PostCode) {
                this.address = service.toAddress(postcode);
                this.postcode = postcode.postcode;
                this.id = postcode.id;
                this.kana = service.toKana(postcode);
            }
        }
    }
}