/// <reference path="./viewcontext.d.ts" />

const prefix = 'nts.uk.storage'
	, OPENWD = `${prefix}.OPEN_WINDOWS_DATA`
	, { ui, request, resource } = nts.uk
	, { windows, block, dialog } = ui
	, $storeSession = function (name: string, params?: any) {
		if (arguments.length === 2) {
			// setter method
			const $value = JSON.stringify({ $value: params })
				, $saveValue = btoa(_.map($value, (s: string) => s.charCodeAt(0)).join('-'));

			return $.Deferred().resolve()
				.then(() => {
					nts.uk.localStorage.setItem(`${prefix}.${name}`, $saveValue);
				})
				.then(() => $storeSession(name));
		} else if (arguments.length === 1) {
			// getter method
			return $.Deferred().resolve()
				.then(() => {
					const $result = nts.uk.localStorage.getItem(`${prefix}.${name}`);

					if ($result.isPresent()) {
						const $string = atob($result.value)
							.split('-').map((s: string) => String.fromCharCode(Number(s)))
							.join('');

						return JSON.parse($string).$value;
					}

					return windows.getShared(name);
				});
		}
	}
	, $storage = function ($data?: any) {
		if (arguments.length === 1) {
			return $storeSession(OPENWD, $data);
		} else if (arguments.length === 0) {
			return $.Deferred().resolve()
				.then(() => $storeSession(OPENWD))
				.then((value: any) => {
					nts.uk.localStorage.removeItem(OPENWD);

					return value;
				});
		}
	};

/** Create new ViewModel and automatic binding to __viewContext */
function bean(): any {
	return function (ctor: any): any {
		__viewContext.ready(() => {
			$storage().then(($params: any) => {
				const $viewModel = new ctor($params)
					, $created = $viewModel['created'];

				// hook to created function
				if ($created && _.isFunction($created)) {
					$created.apply($viewModel, [$params]);
				}

				// hook to mounted function
				$viewModel.$nextTick(() => {
					const $mounted = $viewModel['mounted'];

					_.extend($viewModel, { $el: document.querySelector('#master-wrapper') });

					if ($mounted && _.isFunction($mounted)) {
						$mounted.apply($viewModel, []);
					}
				});

				__viewContext.bind($viewModel);
			});
		});
	}
}

function component(options: { name: string; template: string; }): any {
	return function (ctor: any): any {
		return $.Deferred().resolve(options.template.match(/\.html$/))
			.then((url: boolean) => {
				return url ? $.get(options.template) : options.template;
			})
			.then((template: string) => {
				if (!ko.components.isRegistered(options.name)) {
					ko.components.register(options.name, {
						template,
						viewModel: {
							createViewModel($params: any, $el: any) {
								const $viewModel = new ctor($params)
									, $created = $viewModel['created'];

								// hook to created function
								if ($created && _.isFunction($created)) {
									$created.apply($viewModel, [$params]);
								}

								// hook to mounted function
								$viewModel.$nextTick(() => {
									const $mounted = $viewModel['mounted'];

									_.extend($viewModel, { $el: $el.element });

									if ($mounted && _.isFunction($mounted)) {
										$mounted.apply($viewModel, []);
									}
								});

								Object.defineProperty($viewModel, 'dispose', {
									value: function dispose() {

										if (typeof $viewModel.destroyed === 'function') {
											$viewModel.destroyed.apply($viewModel, []);
										}
									}
								});

								return $viewModel;
							}
						}
					});
				}
			});
	}
}

function handler(params: { virtual?: boolean; bindingName: string; validatable?: boolean; }) {

	return function (constructor: { new(): KnockoutBindingHandler; }) {
		ko.bindingHandlers[params.bindingName] = new constructor();
		ko.virtualElements.allowedBindings[params.bindingName] = !!params.virtual;

		// block rewrite binding
		if (params.validatable) {
			ko.utils.extend(ko.expressionRewriting.bindingRewriteValidators, { [params.bindingName]: false });
		}
	}
}

// create base viewmodel for all implement
function BaseViewModel() { }

function $i18n(text: string, params?: string[]) {
	return resource.getText(text, params);
}

