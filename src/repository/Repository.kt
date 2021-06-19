package com.aiman.repository

import com.aiman.entities.Todo

interface Repository {
    fun getAllTodos(): List<Todo>
    fun getTodo(id: Int): Todo?
    fun addTodo(todo: Todo): Todo
    fun deleteTodo(id: Int): Boolean
    fun updateTodo(id: Int, todo: Todo): Boolean
}