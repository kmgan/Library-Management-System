package app.model

import app.util.Database
import scalafx.beans.property.StringProperty
import scalikejdbc._
import scala.util.Try

class Book(val idS: String, val nameS: String, val authorS: String, val publisherS: String) extends Database {
  def this() = this (null, null, null, null)

  var id = StringProperty(idS)
  var name = new StringProperty(nameS)
  var author = new StringProperty(authorS)
  var publisher = new StringProperty(publisherS)

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into book (id, name, author, publisher)
          values (${id.value}, ${name.value}, ${author.value}, ${publisher.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          update book
          set
          name = ${name.value},
          author = ${author.value},
          publisher = ${publisher.value}
          where id = ${id.value}
        """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          delete from book where
          id = ${id.value}
        """.update.apply()
      })
    } else
      throw new Exception("Book does not exist in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from book where
        id = ${id.value}
      """.map(rs => rs.string("id")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }
  }
}

object Book extends Database {
  def apply(
             idS: String,
             nameS: String,
             authorS: String,
             publisherS: String,
           ): Book = {
    new Book(idS, nameS, authorS, publisherS) {
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
        create table book (
          id varchar(30) not null,
          name varchar(128),
          author varchar(128),
          publisher varchar(128),
          primary key (id)
        )
      """.execute.apply()
    }
  }

  def getAllBooks: List[Book] = {
    DB readOnly { implicit session =>
      sql"select * from book".map(rs => Book(rs.string("id"), rs.string("name"),
        rs.string("author"), rs.string("publisher"))).list.apply()
    }
  }

  def getById(id: String): Option[Book] = {
    DB readOnly { implicit session =>
      sql"select * from book where id = $id".map(rs => Book(
        rs.string("id"),
        rs.string("name"),
        rs.string("author"),
        rs.string("publisher")
      )).single.apply()
    }
  }

  def countTotalRecords: Int = {
    DB readOnly { implicit session =>
      sql"select count(*) from book".map(_.int(1)).single.apply().getOrElse(0)
    }
  }

}
