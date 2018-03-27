module nts.uk.at.view.ktg001.a.viewmodel {
    export class ScreenModel {
      

        constructor() {
            let self = this;

        }

        /**
         * startPage
         */
        public startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
        }
}
}