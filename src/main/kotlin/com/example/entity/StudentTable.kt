package com.example.entity

import org.jetbrains.exposed.sql.Table

object StudentTable:Table("students1") {
    val id = integer("id").autoIncrement()
    val userName = varchar("userName",256)
    val email = varchar("email",256)
    val password = varchar("password",256)
    val mentorId = reference("mentor_id",MentorTable.id)
    override val primaryKey= PrimaryKey(id)


}