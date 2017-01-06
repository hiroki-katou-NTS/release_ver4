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
		        defaultMessage: "このフィールドは無効です",
		        selectMessage: "値を選択してください",
		        rangeSelectMessage: "{0} 以上で {1} 以下の項目を選択してください。",
		        minSelectMessage: "{0} 項目以上を選択してください",
		        maxSelectMessage: "{0} 項目以下を選択してください",
		        rangeLengthMessage: "入力の長さは {0} ～ {1} の間の文字数である必要があります",
		        minLengthMessage: "入力の長さは少なくとも {0} 文字である必要があります",
		        maxLengthMessage: "入力の長さは {0} 文字以下である必要があります",
			    requiredMessage: "このフィールドは必須フィールドです。",
			    patternMessage: '入力が所定のパターンに一致しません',
			    maskMessage: "すべての必須な文字を入力してください",
			    dateFieldsMessage: "日付のフィールド値を入力してください",
			    invalidDayMessage: "月の有効な日を入力してください",
			    dateMessage: "有効な日付を入力してください",
			    numberMessage: "有効な数値を入力してください",
			    rangeValueMessage: "{0} ~ {1} の値を入力してください",
			    minValueMessage: "{0} 以上の値を入力してください",
			    maxValueMessage: "{0} 以下の値を入力してください",
			    emailMessage: '有効なメール アドレスを入力してください',
			    equalToMessage: '2 つの値は一致しません',
			    optionalString: '(オプション)'
		    }
	    };
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Editors 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
* Depends on:
* jquery-1.9.1.js
* jquery.ui.core.js
* jquery.ui.widget.js
* infragistics.util.js
* infragistics.ui.popover.js
* infragistics.ui.notifier.js

