import text = nts.uk.resource.getText;

ko.components.register('kaf022-a10', {
    viewModel: {
        createViewModel: function(params, componentInfo) {
            let vm = {
                itemListA10_3: ko.observableArray([
                    { code: 1, name: text('KAF022_75') },
                    { code: 0, name: text('KAF022_82') }
                ]),
                itemListA5_14: ko.observableArray([
                    { code: 1, name: text('KAF022_36') },
                    { code: 0, name: text('KAF022_37') }
                ]),
                itemListA5_16: ko.observableArray([
                    { code: 0, name: '0' },
                    { code: 1, name: '1' },
                    { code: 2, name: '2' },
                    { code: 3, name: '3' },
                    { code: 4, name: '4' },
                    { code: 5, name: '5' },
                    { code: 6, name: '6' },
                    { code: 7, name: '7' },
                ]),
            };

            _.extend(vm, {
                selectedIdA10_3: params.selectedIdA10_3,
                selectedIdA5_14: params.selectedIdA5_14,
                selectedCodeA5_16: params.selectedCodeA5_16,
                selectedIdA5_18: params.selectedIdA5_18,
                selectedIdA5_19: params.selectedIdA5_19,
                selectedIdA5_20: params.selectedIdA5_20,
                selectedIdA5_21: params.selectedIdA5_21,
                selectedIdA5_22: params.selectedIdA5_22,
                selectedIdA5_23: params.selectedIdA5_23,
            });

            return vm;
        }
    },
    template: `
    <!-- ko let: { $av: $data, text: nts.uk.resource.getText } -->
    <div class="fixed-table-container" style="width: 900px;">
        <table id="fixed-table-a10">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_86')"></th>
                </tr>
            </thead>
            <tbody style="max-height: 536px;">
                <tr>
                    <td style="width: 250px" class="color-header" id="a10_2" data-bind="text: text('KAF022_87')"></td>
                    <td style="width: 650px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA10_3,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA10_3,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="fixed-table-container" style="width: 900px; margin-top: 20px;">
        <table id="fixed-table-a10">
            <thead>
                <tr>
                    <th class="color-header" colspan="2" data-bind="text: text('KAF022_22')"></th>
                </tr>
            </thead>
            <tbody style="max-height: 536px;">
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_2" data-bind="text: text('KAF022_23')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_14,
                                enable: true
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_3" data-bind="text: text('KAF022_24')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <span class="label" data-bind="text: text('KAF022_38')"></span>
                        <div tabindex="17" id="a5_16"
                                                data-bind="ntsComboBox: {
                                                options: itemListA5_16,
                                                optionsValue: 'code',
                                                visibleItemsCount: 8,
                                                value: selectedCodeA5_16,
                                                optionsText: 'name',
                                                editable: false,
                                                enable: false,
                                                columns: [
                                                    { prop: 'name', length: 2 },
                                                ]}"></div>
                        <span class="label" data-bind="text: text('KAF022_40')"></span>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="a5_4 " data-bind="text: text('KAF022_25')"></td>
                    <td></td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_5" data-bind="text: text('KAF022_26')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_18,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_6" data-bind="text: text('KAF022_27')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_19,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_7" data-bind="text: text('KAF022_28')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_20,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="color-header" id="a5_8 " data-bind="text: text('KAF022_29')"></td>
                    <td></td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_9" data-bind="text: text('KAF022_30')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_21,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_10" data-bind="text: text('KAF022_31')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_22,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 250px" class="color-header" id="a5_11" data-bind="text: text('KAF022_32')"></td>
                    <td style="width: 800px" class="k12-radio_1">
                        <div tabindex="1" data-bind="ntsRadioBoxGroup: {
                                options: itemListA5_14,
                                optionsValue: 'code',
                                optionsText: 'name',
                                value: selectedIdA5_23,
                                enable: false
                            }">
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <!-- /ko -->
    `
});