package at.siemens.ct.jmz.expressions.conditional;

import at.siemens.ct.jmz.expressions.bool.BooleanExpression;
import at.siemens.ct.jmz.expressions.integer.IntegerExpression;

public class IntegerConditionalExpression extends ConditionalExpression<Integer>
    implements IntegerExpression {

  public IntegerConditionalExpression(BooleanExpression condition, IntegerExpression thenBranch,
      IntegerExpression elseBranch) {
    super(condition, thenBranch, elseBranch);
  }

}
