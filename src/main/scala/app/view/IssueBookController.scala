package app.view

import app.MainApp
import app.MainApp.{showAllIssuedBook, showHome}
import app.model.{AllIssuedBook, Book, Student}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, ButtonType, Label, TextField}
import scalafxml.core.macros.sfxml

import java.time.LocalDate
import scala.util.{Failure, Success}

@sfxml
class IssueBookController(
                           private val bookIdField: TextField,
                           private val studentIdField: TextField,
                           private val bookNameLabel: Label,
                           private val authorLabel: Label,
                           private val publisherLabel: Label,
                           private val studentNameLabel: Label,
                           private val phoneLabel: Label,
                           private val emailLabel: Label
                         ) {
  def searchBook(action: ActionEvent): Unit = {
    if (bookIdField != null) {
      val bookId = bookIdField.getText.trim()
      if (bookId.isEmpty) {
        showErrorMessage("No valid book ID!")
      } else {
        val book = Book.getById(bookId)
        book match {
          case Some(b) =>
            bookIdField.text = b.id.value
            bookNameLabel.text = b.name.value
            authorLabel.text = b.author.value
            publisherLabel.text = b.publisher.value
          case None =>
            showErrorMessage("Book not found!")
            bookNameLabel.text = "Name"
            authorLabel.text = "Author"
            publisherLabel.text = "Publisher"
        }
      }
    } else {
      println("bookIdField is null")
    }
  }

  def searchStudent(action: ActionEvent): Unit = {
    if (studentIdField != null) {
      val studentId = studentIdField.getText.trim()
      if (studentId.isEmpty) {
        showErrorMessage("No valid student ID!")
      } else {
        val student = Student.getById(studentId)
        student match {
          case Some(s) =>
            studentIdField.text = s.id.value
            studentNameLabel.text = s.name.value
            phoneLabel.text = s.phone.value
            emailLabel.text = s.email.value
          case None =>
            showErrorMessage("Student not found!")
            studentNameLabel.text = "Name"
            phoneLabel.text = "Phone"
            emailLabel.text = "Email"
        }
      }
    } else {
      println("studentIdField is null")
    }
  }

  def issueBook(action: ActionEvent): Unit = {
    if (bookIdField != null && studentIdField != null) {
      val bookId = bookIdField.getText.trim()
      val studentId = studentIdField.getText.trim()
      val issueDate = LocalDate.now()

      if (bookId.isEmpty || studentId.isEmpty) {
        showErrorMessage("Please fill in all fields!")
      } else {
        val confirmation = new Alert(Alert.AlertType.Confirmation) {
          title = "Confirm Issue"
          headerText = s"Issue book $bookId to student $studentId?"
        }

        val result = confirmation.showAndWait()
        if (result.contains(ButtonType.OK)) {
          val book = Book.getById(bookId)
          val student = Student.getById(studentId)

          if (book.isDefined && student.isDefined) {
            val newIssuedBook = new AllIssuedBook(bookId, studentId, LocalDate.now())
            newIssuedBook.save() match {
              case Success(x) =>
                showSuccessMessage("Book issued successfully!")
                MainApp.allIssuedBookData += newIssuedBook
                bookIdField.clear()
                studentIdField.clear()
                showAllIssuedBook()

              case Failure(e) =>
                val alert = new Alert(Alert.AlertType.Warning) {
                  initOwner(MainApp.stage)
                  title = "Failed to Save"
                  headerText = "Database Error"
                  contentText = "Database problem failed to save changes"
                }.showAndWait()
            }
          } else {
            showErrorMessage("Book or student not found!")
          }
        }
      }
    }
  }

  private def showErrorMessage(message: String): Unit = {
    val alert = new Alert(Alert.AlertType.Error) {
      title = "Error"
      headerText = "Invalid Input"
      contentText = message
    }
    alert.showAndWait()
  }

  private def showSuccessMessage(message: String): Unit = {
    val alert = new Alert(Alert.AlertType.Information) {
      title = "Success"
      headerText = "Book Issued"
      contentText = message
    }
    alert.showAndWait()
  }

  def cancelButton(actionEvent: ActionEvent): Unit = {
    showHome()
  }
}