function $jump() {
	const args: Array<any> = [].slice.apply(arguments, [])
		, params = args.length === 3 && _.isString(args[0]) && _.isString(args[1]) ? args[2] :
			(args.length == 2 && _.indexOf(args[1], '.xhtml')) > -1 ? null : args[1];

	if (window.top === window.self) {
		$storage(params).then(() => request.jump.apply(null, args));
	} else {
		// jump from dialog or frame
		$storage(params).then(() => (request as any).jumpFromDialogOrFrame.apply(null, args));
	}
};

BaseViewModel.prototype.$i18n = $i18n;

Object.defineProperties($i18n, {
	text: {
		value: $i18n
	},
	message: {
		value: resource.getMessage
	},
	controlName: {
		value: resource.getControlName
	}
});

BaseViewModel.prototype.$ajax = request.ajax;
BaseViewModel.prototype.$nextTick = ko.tasks.schedule;

BaseViewModel.prototype.$user = __viewContext['user'];
BaseViewModel.prototype.$program = __viewContext['program'];

const $date = {
	diff: 0,
	now() {
		return Date.now()
	},
	today() {
		return $date.now();
	}
};

request.ajax('/server/time/now').then((time: string) => {
	Object.defineProperty($date, 'diff', {
		value: moment(time, 'YYYY-MM-DDTHH:mm:ss').diff(moment())
	});
})

BaseViewModel.prototype.$date = Object.defineProperties($date, {
	now: {
		value: function $now() {
			return moment().add($date.diff, 'ms').toDate();
		}
	},
	today: {
		value: function $today() {
			return moment($date.now()).startOf('day').toDate();
		}
	}
});

BaseViewModel.prototype.$dialog = Object.defineProperties({}, {
	info: {
		value: function $info() {
			const dfd = $.Deferred<void>();

			dialog.info.apply(null, [...(arguments as any)]).then(() => dfd.resolve());

			return dfd.promise();
		}
	},
	alert: {
		value: function $alert() {
			const dfd = $.Deferred<void>();

			dialog.alert.apply(null, [...(arguments as any)]).then(() => dfd.resolve());

			return dfd.promise();
		}
	},
	error: {
		value: function $error() {
			const dfd = $.Deferred<void>();

			dialog.error.apply(null, [...(arguments as any)]).then(() => dfd.resolve());

			return dfd.promise();
		}
	},
	confirm: {
		value: function $confirm() {
			const dfd = $.Deferred<'no' | 'yes' | 'cancel'>();

			const $cf = dialog.confirm.apply(null, [...(arguments as any)]);

			$cf.ifYes(() => {
				dfd.resolve('yes');
			});

			$cf.ifNo(() => {
				dfd.resolve('no');
			});

			$cf.ifCancel(() => {
				dfd.resolve('cancel');
			});

			return dfd.promise();
		}
	}
});

BaseViewModel.prototype.$jump = $jump;

Object.defineProperties($jump, {
	self: {
		value: function $to() {
			$jump.apply(null, [...[].slice.apply(arguments, [])]);
		}
	},
	blank: {
		value: function $other() {
			const args: Array<any> = [].slice.apply(arguments, [])
				, params = args.length === 3 && _.isString(args[0]) && _.isString(args[1]) ? args[2] :
					(args.length == 2 && _.indexOf(args[1], '.xhtml')) > -1 ? null : args[1];

			$storage(params).then(() => (request as any).jumpToNewWindow.apply(null, args));
		}
	}
});


const $size = function (height: string | number, width: string | number) {
	const wd = nts.uk.ui.windows.getSelf();

	if (wd) {
		wd.setSize(height, width);
	}
};

Object.defineProperties($size, {
	width: {
		value: function (width: string | number) {
			const wd = nts.uk.ui.windows.getSelf();

			if (wd) {
				wd.setWidth(width);
			}
		}
	},
	height: {
		value: function (height: string | number) {
			const wd = nts.uk.ui.windows.getSelf();

			if (wd) {
				wd.setHeight(height);
			}
		}
	}
});

