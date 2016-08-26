package at.siemens.ct.jmz.elements;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import at.siemens.ct.common.utils.ListUtils;
import at.siemens.ct.jmz.expressions.Expression;
import at.siemens.ct.jmz.expressions.NamedConstantSet;
import at.siemens.ct.jmz.expressions.array.ArrayConstant;
import at.siemens.ct.jmz.expressions.array.ArrayVariable;
import at.siemens.ct.jmz.expressions.array.ExplicitIntegerList;
import at.siemens.ct.jmz.expressions.array.IntegerArrayVariable;
import at.siemens.ct.jmz.expressions.bool.BooleanVariable;
import at.siemens.ct.jmz.expressions.comprehension.Generator;
import at.siemens.ct.jmz.expressions.comprehension.IteratorExpression;
import at.siemens.ct.jmz.expressions.comprehension.ListComprehension;
import at.siemens.ct.jmz.expressions.integer.IntegerConstant;
import at.siemens.ct.jmz.expressions.integer.IntegerVariable;
import at.siemens.ct.jmz.expressions.integer.NamedIntegerConstant;
import at.siemens.ct.jmz.expressions.set.OptionalIntSet;
import at.siemens.ct.jmz.expressions.set.PseudoOptionalIntSet;
import at.siemens.ct.jmz.expressions.set.RangeExpression;

/**
 * Tests declarations of various {@link Element}s
 *
 * @author z003ft4a (Richard Taupe)
 *
 */
public class TestDeclarations {

  @Test
	@Ignore // TODO
  public void testArrayWithListComprehension() {
    int lb = 1, ub = 10;
    RangeExpression range = new RangeExpression(lb, ub);
    String iteratorName = "i";
    IteratorExpression iterator = range.iterate(iteratorName);
    Generator generator = new Generator(iterator);
    // String expression = "10*i";
    // TODO: re-introduce above expression as soon as integer products are supported
		// TODO: Expression<Integer> expression = iterator.add(10);
		Expression<Integer> expression = iterator;
		ListComprehension<Integer> comprehension = new ListComprehension<>(generator, expression);
		ArrayVariable<Integer> array = new IntegerArrayVariable("a", new RangeExpression(1, 10), comprehension);
    Assert.assertEquals("array[1..10] of var int: a = [ i + 10 | i in 1..10 ];", array.declare());
  }

  @Test
  public void testTwoDimensionalArrayVar() {
    String name = "a";
		NamedConstantSet<Integer> setOneTwoThree = new RangeExpression(1, 3).toNamedConstant("OneTwoThree");
		NamedConstantSet<Integer> setTwoThreeFour = new RangeExpression(2, 4).toNamedConstant("TwoThreeFour");
		List<NamedConstantSet<Integer>> range = ListUtils.fromElements(setOneTwoThree, setTwoThreeFour);
		ArrayVariable<Integer> array = new IntegerArrayVariable(name, range);
    Assert.assertEquals("array[OneTwoThree, TwoThreeFour] of var int: a;", array.declare());
  }

  @Test
  public void testThreeDimensionalArrayVar() {
    String name = "a";
		NamedConstantSet<Integer> setOneTwoThree = new RangeExpression(1, 3).toNamedConstant("OneTwoThree");
		NamedConstantSet<Integer> setTwoThreeFour = new RangeExpression(2, 4).toNamedConstant("TwoThreeFour");
		NamedConstantSet<Integer> setThreeFourFive = new RangeExpression(3, 5).toNamedConstant("ThreeFourFive");
		List<NamedConstantSet<Integer>> range = ListUtils.fromElements(setOneTwoThree, setTwoThreeFour, setThreeFourFive);
		ArrayVariable<Integer> array = new IntegerArrayVariable(name, range);
    Assert.assertEquals("array[OneTwoThree, TwoThreeFour, ThreeFourFive] of var int: a;",
        array.declare());
  }

  @Test
  public void testOptIntSet() {
    String nameOfSet = "I";
    String nameOfVar = "i";
    int lb = 2;
    int ub = 14;
		OptionalIntSet set = new OptionalIntSet(new RangeExpression(lb, ub).toNamedConstant(nameOfSet));
    IntegerVariable var = new IntegerVariable(nameOfVar, set);
    Assert.assertEquals(String.format("var opt %s: %s;", nameOfSet, nameOfVar), var.declare());
  }

  @Test
  public void testThreeDimensionalArrayConstant() {
    String name = "a";
		NamedConstantSet<Integer> setOneTwoThree = new RangeExpression(1, 3).toNamedConstant("OneTwoThree");
		NamedConstantSet<Integer> setTwoThreeFour = new RangeExpression(2, 4).toNamedConstant("TwoThreeFour");
		NamedConstantSet<Integer> setThreeFourFive = new RangeExpression(3, 5).toNamedConstant("ThreeFourFive");
		List<NamedConstantSet<Integer>> range = ListUtils.fromElements(setOneTwoThree, setTwoThreeFour,
        setThreeFourFive);
    List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
		ArrayConstant<Integer> array = new ExplicitIntegerList(range, values).toNamedConstant(name);
    Assert.assertEquals(
        "array[OneTwoThree, TwoThreeFour, ThreeFourFive] of int: a = "
            + "array3d(OneTwoThree, TwoThreeFour, ThreeFourFive, [1, 2, 3, 4, 5, 6, 7, 8, 9, "
            + "10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]);",
        array.declare());
  }

