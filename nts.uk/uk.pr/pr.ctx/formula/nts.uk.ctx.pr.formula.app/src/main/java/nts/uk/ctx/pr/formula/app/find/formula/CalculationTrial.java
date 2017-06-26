package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import nts.arc.time.YearMonth;

@Stateless
public class CalculationTrial {

	private final String add = "+";
	private final String subtract = "-";
	private final String multiple = "*";
	private final String divide = "/";
	private final String pow = "^";
	private String openBracket = "(";
	private String closeBracket = ")";
	private String less = "<";
	private String more = ">";
	private String lessOrEqual = "<=";
	private String moreOrEqual = ">=";
	private String equal = "=";
	private String different = "!=";

	private String conditionalExpression = "F@1";
	private String andFunction = "F@2";
	private String orFunction = "F@3";
	private String roundFunction = "F@4";
	private String truncateFunction = "F@5";
	private String roundUpFunction = "F@6";
	private String maximumFunction = "F@7";
	private String minimumFunction = "F@8";
	private String familyMemberFunction = "F@9";
	private String addMonthFunction = "F@10";
	private String extractYearFunction = "F@11";
	private String extractMonthFunction = "F@12";

	public CalculatorDto init(String formulaContent) {
		// String formula = "関数＠条件式（関数＠最大値（ 100 , 200 ）× " + "関数＠四捨五入（ 20.5 ）＞
		// 0, 1000, 2000）+ "
		// + "関数＠最小値（100, 200）÷ 関数＠切上げ（ 10.6 ）× " + "関数＠条件式（ 関数＠かつ（ 関数＠年抽出（
		// 200010 ）＞1999 ,"
		// + "関数＠月抽出（ 200001 ）＜ 3 ）, 1.5 , 2）";
		// String formula = "1+2*3*2*2+5+5*2/2-5+F@1(2<4,100,200)+F@6(1.2)";
		formulaContent = rePlaceFormula(formulaContent);
		List<String> content = new ArrayList<>();
		replaceBracket(formulaContent, content);
		formulaContent = content.get(0);
		int smallIndex;
		CalculatorDto calculatorDto = new CalculatorDto();
		// case first char is digit
		if (isDigit(formulaContent.charAt(0))) {
			smallIndex = findLocation(formulaContent);
			// get first value of formula
			BigDecimal firstValue = new BigDecimal(formulaContent.substring(0, smallIndex));
			String firstSign = formulaContent.substring(smallIndex, smallIndex + 1);
			// remove first value and sign outside formula
			formulaContent = formulaContent.substring(smallIndex + 1, formulaContent.length());
			List<BigDecimal> resultValue = new ArrayList<>();
			proceedExcuteWithDigit(firstValue, null, firstSign, null, formulaContent, resultValue);
			calculatorDto.setResult(resultValue.get(0).toString());
		} 
		return calculatorDto;
	}
	
	private void replaceBracket (String formula, List<String> content) {
		StringBuilder formulaBuilder = new StringBuilder(formula);
		int index = formulaBuilder.indexOf(openBracket);
		int smallIndex;
		if(index > -1){
			List<String> subText = splitFormula(formula);
			String subFormula = subText.get(0);
			smallIndex = findLocation(subFormula);
			// get first value of formula
			BigDecimal firstValue = new BigDecimal(subFormula.substring(0, smallIndex));
			String firstSign = subFormula.substring(smallIndex, smallIndex + 1);
			// remove first value and sign outside formula
			subFormula = subFormula.substring(smallIndex + 1, subFormula.length());
			List<BigDecimal> resultValue = new ArrayList<>();
			proceedExcuteWithDigit(firstValue, null, firstSign, null, subFormula, resultValue);
			formula = formulaBuilder.replace(index, Integer.parseInt(subText.get(1)) + 1, resultValue.get(0).toString()).toString();
			replaceBracket(formula, content);
		}else {
			content.add(formula);
		}
	}

