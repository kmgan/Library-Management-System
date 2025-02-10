package app.view

import app.model.{AllIssuedBook, Book, Student}
import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml

@sfxml
class HomeController (
                       private val bookCountLabel: Label,
                       private val studentCountLabel: Label,
                       private val issuedBookCountLabel: Label
                     ){
  val bookCount: Int = Book.countTotalRecords
  bookCountLabel.text = bookCount.toString

  val studentCount: Int = Student.countTotalRecords
  studentCountLabel.text = studentCount.toString

  val allIssuedBookCount: Int = AllIssuedBook.countTotalRecords
  issuedBookCountLabel.text = allIssuedBookCount.toString
}
