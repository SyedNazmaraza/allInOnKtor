package com.example.service

import com.example.database.DatabaseFactory
import com.example.entity.StudentTable
import com.example.model.studentsModel.Student
import com.example.model.studentsModel.StudentCredentials
import com.example.security.hash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class  StudentServiceImp : StudentService {
    override suspend fun registerStudent(params: StudentCredentials): Student? {
        var statement =DatabaseFactory.dbQuery {
            StudentTable.insert {
                it[this.userName] = params.userName
                it[this.email] = params.email
                it[this.password] = hash(params.password)
            }
        }
        return rowToUser(statement.resultedValues?.get(0))
    }

    override suspend fun findStudentByEmail(params: StudentCredentials): Student? {
        var student = DatabaseFactory.dbQuery {
            StudentTable.select {
                StudentTable.email eq params.email }.map {
                rowToUser(it) }.firstOrNull()


//            var r = StudentTable.selectAll().firstOrNull {
//                it[StudentTable.email] == "zf"
//            }
        }
        exposedLogger.info(student.toString())
        return student
    }
    override suspend fun deleteStudentByEmail(params: StudentCredentials): Boolean {
        return DatabaseFactory.dbQuery {
            StudentTable.deleteWhere {
                StudentTable.email eq params.email }>0
        }
    }
    override suspend fun getAllStudents(params: StudentCredentials):List<Student?>?{
        var result =  DatabaseFactory.dbQuery {
            StudentTable.selectAll().map {
                rowToUser(it) }
        }
        return result
    }
    suspend fun updatePassword(params: StudentCredentials):Boolean= DatabaseFactory
        .dbQuery {
            StudentTable.update(where = {
                StudentTable.email eq params.email
            })
            {
                it[this.password] = params.password
            }
        }>0
    private fun rowToUser(row:ResultRow?):Student?{
        return if(row==null) null
        else Student(row[StudentTable.id],row[StudentTable.userName],row[StudentTable.email],row[StudentTable.password])
    }
}