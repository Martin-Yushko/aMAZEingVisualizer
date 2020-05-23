# aMAZEing Visualiser

## Overview

The **aMAZEing Visualiser** is an interactable tool that allows anyone to create their
own maze and then observe a pathfinding alogirthm search for the shortest path
through the maze in real time. This is an excellent tool to help explain, demo, or
simply play around with some classic pathfinding algorithms. As a developper with 
an interest in algorithms, I find this to be both an educational project and a
fun experience.

Some of the key features of this application are:
- Implementation of famous pathfinding algorithms such as Dijkastra's and A*
- Ability to customize a maze and add obstacles, "weigths", and "portals" (more on those later)
- Customizeable speed and other visualisation settings

NOTE: Most of the things mentioned above are still under development and may therefore not be
present in the current version of the application.
## User Stories

- As a user, I would like to be able to add obstacles to the maze
- As a user, I would like to be able to change the start and end positions
- As a user, I would like to be able to see the path finding process at every step of the algorithm
- As a user, I would like to be able to see statistics of the path finding, including the 
number of nodes visited, and the time taken to find the path.
- As a user, I would like to be able to save mazes I've created to reuse in the future.
- As a user, I would like to be able to load mazes that I've saved and run the application with said maze.

## Instructions for Grader

### Boring Version:
1. You can generate the first required event by clicking on a cell on the grid. This will create
a new wall in the maze

2. You can generate the second required event by dragging over the grid to place walls much faster.
   Click or drag over them again to remove them. OR click on the "remove all obstacles" button
   
2.5: You can also drag and drop the start and end cells to change the position of those
   
3. You can locate my visual component by pressing the "visualize" button. You can change the speed
of the animation by using the slider at the bottom.

4. You can save the state of my application by clicking the "save" button. Note: no spaces or
special characters in the name please

4. You can reload the state of my application by clicking the "load" button. Try selecting the
hugeMAZE option and then visualize that one with the slider about half-way through!

### Non-Boring Version:
1. When you run the application you will be presented with a menu in the left hand side and a
grid on the right. Click the "Visualize" button.

2. Wasn't that cool? Now on the bottom there is a slider to control the speed at which the
animation will play. Try out the slowest and fastest speeds. Note: The fast speeds may look
a little awkward; I still haven't fully ironed out the math involved in making this look
perfectly smooth.

3. Ok, but that's a boring maze. To spice it up. One thing you can do is change the
dimensions of the grid. Right now it's quite small. Make it bigger! You can do this
by entering numbers in the text fields in the left of the window and pressing the
"Apply" button. Note: please enter the same number for both; I haven't ironed out a 
few bugs when the dimensions are not the same. Second Note: I haven't
handled invalid inputs yet, so please input a natural number. Don't pick a value higher
than 50 or so otherwise it misbehaves a little. I recommend 30! 

4. Now, click on a cell (one of the squares on the grid). It will turn it into a wall! Note:
 You cannot override the start or target points, so those will not respond to being clicked on.
 You can add as many walls as you'd like easily by dragging over the grid. Try it out!

5. To make this grid even better, you can switch around the start and end points. To do this,
just drag and drop them to a new position (but in such a way that they don't override a start
or end cell). Now you have a customized maze!

6. Visualize it again and behold your creation. But your creation is so amazing that it
would be sad to lose it. That's why there is that neat little save button on the left!
Click it, and give your maze an appropriate name. Note: Again, I haven't handled all
incorrect inputs, so please use just letters and no spaces. I recommend naming it
"bestMAZE".

7. Now click the "remove all obstacles" button. Suddenly your beautiful creation is gone
and you are in despair because you already miss it so much. Don't fret though! Click
the "load" button, find "bestMAZE" (or whatever you named it) from the dropdown list,
and click "confirm". Now your maze is back and you can visualize it again if you'd like!

8. Now you've pretty much seen everything about this application. Except for one thing.
Go the the load button again, find the maze called "impossibleMAZE", and visualize it!
There is a neat little animation at the end to signal that the maze has no valid path.

9. To leave you off on a high note, I created a cool grid with a satisfying animation when
visualized. Go to the load button, and select "hugeMAZE". I suggest making the visualisation
speed somewhere close to the middle, and then click visualize one last time.

10. Thank you for your time, and have a great rest of the day!

## Phase 4: Task 2

I chose the option of implementing the Map data structure. I implemented this in the 
SceneAssembler class in which I add every Node present in the project's scene to
this map, and access it through a unique string. I did this so that other methods
throughout the class could access the same objects. This allowed me to compartmentalize
different aspects of the class's responsibilities into separate methods, and serves
as a convenient way to store and access the different elements of the scene. In 
effect, a pointer to these objects persists outside the main method, and a map was
the only way to accomplish this functionality by virtue of the fact that the different
Nodes need to be distinctly identifiable (i.e. a List, for example, fails to guarantee
proper retrieval of a desired Node), which is crucial when initializing their properties.

## Phase 4: Task 2
1. Created DragAndDroppable abstract class. Before, a major responsibility of the
Cell class was to handle dragging events. This required adding various event listeners
and initialize a handler method for these listeners. Now, Cell extends DragAndDroppable,
which makes it more clear which part of the Cell is responsible for drag and dropping
and which is responsible for animation, for example. Also, this allows more flexibility
in the future to add more elements which are drag and droppable by simply requiring
that they extend DragAndDroppable. Although the class itself is still responsible for
the implementation, creating this new abstract class cleans up the code and makes it
more apparent as to what the behaviour of a Cell should be. To further improve cohesion,
I separated the animation responsibility away from the cell and to a new class called
CellAnimator. This new class simply accepts the targeted cell as a parameter and all
the same logic as before is applied. Instead of calling cell.animate(...), we call
CellAnimator.animateCell(cell, ...). This also creates more breathing room if/when I
choose to create more complicated animations.

2. Extracted all "abstract" aspects of AStarFinder class and migrated those things
to the Pathfinder abstract class (which was previously an interface). I chose to
do this becuase in the future, I expect to be adding more pathfinder classes such as
dijkstra's, and I noted some commonality across all pathfinder classes I would have,
including a start/end time, fields relating to statistics, the initializeValues method,
etc. This way, repetition is eliminated and abstraction is maximized. 