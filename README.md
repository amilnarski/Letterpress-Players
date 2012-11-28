# Letterpress-Players
Letterpress-Players is an implementation of [Loren Brichter's Letterpress][3] in Java, along with several players. Currently there are players which use random, greedy, and weighted greedy strategies. This implementation currently lives on the console.
[3]:http://atebits.com/Letterpress/

## Structure
The README, dictionary source files, dictionary processor, and a word map live at the project's root directory. As expected, **src/** contains the source files for the following classes and interfaces.

## Classes
### BadCoordException
Exception thrown when LCoord is instantiated with bad coordinates.
### GameState
Object used to communicate the state of the game to a player.
### GreedyPlayer
Player that uses a greedy strategy to play the game, making the longest possible word.
### HumanPlayer
Player that allows a human to play on the console. Using and updating the WordMap file will make this more practical.
### LCoord
Set of (row, col) coordinates safe for accessing the game board. 
### LMove
List of LCoord representing a list of letters that will signify a word or a pass.
### LPlayer
Abstract superclass of all other Letterpress players.
### Letterpress
The main game class which is responsible for managing the game.
### Move
Encapsulates a move as a set of LCoords. Contains an optional string representation of the move.
### RandomPlayer
Player that makes the first available move.
### WeightedGreedyPlayer
Player that uses a set of relative and absolute weights to direct its moves. It prioritizes letters next to its own, undefended letters of its opponent, letters in corners, and letters on edges.

## Interfaces
### Game
Specifies what methods are needed to be playable by an Player.
### Player
Specifies the methods a Player needs to interact with a Game.

## Sources and Attribution
Much thanks to Loren Brichter for designing Letterpress' game mechanics. [Letterpress is free on the iOS AppStore][4].
[4]:https://itunes.apple.com/us/app/letterpress-word-game/id526619424

Original dictionary file from [Project Gutenberg EBook of Webster's Unabridged Dictionary][1] by Various.

Supplemental dictionary file from [xawill's LetterPress-Finder repo][2].

[1]:http://www.gutenberg.org/files/29765/29765-8.txt
[2]:https://raw.github.com/xawill/LetterPress-Finder/master/SortedDictionary.txt