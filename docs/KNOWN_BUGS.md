# Known bugs

This is a list of known bugs that will be fixed before the first release:

- The icon for highlighted moves appears over the destination piece, but
  misaligned.
- Castling works, but the castling move doesn't appear as a recommended move.
- The game freezes when a king is in check and the only option is to restart
  the game
- Pawn promotion hasn't been implemented yet.
- When a move leaves the player itself in check, it doesn't happen, but a null
  pointer exception is thrown.
- En passant moves are generated many times instead of just once.
