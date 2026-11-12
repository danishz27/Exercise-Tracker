---
title: Exercise Tracker - Phase 03
author: Danish Zaheer (zaheerd@myumanitoba.ca)
date: Winter 2026
---

## Overview

> The program features an Exercise Tracker with several options:
>
> The Main Menu is displayed after a successful sign in attempt on a "SIGN IN" menu
> prior where creating a new account is also an option.
> Further the user has options such as "UPDATE ACCOUNT", which allows the user to
> modify their username and password. The user can "VIEW FEED", which shows their own activities
> mixed with activities of all other users on the tracker.
> Users can also choose to "FOLLOW" or "UNFOLLOW" other users of the Exercise Tracker.

> Important features also include "ADD ACTIVITY", "REMOVE ACTIVITY", "ADD OBSTACLE", and
> "FIND NEW ROUTE" which uses a stack based path finding algorithm to find a new route. Route finding
> has two options: finding a route using exclusively the current user's previously taken routes, or
> finding a route using all routes from any user on the tracker.

> When adding an activity, the user can either enter a new route by providing waypoints, or duplicate
> a route from one of their own previous activities.

> The SIGN IN menu also features options like "CREATE ACCOUNT" and "EXIT".

> The map is displayed as a 10x10 grid where each cell is represented by a symbol:
> * `.` represents an empty area with no activity or obstacle present.
> * `>` represents a waypoint of an activity path that has been added to the map.
> * `*` represents an obstacle such as a RIVER, TREE, or ROCK placed on the map.
> * `@` represents a found route returned by the path finding algorithm.


## Testing a stack

| method      | data                                 | expected outcome                                                 |
| ----------- | ------------------------------------ | ---------------------------------------------------------------- |
| `push()`    | empty stack, push `"hello"`          | size() = 1; isEmpty() = false; peek() = "hello"; pop() = "hello" |
| `push()`    | push multiple elements `"A","B","C"` | size() = 3; peek() = "C"                                         |
| `pop()`     | stack with `"A","B","C"`             | returns "C"; size decreases by 1                                 |
| `pop()`     | single element `"A"`                 | returns "A"; stack becomes empty                                 |
| `pop()`     | empty stack                          | throws exception ("empty stack")                                 |
| `peek()`    | stack with `"A","B"`                 | returns "B"; size unchanged                                      |
| `peek()`    | empty stack                          | throws exception                                                 |
| `size()`    | new stack                            | returns 0                                                        |
| `size()`    | after 5 pushes                       | returns 5                                                        |
| `isEmpty()` | new stack                            | returns true                                                     |
| `isEmpty()` | after push                           | returns false                                                    |
| `isEmpty()` | after popping all elements           | returns true                                                     |



## Edge Cases Handling
* Does not let a user update their username to an existing username.
* Does not let a user create an account with an already existing username.
* Does not let a user follow the same user twice.
* Does not let a user add a path outside the map bounds (0-9, 0-9).
* Does not let a user add an obstacle outside the map bounds (0-9, 0-9).
* Does not let a user duplicate a route when they have no previous activities.
* Does not let a user remove an activity with an invalid index.
* Does not let a user enter non-integer values for coordinates or index selections.
* Does not let a user enter empty username or password fields.

## Flows of interaction

### Updates
* Now the map is a 10x10 hard coded grid instead of being accepted as dimensions from the user.
* All menu options are now number-based instead of text-based commands.
* In Update Account, added password verification upon choosing to update password.
* In View Feed, full feed now shows activities from all users on the tracker regardless of following status.
* In Follow/Unfollow, only shows users that are not already being followed in the follow list.
* In Find New Route, feed route now searches through all users' routes on the tracker rather than only followed users.
* In Add Activity, route options menu is now shown after entering activity details rather than as a separate menu step.
* Cancel option added to Remove Activity to allow backing out without removing.

### Resources

* https://code.cs.umanitoba.ca/comp2450-fall2025/2450-stack/-/blob/main/src/main/java/ca/umanitoba/cs/umbrist1/generics/stacks/Stack.java
* https://en.wikipedia.org/wiki/Backtracking
* https://code.cs.umanitoba.ca/comp2450-winter2026/2450-hack
* https://en.wikipedia.org/wiki/Flowchart
* https://mermaid.js.org/syntax/flowchart.html#a-node-in-a-subroutine-shape
* https://mermaid.js.org/syntax/flowchart.html#decision-diamond
* https://mermaid.js.org/syntax/flowchart.html

### Diagrams

### Sign in

```mermaid
flowchart
subgraph Sign in
    direction TB
    
    start[[Start]]
    getOption[Sign in Menu]

    enterInformation[Enter Information]
    createAccount{Create Account}
    verifyAccount{Verify Account}
    mainMenu[[Main Menu]]
    trackerEnd[[Tracker Ended]]
    
    start -- Show sign in menu --> getOption
    getOption == Sign in, username, password ==> verifyAccount
    getOption == Create Account ==> enterInformation
    getOption == Exit ==> trackerEnd
    

    verifyAccount -. Account found, show main menu .-> mainMenu
    verifyAccount -. Account not found .-> getOption
    verifyAccount -. Invalid input .-> getOption
    
    enterInformation == username, password ==> createAccount
    createAccount -. Account created, return to sign in .-> getOption
    createAccount -. Username already exists .-> enterInformation
    createAccount -. Invalid input .-> enterInformation
end
```

