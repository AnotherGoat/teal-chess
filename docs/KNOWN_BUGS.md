# Known bugs

This is a list of known bugs that will be fixed before the first release:

- The icon for recommended moves makes the destination piece disappear.
- Castling works, but the castling move doesn't appear as a recommended move.
- The game freezes when a king is in check and there's no way to escape the
  softlock.
- Pawn promotion hasn't been implemented yet.
- When a move leaves the player itself in check, it doesn't happen, but a null
  pointer exception is thrown.
- En passant moves are generated many times instead of just once.
