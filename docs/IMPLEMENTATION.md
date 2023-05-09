# Implementation details

This document lists technical implementation details about this chess engine.
Special thanks to
[Chess Programming Wiki](https://www.chessprogramming.org/Main_Page) for
containing so many useful resources.

Due to its usefulness, the documentation for this project will include links to
relevant wiki pages when it makes sense. To add a link to an external page in a
Javadoc, please use this template:

```html
@see <a href="https://example.com/">Example Domain</a>
```

## List of implementation details

### Board Representation

The board is represented using an hybrid solution, which keeps redundant associations to make algorithms simpler.

Piece centric: The board keeps two sets with its pieces, separated by their color.
Each piece knows its location, which is known as a coordinate.
This allows skipping the "scan the board" step during move generation (see the `Board.pieces(Color)` method).

Square centric: The board keeps a map (mailbox) that dispatches pieces or "null" pointers.
The map has exactly 64 entries, which represent an 8x8 board. 
This allows quickly finding pieces by their coordinates (see the `Board.pieceAt(Coordinate)` and `Board.mailbox()` methods).
