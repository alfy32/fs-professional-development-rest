angular.module('todo', []);

angular.module('todo').controller('mainCtrl', function ($scope, todo) {
  $scope.todos = {
    done: todo.done,
    todo: todo.todo
  };

  // Get all the todos from the server
  todo.get();

  $scope.addTodo = function () {
    todo.add($scope.newTodo);
    $scope.newTodo = '';
  };

  $scope.deleteTodo = function (todoItem, type) {
    todo.remove(todoItem, type);
  };

  $scope.todoDone = function (todoItem) {
    todo.todoDone(todoItem);
  };

});

angular.module('todo').factory('todo', function ($http) {
  var todos = {
    done: [],
    todo: []
  };

  function getTodos() {
    $http.get('/todo/api/todo').then(function (resp) {
      var data = resp.data;
      data.done.forEach(function (todoItem) {
        todos.done.push(todoItem);
      });
      data.todo.forEach(function (todoItem) {
        todos.todo.push(todoItem);
      });
    });
  }

  function addTodo(todo) {
    $http.post('/todo/api/todo', {todo: todo}).then(function (resp) {
      todos.todo.push(resp.data);
    })
  }

  function deleteTodo(todo, type) {
    $http.delete('/todo/api/todo/' + todo.id).then(function (resp) {
      if (resp.status === 204) {
        if (type === 'todo') {
          todos.todo.splice(todos.todo.indexOf(todo), 1);
        }
        else if (type === 'done') {
          todos.done.splice(todos.done.indexOf(todo), 1);
        }
      }
    });
  }

  function todoDone(todo) {
    $http.put('/todo/api/todo/' + todo.id + '/done').then(function (resp) {
      if (resp.status === 200) {
        todos.todo.splice(todos.todo.indexOf(todo), 1);
        todos.done.push(resp.data);
      }
    });
  }

  return {
    done: todos.done,
    todo: todos.todo,
    get: getTodos,
    add: addTodo,
    remove: deleteTodo,
    todoDone: todoDone
  };
});

angular.module('todo').directive('ngEnter', function () {
  return function (scope, element, attrs) {
    element.bind("keydown keypress", function (event) {
      if (event.which === 13) {
        scope.$apply(function () {
          scope.$eval(attrs.ngEnter);
        });

        event.preventDefault();
      }
    });
  };
});