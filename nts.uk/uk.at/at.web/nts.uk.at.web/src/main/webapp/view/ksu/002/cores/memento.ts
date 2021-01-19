/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.ui.memento {
	export interface MementoObservableArray<T> extends KnockoutObservableArray<T> {
		undo(): void;
		redo(): void;
		reset(soft?: boolean): void;
		memento(state: StateMemento): void;
		undoAble: KnockoutComputed<boolean>;
		redoAble: KnockoutComputed<boolean>;
		hasChange: KnockoutReadonlyObservable<boolean>;
	}

	export interface Options {
		size: number;
		replace: (sources: any, preview: any) => void;
		softReset: (sources: any) => void;
		hasChange: (sources: any) => boolean;
	}

	export interface StateMemento<T = any> {
		current: T;
		preview: T;
	}

	interface Memento {
		undo: KnockoutObservableArray<StateMemento>;
		redo: KnockoutObservableArray<StateMemento>;
	}

	const memento = function (target: KnockoutObservableArray<any>, options: Options) {
		if (!options) {
			options = {
				size: 9999,
				replace: function () { return true; },
				softReset: function () { },
				hasChange: function () { return false; }
			};
		}

		if (options.size < 1) {
			options.size = 9999;
		}

		if (!options.replace) {
			options.replace = function () { return true; };
		}

		if (!options.softReset) {
			options.softReset = function () { };
		}

		if (!options.hasChange) {
			options.hasChange = function () { return false; };
		}

		const hasChange = ko.observable(false);

		const $memento: Memento = {
			undo: ko.observableArray([]),
			redo: ko.observableArray([])
		};

		// strip length of memento if over size
		const stripMemory = () => {
			while (ko.unwrap($memento.undo).length > options.size) {
				$memento.undo.pop();
			}

			while (ko.unwrap($memento.redo).length > options.size) {
				$memento.redo.pop();
			}
		};

		// extends memento methods to observable
		_.extend(target, {
			reset: function $$reset(soft: boolean = false) {
				if (!soft) {
					$memento.undo([]);
					$memento.redo([]);
				} else {
					options.softReset.apply(target, [ko.unwrap(target)]);
				}

				hasChange(options.hasChange.apply(target, [ko.unwrap(target)]));
			},
			memento: function $$memento(state: StateMemento) {
				hasChange(true);

				$memento.redo([]);

				// push old data to memories			
				$memento.undo.unshift(ko.toJS(state));

				// remove old record when memory size has large than config
				stripMemory();

				hasChange(options.hasChange.apply(target, [ko.unwrap(target)]));
			},
			undo: function $$undo() {
				if (ko.unwrap($memento.undo).length) {
					const state = $memento.undo.shift();

					// add state to redo
					$memento.redo.unshift({ current: state.preview, preview: state.current });

					// remove old record when memory size has large than config
					stripMemory();

					options.replace.apply(target, [ko.unwrap(target), state.preview]);
				}

				hasChange(options.hasChange.apply(target, [ko.unwrap(target)]));
			},
			undoAble: ko.computed(() => !!ko.unwrap($memento.undo).length),
			redo: function $$redo() {
				if (ko.unwrap($memento.redo).length) {
					const state = $memento.redo.shift();

					// add state to undo
					$memento.undo.unshift({ current: state.preview, preview: state.current });

					// remove old record when memory size has large than config
					stripMemory();

					options.replace.apply(target, [ko.unwrap(target), state.preview]);
				}

				hasChange(options.hasChange.apply(target, [ko.unwrap(target)]));
			},
			redoAble: ko.computed(() => !!ko.unwrap($memento.redo).length),
			hasChange
		});

		return target;
	};

	// register memento to ko static
	_.extend(ko.extenders, { memento });
}