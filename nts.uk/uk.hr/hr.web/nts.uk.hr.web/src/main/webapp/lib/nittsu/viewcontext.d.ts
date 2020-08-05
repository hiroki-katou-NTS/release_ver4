/// <reference path="../generic/lodash/lodash.d.ts" />
/// <reference path="../generic/jquery/jquery.d.ts" />
/// <reference path="../generic/jqueryui/jqueryui.d.ts" />
/// <reference path="../generic/momentjs/moment.d.ts" />
/// <reference path="../generic/knockoutjs/knockout.d.ts" />
/// <reference path="./nts.uk.com.web.nittsu.bundles.d.ts" />

/** Decorator for load ViewModel of main View*/
declare function bean(): any;

/** Decorator for auto create binding handler */
declare function handler(params: BindingOption): any;

/** Decorator for load ViewModel of Component */
declare function component(params: ViewModelOption): any;

/** Current language */
declare const systemLanguage: string;

/** Resources data */
declare const names: Resources;

/** Messages data */
declare const messages: Resources;

/** ViewContext (root object of UK App) */
declare const __viewContext: ViewContext;

declare type WEB_APP = 'at' | 'com' | 'hr' | 'pr';

interface ViewContext {
	readonly noHeader: boolean;

	readonly rootPath: string;

	readonly env: Environment;

	readonly messages: Resources;
	readonly codeNames: Resources;

	readonly primitiveValueConstraints: PrimitiveConstraints;

	readonly title: string;
	readonly transferred: nts.uk.util.optional.Optional<any>;

	readonly bind: (viewModel: any) => void;
	readonly ready: (callback: () => void) => void;
}

// Data structure of names and messages
interface Resources {
	readonly [key: string]: string;
}

// Data structure of primitiveConstraints
interface PrimitiveConstraints {
	[key: string]: Constraint;
}

// Constraint structure
interface Constraint {
	min?: number | Date;
	max?: number | Date;
	maxLength?: number;
	mantissaMaxLength?: number;
	isZeroPadded?: boolean;
	formatOption?: {
		autofill: boolean;
		fillcharacter: string;
		filldirection: 'left' | 'right';
	};
	charType?: 'Any' | 'Kana' | 'AnyHalfWidth' | 'AlphaNumeric' | 'Numeric';
	valueType?: 'String' | 'Decimal' | 'Integer' | 'HalfInt' | 'Date' | 'Time' | 'Clock' | 'Duration' | 'TimePoint';
	[key: string]: any;
}

interface Environment {
	readonly isCloud: boolean;
	readonly isOnPremise: boolean;
	readonly japaneseEras: any[];
	readonly pathToManual: string;
	readonly products: {
		readonly attendance: boolean;
		readonly payroll: boolean;
		readonly personnel: boolean;
	};
	readonly systemName: string;
}

interface BindingOption {
	readonly virtual?: boolean;
	readonly bindingName: string;
	readonly validatable?: boolean;
}

interface ViewModelOption {
	readonly name: string;
	readonly template: string;
}

interface KnockoutStatic {
	readonly ViewModel: {
		new(): ComponentViewModel;
	}
}

