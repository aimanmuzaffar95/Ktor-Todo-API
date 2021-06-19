package com.aiman.repository

import com.aiman.entities.Todo

class InMemoryRepository: Repository {
    private val list = mutableListOf(
        Todo(1, "Wake up", false),
        Todo(2, "Eat up", false),
        Todo(3, "Play", false)
    )

    override fun getAllTodos(): List<Todo> {
        return list
    }

    override fun getTodo(id: Int): Todo? {
        return list.firstOrNull { it.id == id }
    }

    override fun addTodo(todo: Todo): Todo {
        todo.id = list.size + 1
        list.add(todo)
        return todo
    }

    override fun deleteTodo(id: Int): Boolean {
        return list.removeIf { it.id == id }
    }

    override fun updateTodo(id: Int, todo: Todo): Boolean {
        val searchedTodo = list.firstOrNull { it.id == id } ?: return false
        searchedTodo.title = todo.title
        searchedTodo.done = todo.done
        return true
    }
}