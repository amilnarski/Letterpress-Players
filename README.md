# Letterpress-Players
Letterpress-Players is an implementation of [Loren Brichter's Letterpress][3] in Java, along with several players. Currently there are players which use random, greedy, and weighted greedy strategies. This implementation currently lives on the console.
[3]:http://atebits.com/Letterpress/

## Classes
### BadCoordException
Exception thrown when LCoord is instantiated with bad coordinates.
### LCoord
Set of (row, col) coordinates safe for accessing the game board. 
### LMove
List of LCoord representing a list of letters that will signify a word or a pass.
### Letterpress
The main game class which is responsible for managing the game.

## Interfaces
### Game
Specifies what methods are needed to be playable by an Player.
### Player
Specifies the methods a Player needs to interact with a Game.

## Sources and Attribution
Original dictionary file from [Project Gutenberg EBook of Webster's Unabridged Dictionary][1] by Various.

Supplemental dictionary file from [xawill's LetterPress-Finder repo][2].

[1]:http://www.gutenberg.org/files/29765/29765-8.txt
[2]:https://raw.github.com/xawill/LetterPress-Finder/master/SortedDictionary.txt