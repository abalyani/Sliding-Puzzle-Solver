# Sliding Puzzle Solver

This project finds the moves to solve any solvable sliding puzzle of any dimensions NxN.

## Use

In an IDE run the main class PuzzleSolver.java and adjust the variables to your liking, and un-comment the algorithms you would like to use.

## Customization

- #### Approach
The puzzle can either be randomly filled and shuffle or manually created. You can do this by un-commenting on the desired approach in the main method in the class PuzzleSolver.java (eg. the random initial is the desired approach):
```java
	public static void main(String[] args) {
		...
//		Choose one approach at a time
		randomInitialState(dimension);
//		preWrittenInitialState(dimension);
	}
```

- #### Size
By changing the integer in the main method you can change the dimensions of the puzzle.
```java
	public static void main(String[] args) {
		// Dimensions of the puzzle
		int dimension = 4;
		...
		}
```


But if you were to also want to change the dimensions of the puzzle for the pre-written initial state approach then you'll also have to manually change the inputed numbers (some states are impossible to solve).
For example here's the code for a 3x3 puzzle:
```java
	public static void preWrittenInitialState(int dimension) {
		...
		int matrix[][] = { { 3, 2, 8 }, { 4, 0, 1 }, { 7, 6, 5 } };
		...
		}
```
And you'd follow the same approach for a 4x4 puzzle:
```java
    public static void preWrittenInitialState(int dimension) {
    	...
		int matrix[][] = { { 1, 4, 8, 0 }, { 6, 2, 3, 11 }, { 13, 7, 10, 12 }, { 5, 9, 15, 14 } };
		...
		}
```
The program will prompt you with this error message if your inputed numbers create an unsolvable puzzle:
>       This puzzle is unsolvable please change the input.
- #### Algorithms
 After customizing the puzzle to your preferences you can now choose which algorithm to solve the puzzle with by un-commenting one algorithm in each run:
 ```java
        ...
		// Choose only one algorithm at a time
		Astar(initial);
//		BFS(initial);
//		DFS(initial);
//		ID(initial);
//		DLS(initial, 5);
        ...
```
## Recommendations and notes
It's not recommended to use any of the uninformed search algorithms (BFS, DFS, ID or DLS) if the dimensions of the puzzle is larger than 4x4. You should instead use the A* Search Algorithm as it yields the quickest and most optimal results thanks to it's admissible heuristic.
I apologize for not providing a gui for this project as it's only an old project which I've recently had time to polish and clean-up.
