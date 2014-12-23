package com.alfy.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.alfy.rest.db.ConnectionPool;
import com.alfy.rest.dto.TodoItem;
import com.alfy.rest.dto.TodoList;
import org.codehaus.enunciate.jaxrs.TypeHint;

/**
 * Author: Alan Christensen
 * 12/22/2014
 */

@Path("/todo")
public class TodoService {
  public static final String APPLICATION_JSON = "application/json";
  public static final String TEXT_PLAIN = "text/plain";

  /**
   * Creates a new TodoItem to be added to the list
   *
   * @param todoItem The TodoItem to be added
   * @return The Response. Created or Error.
   */
  @POST
  @Path("/")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  @TypeHint(TypeHint.NO_CONTENT.class)
  public Response createTodo(TodoItem todoItem) throws URISyntaxException {
    if (todoItem.getTodo() == null || "".equals(todoItem.getTodo())) {
      return Response.status(400).entity("todo cannot be null").build();
    }

    try (Connection conn = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(
          "INSERT INTO todo (todo) VALUES (?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        statement.setString(1, todoItem.getTodo());

        statement.execute();

        try (ResultSet resultSet = statement.getGeneratedKeys()) {
          resultSet.next();
          todoItem.setId(resultSet.getString("todo_id"));
          todoItem.setCreated(resultSet.getString("created"));
          return Response.created(URI.create(todoItem.getId())).entity(todoItem).build();
        }
      }
    }
    catch (SQLException e) {
      return Response.status(500).entity(e).build();
    }
  }

  /**
   * Gets all of the user's todoItems
   *
   * @param query (Optional) A query string to search the todoItem text
   *
   * @return 2 lists of todoItems. A done list and a todoList.
   */
  @GET
  @Path("/")
  @Produces(APPLICATION_JSON)
  @TypeHint(TodoList.class)
  public Response getTodoList(@QueryParam("query") String query) {
    try (Connection conn = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(
          "SELECT todo_id, created, todo, done FROM todo"
      )) {
        try (ResultSet resultSet = statement.executeQuery()) {
          TodoList todoList = new TodoList(new ArrayList<TodoItem>(), new ArrayList<TodoItem>());
          while (resultSet.next()) {
            TodoItem todoItem = new TodoItem();
            todoItem.setId(resultSet.getString("todo_id"));
            todoItem.setCreated(resultSet.getString("created"));
            todoItem.setTodo(resultSet.getString("todo"));
            todoItem.setDone(resultSet.getString("done"));
            if (query == null || todoItem.getTodo().toLowerCase().contains(query.toLowerCase())) {
              if (todoItem.getDone() == null) {
                todoList.getTodo().add(todoItem);
              }
              else {
                todoList.getDone().add(todoItem);
              }
            }
          }
          return Response.ok(todoList).build();
        }
      }
    }
    catch (SQLException e) {
      return Response.status(500).entity(e).build();
    }
  }

  /**
   * Get a todoItem by id.
   *
   * @param todoId The id of the todoItem
   * @return The todoItem requested
   */
  @GET
  @Path("/{todoId}")
  @Produces(APPLICATION_JSON)
  @TypeHint(TodoItem.class)
  public Response getTodo(@PathParam("todoId") long todoId) {
    try (Connection conn = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(
          "SELECT todo_id, created, todo, done FROM todo WHERE todo_id = ?"
      )) {
        statement.setLong(1, todoId);

        try (ResultSet resultSet = statement.executeQuery()) {
          if (!resultSet.next()) {
            return Response.status(404).build();
          }
          else {
            TodoItem todoItem = new TodoItem();
            todoItem.setId(resultSet.getString("todo_id"));
            todoItem.setCreated(resultSet.getString("created"));
            todoItem.setTodo(resultSet.getString("todo"));
            todoItem.setDone(resultSet.getString("done"));
            return Response.ok(todoItem).build();
          }
        }
      }
    }
    catch (SQLException e) {
      return Response.status(500).entity(e).build();
    }
  }

  /**
   * Delete a todoItem.
   *
   * @param todoId The id of the todoItem
   */
  @DELETE
  @Path("/{todoId}")
  @TypeHint(TypeHint.NO_CONTENT.class)
  public Response deleteTodo(@PathParam("todoId") long todoId) {
    try (Connection conn = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(
          "DELETE FROM todo WHERE todo_id = ?"
      )) {
        statement.setLong(1, todoId);
        statement.execute();

        return Response.noContent().build();
      }
    }
    catch (SQLException e) {
      return Response.status(500).entity(e).build();
    }
  }

  /**
   * Mark the todoItem as done.
   *
   * @param todoId The id of the todoItem
   * @return The todoItem
   */
  @PUT
  @Path("/{todoId}/done")
  @Consumes(TEXT_PLAIN)
  @Produces(APPLICATION_JSON)
  @TypeHint(TodoItem.class)
  public Response markTodoDone(@PathParam("todoId") long todoId) {
    try (Connection conn = ConnectionPool.getInstance().getConnection()) {
      try (PreparedStatement statement = conn.prepareStatement(
          "UPDATE todo SET done = now() WHERE todo_id = ?",
          Statement.RETURN_GENERATED_KEYS
      )) {
        statement.setLong(1, todoId);
        statement.executeUpdate();

        try (ResultSet resultSet = statement.getGeneratedKeys()) {
          if (!resultSet.next()) {
            return Response.status(404).build();
          }
          else {
            TodoItem todoItem = new TodoItem();
            todoItem.setId(resultSet.getString("todo_id"));
            todoItem.setCreated(resultSet.getString("created"));
            todoItem.setTodo(resultSet.getString("todo"));
            todoItem.setDone(resultSet.getString("done"));
            return Response.ok(todoItem).build();
          }
        }
      }
    }
    catch (SQLException e) {
      return Response.status(500).entity(e).build();
    }
  }
}
