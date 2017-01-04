﻿/*!@license
* Infragistics.Web.ClientUI Validator localization resources 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global jQuery */
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Validator) {
	    $.ig.Validator = {
		    locale: {
			    defaultMessage: 'Пожалуйста, исправьте это поле',
			    selectMessage: 'Пожалуйста, выберите значение',
			    rangeSelectMessage: 'Пожалуйста, выберите не более {0} и не менее {1} пунктов',
			    minSelectMessage: 'Пожалуйста, выберите по крайней мере {0} пунктов',
			    maxSelectMessage: 'Пожалуйста, выберите не более {0} пунктов',
			    rangeLengthMessage: 'Пожалуйста, введите значение в диапазоне от {0} до {1} символов',
			    minLengthMessage: 'Пожалуйста, введите по крайней мере {0} символов',
			    maxLengthMessage: 'Пожалуйста, введите не более {0) символов',
			    requiredMessage: 'Это поле является обязательным',
			    patternMessage: 'Введенные данные не соответствуют требуемому шаблону',
			    maskMessage: 'Пожалуйста, заполните все необходимые позиции',
			    dateFieldsMessage: 'Пожалуйста, введите значения в поля даты',
			    invalidDayMessage: 'Неверный день месяца. Пожалуйста, введите правильный день',
			    dateMessage: 'Пожалуйста, введите действительную дату',
			    numberMessage: 'Пожалуйста, введите действительный номер',
                rangeValueMessage: 'Пожалуйста, введите значение в диапазоне от {0} до {1}',
		        minValueMessage: 'Пожалуйста, введите значение больше или равно {0}',
		        maxValueMessage: 'Пожалуйста, введите значение меньше или равно {0}',
		        emailMessage: 'Следует ввести правильный адрес электронной почты',
		        equalToMessage: 'Эти два значения не совпадают',
		        optionalString: '(необязательно)'
		    }
	    };
    }
})(jQuery);