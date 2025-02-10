package app.model

import app.util.Database
import scalafx.beans.property.StringProperty
import scalikejdbc._
import scala.util.Try

class Student(val idI: String, val nameS: String, val phoneS: String, val emailS: String) extends Database{
  def this() = this (null, null, null, null)

  var id = new StringProperty(idI)
  var name = new StringProperty(nameS)
  var phone = new StringProperty(phoneS)
  var email = new StringProperty(emailS)

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          insert into student (id, name, phone, email)
          values (${id.value}, ${name.value}, ${phone.value}, ${email.value})
        """.update.apply()
      })
    } else {
      Try(DB autoCommit { implicit session =>
        sql"""
          update student
          set
          name = ${name.value},
          phone = ${phone.value},
          email = ${email.value}
          where id = ${id.value}
        """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB autoCommit { implicit session =>
        sql"""
          delete from student where
          id = ${id.value}
        """.update.apply()
      })
    } else
      throw new Exception("Student does not exist in Database")
  }

  def isExist: Boolean = {
    DB readOnly { implicit session =>
      sql"""
        select * from student where
        id = ${id.value}
      """.map(rs => rs.string("id")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }
  }
}

object Student extends Database {
  def apply(
             idI: String,
             nameS: String,
             phoneS: String,
             emailS: String,
           ): Student = {
    new Student(idI, nameS, phoneS, emailS) {
    }
  }

  def initializeTable() = {
    DB autoCommit { implicit session =>
      sql"""
        create table student (
          id varchar(8) not null,
          name varchar(128),
          phone varchar(15),
          email varchar(50),
          primary key (id)
        )
      """.execute.apply()
    }
  }

  def getAllStudents: List[Student] = {
    DB readOnly { implicit session =>
      sql"select * from student".map(rs => Student(rs.string("id"), rs.string("name"),
        rs.string("phone"), rs.string("email"))).list.apply()
    }
  }

  def getById(id: String): Option[Student] = {
    DB readOnly { implicit session =>
      sql"select * from student where id = $id".map(rs => Student(
        rs.string("id"),
        rs.string("name"),
        rs.string("phone"),
        rs.string("email")
      )).single.apply()
    }
  }

  def countTotalRecords: Int = {
    DB readOnly { implicit session =>
      sql"select count(*) from student".map(_.int(1)).single.apply().getOrElse(0)
    }
  }
}