  @Test
  public void testThreeDimensionalArrayConstantWithNulls() {
    String name = "a";
		NamedConstantSet<Integer> setOneTwoThree = new RangeExpression(1, 3).toNamedConstant("OneTwoThree");
		NamedConstantSet<Integer> setTwoThreeFour = new RangeExpression(2, 4).toNamedConstant("TwoThreeFour");
		NamedConstantSet<Integer> setThreeFourFive = new RangeExpression(3, 5).toNamedConstant("ThreeFourFive");
		List<NamedConstantSet<Integer>> range = ListUtils.fromElements(setOneTwoThree, setTwoThreeFour,
        setThreeFourFive);
    Collection<Integer> values = Arrays.asList(1, 2, null, 4, 5, 6, 7, 8, null, null, 11, 12, 13,
        14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
		ArrayConstant<Integer> array = new ExplicitIntegerList(range, values).toNamedConstant(name);
    Assert.assertEquals(
        "array[OneTwoThree, TwoThreeFour, ThreeFourFive] of opt int: a = "
            + "array3d(OneTwoThree, TwoThreeFour, ThreeFourFive, [1, 2, <>, 4, 5, 6, 7, 8, <>, "
            + "<>, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]);",
        array.declare());
  }

  @Test
  public void testThreeDimensionalArrayConstantWithPseudoNulls() {
    String name = "a";
		NamedConstantSet<Integer> setOneTwoThree = new RangeExpression(1, 3).toNamedConstant("OneTwoThree");
		NamedConstantSet<Integer> setTwoThreeFour = new RangeExpression(2, 4).toNamedConstant("TwoThreeFour");
		NamedConstantSet<Integer> setThreeFourFive = new RangeExpression(3, 5).toNamedConstant("ThreeFourFive");
		List<NamedConstantSet<Integer>> range = ListUtils.fromElements(setOneTwoThree, setTwoThreeFour,
        setThreeFourFive);
		NamedConstantSet<Integer> i = new RangeExpression(1, 27).toNamedConstant("I");
		PseudoOptionalIntSet type = new PseudoOptionalIntSet(i);
    Collection<Integer> values = Arrays.asList(1, 2, null, 4, 5, 6, 7, 8, null, null, 11, 12, 13,
        14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
		ArrayConstant<Integer> array = new ExplicitIntegerList(range, type, values).toNamedConstant(name);
    Assert.assertEquals(
        "array[OneTwoThree, TwoThreeFour, ThreeFourFive] of 0..27: a = "
            + "array3d(OneTwoThree, TwoThreeFour, ThreeFourFive, [1, 2, 0, 4, 5, 6, 7, 8, 0, "
            + "0, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]);",
        array.declare());
  }

  @Test
  public void testPseudoOptionalIntSetDeclaration() {
		NamedConstantSet<Integer> i = new RangeExpression(1, 5).toNamedConstant("I");
		NamedConstantSet<Integer> pseudoOptionalI = new PseudoOptionalIntSet(i).toNamedConstant("I0");
    IntegerVariable var1 = new IntegerVariable("i", pseudoOptionalI);

    // before a pseudo-optional int set is declared, it must be used without its name:
    Assert.assertEquals("var 0..5: i;", var1.declare());

    Assert.assertEquals("set of int: I0 = 0..5;", pseudoOptionalI.declare());

    // after a pseudo-optional int set is declared, it must be used with its name:
    Assert.assertEquals("var I0: i;", var1.declare());
  }

  @Test
  public void testIntSetWithNamedBounds() {
		NamedIntegerConstant lb = new IntegerConstant(1).toNamedConstant("lb");
		NamedIntegerConstant ub = new IntegerConstant(2).toNamedConstant("ub");
		NamedConstantSet<Integer> set = new RangeExpression(lb, ub).toNamedConstant("set");
    Assert.assertEquals("set of int: set = lb..ub;", set.declare());
  }

  @Test
  public void testBoolVarUnassigned() {
    String varName = "x";
    BooleanVariable var = new BooleanVariable(varName);
    String expectedDeclaration = String.format("var bool: %s;", varName);
    Assert.assertEquals("Unexpected declaration", expectedDeclaration, var.declare());
  }

  @Test
  public void testBoolVarAssigned() {
    String varName = "x";
    boolean value = false;
    BooleanVariable var = new BooleanVariable(varName, value);
    String expectedDeclaration = String.format("var bool: %s = %s;", varName, value);
    Assert.assertEquals("Unexpected declaration", expectedDeclaration, var.declare());
  }

}
