# Requirements

This document compiles all the requirements for this project and the
implementation progress of each. Fully implemented requirements will be marked
with a check mark (✓) and incomplete ones will be marked with a cross mark (✗).

## Laws of Chess

The FIDE (Fédération Internationale des Échecs) defines a standard set of rules
for international chess in their
[Laws of Chess](https://handbook.fide.com/chapter/E012023) rulebook. This
project is digital, which is why some of the rules will be replaced by the ones
defined in the FIDE's
[Online Chess Regulations](https://handbook.fide.com/chapter/OnlineChessRegulations).

### Basic Rules of Play

Only the first 3 articles are taken into account.

#### ✗ Article 1: The Nature and Objectives of the Game of Chess

- ✓ (1.1) The game of chess is played between two opponents who move their
  pieces on a square board called a **chessboard**.
- ✓ (1.2)
  - ✓ The player with the light-coloured pieces (White) makes the first move,
  - ✓ then the players move alternately, with the player with the dark-coloured
    pieces (Black) making the next move.
- ✓ (1.3) A player is said to **have the move** when his/her opponent's move has
  been **made**.
- ✓ (1.4) The objective of each player is to place the opponent's king **under
  attack** in such a way that the opponent has no legal move.
  - ✗ (1.4.1)
    - ✗ The player who achieves this goal is said to have **checkmated** the
      opponent's king and to have won the game.
    - ✗ Leaving one's own king under attack, exposing one's own king to attack
      and also **capturing** the opponent's king is not allowed.
  - ✗ (1.4.2) The opponent whose king has been checkmated has lost the game.
  - ✗ (1.4.3) If the position is such that neither player can possibly checkmate
    the opponent's king, the game is drawn (see Article 5.2.2).

#### ✓ Article 2: The Initial Position of the Pieces on the Chessboard

- ✓ (2.2)
  - ✓ At the beginning of the game White has 16 light-coloured pieces (the
    **white** pieces);
  - ✓ (Black has 16 dark-coloured pieces (the **black** pieces).
- ✓ (2.3) The initial position of the pieces on the chessboard is as follows:
- ✓ (2.4)
  - ✓ The eight vertical columns of squares are called **files**.
  - ✓ The eight horizontal rows of squares are called **ranks**.
  - ✓ A straight line of squares of the same colour, running from one edge of
    the board to an adjacent edge, is called a **diagonal**.

#### ✗ Article 3: The Moves of the Pieces

- ✓ (3.1) It is not permitted to move a piece to a square occupied by a piece of
  the same colour.
  - ✓ (3.1.1) If a piece moves to a square occupied by an opponent's piece the
    latter is captured and removed from the chessboard as part of the same move.
  - ✓ (3.1.2) A piece is said to attack an opponent's piece if the piece could
    make a capture on that square according to Articles 3.2 to 3.8.
  - ✓ (3.1.3) A piece is considered to attack a square even if this piece is
    constrained from moving to that square because it would then leave or place
    the king of its own colour under attack.
- ✓ (3.2) The bishop may move to any square along a diagonal on which it stands.
- ✓ (3.3) The rook may move to any square along the file or the rank on which it
  stands.
- ✓ (3.4) The queen may move to any square along the file, the rank or a
  diagonal on which it stands.
- ✓ (3.5) When making these moves, the bishop, rook or queen may not move over
  any intervening pieces.
- ✓ (3.6) The knight may move to one of the squares nearest to that on which it
  stands but not on the same rank, file or diagonal.
- ✗ (3.7) The pawn:
  - ✓ (3.7.1) The pawn may move forward to the square immediately in front of it
    on the same file, provided that this square is unoccupied, or
  - ✓ (3.7.2) on its first move the pawn may move as in 3.7.1 or alternatively
    it may advance two squares along the same file, provided that both squares
    are unoccupied, or
  - ✓ (3.7.3) the pawn may move to a square occupied by an opponent's piece
    diagonally in front of it on an adjacent file, capturing that piece.
    - ✓ (3.7.3.1) A pawn occupying a square on the same rank as and on an
      adjacent file to an opponent's pawn which has just advanced two squares in
      one move from its original square may capture this opponent's pawn as
      though the latter had been moved only one square.
    - ✓ (3.7.3.2) This capture is only legal on the move following this advance
      and is called an **en passant** capture.
    - ✗ (3.7.3.3)
      - ✗ When a player, having the move, plays a pawn to the rank furthest from
        its starting position, he/she must exchange that pawn as part of the
        same move for a new queen, rook, bishop or knight of the same colour on
        the intended square of arrival.
      - ✗ This is called the square of **promotion**.
    - ✗ (3.7.3.4) The player's choice is not restricted to pieces that have been
      captured previously.
    - ✗ (3.7.3.5) This exchange of a pawn for another piece is called promotion,
      and the effect of the new piece is immediate.
- ✗ (3.8) There are two different ways of moving the king:
  - ✓ (3.8.1) by moving to an adjoining square
  - ✓ (3.8.2)
    - ✓ by **castling**.
    - ✓ This is a move of the king and either rook of the same colour along the
      player's first rank, counting as a single move of the king and executed as
      follows: the king is transferred from its original square two squares
      towards the rook on its original square, then that rook is transferred to
      the square the king has just crossed.
    - ✓ (3.8.2.1) The right to castle has been lost:
      - ✓ If the king has already moved,
      - ✓ or with a rook that has already moved.
    - ✗ (3.8.2.2) Castling is prevented temporarily:
      - If the square on which the king stands, or the square which it must
        cross, or the square which it is to occupy, is attacked by one or more
        of the opponent's pieces,
      - ✓ or if there is any piece between the king and the rook with which
        castling is to be effected.
- ✗ (3.9) The king in check:
  - ✗ (3.9.1) The king is said to be 'in check' if it is attacked by one or more
    of the opponent's pieces, even if such pieces are constrained from moving to
    the square occupied by the king because they would then leave or place their
    own king in check.
  - ✗ (3.9.2) No piece can be moved that will either expose the king of the same
    colour to check or leave that king in check.
- ✗ (3.10) Legal and illegal moves; illegal positions:
  - ✗ (3.10.1) A move is legal when all the relevant requirements of Articles
    3.1 – 3.9 have been fulfilled.
  - ✗ (3.10.2) A move is illegal when it fails to meet the relevant requirements
    of Articles 3.1 – 3.9.
  - ✗ (3.10.3) A position is illegal when it cannot have been reached by any
    series of legal moves.

### Online Chess Rules

Only the first 5 articles are taken into account for now.

#### ✓ Article 1: Application of the FIDE Laws of Chess

- ✓ (1.1) Articles 1 – 3 of the Basic rules of play from the FIDE Laws of Chess
  are fully applied, except Article 2.1.
- ✓ (1.2) Article 2.1 of the Basic rules of play from the FIDE Laws of Chess is
  superseded by Article 3.1 of these Regulations.
- ✓ (1.3) Articles 4 and 5 of the Basic rules of play from the FIDE Laws of
  Chess are superseded by Articles 3 and 5 of these Regulations respectively.

#### ✓ Article 2: Playing Zone

- ✓ (2.1) Online chess games are played on a virtual chessboard.
- ✓ (2.2) The virtual chessboard shall be hosted by an online playing zone,
  usually an application or a website.
- ✓ (2.3) The list of moves shall be visible on the screen to the arbiter and
  both players throughout the game.
- ✓ (2.4) Each player is responsible for familiarising themselves with the
  playing zone's features and functionality.

#### ✗ Article 3: Moving the Pieces on the Virtual Chessboard

- ✓ (3.1)
  - ✓ The virtual chessboard is composed of an 8 x 8 grid of 64 equal squares
    alternately light (the **white** squares) and dark (the **black** squares).
  - ✓ The chessboard's right lower corner square is white.
- ✓ (3.2) Moves are made on the virtual chessboard, using a playing device,
  e.g., a computer with a mouse or a tablet.
- ✓ (3.3) The playing zone shall only accept legal moves.
- ✓ (3.4) The player having the move shall be allowed to use any technical means
  available on the playing zone to make his/her moves.
- ✓ (3.5) As a minimum, the playing zone must offer the possibility to select
  the source and target squares to make his/her move.
- ✗ (3.6) The following additional options may be activated and used by the
  player:
  - ✗ (3.6.a) Smart move: the player may play his/her move by selecting a single
    square when a unique move can be done involving that square.
  - ✗ (3.6.b)
    - ✗ Pre-move: the player enters his/her move before his/her opponent made
      his/her own move.
    - ✗ The move is automatically executed on the board as an immediate response
      to the opponent's move.
  - ✗ (3.6.c) Auto promotion to Queen: the player may set up the playing zone to
    force the promotion to a queen without being offered to choose the promoted
    piece.
  - ✗ (3.6.d) Move confirmation: the player may set up the playing zone to
    impose confirmation before their move is actually submitted to the game.
- ✗ (3.7) All moves and clock times after each move are automatically recorded
  by the playing zone and visible for both players.
- ✗ (3.8) If a player is unable to move the pieces, an assistant, who shall be
  acceptable to the arbiter, may be provided by the player to perform this
  operation.

#### ✗ Article 4: Virtual Chessclock

- ✗ (4.1) **Virtual chessclock** means the individual time displays of both
  players displayed by the playing zone.
- ✗ (4.2) When a player has made his/her move on the chessboard, his/her clock
  will automatically stop, and the opponent's clock will start.
- ✗ (4.3)
  - ✗ Each player must complete a minimum number of moves or all moves in an
    allotted period of time, including any additional amount of time with each
    move.
  - ✗ The competition regulations will specify these in advance.
- ✗ (4.4)
  - ✗ If a player does not complete the prescribed number of moves in the
    allotted time, the game is lost by that player.
  - ✗ However, the game is automatically drawn if the position is such that the
    opponent cannot checkmate the player's king by any possible series of legal
    moves.

#### ✗ Article 5: Completing the Game

- ✗ (5.1) The game is won by the player who has checkmated his/her opponent's
  king.
- ✗ (5.2) The game is won by the player whose opponent declares he/she resigns
  by pressing button “resign” or by another method available on the playing
  zone.
- ✗ (5.3)
  - ✗ The player can offer a draw in accordance with any method provided by the
    playing zone.
  - ✗ The offer cannot be withdrawn and remains valid until the opponent accepts
    it, rejects it by playing a move or the game is concluded in some other way.
- ✗ (5.4) The game is automatically drawn when:
  - ✗ (5.4.1) the same position appeared for the third time (as described in
    Article 9.2.2 of the FIDE Laws of Chess);
  - ✗ (5.4.2)
    - ✗ the player to move has no legal move and his/her king is not in check.
    - ✗ The game is said to end in **stalemate**;
  - ✗ (5.4.3) a position has arisen in which neither player can checkmate the
    opponent's king with any series of legal moves;
  - ✗ (5.4.4) the last 50 moves by each player have been completed without the
    movement of any pawn and without any capture.

## File formats

Another objective of this project is to allow exporting and replaying recorded
chess games, which is why some standard file formats will be handled by this
program.

### ✗ Portable Game Notation (PGN)

The PGN is a standard plain text format used for recording chess games. An
archived version of the
[PGN specification](http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm)
is used as a reference. The game will only partially support the standard for
now, and its priority is lower than implementing the actual game.

The FEN notation will probably be implemented first because it makes unit
testing a lot easier.

#### ✗ Movetext section

- ✗ (8.2) Movetext section
  - The movetext section is composed of chess moves, move number indications,
    optional annotations, and a single concluding game termination marker.
  - ✗ (8.2.1) Movetext line justification
  - ✗ (8.2.2) Movetext move number indications
    - ✗ (8.2.2.1) Import format move number indications
    - ✗ (8.2.2.2) Export format move number indications
  - ✗ (8.2.3) Movetext SAN (Standard Algebraic Notation)
    - ✓ (8.2.3.1) Square identification
      - ✓ SAN identifies each of the sixty four squares on the chessboard with a
        unique two character name.
      - ✓ The first character of a square identifier is the file of the square;
        a file is a column of eight squares designated by a single lower case
        letter from "a" (leftmost or queenside) up to and including "h"
        (rightmost or kingside).
      - ✓ The second character of a square identifier is the rank of the square;
        a rank is a row of eight squares designated by a single digit from "1"
        (bottom side [White's first rank]) up to and including "8" (top side
        [Black's first rank]).
    - ✓ (8.2.3.2) Piece identification
      - ✓ SAN identifies each piece by a single upper case letter.
      - ✓ The standard English values: pawn = "P", knight = "N", bishop = "B",
        rook = "R", queen = "Q", and king = "K".
    - ✗ (8.2.3.3) Basic SAN move construction
    - ✗ (8.2.3.4) Disambiguation
    - ✗ (8.2.3.5) Check and checkmate indication characters
    - ✗ (8.2.3.6) SAN move length
    - ✗ (8.2.3.7) Import and export SAN
    - ✗ (8.2.3.8) SAN move suffix annotations
  - ✗ (8.2.6) Game Termination Markers

### Forsyth-Edwards Notation (FEN)

- ✗ FEN is "Forsyth-Edwards Notation"; it is a standard for describing chess
  positions using the ASCII character set.
- ✗ A single FEN record uses one text line of variable length composed of six
  data fields.
- ✗ The first four fields of the FEN specification are the same as the first
  four fields of the EPD specification.
- ✗ A text file composed exclusively of FEN data records should have a file name
  with the suffix ".fen".
- ✗ (16.1.3) Data fields
  - ✗ FEN specifies the piece placement, the active color, the castling
    availability, the en passant target square, the halfmove clock, and the
    fullmove number.
  - ✗ These can all fit on a single text line in an easily read format.
  - ✗ A FEN description has six fields.
  - ✗ Each field is composed only of non-blank printing ASCII characters.
  - ✗ Adjacent fields are separated by a single ASCII space character.
  - ✗ (16.1.3.1) Piece placement data
    - ✗ The first field represents the placement of the pieces on the board.
    - ✗ The board contents are specified starting with the eighth rank and
      ending with the first rank.
    - ✗ For each rank, the squares are specified from file a to file h.
    - ✗ White pieces are identified by uppercase SAN piece letters ("PNBRQK")
      and black pieces are identified by lowercase SAN piece letters ("pnbrqk").
    - ✗ Empty squares are represented by the digits one through eight; the digit
      used represents the count of contiguous empty squares along a rank.
    - ✗ A solidus character "/" is used to separate data of adjacent ranks.
  - ✗ (16.1.3.2) Active color
    - ✗ The second field represents the active color.
    - ✗ A lower case "w" is used if White is to move; a lower case "b" is used
      if Black is the active player.
  - ✗ (16.1.3.3) Castling availability
    - ✗ The third field represents castling availability.
    - ✗ This indicates potential future castling that may of may not be possible
      at the moment due to blocking pieces or enemy attacks.
    - ✗ If there is no castling availability for either side, the single
      character symbol "-" is used.
    - ✗ Otherwise, a combination of from one to four characters are present.
    - ✗ If White has kingside castling availability, the uppercase letter "K"
      appears.
    - ✗ If White has queenside castling availability, the uppercase letter "Q"
      appears.
    - ✗ If Black has kingside castling availability, the lowercase letter "k"
      appears.
    - ✗ If Black has queenside castling availability, then the lowercase letter
      "q" appears.
    - ✗ Those letters which appear will be ordered first uppercase before
      lowercase and second kingside before queenside.
    - ✗ There is no white space between the letters.
  - ✗ (16.1.3.4) En passant target square
    - ✗ The fourth field is the en passant target square.
    - ✗ If there is no en passant target square then the single character symbol
      "-" appears.
    - ✗ If there is an en passant target square then is represented by a
      lowercase file character immediately followed by a rank digit.
    - ✗ Obviously, the rank digit will be "3" following a white pawn double
      advance (Black is the active color) or else be the digit "6" after a black
      pawn double advance (White being the active color).
    - ✗ An en passant target square is given if and only if the last move was a
      pawn advance of two squares.
    - ✗ Therefore, an en passant target square field may have a square name even
      if there is no pawn of the opposing side that may immediately execute the
      en passant capture.
  - ✗ (16.1.3.5) Halfmove clock
    - ✗ The fifth field is a nonnegative integer representing the halfmove
      clock.
    - ✗ This number is the count of halfmoves (or ply) since the last pawn
      advance or capturing move.
    - ✗ This value is used for the fifty move draw rule.
  - ✗ (16.1.3.6) Fullmove number
    - ✗ The sixth and last field is a positive integer that gives the fullmove
      number.
    - ✗ This will have the value "1" for the first move of a game for both White
      and Black.
    - ✗ It is incremented by one immediately after each move by Black.
