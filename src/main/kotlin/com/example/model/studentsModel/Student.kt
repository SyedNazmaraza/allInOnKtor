package com.example.model.studentsModel

import kotlinx.serialization.Serializable

@Serializable
data class Student(var id:Int,var userName:String,var email:String,var password:String) {


}