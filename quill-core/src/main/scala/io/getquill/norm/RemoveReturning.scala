package io.getquill.norm

import io.getquill.ast._

object RemoveReturning extends StatelessTransformer {
  override def apply(e: Action): Action = {
    e match {
      case AssignedAction(FunctionApply(Returning(action, returning), values), assignments) =>
        AssignedAction(FunctionApply(action, values), assignments.filterNot(_.property == returning))
      case _ => super.apply(e)
    }
  }
}
