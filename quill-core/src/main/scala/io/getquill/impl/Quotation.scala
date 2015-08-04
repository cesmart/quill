package io.getquill.impl

import scala.reflect.macros.whitebox.Context
import io.getquill.util.Messages
import scala.annotation.StaticAnnotation

trait Quoted[+T]

trait Quotation extends Messages {

  val c: Context
  import c.universe._

  case class QuotedTree(tree: Any) extends StaticAnnotation

  def quote[T: WeakTypeTag](body: c.Expr[T]) =
    q"""
      new ${c.weakTypeOf[Quoted[T]]} {
        @${c.weakTypeOf[QuotedTree]}(${body.tree})
        def tree = ()
        override def toString = ${body.tree.toString}
      }
    """

  def unquote[T](quoted: c.Expr[Quoted[T]]) =
    unquoteTree(quoted.tree)

  protected def unquoteTree[T](tree: Tree) = {
    val method =
      tree.tpe.decls.find(_.name.decodedName.toString == "tree")
        .getOrElse(fail(s"Can't find the tree method at ${tree}: ${tree.tpe}"))
    val annotation =
      method.annotations.headOption
        .getOrElse(fail(s"Can't find the QuotedTree annotation at $method"))
    annotation.tree.children.lastOption
      .getOrElse(fail(s"Can't find the QuotedTree body at $annotation"))
  }
}