### Updating Account

```mermaid
flowchart
    subgraph Update Account
        direction TB

        start[[Main Menu]]
        getOption[Update Account Menu]
        updateUsername{Update Username}
 
        verifyCurrentPassword{Verify Password}
        newPassword[Enter New Password]
        updatePassword{Update Password}
        success[[Account Updated]]

        start -- Show update options --> getOption
        getOption == Update Username, new username ==> updateUsername
        getOption == Update Password, current password ==> verifyCurrentPassword
        getOption == Cancel ==> start

        updateUsername -. Username updated .-> success
        updateUsername -. Username already exists .-> getOption
        updateUsername -. Invalid input .-> getOption
        
        verifyCurrentPassword -. valid password .-> newPassword
        verifyCurrentPassword -. Invalid password, invalid input .-> getOption
        
        newPassword == new password ==> updatePassword
        newPassword -. Invalid Input .-> getOption

        updatePassword -. Password updated .-> success
        updatePassword -. Invalid input .-> getOption

        success -- Return to main menu --> start
    end
```
### Viewing Feed

```mermaid
flowchart
    subgraph View Feed
        direction TB

        start[[Main Menu]]
        getOption[Feed Menu]
        showActivity{Show Activity}
        filterFeed{Filter Feed}
        loadFeed{Load Feed}

        start -- Load activities --> loadFeed
        loadFeed -. All users activities .-> getOption
        loadFeed -. No activities in feed .-> getOption

        getOption == View Activity, activity index ==> showActivity
        getOption == Filter by own activities ==> filterFeed
        getOption == Filter by followed users ==> filterFeed
        getOption == Back ==> start

        showActivity -. Show activity details and route on map .-> getOption
        showActivity -. Invalid index .-> getOption

        filterFeed -. Show filtered feed .-> getOption
        filterFeed -. No activities found .-> getOption
    end

```

### User Following

```mermaid
flowchart
    subgraph Follow/Unfollow User
        direction TB

        start[[Main Menu]]
        getOption[Follow Menu]
        followUser{Follow User}
        unfollowUser{Unfollow User}
        success[[Action Successful]]

        start -- Load users --> getOption
        getOption == Follow User, user index ==> followUser
        getOption == Unfollow User, user index ==> unfollowUser
        getOption == Back ==> start
        getOption -. No users available .-> start

        followUser -. Successfully followed .-> success
        followUser -. Invalid index .-> getOption

        unfollowUser -. Successfully unfollowed .-> success
        unfollowUser -. Not following user .-> getOption
        unfollowUser -. Invalid index .-> getOption

        success -- Return to follow menu --> getOption
    end
```

### Adding Exercise

```mermaid
flowchart
    subgraph Add Activity
        direction TB

        start[[Main Menu]]
        enterDetails{Verify Activity Details}
        selectRouteOption[Route Options]
        enterWaypoints{Verify Waypoints}
        duplicateRoute{Duplicate Route}
        createActivity{Create Activity}
        success[[Activity Added]]

        start -- Add activity, name, type, distance, duration --> enterDetails

        enterDetails -. valid activity .-> selectRouteOption
        enterDetails -. Invalid input .-> enterDetails

        selectRouteOption == Enter new route, waypoints x y ==> enterWaypoints
        selectRouteOption == Duplicate previous route, activity index ==> duplicateRoute
        selectRouteOption == Back ==> start

        enterWaypoints -. Valid waypoints .-> createActivity
        enterWaypoints -. Invalid waypoints .-> selectRouteOption

        duplicateRoute -. Route duplicated .-> createActivity
        duplicateRoute -. No previous activities .-> selectRouteOption
        duplicateRoute -. Invalid index .-> selectRouteOption

        createActivity -. Activity created .-> success
        createActivity -. Invalid activity .-> enterDetails

        success -- Return to main menu --> start
    end
```

### Finding New Route

```mermaid
flowchart
    subgraph Find New Route
        direction TB

        start[[Main Menu]]
        getOption[Route Finder Menu]
        findPersonal{Find Personal Route}
        findFeed{Find All Users Route}
        showRoute[[Route Found]]

        start -- Show route finder menu --> getOption
        getOption == Personal routes only, start point, end point ==> findPersonal
        getOption == All users routes, start point, end point ==> findFeed
        getOption == Back ==> start

        findPersonal -. Route found, show route on map .-> showRoute
        findPersonal -. No route found .-> getOption
        findPersonal -. Invalid points .-> getOption

        findFeed -. Route found, show route on map .-> showRoute
        findFeed -. No route found .-> getOption
        findFeed -. Invalid points .-> getOption

        showRoute -- Return to main menu --> start
    end
```

