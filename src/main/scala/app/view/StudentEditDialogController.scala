package app.view

import app.model.Student
import scalafx.scene.control.{TextField, Alert}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import scalafx.event.ActionEvent
import scalafx.Includes._

@sfxml
class StudentEditDialogController (
                                   private val  idField : TextField,
                                   private val  nameField : TextField,
                                   private val  phoneField : TextField,
                                   private val  emailField : TextField,
                                 ){

  var         dialogStage : Stage  = null
  private var _student     : Student = null
  var         okClicked            = false

  def student = _student
  def student_=(x : Student) {
    _student = x

    idField.text = _student.id.value
    nameField.text  = _student.name.value
    phoneField.text    = _student.phone.value
    emailField.text= _student.email.value
  }

  def handleOk(action :ActionEvent){

    if (isInputValid()) {
      _student.id.value = idField.text()
      _student.name.value = nameField.text()
      _student.phone.value = phoneField.text()
      _student.email.value = emailField.text()

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
    if (nullChecking(phoneField.text.value))
      errorMessage += "No valid phone!\n"
    if (nullChecking(emailField.text.value))
      errorMessage += "No valid email!\n"

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