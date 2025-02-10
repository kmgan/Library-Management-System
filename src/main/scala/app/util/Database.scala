package app.util

import scalikejdbc._
import app.model.{AllIssuedBook, Book, Student}

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"

  ConnectionPool.singleton(dbURL, "me", "mine")

  implicit val session = AutoSession
}

object Database extends Database{
  def setupDB(): Unit = {
    if (!hasDBInitialize("Book")) {
      Book.initializeTable()
    }

    if (!hasDBInitialize("Student")) {
      Student.initializeTable()
    }

    if (!hasDBInitialize("AllIssuedBook")){
      AllIssuedBook.initializeTable()
    }
  }

  private def hasDBInitialize(tableName: String): Boolean = {
    DB.getTable(tableName) match {
      case Some(_) => true
      case None => false
    }
  }
}

