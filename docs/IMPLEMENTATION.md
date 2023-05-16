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

The board is represented using a hybrid solution, which keeps redundant associations to make some algorithms simpler.

Piece centric: The board keeps a 3D array of bitboards, indexed first by their piece type and then by their color.
There are 12 bitboards in total, each stored as a Java 64-bit long primitive type.
The squares are indexed from 0 (a1) to 63 (h8), using Little-Endian Rank-File mapping (LERF).
The indexes are known as "squares".
Performing the operation `1L << square` returns the bit that represents a particular square.

Square centric: The board keeps an array (mailbox) that dispatches piece records or "null" pointers.
The mailbox is implemented as an 8x8 board, which stores exactly 64 values.
Performing the operation `mailbox[square]` returns the piece at a particular square.
The squares used in the mailbox are exactly the sames as the ones used in the bitboard.