interface ComponentViewModel {
	readonly $el: HTMLElement;
	readonly $user: {
		readonly contractCode: string;
		readonly companyId: string;
		readonly companyCode: string;
		readonly isEmployee: boolean;
		readonly employeeId: string;
		readonly employeeCode: string;
		readonly selectedLanguage: {
			readonly basicLanguageId: string;
			readonly personNameLanguageId: string;
		};
		readonly role: {
			readonly attendance: string | null;
			readonly companyAdmin: string | null;
			readonly groupCompanyAdmin: string | null;
			readonly officeHelper: string | null;
			readonly payroll: string | null;
			readonly personalInfo: string | null;
			readonly personnel: string | null;
			readonly systemAdmin: string | null;
		};
	};
	readonly $program: {
		readonly webapi: WEB_APP;
		readonly programId: string;
		readonly programName: string;
		readonly path: string;
		readonly isDebugMode: boolean;
	};
	readonly $date: {
		readonly now: {
			(): Date;
		};
		readonly today: {
			(): Date;
		};
	};
	readonly $i18n: {
		(textId: string): string;
		(textId: string, params: string[]): string;
		readonly text: {
			(textId: string): string;
			(textId: string, params: string[]): string;
		};
		readonly message: {
			(messageId: string): string;
			(messageId: string, messageParams: string[]): string;
		};
		readonly controlName: {
			(name: string): string;
		};
	};
	readonly $ajax: {
		(url: string): JQueryDeferred<any>;
		(url: string, data: any): JQueryDeferred<any>;
		(webapp: WEB_APP, url: string, data: any): JQueryDeferred<any>;
	};
	readonly $window: {
		readonly size: {
			(height: string | number, width: string | number): void;
			readonly width: (width: string) => void;
			readonly height: (height: string) => void;
		};
		readonly close: {
			(): void;
			(result: any): void;
		};
		readonly modal: {
			(url: string): JQueryDeferred<any>;
			(url: string, data: any): JQueryDeferred<any>;
			(webapp: WEB_APP, url: string): JQueryDeferred<any>;
			(webapp: WEB_APP, url: string, data: any): JQueryDeferred<any>;
		};
		readonly modeless: {
			(url: string): JQueryDeferred<any>;
			(url: string, data: any): JQueryDeferred<any>;
			(webapp: WEB_APP, url: string): JQueryDeferred<any>;
			(webapp: WEB_APP, url: string, data: any): JQueryDeferred<any>;
		};
		readonly storage: {
			(name: string): JQueryDeferred<any>;
			(name: string, params: any): JQueryDeferred<any>;
		};
	}
	readonly $dialog: {
		readonly info: {
			(message: string): JQueryDeferred<void>;
			(options: { messageId: string; }): JQueryDeferred<void>;
			(options: { messageId: string; messageParams: string[]; }): JQueryDeferred<void>;
		};
		readonly alert: {
			(message: string): JQueryDeferred<void>;
			(options: { messageId: string; }): JQueryDeferred<void>;
			(options: { messageId: string; messageParams: string[]; }): JQueryDeferred<void>;
		};
		readonly error: {
			(message: string): JQueryDeferred<void>;
			(options: { messageId: string; }): JQueryDeferred<void>;
			(options: { messageId: string; messageParams: string[]; }): JQueryDeferred<void>;
		};
		readonly confirm: {
			(message: string): JQueryDeferred<void>;
			(options: { messageId: string; }): JQueryDeferred<'no' | 'yes' | 'cancel'>;
			(options: { messageId: string; messageParams: string[]; }): JQueryDeferred<'no' | 'yes' | 'cancel'>;
		};
	}
	readonly $blockui: (act: 'show' | 'hide' | 'clear' | 'invisible' | 'grayout') => JQueryDeferred<void>;
	readonly $validate: {
		(): JQueryDeferred<boolean>;
		(selector: string): JQueryDeferred<boolean>;
		(selectors: string[]): JQueryDeferred<boolean>;
		(...selectors: string[]): JQueryDeferred<boolean>;
		readonly constraint: {
			(): JQueryDeferred<PrimitiveConstraints>;
			(name: string): JQueryDeferred<Constraint>;
			(name: string, value: Constraint): JQueryDeferred<void>;
		};
	};
	readonly $errors: {
		(): JQueryDeferred<boolean>;
		(act: 'clear'): JQueryDeferred<boolean>;
		(act: 'clear', names: string[]): JQueryDeferred<boolean>;
		(act: 'clear', ...names: string[]): JQueryDeferred<boolean>;
		(name: string, messageId: string): JQueryDeferred<boolean>;
		(name: string, message: { messageId: string }): JQueryDeferred<boolean>;
		(name: string, message: { messageId: string; messageParams: string[]; }): JQueryDeferred<boolean>;
		(errors: { [name: string]: { messageId: string; messageParams?: string[]; } }): JQueryDeferred<boolean>;
	}
	readonly $jump: {
		(url: string): void;
		(url: string, params: any): void;
		(webapp: WEB_APP, url: string): void;
		(webapp: WEB_APP, url: string, params: any): void;
		readonly self: {
			(url: string): void;
			(url: string, params: any): void;
			(webapp: WEB_APP, url: string): void;
			(webapp: WEB_APP, url: string, params: any): void;
		}
		readonly blank: {
			(url: string): void;
			(url: string, params: any): void;
			(webapp: WEB_APP, url: string): void;
			(webapp: WEB_APP, url: string, params: any): void;
		}
	};

	/**
	 * $nextTick be call when DOM is ready or all descendant component, binding are updated.
	 */
	readonly $nextTick: {
		(cb: () => void): number;
	};
}
