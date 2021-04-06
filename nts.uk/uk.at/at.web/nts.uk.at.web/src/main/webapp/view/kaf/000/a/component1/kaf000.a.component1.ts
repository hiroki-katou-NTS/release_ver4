module nts.uk.at.view.kaf000.a.component1.viewmodel {
    import CommonProcess = nts.uk.at.view.kaf000.shr.viewmodel.CommonProcess;

    @component({
        name: 'kaf000-a-component1',
        template: `
            <div id="kaf000-a-component1">
				<div data-bind="if: displayMsg">
					<div class="message-div">
						<div style="word-break: break-all; word-wrap: break-word;" data-bind="html: message"></div>
					</div>
				</div>
				<div data-bind="if: displayDeadline">
					<div class="message-div">
						<div data-bind="text: $i18n('KAF000_2')" style="padding-top: 20px; padding-bottom: 20px;"></div>
						<div data-bind="html: deadline"></div>
					</div>
				</div>
			</div>
        `
    })
    class Kaf000AComponent1ViewModel extends ko.ViewModel {
		appType: KnockoutObservable<number> = null;
        appDispInfoStartupOutput: any;
        message: KnockoutObservable<string>;
        deadline: KnockoutObservable<string>;
        displayMsg: KnockoutObservable<boolean>;
        displayDeadline: KnockoutObservable<boolean>;

        created(params: any) {
            const vm = this;
			vm.appType = params.appType;
            vm.appDispInfoStartupOutput = params.appDispInfoStartupOutput;
            vm.message = ko.observable("line111111111111111111");
            vm.deadline = ko.observable("line222222222222222222");
            vm.displayMsg = ko.observable(false);
            vm.displayDeadline = ko.observable(false);

            vm.appDispInfoStartupOutput.subscribe(value => {
                CommonProcess.initDeadlineMsg(value, vm);
            });
        }

        mounted() {
            const vm = this;
        }
    }
}