BaseViewModel.prototype.$window = Object.defineProperties({}, {
	size: {
		value: $size
	},
	close: {
		value: function $close(result?: any) {
			if (window.top !== window) {
				$.Deferred().resolve()
					.then(() => $storage(result))
					.then(() => windows.close());
			}
		}
	},
	modal: {
		value: function $modal(webapp: string, path: string, params?: any) {
			const jdf = $.Deferred<any>();
			const nowapp = ['at', 'pr', 'hr', 'com'].indexOf(webapp) === -1;

			if (nowapp) {
				$storage(path).then(() => {
					windows.sub.modal(webapp)
						.onClosed(() => {
							const { localShared } = windows.container;

							_.each(localShared, (value: any, key: string) => {
								windows.setShared(key, value);
							});

							$storage().then(($data: any) => {
								jdf.resolve($data);
							});
						});
				});
			} else {
				$storage(params).then(() => {
					windows.sub.modal(webapp, path)
						.onClosed(() => {
							const { localShared } = windows.container;

							_.each(localShared, (value: any, key: string) => {
								windows.setShared(key, value);
							});


							$storage().then(($data: any) => {
								jdf.resolve($data);
							});
						});
				});
			}

			return jdf.promise();
		}
	},
	modeless: {
		value: function $modeless(webapp: string, path: string, params?: any) {
			const jdf = $.Deferred<any>();
			const nowapp = ['at', 'pr', 'hr', 'com'].indexOf(webapp) === -1;

			if (nowapp) {
				$storage(path).then(() => {
					windows.sub.modeless(webapp)
						.onClosed(() => {
							const { localShared } = windows.container;

							_.each(localShared, (value: any, key: string) => {
								windows.setShared(key, value);
							});

							$storage().then(($data: any) => {
								jdf.resolve($data);
							});
						});
				});
			} else {
				$storage(params).then(() => {
					windows.sub.modeless(webapp, path)
						.onClosed(() => {
							const { localShared } = windows.container;

							_.each(localShared, (value: any, key: string) => {
								windows.setShared(key, value);
							});

							$storage().then(($data: any) => {
								jdf.resolve($data);
							});
						});
				});
			}

			return jdf.promise();
		}
	},
	storage: {
		value: function $storage(name: string, params: any) {
			if (arguments.length == 1) {
				return $storeSession(name);
			} else {
				return $.Deferred().resolve()
					.then(() => {
						$storeSession(name, params);
						// for old page
						windows.setShared(name, params);
					});
			}
		}
	}
});

// Hàm blockui được wrapper lại để gọi cho thống nhất
BaseViewModel.prototype.$blockui = function $blockui(act: 'show' | 'hide' | 'clear' | 'invisible' | 'grayout') {
	return $.Deferred().resolve()
		.then(() => {
			switch (act) {
				default:
				case 'hide':
				case 'clear':
					block.clear();
					break;
				case 'show':
				case 'invisible':
					block.invisible();
					break;
				case 'grayout':
					block.grayout();
					break;
			}
		});
};

BaseViewModel.prototype.$errors = function $errors() {
	const args: any[] = Array.prototype.slice.apply(arguments);

	if (args.length == 1) {
		// if action is clear, call validate clear action
		if (args[0] === 'clear') {
			return $.Deferred().resolve()
				.then(() => $('.nts-input').ntsError('clear'));
		} else {
			const errors: {
				[name: string]: any;
			} = args[0];

			return $.Deferred().resolve()
				.then(() => {
					_.each(errors, (value: any, key: string) => $(key).ntsError('set', value));
				})
				.then(() => !$(_.keys(errors).join(', ')).ntsError('hasError'));
		}
	} else if (args.length === 2) {
		const name: string = args[0]
			, messageId: any = args[1];

		if (name === 'clear') {
			if (_.isString(messageId)) {
				const $selector = messageId;

				return $.Deferred().resolve()
					.then(() => $($selector).ntsError('clear'))
					.then(() => !$($selector).ntsError('hasError'));
			} else if (_.isArray(messageId)) {
				const $selectors = messageId.join(', ');

				return $.Deferred().resolve()
					.then(() => $($selectors).ntsError('clear'))
					.then(() => !$($selectors).ntsError('hasError'));
			}
		} else {
			if (_.isString(messageId)) {
				return $.Deferred().resolve()
					.then(() => $(name).ntsError('set', { messageId }))
					.then(() => !$(name).ntsError('hasError'));
			} else {
				return $.Deferred().resolve()
					.then(() => $(name).ntsError('set', messageId))
					.then(() => !$(name).ntsError('hasError'));
			}
		}
	} else if (args.length > 2) {
		if (args[0] === 'clear') {
			const $selectors = args.join(', ').replace(/^clear ,/, '');

			return $.Deferred().resolve()
				.then(() => $($selectors).ntsError('clear'))
				.then(() => !$($selectors).ntsError('hasError'));
		}
	}

	return $.Deferred().resolve()
		/** Nếu có lỗi thì trả về false, không thì true */
		.then(() => !$('.nts-input').ntsError('hasError'));;
};