	private String rePlaceFormula(String formula) {
		StringBuilder formulaBuilder = new StringBuilder(formula);
		// get conditionalExpression
		int indexOfformula = findFormula(conditionalExpression, formulaBuilder);
		int indexOfCloseBracket;
		int indexOfOpenBracket;
		String subText = null;
		BigDecimal value = null;
		boolean trueValue;
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = conditionalExpression(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get andFunction
		indexOfformula = findFormula(andFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			trueValue = andFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, trueValue + "");
		}
		// get orFunction
		indexOfformula = findFormula(orFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			trueValue = orFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, trueValue + "");
		}
		// get roundFunction
		indexOfformula = findFormula(roundFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = roundFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get truncateFunction
		indexOfformula = findFormula(truncateFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = truncateFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get roundUpFunction
		indexOfformula = findFormula(roundUpFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = roundUpFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get maximumFunction
		indexOfformula = findFormula(maximumFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = maximumFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get minimumFunction
		indexOfformula = findFormula(minimumFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = minimumFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get familyMemberFunction
		indexOfformula = findFormula(familyMemberFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = familyMemberFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get addMonthFunction
		indexOfformula = findFormula(addMonthFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = addMonthFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get extractYearFunction
		indexOfformula = findFormula(extractYearFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = extractYearFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		// get extractMonthFunction
		indexOfformula = findFormula(extractMonthFunction, formulaBuilder);
		if (indexOfformula > -1) {
			subText = formulaBuilder.substring(indexOfformula);
			indexOfOpenBracket = subText.indexOf(openBracket);
			indexOfCloseBracket = subText.indexOf(closeBracket);
			subText = subText.substring(indexOfOpenBracket + 1, indexOfCloseBracket);
			value = extractMonthFunction(subText);
			formulaBuilder.replace(indexOfformula, indexOfformula + indexOfCloseBracket + 1, value.toString());
		}
		return formulaBuilder.toString();
	}

	private void proceedExcuteWithDigit(BigDecimal result, BigDecimal preValue, String firstSign, String preSign,
			String formula, List<BigDecimal> resultValue) {
		int smallIndex;
		if (formula != null && !checkIsEndOfString(formula) && isDigit(formula.charAt(0))) {
			smallIndex = findLocation(formula);
			// get second value of formula
			BigDecimal secondValue = new BigDecimal(formula.substring(0, smallIndex));
			String secondSign = formula.substring(smallIndex, smallIndex + 1);

			if (secondSign.equals(add) || secondSign.equals(subtract)
					|| ((secondSign.equals(multiple) || secondSign.equals(divide))
							&& (firstSign.equals(multiple) || firstSign.equals(divide)))) {
				if (preValue == null) {
					result = executeSign(result, secondValue, firstSign);
				} else {
					// result = executeSign(result, preValue, firstSign);
					// result = executeSign(result, secondValue, preSign);
				}
				firstSign = secondSign;
				// remove second value and sign outside formula
				formula = formula.substring(smallIndex + 1, formula.length());
				proceedExcuteWithDigit(result, null, firstSign, null, formula, resultValue);
			} else if ((secondSign.equals(multiple) || secondSign.equals(divide))
					&& (firstSign.equals(add) || firstSign.equals(subtract))) {
				if (isDigit(formula.charAt(smallIndex + 1))) {
					int index = findLocationAddOrSubtract(formula);
					String subformula = index > -1 ? formula.substring(0, index) : formula;
					List<BigDecimal> value = new ArrayList<>();
					BigDecimal totalValue = calculateMulOrDiv(value, subformula);
					result = executeSign(result, totalValue, firstSign);
					firstSign = index > -1 ? formula.substring(index, index + 1) : null;
					formula = index > -1 ? formula.substring(index + 1, formula.length()) : null;

					// int nextIndex = findLocation(formula);
					// BigDecimal nextValue = new
					// BigDecimal(formula.substring(0, nextIndex));
					// String thirdSign = formula.substring(nextIndex, nextIndex
					// + 1);
					// preValue = executeSign(secondValue, nextValue,
					// secondSign);
					// formula = formula.substring(nextIndex + 1,
					// formula.length());
					// preSign = thirdSign;
					proceedExcuteWithDigit(result, null, firstSign, null, formula, resultValue);
				}
			}
			// else if ((secondSign.equals(multiple) ||
			// secondSign.equals(divide))
			// && (firstSign.equals(add) || firstSign.equals(subtract))) {
			// if (isDigit(formula.charAt(0))) {
			// // int nextIndex = findLocation(formula);
			// BigDecimal nextValue = new BigDecimal(formula);
			// preValue = executeSign(secondValue, nextValue, secondSign);
			// result = executeSign(result, preValue, firstSign);
			// }
			// }
		} else if (formula == null || checkIsEndOfString(formula) && isDigit(formula.charAt(0))) {
			if (firstSign != null && preSign == null) {
				result = executeSign(result, new BigDecimal(formula), firstSign);
			} else if (preSign != null) {
				if (preSign.equals(multiple) || preSign.equals(divide)) {
					preValue = executeSign(preValue, new BigDecimal(formula), preSign);
					result = executeSign(result, preValue, firstSign);
				} else {
					result = executeSign(result, preValue, firstSign);
					result = executeSign(result, new BigDecimal(formula), preSign);
				}
			}
			resultValue.add(result);
		}

	}

	private boolean checkIsEndOfString(String formula) {
		int index = findLocation(formula);
		return index > -1 ? false : true;
	}

	private BigDecimal calculateMulOrDiv(List<BigDecimal> resultValue, String formula) {
		BigDecimal result = null;
		int smallIndex;
		if (formula != null && isDigit(formula.charAt(0))) {
			smallIndex = findLocation(formula);
			String firstSign = formula.substring(smallIndex, smallIndex + 1);
			// get first value of formula
			result = new BigDecimal(formula.substring(0, smallIndex));
			formula = formula.substring(smallIndex + 1, formula.length());
			smallIndex = findLocation(formula);
			BigDecimal secondValue = smallIndex > -1 ? new BigDecimal(formula.substring(0, smallIndex))
					: new BigDecimal(formula);
			result = executeSign(result, secondValue, firstSign);
			if (formula.length() <= 1) {
				resultValue.add(result);
			}
			formula = formula.length() > 1 ? result + formula.substring(smallIndex, formula.length()) : null;
			calculateMulOrDiv(resultValue, formula);
		}
		return resultValue.get(0);
	}

	private boolean isDigit(Character para) {
		return Character.isDigit(para);
	}

	private BigDecimal executeSign(BigDecimal firstValue, BigDecimal secondValue, String sign) {
		BigDecimal result = null;
		switch (sign) {
		case add:
			result = firstValue.add(secondValue);
			break;
		case subtract:
			result = firstValue.subtract(secondValue);
			break;
		case multiple:
			result = firstValue.multiply(secondValue);
			break;
		case divide:
			result = firstValue.divide(secondValue);
			break;
		case pow:
			result = firstValue.pow(secondValue.intValue());
			break;
		}
		return result;
	}

	private int findFormula(String formula, StringBuilder text) {
		return text.indexOf(formula);
	}

	private int findLocation(String formula) {
		int[] arr = new int[] { formula.indexOf(add), formula.indexOf(subtract), formula.indexOf(multiple),
				formula.indexOf(divide), formula.indexOf(pow, formula.indexOf(openBracket)) };
		return Arrays.stream(arr).filter(item -> item != -1).count() > 0
				? Arrays.stream(arr).filter(item -> item != -1).reduce((x, y) -> x < y ? x : y).getAsInt() : -1;
	}

	private int findLocationAddOrSubtract(String formula) {
		int[] arr = new int[] { formula.indexOf(add), formula.indexOf(subtract) };
		return Arrays.stream(arr).filter(item -> item != -1).count() > 0
				? Arrays.stream(arr).filter(item -> item != -1).reduce((x, y) -> x < y ? x : y).getAsInt() : -1;
	}

	private List<String> splitFormula(String formula) {
		int openIndex = formula.indexOf(openBracket);
		int closeIndex = formula.indexOf(closeBracket);
		int start = openIndex;
		int countOpen = 1;
		int countClose = 1;
		int end = closeIndex;
		while (openIndex != -1 && openIndex < closeIndex) {
			openIndex = formula.indexOf("(", openIndex + 1);
			if (openIndex != -1 && openIndex < closeIndex) {
				countOpen += 1;
			}
		}
		while (countClose < countOpen) {
			if (openIndex > -1 && openIndex < formula.length()) {
				openIndex = formula.indexOf(openBracket, openIndex < closeIndex ? closeIndex + 1 : openIndex + 1);
				if (openIndex != -1 && (openIndex < closeIndex || openIndex > end)) {
					countOpen += 1;
				}
			}
			closeIndex = formula.indexOf(closeBracket, closeIndex + 1);
			countClose += 1;
		}
		List<String> subFormula = new ArrayList<>();
//		if (!Character.isDigit(formula.charAt(0))) {
			subFormula.add(formula.substring(start + 1, closeIndex));
			subFormula.add(closeIndex + "");
//		}
		return subFormula;
	}

	private BigDecimal conditionalExpression(String expression) {
		String[] arrs = expression.split(",");
		String[] exps = arrs[0].split(">|<|>=|<=|!=|=");

		String conditionFormula = exps[0];
		BigDecimal conditionValue = null;
		List<BigDecimal> resultValue = new ArrayList<>();
		int smallIndex = findLocation(conditionFormula);
		if (smallIndex > -1) {
			// get first value of formula
			BigDecimal firstValue = new BigDecimal(conditionFormula.substring(0, smallIndex));
			String firstSign = conditionFormula.substring(smallIndex, smallIndex + 1);
			conditionFormula = conditionFormula.substring(smallIndex + 1, conditionFormula.length());
			proceedExcuteWithDigit(firstValue, null, firstSign, null, conditionFormula, resultValue);
			conditionValue = resultValue.get(0);
		} else {
			conditionValue = new BigDecimal(exps[0]);
		}

		BigDecimal fValue = new BigDecimal(arrs[1]);
		BigDecimal sValue = new BigDecimal(arrs[2]);
		BigDecimal result = null;
		int compare = conditionValue.compareTo(new BigDecimal(exps[1]));
		if (arrs[0].contains(more)) {
			result = compare > 0 ? fValue : sValue;
		} else if (arrs[0].contains(less)) {
			result = compare < 0 ? fValue : sValue;
		} else if (arrs[0].contains(moreOrEqual)) {
			result = compare >= 0 ? fValue : sValue;
		} else if (arrs[0].contains(lessOrEqual)) {
			result = compare <= 0 ? fValue : sValue;
		} else if (arrs[0].contains(different)) {
			result = compare != 0 ? fValue : sValue;
		} else if (arrs[0].contains(equal)) {
			result = compare == 0 ? fValue : sValue;
		}
		return result;
	}

	private boolean andFunction(String paras) {
		String[] arrs = paras.split(",");
		for (String formula : arrs) {

		}
		return true;
	}

	private boolean orFunction(String paras) {
		String[] arrs = paras.split(",");
		for (String formula : arrs) {

		}
		return true;
	}

	private BigDecimal roundFunction(String para) {
		return new BigDecimal(para).setScale(0, RoundingMode.HALF_UP);
	}

	private BigDecimal truncateFunction(String para) {
		return new BigDecimal(para).setScale(0, RoundingMode.DOWN);
	}

	private BigDecimal roundUpFunction(String para) {
		return new BigDecimal(para).setScale(0, RoundingMode.UP);
	}

	private BigDecimal maximumFunction(String paras) {
		String[] arrs = paras.split(",");
		List<BigDecimal> values = new ArrayList<>();
		for (String value : arrs) {
			values.add(new BigDecimal(value));
		}
		values.sort((a, b) -> a.compareTo(b));
		return values.get(values.size() - 1);
	}

	private BigDecimal minimumFunction(String paras) {
		String[] arrs = paras.split(",");
		List<BigDecimal> values = new ArrayList<>();
		for (String value : arrs) {
			values.add(new BigDecimal(value));
		}
		values.sort((a, b) -> a.compareTo(b));
		return values.get(0);
	}

	private BigDecimal familyMemberFunction(String para) {
		return BigDecimal.ONE;
	}

	private BigDecimal addMonthFunction(String para) {
		String[] arrs = para.split(",");
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(arrs[0])).addMonths(Integer.parseInt(arrs[1]));
		return new BigDecimal(yearMonth.v());
	}

	private BigDecimal extractYearFunction(String para) {
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(para.replace("/", "")));
		return new BigDecimal(yearMonth.year());
	}

	private BigDecimal extractMonthFunction(String para) {
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(para.replace("/", "")));
		return new BigDecimal(yearMonth.month());
	}
}
