package app.view

import app.MainApp.{showAllIssuedBook, showBook, showHome, showIssueBook, showReturnBook, showStudent}
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class SideBarController{
  def homeButton(action: ActionEvent): Unit = {
    showHome()
  }

  def booksButton(action: ActionEvent): Unit = {
    showBook()
  }

  def studentsButton(action: ActionEvent): Unit = {
    showStudent()
  }

  def issueBookButton(action: ActionEvent): Unit = {
    showIssueBook()
  }

  def returnBookButton(action: ActionEvent): Unit = {
    showReturnBook()
  }

  def allIssuedBooksButton(action: ActionEvent): Unit = {
    showAllIssuedBook()
  }
}