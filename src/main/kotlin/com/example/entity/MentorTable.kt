package com.example.entity

import org.jetbrains.exposed.sql.Table

object MentorTable:Table("mentor") {
    var id = integer("id").autoIncrement()
    var name = varchar("name",256)
    var subject = varchar("subject",256)
}