### Adding Obstacle

```mermaid
flowchart
    subgraph Add Obstacle
        direction TB

        start[[Main Menu]]
        getOption[Add Obstacle Menu]
        addObstacle{Add Obstacle}
        success[[Obstacle Added]]

        start -- Show add obstacle menu --> getOption
        getOption == Add obstacle, obstacle type, x y coordinates ==> addObstacle
        getOption == Back ==> start

        addObstacle -. Obstacle added .-> success
        addObstacle -. Invalid type .-> getOption
        addObstacle -. Invalid coordinates .-> getOption
        addObstacle -. Out of bounds .-> getOption

        success -- Return to main menu --> start
    end
```
### Removing Exercise

```mermaid
flowchart
    subgraph Remove Activity
        direction TB

        start[[Main Menu]]
        getOption[Remove Activity Menu]
        removeActivity{Remove Activity}
        success[[Activity Removed]]

        start -- Load current user activities --> getOption
        getOption == activity index ==> removeActivity
        getOption == Cancel ==> start
        getOption -. No activities found .-> start

        removeActivity -. Activity removed .-> success
        removeActivity -. Invalid index .-> getOption

        success -- Return to main menu --> start
    end
```

## Domain Model

## Changes

#### Removed the Node class and related invariant notes and relationships as Node class is implemented internally in LinkedStack.
#### Added adequate invariant checks for the new classes.
#### Updated relationships accordingly.

## Resources

* https://code.cs.umanitoba.ca/comp2450-winter2026/2450-hack
* https://mermaid.js.org/syntax/classDiagram.html#defining-relationship
* https://mermaid.js.org/syntax/classDiagram.html#annotations-on-classes
* https://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop
* https://en.wikipedia.org/wiki/ASCII

## Diagram

```mermaid
    
classDiagram

    %% Classes
    
    class User{
        -String username
        -String password
        -List~Exercise~ exercises
        -List~User~ following
        
        +addExercise(exercise) void
        +removeExercise(index) void
        +followUser(user) void
        +unfollowUser(user) void
    }

    class Exercise {
        -String name
        -ExerciseType type
        -double distance
        -int duration
        -Path exercisePath
    }


    class Map {
        -int width
        -int height
        -List~Path~ paths
        -List~Obstacles~ obstacleList

        +addPath(path) void
        +removePath(path) void
        +addObstacle(obstacle) void
        +removeObstacle(obstacle) boolean
    }
    
    class Obstacles {
        -ObstacleType type
        -Point position

        +isWithinBounds(width, height) boolean
    }

    class Path {
        -String name
        -List~Point~ waypoints

        +addWaypoint(point) void
        +isValid() boolean
        +isWithinBounds(width, height) boolean
    }
    
    class Point {
        -int x
        -int y
    }

    class Tracker {
        -List~User~ users
        -Map worldMap

        +addUser(user) void
        +removeUser(user) void
        +hasUser(username) boolean
    }

    class LinkedStack~T~ {
        -Node~T~ top
        -int size

        +push(item) void
        +pop() T
        +peek() T
        +size() int
        +isEmpty() boolean
    }
    
    %% Enumerations
    
    class ExerciseType {
        <<enumeration>>
        RUNNING
        CYCLING
    }
    
    class ObstacleType {
        <<enumeration>>
        RIVER
        TREE
        ROCK
    }
    
    %% Interfaces
    
    class Stack~T~{
        <<interface>>
        +push(item) void
        +pop() T
        +peek() T
        +size() int
        +isEmpty() boolean
    }
    
    %% Class Invariants

    note for User "Invariant Properties
    * username != null && username.length() > 0
    * password != null && password.length() > 0
    * exercises != null
    * following != null
    * following does not contain self
    "

    note for Exercise "Invariant Properties
    * name != null && name.length() > 0
    * type != null
    * distance >= 0
    * duration > 0
    * exercisePath != null
    "

    note for Map "Invariant Properties
    * width > 0
    * height > 0
    * paths != null
    * obstacleList != null
    "
    
    note for Obstacles "Invariant Properties
    * type != null
    * position != null
    "

    note for Path "Invariant Properties
    * name != null && name.length() > 0
    * waypoints != null
    "
    
    note for Point "Invariant Properties
     * x >= 0
     * y >= 0
     "
    
    note for Tracker "Invariant Properties
    * users != null
    * worldMap != null
    "
    
    note for LinkedStack "Invariant Properties
    * size >= 0
    *size == 0 means top == null
    *size > 0 means top != null
    "
    
    %% Relationships
    
    User --* Exercise
    User --o User

    Exercise --o Path
    Exercise --> ExerciseType

    Map --* Obstacles
    Map --* Path

    Obstacles --> ObstacleType
    Obstacles --* Point

    Path --* Point

    Tracker --* User
    Tracker --* Map
    
    LinkedStack ..|> Stack

```
