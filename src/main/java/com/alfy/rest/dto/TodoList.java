package com.alfy.rest.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Author: Alan Christensen
 * 12/22/2014
 */

@XmlRootElement
public class TodoList {
  private List<TodoItem> done;
  private List<TodoItem> todo;

  public TodoList() { }

  public TodoList(List<TodoItem> done, List<TodoItem> todo) {
    this.done = done;
    this.todo = todo;
  }

  public List<TodoItem> getDone() {
    return done;
  }

  public void setDone(List<TodoItem> done) {
    this.done = done;
  }

  public List<TodoItem> getTodo() {
    return todo;
  }

  public void setTodo(List<TodoItem> todo) {
    this.todo = todo;
  }
}