* Example to use:
*	<script type="text/javascript">
*	$(function () {
*		$('#text1').igValidator({ minLength: 3 });
*	});
*	</script>
*	<input id="text1" type="text" />
*/
/*global jQuery*/
(function ($) {
/*
	igValidator is a widget based on jQuery UI that provides functionality to validate value in target and show appropriate error message.
	It can be attached to a INPUT, TEXTAREA, SELECT html element in order to validate its value or entire FORM (or inner container) to handle multiple fields at once.
	It can also be attached to a igEditors, igCombo and igRating.
	Validation can be triggered on various events like onchange, onblur, onsubmit.
	Every igValidator may have its own rules and enable/disable specific trigger-validation events, fields collection can provide thier own rules as well as inherit from the main configuretion.
	If target element is INPUT type=radio/checkbox, then all elements with attribute name of that target are validated.
	If validator is enabled for igEditor, then any specific failure like not filled required positions in igMaskEditor or invalid day of month in igDateEditor, etc, will trigger validation and show corresponding message.

	In order to customize the default submit behavior globally for all widgets, the "$.ui.igValidator.defaults" object can be used to allow showing all errors when validating a form. Default is true.
	Example:
	$.ui.igValidator.defaults.showAllErrorsOnSubmit = false;
*/
$.widget("ui.igValidator", {
	options: {
		/* type="bool" Gets or sets triggering validation when value in editor was changed. */
		onchange: false,
		/* type="bool" Gets or sets triggering validation when editor lost focus. */
		onblur: true,
		/* type="bool" Gets or sets triggering validation when form gets onsubmit event. */
		onsubmit: true,

		/* type="bool|object" Gets or sets option to validate if value was entered (not empty text, selected item, etc.)
			bool type="bool" A boolean value indicating if the field is required.
			object type="object" A configuration object with optional error message (e.g. required: { errorMessage: "Error!"} )
			*/
		required: false,
		/* type="bool|object" Gets or sets option to validate if value is a number. Default separators for decimals and thousands are '.' and ',' respectively.
			bool type="bool" A boolean value indicating if the field should be a number.
			object type="object" A configuration object with errorMessage, decimalSeparator and thousandsSeparator. Those properties are all optional.
			*/
		number: false,
		/* type="bool|object" Gets or sets option to validate if value is a date. This can help guide the valueRange validation.
			Note: Dependat on JavaScript Date parsing which will accept a wide range of values.
			bool type="bool" A boolean value indicating if the field should be a valid JavaScript Date or can be parsed as one.
			object type="object" A configuration object with optional error message (e.g. date: { errorMessage: "Enter a valid number"} )
		*/
		date: false,
		/* type="bool|object" Gets or sets option to validate if value is an email.
			Note: Dependat on JavaScript Date parsing which will accept a wide range of values.
			bool type="bool" A boolean value indicating if the field should be an email.
			object type="object" A configuration object with optional error message (e.g. email: { errorMessage: "Enter a valid email"} )
		*/
		email: false,
		/* type="array|object" Gets or sets minimum and maximum length of text or number of selected items. Null or 0 values are ignored.
			array type="array" An array of two numbers, where the first value is the minimum and the second is the maximum. (e.g. lengthRange: [ 1, 10] )
			object type="object" A configuration object with optional error message. Message strings can contain format items for min and max respecitively (e.g. lengthRange: { min: 6, max: 20, errorMessage: "Password must be at least {0} long and no more than {1}." } )
			*/
		lengthRange: null,
		/* type="array|object" Gets or sets minimum and maximum values. Null values are ignored.
			array type="array" An array of two numbers or dates, where the first is the minimum and the second is the maximum. (e.g. valueRange: [ 1, 10] )
			object type="object" A configuration object with optional error message. Message strings can contain format items for min and max respecitively (e.g. lengthRange: { min: 6, max: 20, errorMessage: "Value must be between {0} and {1}." } )
			*/
		valueRange: null,
		/* type="string|object" Gets or sets regular expression which is used to validate value in text editor.
			string type="string" A string containing regular expression
			object type="RegExp" A RegExp object or an object with expression and errorMessage properties.
		*/
		pattern: null,
		/* type="dom" Gets or sets custom jQuery element, which innerHTML will be used to show validation messages. That can be SPAN, LABEL or DIV. */
		messageTarget: null,
		/* type="string" Gets or sets text for error-message to be used if none is set for the particular rule. Overrides default rule-specific error messages.*/
		errorMessage: null,
		/* type="string" Gets or sets text for success message. Note there is no default, so setting this message will enable showing success indications. */
		successMessage: null,
		/* type="number" Gets or sets validation minimum input length. Validation won't be triggered for input before that value is reached on change and blur.
			Note: This will have no effect on required fields on form submit.*/
		threshold: -1,
		/* type="string|object" Requires the value in this field to be the same as another input element or editor control.
			string type="string" A valid jQuery selector for the target element
			object type="object" A reference to the jQuery object for the target or an object with selector property and custom errorMessage.
		*/
		equalTo: null,
		/* type="function|string|object" Gets or sets a custom function to perform validation. Use 'this' to reference the calling validator and the value and optional field settings arguement to determine and return the state of the field.
			function type="function" The function to call
			string type="string" Function name, must be in global namespace (window["name"])
			object type="object" A configuration object with method property being the function and optional error message.
		*/
		custom: null,
		/* type="array" List of field items describing each field with validation options and a required selector. Fields can contain any of the validation rules and triggers but not other fields or event handlers.
			Accepted options are also inherited from the global control configuration if not set.
		*/
		fields: [{
			/* type="string|object" The DOM element (input or control target) to be validated. This field setting is required.
				string type="string" A valid jQuery selector for the element
				object type="object" A reference to a jQuery object
			*/
			selector: null
		}],
		/* type="object" Options for the igNotifier used to show error messages. */
		notificationOptions: null,
		/* type="bool" Option to show an asterisks indication next to required fields.
			Note: Indicators are not supported on grouped controls such as checkbox or radio button sets and the igRating control.
			*/
		requiredIndication: false,
		/* type="bool" Option to show a label indication next to optional fields.
			Note: Indicators are not supported on grouped controls such as checkbox or radio button sets and the igRating control.
			*/
		optionalIndication: false
	},
	css: {
		/* Class applied to the target element with validation. Has no visual effect. */
		target: "ui-igvalidator-target",
		/* Class applied to the asterisks indication span next to required fields . */
		requiredIndication: "ui-igvalidator-required-indication",
		/* Class applied to the indication span next to optional fields . */
		optionalIndication: "ui-igvalidator-optional-indication"
	},
	events: {
		/* cancel="true" Event which is raised on validation before default validation logic is applied.
			Return false in order to cancel the event and consider the field valid.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.value to get current value in target.
			ui.fieldOptions is populated with options for the specific field in the collection or null. */
		validating: "validating",
		/* cancel="false" Event which is raised after value was validated but before any action takes effect.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.value to get current value in target.
			Use ui.valid to determine the outcome of the validation.
			Use ui.message to get text of message.
			ui.rule is populated with the name of the rule that failed validation, if any.
			ui.fieldOptions is populated with options for the specific field in the collection or null. */
		validated: "validated",
		/* cancel="false" Event raised for valid field  after value was validated but before any action takes effect.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.value to get current value in target.
			Use ui.valid to determine the outcome of the validation.
			Use ui.message to get text of message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		success: "success",
		/* cancel="false" Event raised for invalid field after value was validated but before any action takes effect.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.value to get current value in target.
			Use ui.valid to determine the outcome of the validation.
			Use ui.message to get text of message.
			ui.rule is populated with the name of the rule that failed validation, if any.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		error: "error",
		/* cancel="true" Event which is raised before error message is displayed.
			Return false in order to prevent error message display.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		errorShowing: "errorShowing",
		/* cancel="true" Event which is raised before error message is hidden.
			Return false in order to keep error message displayed.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		errorHiding: "errorHiding",
		/* cancel="false" Event which is raised after error message was displayed.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		errorShown: "errorShown",
		/* cancel="false" Event which is raised after error message was hidden.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null. */
		errorHidden: "errorHidden",
		/* cancel="true" Event which is raised before success message is displayed.
			Return false in order to prevent success message display.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		successShowing: "successShowing",
		/* cancel="true" Event which is raised before success message is hidden.
			Return false in order to keep success message displayed.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null. */
		successHiding: "successHiding",
		/* cancel="false" Event which is raised after success message was displayed.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null. */
		successShown: "successShown",
		/* cancel="false" Event which is raised after success message was hidden.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.message to get text of message.
			Use ui.target to get reference to the target of the message.
			ui.fieldOptions is populated with options for the specific field in the collection or null.  */
		successHidden: "successHidden",
		/* cancel="true" Event triggered on Validator instance level before handling a form submit event.
			Return false to cancel to skip validating and potentially allow the submit if no other other validators return erros.
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.target to get reference of the event target form. */
		formValidating: "formValidating",
		/* cancel="false" Event triggered on Validator instance level after validation on form submit event
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.target to get reference of the event target form.
			Use ui.valid to determine the outcome of the validation. */
		formValidated: "formValidated",
		/* cancel="false" Event triggered on Validator instance level after failed validation on form submit event
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.target to get reference of the event target form. */
		formError: "formError",
		/* cancel="false" Event triggered on Validator instance level after successful validation on form submit event
			Function takes arguments evt and ui.
			Use ui.owner to get reference to the igValidator widget.
			Use ui.target to get reference of the event target form. */
		formSuccess: "formSuccess"
	},
	locale: {
		/* Message used when validator is not able to identify specific reason for failure */
		defaultMessage: "Please fix this field",
		/* Message used when user should select a value (in drop-down list, radio buttons or similar) */
		selectMessage: "Please select a value",
		/* Message for range selection */
		rangeSelectMessage: "Please select no more than {1} and not less than {0} items",
		/* Message for minimum number of selected items */
		minSelectMessage: "Please select at least {0} items",
		/* Message for maximum number of selected items */
		maxSelectMessage: "Please select no more than {0} items",
		/* Message for range of text length */
		rangeLengthMessage: "Please enter a value between {0} and {1} characters long",
		/* Message for minimum length of text */
		minLengthMessage: "Please enter at least {0} characters",
		/* Message for maximum length of text */
		maxLengthMessage: "Please enter no more than {0} characters",
		/* Message for required entry */
		requiredMessage: "This field is required",
		/* Message regular expression (pattern option) */
		patternMessage: "Please fix pattern of this field",
		/* Message for igMaskEditor when required positions are missing */
		maskMessage: "Please fill all required positions",
		/* Message for igDateEditor when date fields are missing */
		dateFieldsMessage: "Please enter values in date fields",
		/* Message for igDateEditor when day of month is invalid */
		invalidDayMessage: "Invalid day of month. Please enter correct day",
		/* Message for igDateEditor when date is invalid (zero day, month or similar) */
		dateMessage: "Please enter a valid date",
		/* Message for igNumericEditor when string can not be converted to a number (no digits) */
		numberMessage: "Please enter a valid number",
		/* Message for igDateEditor and igNumericEditor when range validation of editor failed */
		rangeValueMessage: "Please enter a value between {0} and {1}",
		/* Message for igDateEditor and igNumericEditor when minValue of editor is set and editor has larger value */
		minValueMessage: "Please enter a value greater than or equal to {0}",
		/* Message for igDateEditor and igNumericEditor when maxValue of editor is set and editor has smaller value */
		maxValueMessage: "Please enter a value less than or equal to {0}",
		/* Message for invalid email input */
		emailMessage: "Please enter a valid email address",
		/* Message for invalid email input */
		equalToMessage: "Values don't match",
		optionalString: "(optional)"
	},
	/* default email checking RegExp */
	emailRegEx: /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
	/* default email checking RegExp */
	numberRegEx: "^-?[\\d]+({0}[\\d]+)?$",
	/* defaults for the notifier */
	notifierDefaults: {
		state: "error"
	},
	_createWidget: function () {
		// strip dummy fields collection
		delete this.options.fields;
		$.Widget.prototype._createWidget.apply(this, arguments);
	},
	_create: function () {
		//defaults
		this._decimalSeparator = ".";
		this._thousandsSeparator = ",";

		// internal counter for how many fields require form handling
		this._formHandleCounter = 0;
		this._fieldOptions = this.options.fields ? $.extend([], this.options.fields) : null; // TODO use internal
		var shouldHandleForm = false;

		// fields:
		if (this.options.fields) {
			for (var i = 0; i < this.options.fields.length; i++) {
				var options = this.options.fields[ i ];
				this._initializeField($(options.selector), options);

				// handle form for multiple fields, ignore global
				if (options.onsubmit !== undefined ? options.onsubmit : this.options.onsubmit) {
					shouldHandleForm = true;
					this._formHandleCounter++;
				}
			}
		} else {
			this._initializeField(this.element, this.options);
		}

		this._attachToForm(shouldHandleForm || this.options.onsubmit);
	},
	_setOption: function (option, value) {
		switch (option) {
			case "notificationOptions":
				this.options.notificationOptions = value;
				this._updateNotifiers();
				break;
			case "onchange":
			case "onblur":
				if (!this.options.fields && !this.options._control) { // TODO: ignore?
					this.element.unbind(".validator");
					this._attachFieldEvents(this.element);
				}
				break;
			case "onsubmit":
				if (this.options.onsubmit === value) {
					break;
				}
				if (this.options.fields) {
					for (var i = 0; i < this.options.fields.length; i++) {

						//go though fields that inherit the global setting
						if (this.options.fields[ i ].onsubmit === undefined) {
							if (value) {
								if (!this._formHandleCounter) {
									// handle form for multiple fields, ignore global
									this._attachToForm(true);
								}
								this._formHandleCounter++;
							} else if (!--this._formHandleCounter) {
								// if no more fields need form submit, detach
								this._detachFromForm();
							}
						}
					}
				}
				break;
			case "messageTarget":
				var oldVisible = this._fieldMessageVisible(this.options);
				this._clearMessageTarget(this.options);
				this._hideSuccess(this.options);
				this._hideError(this.options);

				this.options.messageTarget = value;
				this._evalMessageTarget(this.options);
				if (oldVisible) {
					if (this.options.isValid) {
						this._showSuccess(this.options, { message: this.options._currentMessage });
					} else {
						this._showError(this.options, { message: this.options._currentMessage });
					}
				}
				break;
			case "errorMessage":
			case "successMessage":

				//var oldVisible = this._fieldMessageVisible(this.options);
				// TODO: hide enough? let user call validate after?
				this._hideError(this.options);
				this._hideSuccess(this.options);
				break;
			case "requiredIndication":
			case "optionalIndication":
				this._removeFieldIndications(this.options);
				this.options[ option ] = value;
				this._addFieldIndications(this.options, this.element);
				break;

			// Not supported after init:
			case "fields":
				return;
			default:
				break;

		}
		$.Widget.prototype._setOption.apply(this, arguments);
	},
	_initializeField: function (element, options) {
		var target = element;
		if (!target.length) {
			// ignore invalid selector on fields
			options._ignored = true;
			return;
		}

		// check if initialized on a editor control:
		options._control = this._getEditor(target);
		if (options._control) {
			this._form = this._form || target.closest("form").get(0);
			if (options._control.widgetName === "igCombo") {
				options._type = "selectrange";
			} else if (options._control.widgetName === "igRating") {
				options._type = "select";
			}

			// bridge
			if (options._control._options) {    // igCombo
				options._control._options.validator = this;
			} else {    // igEditors
				options._control._validator = this;
			}
		} else {
			// evaluate target type
			var elemType = target[ 0 ].tagName;
			switch (elemType) {
				case "INPUT":
					if (target[ 0 ].type === "checkbox") {
						// check for other checkboxes:
						options._group = this._findGroupTargets(target);
						options._type = options._group.length > 1 ? "checkboxrange" : "checkbox";
						target = options._group;
					} else if (target[ 0 ].type === "radio") {
						// get all radios:
						options._group = this._findGroupTargets(target);
						options._type = "radio";
						target = options._group;
					} else {
						options._type = "input";
					}
					break;
				case "TEXTAREA":
					options._type = "textarea";
					break;
				case "SELECT":
					if (target[ 0 ].multiple) {
						options._type = "selectrange";
					} else {
						options._type = "select";
					}
					break;

					// forms and containers, skip other steps:
				case "FORM":
					this._form = this._form || target[ 0 ];
					options._ignored = true;
					return;

				default:

					// assume container inside the form?
					this._form = this._form || target.closest("form").get(0);
					options._ignored = true;
					return;
			}

			// attach events (only for fields)
			this._attachFieldEvents(target);
		}
		options._ignored = false;
		target.addClass(this.css.target);
		target.data("igValidatorField", options);
		options.notifyTarget = this._targetFromOptions(options, true);
		this._evalMessageTarget(options);
		this._ensureNotifier(options, true);

		this._addFieldIndications(target, options);
	},
	_findGroupTargets: function (target) {
		if (target[ 0 ].name) {
			return $("[name=" + target[ 0 ].name + "]", target[ 0 ].form || document);
		}
		return target;
	},
	_attachFieldEvents: function (element) {
		var self = this, evts = {
			"keyup.validator": function (e) {
				// skip ctrls, alts, shifts, caps + TAB
				if (e.keyCode !== 9 && e.keyCode < 15 || e.keyCode > 20) {
					self._validateInternal(element, e);
				}
			},
			"change.validator": function (e) { self._validateInternal(element, e); },

			// fires before actual value
			"cut.validator": function (e) {
				setTimeout(function () { self._validateInternal(element, e); }, 10);
			},
			"paste.validator": function (e) {
				setTimeout(function () { self._validateInternal(element, e); }, 10);
			},
			"drop.validator": function (e) {
				setTimeout(function () { self._validateInternal(element, e); }, 10);
			},
			"dragend.validator": function (e) {
				setTimeout(function () { self._validateInternal(element, e); }, 10);
			},
			"blur.validator": function (e) { self._validateInternal(element, e, true); }
		};
		element.bind(evts);
	},
	_ensureNotifier: function (options, reinit) {
		/* Checks for and/or initializes igNotifier */
		if (reinit && options.notifyTarget.data("igNotifier")) {
			options.notifyTarget.igNotifier("destroy").unbind(".validator");
		}
		if (!options.notifyTarget.data("igNotifier")) {
			var args = {
				owner: this,
				target: options.notifyTarget,
				fieldOptions: options === this.options ? null : options
			};

			// proxy events:
			options.notifyTarget.igNotifier($.extend({},
					this.notifierDefaults,
					this.options.notificationOptions,
					options.notificationOptions
				))
				.bind({
					"ignotifiershowing.validator": function (evt, ui) {
						return args.owner._handleNotifierEvent(evt, ui, "Showing", args);
					},
					"ignotifiershown.validator": function (evt, ui) {
						return args.owner._handleNotifierEvent(evt, ui, "Shown", args);
					},
					"ignotifierhiding.validator": function (evt, ui) {
						return args.owner._handleNotifierEvent(evt, ui, "Hiding", args);
					},
					"ignotifierhidden.validator": function (evt, ui) {
						return args.owner._handleNotifierEvent(evt, ui, "Hidden", args);
					}
				});
		}
	},
	_updateNotifiers: function () {
		if (this.options.fields) {
			for (var i = 0; i < this.options.fields.length; i++) {
				this._ensureNotifier(this.options.fields[ i ], true);
			}
		} else {
			this._ensureNotifier(this.options, true);
		}
	},
	_clearMessageTarget: function (options) {
		if (options._$messageTarget) {
			options._$messageTarget
				.removeClass("field-validation-valid field-validation-error")
				.empty().css("display", "");
		}
	},
	_evalMessageTarget: function (options) {
		options._$messageTarget = options.messageTarget;
		if (typeof options._$messageTarget === "string") {
			var target = $("[data-valmsg-for='" + options._$messageTarget + "']");
			options._$messageTarget = target.length ? target : $(options._$messageTarget);
		}
		if (options._$messageTarget instanceof $) {
			if (options._$messageTarget.length) {
				options._$messageTarget.hide();
			} else {
				options._$messageTarget = null;
			}
		}
	},
	_addFieldIndications: function (element, options) {
		if ((options._group && options._group.length > 1) ||
			(options._control && options._control.widgetName === "igRating")) {
			// not supported on groups
			return;
		}
		if (options.required && options.requiredIndication) {
			// TODO: or use CSS?
			options._$indicator = element.after(
				"<span title='" + this._getLocalizedMessage("required") +
				"' class='" + this.css.requiredIndication + "'>*</span>")
			.next();
		}
		if (!options.required && options.optionalIndication) {
			// TODO: or use CSS?
			options._$indicator = element.after("<span class='" +
				this.css.optionalIndication + "'>" +
				this._getLocalizedMessage("optional", "String") + "</span>")
			.next();
		}
	},
	_removeFieldIndications: function (options) {
		if (options._$indicator) {
			// TODO: hide or cleanup?
			options._$indicator.hide();
			options._$indicator.remove();
			delete options._$indicator;
		}
	},
	_attachToForm: function (shouldHandleForm) {
		this._form = this._form || this.element[ 0 ].form || this.element.closest("form").get(0);
		if (!this._form || !shouldHandleForm) {
			return;
		}

		if (!this._form._igValidators || !this._form._igValidators.length) {
			this._form._igValidators = [];
			$(this._form).bind("submit.validator", function (e) {
				this._igErrorShown = false;
				var summaryResult = true, current;
				for (var i = 0; i < this._igValidators.length; i++) {
					current = this._igValidators[ i ]._validateForm(e);

					summaryResult = summaryResult ? current : summaryResult;
				}
				if (!summaryResult) {
					e.preventDefault();
					e.stopPropagation();
				}
			});
		}
		this._form._igValidators.push(this);
	},
	_detachFromForm: function () {
		var index;
		if (this._form && (index = $.inArray(this, this._form._igValidators)) > -1) {
			this._form._igValidators.splice(index, 1);
			if (!this._form._igValidators.length) {
				//also detach handler if all validators are destoyed
				$(this._form).unbind("submit.validator");
			}
		}
	},
	_validate: function (field, evt, isSubmitting) {
		var current, i,
			valid = true; /* sticky valid state (should stay false between multiple checks) */

		if (this.options.fields) {
			if (field !== undefined && (i = this._fieldIndexOf(field)) > -1) {
				// single field passed
				field = this.options.fields[ i ];
				valid = field.isValid = this._validateField(field, evt, isSubmitting);
			} else {
				// check all
				for (i = 0 ; i < this.options.fields.length; i++) {
					field = this.options.fields[ i ];
					current = field.isValid = this._validateField(field, evt, isSubmitting);
					valid = valid ? current : valid;
				}
			}
		} else {
			valid = this._validateField(this.options, evt, isSubmitting);
			this.options.isValid = valid;
		}

		return valid;
	},
	_validateForm: function (evt) {
		var valid = true,
			args = {
				owner: this,
				target: $(evt.target)
			};

		// overall "form" event
		if (this._trigger(this.events.formValidating, evt, args)) {
			if (!$.ui.igValidator.defaults.showAllErrorsOnSubmit &&
					this._form && this._form._igErrorShown) {
				this._skipMessages = true;
			}
			args.valid = valid = this._validate(null, evt, true);

			// overall "form" event
			this._trigger(this.events.formValidated, evt, args);
			this._trigger(valid ? this.events.formSuccess : this.events.formError, evt, args);
		}
		return valid;
	},
	_validateInternal: function (element, evt, blur, value) {
		// Called from events && internally used by other controls
		element = element ||
				(evt && (element = $(evt.target).closest("." + this.css.target)).length) ||
				this.element;
		var field = element.data("igValidatorField");
		if (field) {
			field.isValid = this._validateField(field, evt || {}, false, value, blur);
			return field.isValid;
		}

		if (value !== undefined) {
			// internal check:
			this.options.isValid = this._validateField(this.options, evt || {}, false, value, blur);
			return this.options.isValid;
		} else {
			return this._validate(null, evt || {});
		}
	},
	_validateField: function (opts, evt, isSubmitting, value, blur) {
		if (opts._ignored) {
			return true;
		}
		var options = this._addGlobalSettings(opts);

		// Called per field with optional value to check, event and blur flag
		value = value !== undefined ? value : this._getTargetValue(options);
		var min, max,
			isNumber = this._isNumber(options, value),
			isDateParsable = !isNaN(new Date(value).getSeconds()),
			isDate = value instanceof Date,
			hasLength = value !== undefined && value !== null && value.length !== undefined,
			internalValue = isNumber ? value.toString() : value; // 0 needs to be valid for required fields!

		// validation stop rules (threshold, triggers validation)
		// Note: Options must be extended with globals at this point to properly validate triggers and conditions
		if (isSubmitting && !options.onsubmit) {
			return true;
		}
		if (!this._forceValidation && !isSubmitting) { // set on API calls
			if ((blur && !options.onblur) || (!blur && !options.onchange)) {
				// validate change and blur internal calls without the setting
				return true;
			}

			if (options.hasOwnProperty("threshold") && hasLength && value.length <= options.threshold) {
				// threshold in effect, skip checks
				return true;
			}
		}

		var args = {
			value: value,
			owner: this,
			fieldOptions: options === this.options ? null : opts
		};

		if (evt && !this._trigger(this.events.validating, evt, args)) {
			//cancel, state remains unchanged
			return true;
		}

		opts._currentMessage = null;

		// Required
		if (options.required && (!internalValue || value.length === 0)) {
			switch (options._type) {
				case "checkboxrange":
				case "radio":
				case "select":
				case "selectrange":
					opts._currentMessage = this._getRuleMessage(options, "required", "select");
					break;
				default:
					opts._currentMessage = this._getRuleMessage(options, "required", "required");
			}
			args.message = opts._currentMessage;
			args.rule = "required";
			this._showError(options, args, evt);
			return false;
		} else if (!options.required && !internalValue) {
			//no value and not required, return
			args.message = opts._currentMessage = options.successMessage;
			this._success(options, args, evt, isSubmitting);
			return true;
		}

		// igControl
		if (options._control && typeof options._control.isValid === "function") {
			var result = options._control.isValid();
			if (!result) {
				opts._currentMessage = options.errorMessage ||
					options._control._currentMessage ||
					this._getLocalizedMessage("default");

				args.message = opts._currentMessage;
				args.rule = "control";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Number
		if (options.number && !isNumber && internalValue) {
			opts._currentMessage = this._getRuleMessage(options, "number", "number");
			args.message = opts._currentMessage;
			args.rule = "number";
			this._showError(options, args, evt);
			return false;
		}

		// Date
		if (options.date && !isDateParsable && internalValue) {
			opts._currentMessage = this._getRuleMessage(options, "date", "date");
			args.message = opts._currentMessage;
			args.rule = "date";
			this._showError(options, args, evt);
			return false;
		}

		// Min/Max Length
		if (hasLength && value.length && (options.lengthRange)) {
			var messageType = value.push ? "Select" : "Length",
				minLength = options.lengthRange.push ? options.lengthRange[ 0 ] : options.lengthRange.min,
				maxLength = options.lengthRange.push ? options.lengthRange[ 1 ] : options.lengthRange.max;

			min = minLength && value.length < minLength;
			max = maxLength && value.length > maxLength;

			if (minLength && maxLength && (min || max)) {
				// range message
				opts._currentMessage = this._getRuleMessage(options, "lengthRange", "range" + messageType);
				opts._currentMessage = opts._currentMessage.replace("{0}", minLength)
										.replace("{1}", maxLength);
				args.message = opts._currentMessage;
			} else if (min) {
				opts._currentMessage = this._getRuleMessage(options, "lengthRange", "min" + messageType);
				opts._currentMessage = opts._currentMessage.replace("{0}", minLength);
				args.message = opts._currentMessage;
			} else if (max) {
				opts._currentMessage = this._getRuleMessage(options, "lengthRange", "max" + messageType);
				opts._currentMessage = opts._currentMessage.replace("{0}", maxLength);
				args.message = opts._currentMessage;
			}

			if (args.message) {
				args.rule = "lengthRange";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Min/Max Value
		if (options.valueRange && (isDateParsable || isNumber)) {
			var minValue = options.valueRange.push ? options.valueRange[ 0 ] : options.valueRange.min,
				maxValue = options.valueRange.push ? options.valueRange[ 1 ] : options.valueRange.max,

				// must be type checked, 0 should be valid
				hasMin = typeof minValue === "number" || minValue,
				hasMax = typeof maxValue === "number" || maxValue;

			if ((hasMin || hasMax)) {
				if (isNumber && !options.date) { // parseFloat is quite eager to parse date strings too...
					value = parseFloat(value);
					min = hasMin && minValue;
					min = value < min ? min.toString() : null;
					max = hasMax && maxValue;
					max = value > max ? max.toString() : null;
				}
				if (isDateParsable && (options.date || isDate)) {
					value = new Date(value);
					if (hasMin) {
						min = minValue = new Date(minValue);
						minValue = minValue.toLocaleString();
					}
					min = value < min ? min.toLocaleString() : null;

					if (hasMax) {
						max = maxValue = new Date(maxValue);
						maxValue = maxValue.toLocaleString();
					}
					max = value > max ? max.toLocaleString() : null;
				}

				if (hasMin && hasMax && (min || max)) {
					// range message
					opts._currentMessage = this._getRuleMessage(options, "valueRange", "rangeValue");
					opts._currentMessage = opts._currentMessage.replace("{0}", min || minValue)
											.replace("{1}", max || maxValue);
					args.message = opts._currentMessage;
				} else if (min) {
					opts._currentMessage = this._getRuleMessage(options, "valueRange", "minValue");
					opts._currentMessage = opts._currentMessage.replace("{0}", min);
					args.message = opts._currentMessage;
				} else if (max) {
					opts._currentMessage = this._getRuleMessage(options, "valueRange", "maxValue");
					opts._currentMessage = opts._currentMessage.replace("{0}", max);
					args.message = opts._currentMessage;
				}

				if (args.message) {
					args.rule = "valueRange";
					this._showError(options, args, evt);
					return false;
				}
			}
		}

		// Equals To:
		if (options.equalTo) {
			var selector = options.equalTo.selector || options.equalTo,
				targetValue = this._getTargetValue({
					_control: this._getEditor($(selector)),
					selector: selector
				});
			if ($.ig.util.compare(value, targetValue)) {
				opts._currentMessage = this._getRuleMessage(options, "equalTo", "equalTo");
				args.message = opts._currentMessage;
				args.rule = "equalTo";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Email
		if (options.email) {
			if (!this.emailRegEx.test(value)) {
				opts._currentMessage = this._getRuleMessage(options, "email", "email");
				args.message = opts._currentMessage;
				args.rule = "email";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Pattern
		if (options.pattern) {
			// D.P. 22th Dec 2015 Bug 211530: Misspelled "expression" in pattern option, keeping both versions per customer request
			var regEx = options.pattern.expresion || options.pattern.expression || options.pattern;
			regEx = regEx.test ? regEx : new RegExp(regEx.toString());

			if (!regEx.test(value)) {
				opts._currentMessage = this._getRuleMessage(options, "pattern", "pattern");
				args.message = opts._currentMessage;
				args.rule = "pattern";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Custom
		if (options.custom) {
			var func = options.custom.method || options.custom;
			if (typeof func === "string") {
				func = window[ func ];
			}
			if (typeof func === "function" && !func.apply(this, [ value, args.fieldOptions ])) {
				opts._currentMessage = this._getRuleMessage(options, "custom", "default");
				args.message = opts._currentMessage;
				args.rule = "custom";
				this._showError(options, args, evt);
				return false;
			}
		}

		// Success
		args.message = opts._currentMessage = options.successMessage;
		this._success(options, args, evt, isSubmitting);
		return true;
	},
	_success: function (options, args, evt, isSubmitting) {
		// Success
		args.valid = true;
		if (evt) {
			this._trigger(this.events.validated, evt, args);
			this._trigger(this.events.success, evt, args);
		}

		if (!isSubmitting) {
			this._showSuccess(options, args, evt);
		}
	},
	_isNumber: function (options, value) {
		if (typeof value === "number") {
			return true;
		} else if (typeof value === "string") {
			var decimalSeparator = options.number && options.number.decimalSeparator,
				thousandsSeparator = options.number && options.number.thousandsSeparator,
				thousandsRegEx, regEx;

			decimalSeparator = decimalSeparator || this._decimalSeparator;
			thousandsSeparator = thousandsSeparator || this._thousandsSeparator;
			thousandsRegEx = new RegExp("\\" + thousandsSeparator, "g");
			regEx = new RegExp(this.numberRegEx.replace("{0}", "\\" + decimalSeparator));

			value = value.replace(thousandsRegEx, "");
			if (regEx.test(value) && this._parseNumber(value, decimalSeparator) !== null) {
				return true;
			}
		}
		return false;
	},
	_parseNumber: function (value, decimalSeparator) {
		/* returns the parsed number or null */
		var result = value.replace(decimalSeparator, this._decimalSeparator);

		result = parseFloat(result);
		if (isNaN(result)) {
			return null;
		}
		return result;
	},
	_showError: function (options, args, evt) {
		args.valid = false;
		if (evt) {
			this._trigger(this.events.validated, evt, args);
			this._trigger(this.events.error, evt, args);
		}

		if (this._skipMessages) {
			return;
		}

		this._hideSuccess(options, evt);

		// D.P. 9th June 2016 Bug 216714: Event parameters consistency, adjust target
		args = {
			owner: this,
			message: args.message,
			target: options._$messageTarget ? options._$messageTarget : options.notifyTarget,
			fieldOptions: args.fieldOptions
		};

		if (evt && !this._trigger(this.events.errorShowing, evt, args)) {
			return; //canceled
		}
		this._ensureNotifier(options);
		if (options._$messageTarget) {
			// custom error container
			options._$messageTarget.removeClass("field-validation-valid")
					.addClass("field-validation-error")
					.html(args.message).show();
			options.notifyTarget.data("igNotifier")._setOption("state", "error");
			options.notifyTarget.data("igNotifier")._setTargetState();
		} else {
			options.notifyTarget.igNotifier("notify", "error", args.message);
			if (this._form) {
				this._form._igErrorShown = true;
			}
		}
		if (evt) {
			this._trigger(this.events.errorShown, evt, args);
		}
	},
	_hideError: function (options, evt) {
		var notifier = options._$messageTarget || options.notifyTarget.data("igNotifier"),
			args = {
				owner: this,
				target: options._$messageTarget || options.notifyTarget,
				message: notifier._currentText || options._$messageTarget && options._$messageTarget.text(),
				fieldOptions: options === this.options ? null : options
			};

		if (this._hasVisibleError(options)) {
			if (evt && !this._trigger(this.events.errorHiding, evt, args)) {
				return; //canceled
			}

			// will call either igNotifier or jQuery hide this way
			notifier.hide();
			options.notifyTarget.data("igNotifier")._setTargetState(true);
			if (evt) {
				this._trigger(this.events.errorHidden, evt, args);
			}
		}
	},
	_showSuccess: function (options, args, evt) {
		if (this._skipMessages) {
			return;
		}

		this._hideError(options, evt);

		if (args.message) {
			// D.P. 9th June 2016 Bug 216714: Event parameters consistency, adjust target
			args = {
				owner: this,
				message: args.message,
				target: options._$messageTarget ? options._$messageTarget : options.notifyTarget,
				fieldOptions: args.fieldOptions
			};

			// D.P. 13th Apr 2016 Bug 216717: Success showing will trigger even where there's no message set
			if (evt && !this._trigger(this.events.successShowing, evt, args)) {
				return; //canceled
			}

			this._ensureNotifier(options);
			if (options._$messageTarget) {
				// custom error container
				options._$messageTarget.removeClass("field-validation-error")
						.addClass("field-validation-valid")
						.html(args.message).show();

				options.notifyTarget.data("igNotifier")._setOption("state", "success");
				options.notifyTarget.data("igNotifier")._setTargetState();
			} else {
				options.notifyTarget.igNotifier("notify", "success", args.message);
			}
			if (evt) {
				this._trigger(this.events.successShown, evt, args);
			}
		}
	},
	_hideSuccess: function (options, evt) {
		var notifier = options._$messageTarget || options.notifyTarget.data("igNotifier"),
			args = {
				owner: this,
				target: options._$messageTarget || options.notifyTarget,
				message: notifier._currentText || options._$messageTarget && options._$messageTarget.text(),
				fieldOptions: options === this.options ? null : options
			};

		if (this._hasVisibleSuccess(options)) {
			if (evt && !this._trigger(this.events.successHiding, evt, args)) {
				return; //canceled
			}

			// will call either igNotifier or jQuery hide this way
			notifier.hide();
			if (evt) {
				this._trigger(this.events.successHidden, evt, args);
			}
		}
	},
	_handleNotifierEvent: function (evt, ui, type, args) {
		/* type is Shown, Hiding, etc */
		var state = ui.owner.options.state;

		// D.P. 9th June 2016 Bug 216714: No message/fieldOptions from notifier events
		args.message = ui.owner._currentText;

		if (state === "error") {
			return args.owner._trigger(this.events[ "error" + type ], evt, args);
		} else if (state === "success") {
			return args.owner._trigger(this.events[ "success" + type ], evt, args);
		}
		return true;
	},
	_getTargetValue: function (options) {
		if (options._control) {
			if (options._control.options.checked !== undefined) { // CheckBoxEditor
				return options._control.options.checked;
			} else if (options._control.refreshValue && options._control.options.allowCustomValue) { // igCombo + allowCustomValue
				options._control.refreshValue();
			}
			return options._control.value(); // igEditors, igCombo, igRating
		}
		var $target = this._targetFromOptions(options);
		if (!$target.length) {
			return null;
		}
		switch (options._type) {
			case "textarea":

				// TODO: val() strips \r, http://api.jquery.com/val/
				return $target.val().replace(/\r?\n/g, "\r\n");
			case "checkbox":
				return $target[ 0 ].checked;
			case "radio":
			case "checkboxrange":
				return options._group.filter(":checked").map(function () {
					return this.value;
				}).get();
			case "input":
			case "select":
			case "selectrange":
				return $target.val();
		}

		// D.P. 15th Dec 2015 Bug 211119: default in case there's no _type evaluated from equalTo target
		return $target.val && $target.val();
	},
	_getRuleMessage: function (options, rule, messageName) {
		if (options[ rule ].errorMessage) {
			return options[ rule ].errorMessage;
		} else if (options.errorMessage) {
			return options.errorMessage;
		}
		return this._getLocalizedMessage(messageName);
	},
	_getLocalizedMessage: function (key, postfix) {
		key += postfix || "Message";
		var message = this.options.locale ? this.options.locale[ key ] : null;
		if (!message && $.ig && $.ig.Validator && $.ig.Validator.locale) {
			message = $.ig.Validator.locale[ key ];
		}
		return message || this.locale[ key ];
	},
	_targetFromOptions: function (options, outer) {
		if (outer && options._control) {
			if (options._control.editorContainer) {
				return options._control.editorContainer();
			} else if (options._control.comboWrapper) {
				return options._control.comboWrapper().children().first();
			}
		}
		if (options.selector) {
			return options.selector instanceof $ ? options.selector : $(options.selector);
		} else {
			return this.element;
		}
	},
	_getEditor: function (elem) {
		// check if initialized on a editor control:
		var widgets = elem.data(),
			controls = [ "Editor", "Combo", "Rating", "DatePicker" ],

			// regEx results in something like /ig.*?(Editor|Combo|Rating)/
			regEx = new RegExp("ig.*?(" + controls.join("|") + ")");
		for (var i in widgets) {
			if (widgets[ i ].widgetName && regEx.test(widgets[ i ].widgetName)) {
				return widgets[ i ];
			}
		}
		return null;
	},
	_cleanupField: function (options) {
		var element = this._targetFromOptions(options || this.options);
		element.unbind(".validator");
		if (options.notifyTarget && options.notifyTarget.data("igNotifier")) {
			options.notifyTarget.igNotifier("destroy").unbind(".validator");
		}
		this._clearMessageTarget(options);
		if (element.data("igValidatorField")) {
			element.removeData("igValidatorField");
		}
		this._removeFieldIndications(options);

		// bridge
		if (options._control) {
			if (options._control._options) {    // igCombo
				options._control._options.validator = null;
			} else {    // igEditors
				options._control._validator = null;
			}
		}
		element.removeClass(this.css.target);
	},
	_addGlobalSettings: function (options) {
		if (options === this.options) {
			return options;
		}

		// cherry-pick options to merge
		var properties = [ "required", "threshold", "lengthRange", "number", "date",
							"valueRange", "email", "triggers", "onblur", "onchange",
							"onsubmit", "successMessage", "errorMessage" ],
			extendedOptions = $.extend({}, options);

		for (var i = 0; i < properties.length; i++) {
			if (!options.hasOwnProperty(properties[ i ]) && this.options[ properties[ i ] ] !== null) {
				// TODO: if (typeof this.options[properties[ i ]] !== object) extend complex options?
				extendedOptions[ properties[ i ] ] = this.options[ properties[ i ] ];
			}
		}
		return extendedOptions;
	},
	_indexOfByProerty: function (array, property, value) {
		// TODO: move to utils
		for (var i = 0; i < array.length; i++) {
			if (array[ i ][ property ] && array[ i ][ property ] === value) {
				return i;
			}
		}
		return -1;
	},
	_hasVisibleSuccess: function (options) {
		if (this._fieldMessageVisible(options)) {
			if (options._$messageTarget && options._$messageTarget.hasClass("field-validation-valid")) {
				return true;
			} else {
				return options.notifyTarget.data("igNotifier").options.state === "success";
			}
		}
		return false;
	},
	_hasVisibleError: function (options) {
		if (this._fieldMessageVisible(options)) {
			if (options._$messageTarget && options._$messageTarget.hasClass("field-validation-error")) {
				return true;
			} else {
				return options.notifyTarget.data("igNotifier").options.state === "error";
			}
		}
		return false;
	},
	_fieldMessageVisible: function (options) {
		if (options._$messageTarget) {
			return options._$messageTarget.is(":visible");
		}
		if (options.notifyTarget.data("igNotifier")) {
			return options.notifyTarget.data("igNotifier").isVisible();
		}
		return false;
	},
	_fieldIndexOf: function (fieldParam) {
		/* extracts the field index from a field parameter of multiple types */
		var index = -1;
		if (typeof fieldParam === "number" && this.options.fields[ fieldParam ]) {
			return fieldParam;
		}
		if (typeof fieldParam === "string") {
			index = this._indexOfByProerty(this.options.fields, "selector", fieldParam );
		}
		if (typeof fieldParam === "object") {
			index = $.inArray(fieldParam, this.options.fields);
		}
		if (index >= this.options.fields.length) {
			return -1;
		}
		return index;
	},
	validate: function (field) {
		/* Trigger validation and show errors for invalid fields.
			paramType="object" optional="true" Optional field object to check. Only has effect with fields collection and skips other fields.
			returnType="bool" True if the field(s) passed all checks.
		*/
		var valid;

		// apply validation-forcing flag
		this._forceValidation = true;

		valid = this._validate(field);

		this._forceValidation = false;
		return valid;
	},
	isValid: function (field) {
		/* Trigger validation but do not display error messages.
			paramType="number|string|object" optional="true" Optional field object to check (skips other fields, only works with fields collection).
			returnType="bool" True if the field(s) passed all checks.
		*/
		this._skipMessages = true;
		var valid = this.validate(field);
		this._skipMessages = false;
		return valid;
	},
	hide: function (field) {
		/* Hide any possible message (either messageTarget or igNotifier).
			Note: When the validator has a fields colleciton, not passing a field will hide messages on all fields.
			paramType="number|string|object" optional="true" Optional field to hide message for.
		*/
		var i;
		if (this.options.fields) {
			if (field !== undefined) {
				// single field passed
				if ((i = this._fieldIndexOf(field)) > -1) {
					this._hideError(this.options.fields[ i ]);
					this._hideSuccess(this.options.fields[ i ]);
				}
				return;
			}
			for (i = 0; i < this.options.fields.length; i++) {
				if (this.options.fields[ i ].isValid !== undefined) {
					// single field passed
					this._hideError(this.options.fields[ i ]);
					this._hideSuccess(this.options.fields[ i ]);
				}
			}
		} else {
			this._hideError(this.options);
			this._hideSuccess(this.options);
		}
	},
	getErrorMessages: function (field) {
		/* Gets all current error messages for invalid field(s). Note that this method does not valdiate and states and messages are only updated on validation, so
			this can be used on formValidated event or after validate/isValid method calls.
			paramType="number|string" optional="true" Selector or zero-based index for a single field to get error message for.
			returnType="array" An array of all current error messages.
		*/
		var result = [], i;
		if (this.options.fields) {
			if (field !== undefined) {
				// single field passed
			    if ((i = this._fieldIndexOf(field)) > -1 &&
                    this.options.fields[ i ].isValid !== undefined &&
                    !this.options.fields[ i ].isValid) {
					result.push(this.options.fields[ i ]._currentMessage);
				}
				return result;
			}
			for (i = 0; i < this.options.fields.length; i++) {
				if (this.options.fields[ i ].isValid !== undefined && !this.options.fields[ i ].isValid) {
					result.push(this.options.fields[ i ]._currentMessage);
				}
			}
    } else if (this.options.isValid !== undefined && !this.options.isValid) {
			result.push(this.options._currentMessage);
		}
		return result;
	},
	isMessageDisplayed: function (field) {
		/* Check for currently displayed message(s). Takes an optional field.
			Note: When the validator has a fields colleciton, not passing a field will return a cumulative true even if just one field has visible message.
			paramType="number|string" optional="true" Selector or zero-based index for a single field to get error message for.
			returnType="bool" True if there is a currently visible message.
		*/
		var result = false, i;
		if (this.options.fields) {
			if (field !== undefined) {
				// single field passed
				if ((i = this._fieldIndexOf(field)) > -1) {
					result = !result ? this._fieldMessageVisible(this.options.fields[ i ]) : result;
				}
				return result;
			}
			for (i = 0; i < this.options.fields.length; i++) {
				result = !result ? this._fieldMessageVisible(this.options.fields[ i ]) : result;
			}
		} else {
			result = this._fieldMessageVisible(this.options);
		}
		return result;
	},
	notifier: function (field) {
		/* Gets the notifier for the igValidator or for a single filed.
			paramType="number|string|object" optional="true" Optional field object, its selector or zero-based index to get notifier for.
			returnType="object" Reference to igNotifier or null on incorect field.
		*/
		var i, notifier;
		if (field !== undefined && this.options.fields && (i = this._fieldIndexOf(field)) > -1) {
			notifier = this.options.fields[ i ].notifyTarget &&
						this.options.fields[ i ].notifyTarget.data("igNotifier");
		} else {
			notifier = this.options.notifyTarget && this.options.notifyTarget.data("igNotifier");
		}
		return notifier || null;
	},
	addField: function (field) {
		/* Adds an new input to the fields collection and initializes it with the validator. Note: Additional fields are only accepted if the validator has been created with the collection.
			paramType="object" optional="false" An object with the field selector and options.
		*/
		if (!this.options.fields) {
			return;
		}
		this.options.fields.push(field);
		this._initializeField($(field.selector), field);

		var options = this._addGlobalSettings(field);
		if (options.onsubmit) {
			if (!this._formHandleCounter) {
				// handle form for multiple fields, ignore global
				this._attachToForm(true);
			}
			this._formHandleCounter++;
		}
	},
	removeField: function (field) {
		/* Removes an input from the fields collection.
			paramType="object|number|string" optional="false" The field object to remove, its zero-based index or selector.
		*/
		if (!this.options.fields) {
			return;
		}
		var index = this._fieldIndexOf(field);

		if (index > -1) {
			var removed = this.options.fields.splice(index, 1)[ 0 ],
				options = this._addGlobalSettings(removed);
			this._cleanupField(removed);
			if (options.onsubmit && !--this._formHandleCounter) {
				// if no more fields need form submit, detach
				this._detachFromForm();
			}
		}
	},
	updateField: function (field, fieldOptions) {
		/* Updates a field in the validator collection. Used to reinitialize field in case a control has been created after the validator or to pass in new options.
			paramType="object|number|string" optional="false" The field object to update, its zero-based index or selector.
			paramType="object" optional="true" New options to apply to the fields.
		*/
		if (!this.options.fields) {
			return;
		}
		var index = this._fieldIndexOf(field);

		if (index > -1) {
			if (!fieldOptions) {
				this._cleanupField(this.options.fields[ index ]);
				this._initializeField($(this.options.fields[ index ].selector), this.options.fields[ index ]);
				return;
			}

			fieldOptions = $.extend({}, this.options.fields[ index ], fieldOptions);

			// TODO just remove and add?
			var current = this._addGlobalSettings(this.options.fields[ index ]),
				options = this._addGlobalSettings(fieldOptions);
			this._cleanupField(current);
			this._initializeField($(fieldOptions.selector), fieldOptions);

			if (current.onsubmit && !--this._formHandleCounter) {
				// if no more fields need form submit, detach
				this._detachFromForm();
			}
			if (options.onsubmit) {
				if (!this._formHandleCounter) {
					// handle form for multiple fields, ignore global
					this._attachToForm(true);
				}
				this._formHandleCounter++;
			}

			// swap fields:
			this.options.fields.splice(index, 1, fieldOptions);

			//or options.fields.splice(index, 0, options);
		}
	},
	destroy: function () {
		/* Destroys the validator widget.
		 */
		if (!this.options.fields) {
			this._cleanupField(this.options);
		} else {
			for (var i = 0; i < this.options.fields.length; i++) {
				this._cleanupField(this.options.fields[ i ]);
			}
		}
		this._detachFromForm();
		$.Widget.prototype.destroy.apply(this, arguments);
	}
});
$.extend($.ui.igValidator, { version: "16.1.20161.2145" });
/* Global defaults used by igValidator. If appication change them, then all igValidators created after that will pickup new defaults. */
$.ui.igValidator.defaults = {
	/* type="bool" Gets or sets ability to show all errors on submit.
		Value false will show error message only for the first failed target.
		Default value is true. */
	showAllErrorsOnSubmit: true
};

}(jQuery));



