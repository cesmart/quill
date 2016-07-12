package io.getquill.norm

import io.getquill.ast.{ Entity, _ }

object RemoveReturning extends StatelessTransformer {
  override def apply(e: Action): Action = {
    e match {
      case Returning(AssignedAction(Insert(table: Entity), assignments), returning) =>
        AssignedAction(Insert(table: Entity), assignments.filterNot(_.property == returning))
      case _ => super.apply(e)
    }
  }
}
