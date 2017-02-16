Introduction
===

In this project we will be designing a tool for visualizing Cellular Automata simulations. The goal for the program is for it to be able to simulate any rule that the user could want for any starting population that the user can specify. The simulation will be shown on a 2-D grid that should be flexible enough to display any number of cell states. The cells will be the most flexible part of this project considering that they will contain the "intelligence" for the program to operate correctly. In order to achieve this flexibility, we will design our cells so that they are open for extension in the future. However, the other aspects of our project, such as the display and UI controller, will not be need to change. These two aspects are necessary and have set functions for any variation of the project, so they will be closed. The configuration of the simulation will be set by an input file formatted in XML. The user interface should provide the user with options to load this configuration file and begin the simulation.

Overview
===
![Figure 1](cellsociety_overview.png)

The design is split into three broad categories: visualization, simulation, and configuration. The visualization components make up the user interface and display the results of the simulation. This is implemented using the various Display and GUIController classes. The GUIController Is in charge of instantiating and connecting everything. The simulation components contain the program logic and present the state of the cell society to the display after every step through the simulation. This is implemented through the Cell and Simulation classes. The cell's getNeighborsCoords() method will allows the grid to find each cell's neighbors, and then it can inform the cells of the state of their neighbors. And each cell contains the "intelligence," in its computeNextState() and advanceState() method, to know how to act accordingly when given this information. The simulation then adjusts accordingly after the cell informs it what the next step is with its advanceGrid() method. The configuration components respond to requests from the user interface to load a new file, it reads the corresponding XML file, and it sets all of the user-specified parameters for the simulation. This is implemented through the XMLReader and ConfigDoc classes. The XMLReader builds a ConfigDoc with all of the simulation parameters specified, and then this object is passed around so that every part of the program agrees on the simulation parameters. 


User Interface
===
![Figure 2](cellsociety_UI.png)

The primary functions of the user interface are to provide a means of loading the configuration file and to give the user control over stepping through the simulation. To select a configuration file, the user can browse the files in the "data" directory. Then the user has options to run the simulation continuously or to step through it frame-by-frame. As the simulation progresses, the display shows a graph of all of the steps that have occurred and the population at each step. To start over, the user can reset the simulation to its initial state. The user also has options to modify the grid width, height, shape, edge type, and grid line display.

Design Details
===

The visualization component of the design will include the input and output. For output of the grid, we have a GridDisplay class that contains the method drawGrid(). This superclass is extended by TriangleDisplay.java, which makes the shape of the cells into triangles instead of rectangles by overriding the makeShape() method called in drawGrid. 
drawGrid() prints out the current state of the grid by getting relevant information from the ConfigDoc.
For output of the graph, we have a GraphDisplay class that uses information from the Simulation and the ConfigDoc to update the series' being graphed at each step. The GraphDisplay and GridDisplay are both instantiated in the GUIController. 
For input, we have a ControlDisplay class that contains all elements (buttons, sliders) that  trigger events when clicked. This is instantiated in the GUIController class that connects them to the rest of the program. It also requires a GUIController object to be passed to it in order to be able to control the animation. 

We have a XMLReader class that is responsible for translating the XML input files. This class reads in the data from the XML file, makes a ConfigDoc, and hands it off to the classes that need to know the simulation parameters. The ConfigDoc also has methods to set the parameters to allow mid-simulation updating. 
The main methods of the XMLReader are getString, getInt, and getDouble, which are used by the ConfigDoc to look up elements in the XML file by their tag names. The ConfigDoc itself then has methods like getGridWidth() that are more user-friendly in terms of being very clear and not requiring any knowledge of the XML for other classes.

The state of the Cell Society is kept in a Simulation class that contains a data structure holding Cells. The Simulation class has a variety of methods that manipulate or get the state of the grid: getCellAtPoint(), buildGrid(), computeNextGrid(), advanceGrid(), applyAllSwaps(). 
getCellAtPoint() allows the UI to find cells in the grid
buildGrid() will initialize any parts of the grid that haven't been initialized
computeNextGrid() is used to cause all of the cells to find their next state
advanceGrid() causes all cells to switch to their next state synchronously
applyAllSwaps() causes all cells to move at the same time

The Cell class contains the current and next states of each cell, coordinates of said cell and methods to update the state of the cell based on the rule: computeNextState(), advanceState() and getNeighborCoords(). As the Simulation class iterates through the grid data structure, it will call cell.getNeighborCoords(), which will return coordinates of each neighboring cell. The Simulation class will then determine the states of all those cells, and inform the Cell to compute its next state based on this information. This info is passed to cell.computeNextState(), where the rule will be applied based on the neighbors to calculate the cell's next state. The advanceState() method will then take this computed next state and update the current and next states of each cell accordingly. Once this has been done for every cell, the grid has essentially been updated to contain the next state of every cell and the new grid will be passed to the display. 

#####Use Cases:
1. Apply the rules to a middle cell 
	To apply the rules to a middle cell, the Grid class will call cell.getNeighbors(), which will return a data structure of the coordinates of the cell's neighbors. The Grid will use this information to determine the states of all the neighbors. These states will then be passed to cell.computeNext() which will use the states to compute the new state. This new state will become that cell's next state but the current state will remain unaffected for now.
2. Apply the rules to an edge cell
	Because we plan to use the coordinates of neighbors to update the state of any given cell, the process should be the same for the edge cells as the middle cells. We plan to ignore any null coordinates and just use the neighbors given to compute the next state of the given edge cell. 
3. Move to the next generation
	This starts by the user pressing a button to start the simulation. This will trigger the Grid to iterate over every Cell and call advanceState() to switch all of the cells to the next state that they have already calculated. Grid will then pass this information to the Display to be printed.
4. Set a simulation parameter
	This will be done by the FileReader. When the user loads a file, it will set an instance variable that the Cells can get later on when they are trying to determine how often to follow the rule.
5. Switch Simulations:
	To switch a simulation, simply click the load file button and choose the file you want. The XMLReader then loads the simulation and all the necessary changes take place. This is entirely done by the FileReader. The simulations are entirely described in XML so that when the input file is loaded, the simulation will be completely set up. 

Design Considerations
===

We have considered adding separate classes for cells on the edge of the simulation, but we think that probably reduces flexibility too much because it would directly limit cells from moving around the grid if they are not all of the same type.
We have also considered different solutions for how the cells figure out their neighboring cells. We think that this is something that can be handled in the Cell class, but it is possible we might have to move this up to the Grid class if it is too complicated to store this information in the Cell class. 

Team Responsibilities
===
#####Jay
Primary Responsibilities: file reading + cell 

Secondary Responsibilities: working on communication between different parts 

#####Tahia
Primary Responsibilities: GUI controller, display 

Secondary Responsibilities: helping out with cell

#####Belal
Primary Responsibilities: grid, button class 

Secondary Responsibilities: helping out with cell, GUIcontroller 

