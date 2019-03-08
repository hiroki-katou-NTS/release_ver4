module validationcps009 {

    import nou = nts.uk.util.isNullOrUndefined;

    let __viewContext: any = window['__viewContext'] || {},
        rmError = nts.uk.ui.errors["removeByCode"],
        getError = nts.uk.ui.errors["getErrorByElement"],
        getErrorList = nts.uk.ui.errors["getErrorList"],
        removeErrorByElement = window['nts']['uk']['ui']['errors']["removeByElement"],
        clearError = window['nts']['uk']['ui']['errors']['clearAll'],
        parseTimeWidthDay = window['nts']['uk']['time']['minutesBased']['clock']['dayattr']['create'];

    export function initCheckError(items: Array<any>) {

        _.each(items, item => {
            // validate button, radio button
            let v: any = ko.toJS(item),
                id = v.perInfoItemDefId,
                element = document.getElementById(id),
                $element = $(element);

            switch (item.dataType()) {
                case ITEM_SINGLE_TYPE.STRING:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.stringValue.subscribe(d => {
                        !nou(d) && (rmError($element, "Msg_824") || rmError($element, "FND_E_REQ_INPUT"));
                    });
                    break;
                case ITEM_SINGLE_TYPE.NUMERIC:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.numbereditor.value.subscribe(d => {
                        !nou(d) && (rmError($element, "Msg_824") || rmError($element, "FND_E_REQ_INPUT"));
                    });
                    break;
                case ITEM_SINGLE_TYPE.TIME:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.dateWithDay.subscribe(d => {
                        !nou(d) && (rmError($element, "Msg_824") || rmError($element, "FND_E_REQ_INPUT"));
                    });
                    break;
                case ITEM_SINGLE_TYPE.TIMEPOINT:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.dateWithDay.subscribe(d => {
                        !nou(d) && (rmError($element, "Msg_824") || rmError($element, "FND_E_TIME"));
                    });
                    break;
                case ITEM_SINGLE_TYPE.SEL_BUTTON:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.selectionName.subscribe(d => {
                        !nou(d) && rmError($element, "Msg_824");
                    });
                    break;
                case ITEM_SINGLE_TYPE.DATE:
                    rmError($element, "FND_E_REQ_INPUT") || rmError($element, "Msg_824");
                    item.dateValue.subscribe(d => {
                        !nou(d) && (rmError($element.find('.nts-input'), "Msg_824") || rmError($element.find('.nts-input'), "FND_E_DATE_YMD"));
                    });
                    break;
                default: break;

            }

        });

    }

    export function checkError(items: Array<any>) {
        _.each(items, item => {
            let v: any = ko.toJS(item),
                id = v.perInfoItemDefId,
                element = document.getElementById(id),
                $element = $(element);
            if (element.tagName.toUpperCase() == "INPUT" && !!v.enableControl) {

                switch (item.dataType()) {

                    case ITEM_SINGLE_TYPE.STRING:
                        if (_.isNil(item.stringValue()) || _.isEmpty(item.stringValue())) {
                            $element.ntsError('set', {
                                messageId: "Msg_824",
                                messageParams: [item.itemName()]
                            });
                        }
                        break;

                    case ITEM_SINGLE_TYPE.NUMERIC:
                        if (_.isNil(item.numbereditor.value())) {
                            $element.ntsError('set', {
                                messageId: "Msg_824",
                                messageParams: [item.itemName()]
                            });

                        }
                        break;

                    case ITEM_SINGLE_TYPE.TIME:
                        if (_.isNil(item.dateWithDay())) {
                            $element.ntsError('set', {
                                messageId: "Msg_824",
                                messageParams: [item.itemName()]
                            });
                        }
                        break;

                    case ITEM_SINGLE_TYPE.TIMEPOINT:
                        if (_.isNil(item.dateWithDay())) {
                            $element.ntsError('set', {
                                messageId: "Msg_824",
                                messageParams: [item.itemName()]
                            });
                        }
                        break;

                    default: break;

                }
               

            } else if (element.tagName.toUpperCase() == "BUTTON") {
                if (_.isNil(item.selectionName()) || _.isEmpty(item.selectionName())) {
                    $element.ntsError('set', {
                        messageId: "Msg_824",
                        messageParams: [item.itemName()]
                    });
                }
            } else  if ((item.dataType()== 6)  && !!v.enableControl &&(_.isNil(item.selectedCode()) || _.isEmpty(item.selectedCode()))) {
                $element.find('.nts-input').attr('nameid', item.itemName());
                $element.addClass("error");
                $element.ntsError('set', {
                    messageId: "Msg_824",
                    messageParams: [item.itemName()]
                });
            } else if((item.dataType()== 3) && _.isNil(item.dateValue()) && !!v.enableControl){
                $element.addClass("error");
                $element.find('.nts-input').attr('nameid', item.itemName());
                $element.find('.nts-input').ntsError('set', {
                    messageId: "Msg_824",
                    messageParams: [item.itemName()]
                });
            }
        });
    }

    export enum ITEM_SINGLE_TYPE {
        STRING = 1,
        NUMERIC = 2,
        DATE = 3,
        TIME = 4,
        TIMEPOINT = 5,
        SELECTION = 6,
        SEL_RADIO = 7,
        SEL_BUTTON = 8,
        READONLY = 9,
        RELATE_CATEGORY = 10,
        NUMBERIC_BUTTON = 11,
        READONLY_BUTTON = 12
    }
}

