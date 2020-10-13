module nts.uk.com.view.cmm018.x.viewmodel {
	@bean()
	export class Cmm018XViewModel extends ko.ViewModel {
		systemAtr: KnockoutObservable<number> = ko.observable(SystemAtr.EMPLOYMENT);
		created() {
			const self = this;
			let url = $(location).attr('search');
            let urlParam: number = url.split("=")[1];
			self.systemAtr(urlParam || SystemAtr.EMPLOYMENT);
		}
		mounted() {

		}

		openDialogQ() {
			console.log('openDialogQ');
			const self = this;
			let param = {
				systemAtr: ko.toJS(self.systemAtr)
			}
			self.$window
				.modal('com', '/view/cmm/018/q/index.xhtml', param)
				.then((result: any) => {
					// bussiness logic after modal closed
					location.reload();
				});
		}
	}
	export const SystemAtr = {
		EMPLOYMENT: 0,
		HUMAN_RESOURSE: 1
	}

}