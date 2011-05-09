package blueeyes.core.storeable

sealed trait Record2[T1, T2] extends Product with Record{ self =>
  def companion: Record2Companion[_ <: Record2[T1, T2], T1, T2]
}

trait Record2Companion[R <: Record2[T1, T2], T1, T2] extends Product2[Field[R, T1], Field[R, T2]] with Companion[R]

//import scalaz.Failure
//case class Person(name: ValueString, age: ValueInt) extends Record2[ValueString, ValueInt] {
//  def companion = Person
//}

//object Person extends Record2Companion[Person, String, Int]{
//  val _1 = Field[Person, String]("name", _.name, (person, name) => person.copy(name = name), Failure("No name specified!"))
//
//  val _2 = Field[Person, Int]("age", _.age, (person, age) => person.copy(age = age), -1)
//
//  def _example: Person = Person("John Doe", 123)
//}
