package com.example.controller

import com.example.database.DatabaseFactory
import com.example.entity.CustomerEntity
import com.example.model.customerModel.customerReq
import com.example.model.customerModel.customermodel
import com.example.utils.BaseResponse
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import org.ktorm.dsl.*

fun Application.configureRouting() {
    var database = DatabaseFactory.db1
    routing {
        route("/customer") {
            rateLimit(RateLimitName("public")) {
                get("/") {
                    var n = database.from(CustomerEntity).select()
                        .map {
                            val id = it[CustomerEntity.id]
                            val name = it[CustomerEntity.name]
                            val age = it[CustomerEntity.age]
                            customermodel(id ?: -1, name ?: "", age ?: -1)
                        }
                    call.respond(n)
                }
            }
            post("/") {
                var node = call.receive<customerReq>()
                var re = database.insert(CustomerEntity) {
                    set(it.name, node.name)
                    set(it.age, node.age)
                }
                if (re == 1) {
                    call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(mes = "true", data = "Success"))
                } else {
                    call.respond(HttpStatusCode.BadGateway, BaseResponse.ErrorResponse(mes = "false", data = "Failed"))
                }

            }
            get("/{id}") {
                var id = call.parameters["id"]?.toInt() ?: -1
                var re = database.from(CustomerEntity).select().where { CustomerEntity.id eq id }
                    .map {
                        var id = it[CustomerEntity.id]
                        val name = it[CustomerEntity.name]
                        val age = it[CustomerEntity.age]
                        customermodel(id ?: -1, name ?: "", age ?: -1)
                    }
                call.respond(re.get(0))
            }
            put("/{id}") {
                var id = call.parameters["id"]?.toInt() ?: -1
                var updatedNode = call.receive<customerReq>()
                var re = database.update(CustomerEntity) {
                    set(it.name, updatedNode.name)
                    where { it.id eq id }
                }
                if (re == 1) {
                    call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(mes = "true", data = "Success"))
                } else {
                    call.respond(HttpStatusCode.BadGateway, BaseResponse.ErrorResponse(mes = "false", data = "Failed"))
                }
            }
            delete("/{id}") {
                var id = call.parameters["id"]?.toInt() ?: -1
                var re = database.delete(CustomerEntity) {
                    it.id eq id
                }
                if (re == 1) {
                    call.respond(HttpStatusCode.OK, BaseResponse.SuccessResponse(mes = "true", data = "Success"))
                } else {
                    call.respond(HttpStatusCode.BadGateway, BaseResponse.ErrorResponse(mes = "false", data = "Failed"))
                }
            }
        }
    }
}


