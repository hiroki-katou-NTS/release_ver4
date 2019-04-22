module view.ccg007.sso {
    export module viewmodel {
        export class ScreenModel {
            constructor() {
            }

            account(){
                service.account().done(data => {
                    alert('domain: ' + data.domain + '\n' + 'user name: ' + data.userName)
                });
            }
        }
    }
}