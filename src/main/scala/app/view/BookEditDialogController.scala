package app.view

import app.model.Book
import scalafx.scene.control.{TextField, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.event.ActionEvent
import scalafx.Includes._

@sfxml
class BookEditDialogController (
                                    private val  idField : TextField,
                                    private val  nameField : TextField,
                                    private val  authorField : TextField,
                                    private val  publisherField : TextField,
                                  ){

  var         dialogStage : Stage  = null
  private var _book     : Book = null
  var         okClicked            = false

  def book = _book
  def book_=(x : Book) {
    _book = x

    idField.text = _book.id.value
    nameField.text  = _book.name.value
    authorField.text    = _book.author.value
    publisherField.text= _book.publisher.value
  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _book.id.value = idField.text()
      _book.name.value = nameField.text()
      _book.author.value = authorField.text()
      _book.publisher.value = publisherField.text()

      okClicked = true;
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close();
  }

  def nullChecking (x : String) = x == null || x.length == 0

  def isInputValid() : Boolean = {

    var errorMessage = ""

    if (nullChecking(idField.text.value))
      errorMessage += "No valid ID!\n"
    if (nullChecking(nameField.text.value))
      errorMessage += "No valid name!\n"
    if (nullChecking(authorField.text.value))
      errorMessage += "No valid author!\n"
    if (nullChecking(publisherField.text.value))
      errorMessage += "No valid publisher!\n"

    if (errorMessage.length() == 0) {
      return true;
    } else {
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }
}