// Hàm validate được wrapper lại để có thể thực hiện promisse
const $validate = function $validate(act: string[]) {
	const args: string[] = Array.prototype.slice.apply(arguments);

	if (args.length === 0) {
		return $.Deferred().resolve()
			/** Gọi xử lý validate của kiban */
			.then(() => $('.nts-input').trigger("validate"))
			/** Nếu có lỗi thì trả về false, không thì true */
			.then(() => !$('.nts-input').ntsError('hasError'));
	} else if (args.length === 1) {
		let selectors = '';

		if (_.isString(act)) {
			selectors = act;
		} else if (_.isArray(act)) {
			selectors = act.join(', ');
		}

		return $.Deferred().resolve()
			/** Gọi xử lý validate của kiban */
			.then(() => $(selectors).trigger("validate"))
			/** Nếu có lỗi thì trả về false, không thì true */
			.then(() => !$(selectors).ntsError('hasError'));
	} else {
		let selectors = args.join(', ');

		return $.Deferred().resolve()
			/** Gọi xử lý validate của kiban */
			.then(() => $(selectors).trigger("validate"))
			/** Nếu có lỗi thì trả về false, không thì true */
			.then(() => !$(selectors).ntsError('hasError'));
	}
};

Object.defineProperty($validate, "constraint", {
	value: function $constraint(name: string, value: any) {
		if(arguments.length === 0) {
			return $.Deferred().resolve()
			.then(() => __viewContext.primitiveValueConstraints);
		} else if (arguments.length === 1) {
			return $.Deferred().resolve()
				.then(() => _.get(__viewContext.primitiveValueConstraints, name));
		} else {
			return $.Deferred().resolve()
				.then(() => (ui.validation as any).writeConstraint(name, value));
		}
	}
});

BaseViewModel.prototype.$validate = $validate;

Object.defineProperty(ko, 'ViewModel', { value: BaseViewModel });

@handler({
	bindingName: 'i18n',
	validatable: true,
	virtual: false
})
class I18nBindingHandler implements KnockoutBindingHandler {
	update(element: HTMLElement, valueAccessor: () => string, allBindingsAccessor?: KnockoutAllBindingsAccessor): void {
		const msg = ko.unwrap(valueAccessor());
		const params = ko.unwrap(allBindingsAccessor.get('params'));

		$(element).text(nts.uk.resource.getText(msg, params));
	}
}

@handler({
	bindingName: 'icon',
	validatable: true,
	virtual: false
})
class IconBindingHandler implements KnockoutBindingHandler {
	update(el: HTMLElement, value: () => KnockoutObservable<number> | number) {
		ko.computed(() => {
			const numb: number = ko.toJS(value());
			const url = `/nts.uk.com.js.web/lib/nittsu/ui/style/stylesheets/images/icons/numbered/${numb}.png`;

			$.get(url)
				.then(() => {
					$(el).css({
						'background-image': `url('${url}')`,
						'background-repeat': 'no-repeat',
						'background-position': 'center'
					});
				});
		});
	}
}

@handler({
	bindingName: 'date',
	validatable: true,
	virtual: false
})
class DateBindingHandler implements KnockoutBindingHandler {
	update(element: HTMLElement, valueAccessor: () => KnockoutObservable<Date> | Date, allBindingsAccessor?: KnockoutAllBindingsAccessor) {
		const date = ko.unwrap(valueAccessor());
		const format = ko.unwrap(allBindingsAccessor.get('format')) || 'YYYY/MM/DD';

		$(element).text(moment(date).format(format));
	}
}