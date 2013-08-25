package models

import java.util.SortedMap
import java.util.TreeMap
import java.util.Comparator
import com.google.common.collect.Ordering
import java.util.Collections
import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer
import play.api.libs.json._

case class Action(action: Int, index: Int, side: Int, price: Double, quantity: Long)
case class Entry(quantity: Long, var index: Int)

class DepthSide(val side: Int) {
  val comp = Ordering.natural()

  val depths: SortedMap[Double, Entry] = if (side == 0)
    new TreeMap(Collections.reverseOrder().asInstanceOf[Comparator[Double]]) else new TreeMap()

  def add(price: Double, quantity: Long): Action = {
    val e = Entry(quantity, 0)
    val x = depths.put(price, e)
    if (x == null) {
      val head = depths.headMap(price)
      val newIndex = if (head.isEmpty) -1 else head.get(head.lastKey).index
      e.index = newIndex
      val tailMap = depths.tailMap(price)
      for ((_, ee) <- tailMap)
        ee.index = ee.index + 1
    } else e.index = x.index
    Action(if (x == null) 0 else 1, e.index, side, price, quantity)
  }

  def del(price: Double): Option[Action] = {
    val x = depths.remove(price)
    if (x != null) {
      val tail = depths.tailMap(price)
      for ((_, ee) <- tail)
        ee.index = ee.index - 1
      Some(Action(2, x.index, side, price, 0))
    } else None
  }

  override def toString = depths.values().map { v => v.toString } mkString
}

case class Depth(price: Double, var quantity: Long)
class DepthArray {
  val d: ArrayBuffer[Depth] = ArrayBuffer.empty

  def doAction(a: Action) = a.action match {
    case 0 =>
      d.insert(a.index, Depth(a.price, a.quantity))
    case 1 =>
      val x = d(a.index)
      x.quantity = a.quantity
    case 2 =>
      d.remove(a.index)
  }

  override def toString() = d map { _.toString } mkString
}

object DepthSide {
  implicit val depthWriter = new Writes[Depth] {
    def writes(d: Depth): JsValue = {
      Json.obj(
        "price" -> d.price,
        "quantity" -> d.quantity)
    }
  }

  val buySide = new DepthSide(1)
  val actions = List(
    buySide.add(100, 1),
    buySide.add(99, 2),
    buySide.add(101, 3),
    buySide.add(102, 4),
    buySide.add(101, 5),
    buySide.del(102).get,
    buySide.del(100).get)

  println(buySide)
  println(actions)

  val buySideCopy = new DepthArray()
  for (a <- actions)
    buySideCopy.doAction(a)

  println(buySideCopy)
}