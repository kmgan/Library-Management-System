package app

import app.model.{AllIssuedBook, Book, Student}
import app.util.Database
import app.view.{BookEditDialogController, StudentEditDialogController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp{
  Database.setupDB()

  val bookData = new ObservableBuffer[Book]()
  val studentData = new ObservableBuffer[Student]()
  val allIssuedBookData = new ObservableBuffer[AllIssuedBook]()

  bookData ++= Book.getAllBooks
  studentData ++= Student.getAllStudents
  allIssuedBookData ++= AllIssuedBook.getAllIssuedBooks

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()

  val roots = loader.getRoot[jfxs.layout.BorderPane]
  stage = new PrimaryStage{
    title = "Library Management System"
    icons += new Image("images/library.png")
    scene = new Scene(){
      root = roots
    }
  }

  def showSideBar(): Unit = {
    val resource = getClass.getResource("view/SideBar.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setLeft(roots)
  }

  def showHome(): Unit = {
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showBook(): Unit = {
    val resource = getClass.getResource("view/Book.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showStudent(): Unit = {
    val resource = getClass.getResource("view/Student.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showIssueBook(): Unit = {
    val resource = getClass.getResource("view/IssueBook.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showReturnBook(): Unit = {
    val resource = getClass.getResource("view/ReturnBook.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showAllIssuedBook(): Unit = {
    val resource = getClass.getResource("view/AllIssuedBook.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showStudentEditDialog(student: Student): Boolean = {
    val resource = getClass.getResourceAsStream("view/StudentEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[StudentEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.student = student
    dialog.showAndWait()
    control.okClicked
  }

  def showBookEditDialog(book: Book): Boolean = {
    val resource = getClass.getResourceAsStream("view/BookEditDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[BookEditDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.book = book
    dialog.showAndWait()
    control.okClicked
  }

  showSideBar()
  showHome()
}
