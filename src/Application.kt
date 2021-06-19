package com.aiman

import com.aiman.entities.Todo
import com.aiman.repository.InMemoryRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {

        val repository = InMemoryRepository()

        get("/") {
            call.respondText("It works!")
        }

        get("/todos") {
            call.respond(repository.getAllTodos())
        }

        get("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest, "id parameter has to be a number")
                return@get
            }

            val todo = repository.getTodo(id)
            if(todo == null) {
                call.respond(HttpStatusCode.NotFound, "No item found with the specified id")
            } else {
                call.respond(todo)
            }
        }

        post("/todos") {
            val todoDraft = call.receive<Todo>()

            val todo = repository.addTodo(todoDraft)
            call.respond(todo)
        }

        put("/todos/{id}") {
            val todoId = call.parameters["id"]?.toIntOrNull()
            if(todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "invalid value for parameter id")
                return@put
            }

            val todoDraft = call.receive<Todo>()
            val updated = repository.updateTodo(todoId, todoDraft)
            if(updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "No todo found with id $todoId")
            }
        }

        delete("/todos/{id}") {
            val todoId = call.parameters["id"]?.toIntOrNull()
            if(todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "invalid value for parameter id")
                return@delete
            }

            val deleted = repository.deleteTodo(todoId)
            if(deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "No todo found with id $todoId")
            }
        }

    }
}

