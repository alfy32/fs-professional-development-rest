package com.alfy.rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Author: Alan Christensen
 * 12/22/2014
 */

@XmlRootElement
public class TodoItem {
  private String id;
  private String created;
  private String todo;
  private String done;

  public TodoItem() { }

  public TodoItem(String id, String created, String todo, String done) {
    this.id = id;
    this.created = created;
    this.todo = todo;
    this.done = done;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getTodo() {
    return todo;
  }

  public void setTodo(String todo) {
    this.todo = todo;
  }

  public String getDone() {
    return done;
  }

  public void setDone(String done) {
    this.done = done;
  }
}
