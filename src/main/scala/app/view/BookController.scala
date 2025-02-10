package app.view

import app.model.Book
import app.MainApp
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}

import scala.util.{Failure, Success}

@sfxml
class BookController(
                       private val bookTable : TableView[Book],
                       private val idColumn : TableColumn[Book, String],
                       private val nameColumn : TableColumn[Book, String],
                       private val authorColumn : TableColumn[Book, String],
                       private val publisherColumn : TableColumn[Book, String],
                     ) {
  bookTable.items = MainApp.bookData
  idColumn.cellValueFactory = {_.value.id}
  nameColumn.cellValueFactory  = {_.value.name}
  authorColumn.cellValueFactory = {_.value.author}
  publisherColumn.cellValueFactory = {_.value.publisher}

  private def showBookDetails(book: Option[Book]) = {
    book match {
      case Some(book) =>
        idColumn.cellValueFactory = {_.value.id}
        nameColumn.cellValueFactory = {_.value.name}
        authorColumn.cellValueFactory = {_.value.author}
        publisherColumn.cellValueFactory = {_.value.publisher}

      case None =>
        idColumn.cellValueFactory = { _ => StringProperty("") }
        nameColumn.cellValueFactory = { _ => StringProperty("") }
        authorColumn.cellValueFactory = { _ => StringProperty("") }
        publisherColumn.cellValueFactory = { _ => StringProperty("") }
    }
  }

  def handleDeleteBook(action: ActionEvent) = {
    val selectedIndex = bookTable.selectionModel().selectedIndex.value
    val selectedBook = bookTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) {
      selectedBook.delete() match {
        case Success(x) =>
          MainApp.bookData.remove(selectedIndex)
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem failed to save changes"
          }.showAndWait()
      }
    } else {
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Book Selected"
        contentText = "Please select a book in the table."
      }.showAndWait()
    }
  }

  def handleNewBook(action: ActionEvent) = {
    val book = new Book("", "", "", "")
    val okClicked = MainApp.showBookEditDialog(book);
    if (okClicked) {
      book.save() match {
        case Success(x) =>
          MainApp.bookData += book
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning) {
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem failed to save changes"
          }.showAndWait()
      }
    }
  }

  def handleEditBook(action: ActionEvent) = {
    val selectedBook = bookTable.selectionModel().selectedItem.value
    if (selectedBook != null) {
      val okClicked = MainApp.showBookEditDialog(selectedBook)

      if (okClicked) {
        selectedBook.save() match {
          case Success(x) =>
            showBookDetails(Some(selectedBook))
          case Failure(e) =>
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = "Database problem failed to save changes"
            }.showAndWait()
        }
      }
    }else {
        val alert = new Alert(Alert.AlertType.Warning) {
          initOwner(MainApp.stage)
          title = "No Selection"
          headerText = "No Book Selected"
          contentText = "Please select a book in the table."
        }.showAndWait()
      }
  }

}