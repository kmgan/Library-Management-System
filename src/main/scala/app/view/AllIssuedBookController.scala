package app.view

import app.model.AllIssuedBook
import app.MainApp
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}

import java.time.LocalDate
import scala.util.{Failure, Success}

@sfxml
class AllIssuedBookController(
                         private val allIssuedBookTable : TableView[AllIssuedBook],
                         private val bookIdColumn : TableColumn[AllIssuedBook, String],
                         private val studentIdColumn : TableColumn[AllIssuedBook, String],
                         private val issueDateColumn : TableColumn[AllIssuedBook, LocalDate],
                       ) {
  allIssuedBookTable.items = MainApp.allIssuedBookData
  bookIdColumn.cellValueFactory = {_.value.bookId}
  studentIdColumn.cellValueFactory  = {_.value.studentId}
  issueDateColumn.cellValueFactory = {_.value.issueDate}
}

