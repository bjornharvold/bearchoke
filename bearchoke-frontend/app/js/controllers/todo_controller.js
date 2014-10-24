/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

angular.module("app").controller('ToDoController', function ($scope, $log, ToDoService) {
    $log.info("ToDo Controller");

    $scope.todo = {};
    /* Init parameters, some todoItems */
    $scope.todoItems = [];
    var todoLookup = {};

    $scope.connectionStatus = "Waiting";
    $scope.connectionStatusClass = "alert-error";

    /* Add a new to do item to the list */
    $scope.addToDo = function () {
        $scope.todo.action = 'CREATE';
        ToDoService.send($scope.todo);
        $scope.todo = {};
    };

    /* mark todo item as completed */
    $scope.markCompleted = function (todoItem) {
        todoItem.action = 'COMPLETED';
        ToDoService.send(todoItem);
    };

    /* remove todo item */
    $scope.deleteToDo = function (todoItem) {
        todoItem.action = 'DELETE';
        ToDoService.send(todoItem);
    };

    /* Handle server messages */
    ToDoService.receive().then(null, null, function (todo) {

        switch (todo.action) {
            case 'CREATE':
                // add todo to the array
                $scope.todoItems.push(todo);
                // add to lookup table as well
                todoLookup[todo.id] = todo;
                $scope.connectionStatus = "To-Do note added successfully";
                break;
            case 'COMPLETED':
                if (todoLookup.hasOwnProperty(todo.id)) {
                    todoLookup[todo.id].action = todo.action;
                    $scope.connectionStatus = "To-Do note marked as complete";
                }
                break;
            case 'DELETE':
                // remove from array
                for (var i = 0; i < $scope.todoItems.length; i++) {
                    if ($scope.todoItems[i].id === todo.id) {
                        $scope.todoItems.splice(i, 1);
                        break;
                    }
                }
                // remove from object
                if (todoLookup.hasOwnProperty(todo.id)) {
                    delete todoLookup[todo.id];

                    $scope.connectionStatus = "ToDo note removed successfully";
                }
                break;
            default:
                $log.error("Can't handle server response with action: " + todo.action);
        }

    });

});
