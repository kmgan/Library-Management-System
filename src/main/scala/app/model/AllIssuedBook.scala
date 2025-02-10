package app.model

import app.util.Database
import app.util.DateUtil.{DateFormater, StringFormater}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalikejdbc._

import java.time.LocalDate
import scala.util.Try

class AllIssuedBook(val bookIdS: String, val studentIdI: String, val issueDateD: LocalDate) extends Database {
  def this() = this ("", "", LocalDate.of(2001,1,1))

  var bookId = StringProperty(bookIdS)
  var studentId = StringProperty(studentIdI)
  var issueDate = ObjectProperty[LocalDate](issueDateD)

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into allIssuedBook (bookId, studentId, issueDate)
          values (${bookId.value}, ${studentId.value}, ${issueDate.value.asString})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          update allIssuedBook
          set
          bookId = ${bookId.value},
          studentId = ${studentId.value},
          issueDate = ${issueDate.value.asString}
        """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          delete from allIssuedBook where
          bookId = ${bookId.value}
        """.update.apply()
      })
    } else
      throw new Exception("Issued book does not exist in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from allIssuedBook where
        bookId = ${bookId.value}
      """.map(rs => rs.string("bookId")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }
  }
}

object AllIssuedBook extends Database {
  def apply(
             bookIdS: String,
             studentIdI: String,
             issueDateD: LocalDate,
           ): AllIssuedBook = {
    new AllIssuedBook(bookIdS, studentIdI, issueDateD) {
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
        create table allIssuedBook (
          bookId varchar(30) not null,
          studentId varchar(8),
          issueDate varchar(64),
          primary key (bookId),
          foreign key (bookId) references book(id),
          foreign key (studentId) references student(id)
        )
      """.execute.apply()
    }
  }

  def getAllIssuedBooks: List[AllIssuedBook] = {
    DB readOnly { implicit session =>
      sql"select * from allIssuedBook".map(rs => AllIssuedBook(rs.string("bookId"), rs.string("studentId"),
        rs.string("issueDate").parseLocalDate)).list.apply()
    }
  }

  def getById(bookId: String): Option[AllIssuedBook] = {
    DB readOnly { implicit session =>
      sql"select * from allIssuedBook where bookId = $bookId".map(rs => AllIssuedBook(
        rs.string("bookId"),
        rs.string("studentId"),
        rs.string("issueDate").parseLocalDate
      )).single.apply()
    }
  }

  def countTotalRecords: Int = {
    DB readOnly { implicit session =>
      sql"select count(*) from allIssuedBook".map(_.int(1)).single.apply().getOrElse(0)
    }
  }

}