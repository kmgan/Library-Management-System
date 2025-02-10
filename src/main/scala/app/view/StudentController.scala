package app.view

import app.model.Student
import app.MainApp
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.StringProperty

import scala.util.{Failure, Success}

@sfxml
class StudentController(
                      private val studentTable : TableView[Student],
                      private val idColumn : TableColumn[Student, String],
                      private val nameColumn : TableColumn[Student, String],
                      private val phoneColumn : TableColumn[Student, String],
                      private val emailColumn : TableColumn[Student, String]
                    ) {
  studentTable.items = MainApp.studentData
  idColumn.cellValueFactory = {_.value.id}
  nameColumn.cellValueFactory  = {_.value.name}
  phoneColumn.cellValueFactory = {_.value.phone}
  emailColumn.cellValueFactory = {_.value.email}

  private def showStudentDetails(student: Option[Student]) = {
    student match {
      case Some(student) =>
        idColumn.cellValueFactory = {_.value.id}
        nameColumn.cellValueFactory = {_.value.name}
        phoneColumn.cellValueFactory = {_.value.phone}
        emailColumn.cellValueFactory = {_.value.email}

      case None =>
        idColumn.cellValueFactory = { _ => StringProperty("") }
        nameColumn.cellValueFactory = { _ => StringProperty("") }
        phoneColumn.cellValueFactory = { _ => StringProperty("") }
        emailColumn.cellValueFactory = { _ => StringProperty("") }
    }
  }

  def handleDeleteStudent(action: ActionEvent) = {
    val selectedIndex = studentTable.selectionModel().selectedIndex.value
    val selectedStudent = studentTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) {
      selectedStudent.delete() match {
        case Success(x) =>
          MainApp.studentData.remove(selectedIndex)
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
        headerText = "No Student Selected"
        contentText = "Please select a student in the table."
      }.showAndWait()
    }
  }

  def handleNewStudent(action: ActionEvent) = {
    val student = new Student("", "", "", "")
    val okClicked = MainApp.showStudentEditDialog(student);
    if (okClicked) {
      student.save() match {
        case Success(x) =>
          MainApp.studentData += student
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

  def handleEditStudent(action: ActionEvent) = {
    val selectedStudent = studentTable.selectionModel().selectedItem.value
    if (selectedStudent != null) {
      val okClicked = MainApp.showStudentEditDialog(selectedStudent)

      if (okClicked) {
        selectedStudent.save() match {
          case Success(x) =>
            showStudentDetails(Some(selectedStudent))
          case Failure(e) =>
            println(s"Update failed: ${e.getMessage}")
            val alert = new Alert(Alert.AlertType.Warning) {
              initOwner(MainApp.stage)
              title = "Failed to Save"
              headerText = "Database Error"
              contentText = s"Database problem failed to save changes: ${e.getMessage}"
            }.showAndWait()
        }
      }
    }else {
        val alert = new Alert(Alert.AlertType.Warning) {
          initOwner(MainApp.stage)
          title = "No Selection"
          headerText = "No Student Selected"
          contentText = "Please select a student in the table."
        }.showAndWait()
      }
  }

}