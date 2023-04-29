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

#### ✓ Article 1: The Nature and Objectives of the Game of Chess

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
  - ✓ (1.4.1)
    - ✗ The player who achieves this goal is said to have **checkmated** the
      opponent's king and to have won the game.
    - ✓ Leaving one's own king under attack, exposing one's own king to attack
      and also **capturing** the opponent's king is not allowed.
  - ✓ (1.4.2) The opponent whose king has been checkmated has lost the game.
  - ✓ (1.4.3) If the position is such that neither player can possibly checkmate
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
      - ✓ This is called the square of **promotion**.
    - ✓ (3.7.3.4) The player's choice is not restricted to pieces that have been
      captured previously.
    - ✓ (3.7.3.5) This exchange of a pawn for another piece is called promotion,
      and the effect of the new piece is immediate.
- ✓ (3.8) There are two different ways of moving the king:
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
    - ✓ (3.8.2.2) Castling is prevented temporarily:
      - ✓ If the square on which the king stands, or the square which it must
        cross, or the square which it is to occupy, is attacked by one or more
        of the opponent's pieces,
      - ✓ or if there is any piece between the king and the rook with which
        castling is to be effected.
- ✓ (3.9) The king in check:
  - ✓ (3.9.1) The king is said to be 'in check' if it is attacked by one or more
    of the opponent's pieces, even if such pieces are constrained from moving to
    the square occupied by the king because they would then leave or place their
    own king in check.
  - ✓ (3.9.2) No piece can be moved that will either expose the king of the same
    colour to check or leave that king in check.
- ✓ (3.10) Legal and illegal moves; illegal positions:
  - ✓ (3.10.1) A move is legal when all the relevant requirements of Articles
    3.1 – 3.9 have been fulfilled.
  - ✓ (3.10.2) A move is illegal when it fails to meet the relevant requirements
    of Articles 3.1 – 3.9.
  - ✓ (3.10.3) A position is illegal when it cannot have been reached by any
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

- ✓ (5.1) The game is won by the player who has checkmated his/her opponent's
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
  - ✓ (5.4.2)
    - ✓ the player to move has no legal move and his/her king is not in check.
    - ✓ The game is said to end in **stalemate**;
  - ✗ (5.4.3) a position has arisen in which neither player can checkmate the
    opponent's king with any series of legal moves;
  - ✗ (5.4.4) the last 50 moves by each player have been completed without the
    movement of any pawn and without any capture.

## File formats

Another objective of this project is to allow exporting and replaying recorded
chess games, which is why some standard file formats will be handled by this
program, for both parsing and serialization.

### ✗ Portable Game Notation (PGN)

The PGN is a standard plain text format used for recording chess games. An
archived version of the
[PGN specification](http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm)
is used as a reference. The game will only partially support the standard for
now, and its priority is lower than implementing the actual game.

The following parts will be implemented in the future:

- Import format
- Reduced export format
- Escape mechanisms
- Seven tag roster
- Movetext SAN

#### ✗ Formats: import and export

- ✓ There are two formats in the PGN specification.
- ✓ These are the "import" format and the "export" format.
- ✓ These are the two different ways of formatting the same PGN data according
  to its source.
- ✗ (3.1) Import format allows for manually prepared data
  - ✓ The import format is rather flexible and is used to describe data that may
    have been prepared by hand, much like a source file for a high level
    programming language.
  - ✗ A program that can read PGN data should be able to handle the somewhat lax
    import format.
- ✗ (3.2) Export format used for program generated output
  - ✗ The export format is rather strict and is used to describe data that is
    usually prepared under program control, something like a pretty printed
    source program reformatted by a compiler.
  - (3.2.1) Byte equivalence
    - ✗ For a given PGN data file, export format representations generated by
      different PGN programs on the same computing system should be exactly
      equivalent, byte for byte.
  - (3.2.2) Archival storage and the newline character
    - ✗ Export format should also be used for archival storage.
    - ✗ The only extra requirement for archival storage is that the newline
      character have a specific representation that is independent of its value
      for a particular computing system's text file usage.
    - ✗ The archival representation of a newline is the ASCII control character
      LF (line feed, decimal value 10, hexadecimal value 0x0a).
  - ✗ (3.2.4) Reduced export format
    - ✗ A PGN game represented using export format is said to be in "reduced
      export format" if all of the following hold:
      - ✗ it has no commentary,
      - ✗ it has only the standard seven tag roster identification information
        ("STR", see below),
      - ✗ it has no recursive annotation variations ("RAV", see below), and
      - ✗ it has no numeric annotation glyphs ("NAG", see below).
    - ✗ Reduced export format is used for bulk storage of unannotated games.
    - ✗ It represents a minimum level of standard conformance for a PGN
      exporting application.

#### Lexicographical issues

- ✗ PGN data is composed of characters; non-overlapping contiguous sequences of
  characters form lexical tokens.
- (4.1) Character codes
  - ✗ PGN data is represented using a subset of the eight bit ISO 8859/1
    (Latin 1) character set.
  - ✗ This set is also known as ECMA-94 and is similar to other ISO Latin
    character sets.
  - ✗ ISO 8859/1 includes the standard seven bit ASCII character set for the 32
    control character code values from zero to 31.
  - ✗ The 95 printing character code values from 32 to 126 are also equivalent
    to seven bit ASCII usage.
  - ✗ Code value 127, the ASCII DEL control character, is a graphic character in
    ISO 8859/1; it is not used for PGN data representation.
  - ✗ The 32 ISO 8859/1 code values from 128 to 159 are non-printing control
    characters.
  - ✗ They are not used for PGN data representation.
  - ✗ Finally, the 64 code values from 192 to 255 are mostly alphabetic printing
    characters with various diacritical marks; their use is encouraged for those
    languages that require such characters.
  - ✗ The graphic representations of this last set of 64 characters is fairly
    constant for the ISO Latin family.
  - ✗ Printing character codes outside of the seven bit ASCII range may only
    appear in string data and in commentary.
  - ✗ They are not permitted for use in symbol construction.
  - ✗ Only four of the ASCII control characters are permitted in PGN import
    format; these are the horizontal and vertical tabs along with the linefeed
    and carriage return codes.
  - ✗ The external representation of the newline character may differ among
    platforms; this is an acceptable variation as long as the details of the
    implementation are hidden from software implementors and users.
  - ✗ When a choice is practical, the Unix "newline is linefeed" convention is
    preferred.
- (4.2) Tab characters
  - ✗ Tab characters, both horizontal and vertical, are not permitted in the
    export format.
  - ✗ This is because the treatment of tab characters is highly dependent upon
    the particular software in use on the host computing system.
  - ✗ Also, tab characters may not appear inside of string data.
- (4.3) Line lengths
  - ✗ PGN data are organized as simple text lines without any special bytes or
    markers for secondary record structure imposed by specific operating
    systems.
  - ✗ Import format PGN text lines are limited to having a maximum of 255
    characters per line including the newline character.
  - ✗ Lines with 80 or more printing characters are strongly discouraged because
    of the difficulties experienced by common text editors with long lines.
  - ✗ In some cases, very long tag values will require 80 or more columns, but
    these are relatively rare.
  - ✗ An example of this is the "FEN" tag pair; it may have a long tag value,
    but this particular tag pair is only used to represent a game that doesn't
    start from the usual initial position.

#### ✗ Escape mechanism

- ✗ There is a special escape mechanism for PGN data.
- ✗ This mechanism is triggered by a percent sign character ("%") appearing in
  the first column of a line; the data on the rest of the line is ignored by
  publicly available PGN scanning software.
- ✗ This escape convention is intended for the private use of software
  developers and researchers to embed non-PGN commands and data in PGN streams.
- ✗ A percent sign appearing in any other place other than the first position in
  a line does not trigger the escape mechanism.

#### ✗ Tokens

- ✗ PGN character data is organized as tokens.
- ✗ A token is a contiguous sequence of characters that represents a basic
  semantic unit.
- ✗ Tokens may be separated from adjacent tokens by white space characters.
- ✗ White space characters include space, newline, and tab characters.
- ✗ Some tokens are self delimiting and do not require white space characters.
- ✗ A string token is a sequence of zero or more printing characters delimited
  by a pair of quote characters (ASCII decimal value 34, hexadecimal value
  0x22).
- ✗ An empty string is represented by two adjacent quotes.
- ✗ Note: an apostrophe is not a quote.
- ✗ A quote inside a string is represented by the backslash immediately followed
  by a quote.
- ✗ A backslash inside a string is represented by two adjacent backslashes.
- ✗ Strings are commonly used as tag pair values (see below).
- ✗ Non-printing characters like newline and tab are not permitted inside of
  strings.
- ✗ A string token is terminated by its closing quote.
- ✗ Currently, a string is limited to a maximum of 255 characters of data.
- ✗ An integer token is a sequence of one or more decimal digit characters.
- ✗ It is a special case of the more general "symbol" token class described
  below.
- ✗ Integer tokens are used to help represent move number indications (see
  below).
- ✗ An integer token is terminated just prior to the first non-symbol character
  following the integer digit sequence.
- ✗ A period character (".") is a token by itself.
- ✗ It is used for move number indications (see below).
- ✗ It is self terminating.
- ✗ An asterisk character ("\*") is a token by itself.
- ✗ It is used as one of the possible game termination markers (see below); it
  indicates an incomplete game or a game with an unknown or otherwise
  unavailable result.
- ✗ It is self terminating.
- ✗ The left and right bracket characters ("[" and "]") are tokens.
- ✗ They are used to delimit tag pairs (see below).
- ✗ Both are self terminating.
- ✗ The left and right parenthesis characters ("(" and ")") are tokens.
- ✗ They are used to delimit Recursive Annotation Variations (see below).
- ✗ Both are self terminating.
- ✗ The left and right angle bracket characters ("<" and ">") are tokens.
- ✗ They are reserved for future expansion.
- ✗ Both are self terminating.
- ✗ A Numeric Annotation Glyph ("NAG", see below) is a token; it is composed of
  a dollar sign character ("$") immediately followed by one or more digit
  characters.
- ✗ It is terminated just prior to the first non-digit character following the
  digit sequence.
- ✗ A symbol token starts with a letter or digit character and is immediately
  followed by a sequence of zero or more symbol continuation characters.
- ✗ These continuation characters are letter characters ("A-Za-z"), digit
  characters ("0-9"), the underscore ("\_"), the plus sign ("+"), the octothorpe
  sign ("#"), the equal sign ("="), the colon (":"), and the hyphen ("-").
- ✗ Symbols are used for a variety of purposes.
- ✗ All characters in a symbol are significant.
- ✗ A symbol token is terminated just prior to the first non-symbol character
  following the symbol character sequence.
- ✗ Currently, a symbol is limited to a maximum of 255 characters in length.

#### ✗ Parsing games

- ✗ A PGN database file is a sequential collection of zero or more PGN games.
- ✗ An empty file is a valid, although somewhat uninformative, PGN database.
- ✗ A PGN game is composed of two sections.
- ✗ The first is the tag pair section and the second is the movetext section.
- ✗ The tag pair section provides information that identifies the game by
  defining the values associated with a set of standard parameters.
- ✗ The movetext section gives the usually enumerated and possibly annotated
  moves of the game along with the concluding game termination marker.
- ✗ The chess moves themselves are represented using SAN (Standard Algebraic
  Notation), also described later in this document.

##### ✗ Tag pair section

- ✗ The tag pair section is composed of a series of zero or more tag pairs.
- ✗ A tag pair is composed of four consecutive tokens: a left bracket token, a
  symbol token, a string token, and a right bracket token.
- ✗ The symbol token is the tag name and the string token is the tag value
  associated with the tag name.
- ✗ The same tag name should not appear more than once in a tag pair section.
- ✗ A further restriction on tag names is that they are composed exclusively of
  letters, digits, and the underscore character.
- ✗ This is done to facilitate mapping of tag names into key and attribute names
  for use with general purpose database programs.
- ✗ For PGN import format, there may be zero or more white space characters
  between any adjacent pair of tokens in a tag pair.
- ✗ For PGN export format, there are no white space characters between the left
  bracket and the tag name, there are no white space characters between the tag
  value and the right bracket, and there is a single space character between the
  tag name and the tag value.
- ✗ Tag names, like all symbols, are case sensitive.
- ✗ All tag names used for archival storage begin with an upper case letter.
- ✗ PGN import format may have multiple tag pairs on the same line and may even
  have a tag pair spanning more than a single line.
- ✗ Export format requires each tag pair to appear left justified on a line by
  itself; a single empty line follows the last tag pair.
- ✗ Some tag values may be composed of a sequence of items.
- ✗ For example, a consultation game may have more than one player for a given
  side.
- ✗ When this occurs, the single character ":" (colon) appears between adjacent
  items.
- ✗ Because of this use as an internal separator in strings, the colon should
  not otherwise appear in a string.
- ✗ The tag pair format is designed for expansion; initially only strings are
  allowed as tag pair values.
- ✗ Tag value formats associated with the STR (Seven Tag Roster, see below) will
  not change; they will always be string values.
- ✗ However, there are long term plans to allow general list structures as tag
  values for non-STR tag pairs.
- ✗ Use of these expanded tag values will likely be restricted to special
  research programs.
- ✗ In all events, the top level structure of a tag pair remains the same: left
  bracket, tag name, tag value, and right bracket.
- ✗ (8.1.1) Seven Tag Roster
  - ✗ There is a set of tags defined for mandatory use for archival storage of
    PGN data.
  - ✗ This is the STR (Seven Tag Roster).
  - ✗ The interpretation of these tags is fixed as is the order in which they
    appear.
  - ✗ Although the definition and use of additional tag names and semantics is
    permitted and encouraged when needed, the STR is the common ground that all
    programs should follow for public data interchange.
  - ✗ For import format, the order of tag pairs is not important.
  - ✗ For export format, the STR tag pairs appear before any other tag pairs.
  - ✗ The STR tag pairs must also appear in order; this order is described
    below.
  - ✗ Also for export format, any additional tag pairs appear in ASCII order by
    tag name.
  - ✗ For PGN export format, a single blank line appears after the last of the
    tag pairs to conclude the tag pair section.
  - ✗ This helps simple scanning programs to quickly determine the end of the
    tag pair section and the beginning of the movetext section.
  - ✗ (8.1.1.1) The Event tag
    - ✗ The Event tag value should be reasonably descriptive.
    - ✗ Abbreviations are to be avoided unless absolutely necessary.
    - ✗ A consistent event naming should be used to help facilitate database
      scanning.
    - ✗ If the name of the event is unknown, a single question mark should
      appear as the tag value.
  - ✗ (8.1.1.2) The Site tag
    - The Site tag value should include city and region names along with a
      standard name for the country.
    - ✗ The use of the IOC (International Olympic Committee) three letter names
      is suggested for those countries where such codes are available.
    - ✗ If the site of the event is unknown, a single question mark should
      appear as the tag value.
    - ✗ A comma may be used to separate a city from a region.
    - ✗ No comma is needed to separate a city or region from the IOC country
      code.
    - ✗ A later section of this document gives a list of three letter nation
      codes along with a few additions for "locations" not covered by the IOC.
  - ✗ (8.1.1.3) The Date tag
    - ✗ The Date tag value gives the starting date for the game.
    - ✗ Note: this is not necessarily the same as the starting date for the
      event.
    - ✗ The date is given with respect to the local time of the site given in
      the Event tag.
    - ✗ The Date tag value field always uses a standard ten character format:
      "YYYY.MM.DD".
    - ✗ The first four characters are digits that give the year, the next
      character is a period, the next two characters are digits that give the
      month, the next character is a period, and the final two characters are
      digits that give the day of the month.
    - ✗ If the any of the digit fields are not known, then question marks are
      used in place of the digits.
  - ✗ (8.1.1.4) The Round Tag - ✗ The Round tag value gives the playing round
    for the game. - ✗ In a match competition, this value is the number of the
    game played. - ✗ If the use of a round number is inappropriate, then the
    field should be a single hyphen character. - ✗ If the round is unknown, a
    single question mark should appear as the tag value. - ✗ Some organizers
    employ unusual round designations and have multipart playing rounds and
    sometimes even have conditional rounds. - ✗ In these cases, a multipart
    round identifier can be made from a sequence of integer round numbers
    separated by periods. - ✗ The leftmost integer represents the most
    significant round and succeeding integers represent round numbers in
    descending hierarchical order.
  - ✗ (8.1.1.5) The White tag
    - ✗ The White tag value is the name of the player or players of the white
      pieces.
    - ✗ The names are given as they would appear in a telephone directory.
    - ✗ The family or last name appears first.
    - ✗ If a first name or first initial is available, it is separated from the
      family name by a comma and a space.
    - ✗ Finally, one or more middle initials may appear.
    - ✗ Wherever a comma appears, the very next character should be a space.
    - ✗ Wherever an initial appears, the very next character should be a period.
    - ✗ If the name is unknown, a single question mark should appear as the tag
      value.
    - ✗ The intent is to allow meaningful ASCII sorting of the tag value that is
      independent of regional name formation customs.
    - ✗ If more than one person is playing the white pieces, the names are
      listed in alphabetical order and are separated by the colon character
      between adjacent entries.
    - ✗ A player who is also a computer program should have appropriate version
      information listed after the name of the program.
    - ✗ The format used in the FIDE Rating Lists is appropriate for use for
      player name tags.
  - ✗ (8.1.1.6) The Black tag
    - ✗ The Black tag value is the name of the player or players of the black
      pieces.
    - ✗ The names are given here as they are for the White tag value.
  - ✗ (8.1.1.7) The Result tag
    - ✗ The Result field value is the result of the game.
    - ✗ It is always exactly the same as the game termination marker that
      concludes the associated movetext.
    - ✗ It is always one of four possible values: "1-0" (White wins), "0-1"
      (Black wins), "1/2-1/2" (drawn game), and "\*" (game still in progress,
      game abandoned, or result otherwise unknown).
    - ✗ Note that the digit zero is used in both of the first two cases; not the
      letter "O".

##### ✗ Movetext section

- ✗ The movetext section is composed of chess moves, move number indications,
  optional annotations, and a single concluding game termination marker.
- ✗ Because illegal moves are not real chess moves, they are not permitted in
  PGN movetext.
- ✗ (8.2.1) Movetext line justification
  - ✗ In PGN import format, tokens in the movetext do not require any specific
    line justification.
  - ✗ In PGN export format, tokens in the movetext are placed left justified on
    successive text lines each of which has less than 80 printing characters.
  - ✗ As many tokens as possible are placed on a line with the remainder
    appearing on successive lines.
  - ✗ A single space character appears between any two adjacent symbol tokens on
    the same line in the movetext.
  - ✗ As with the tag pair section, a single empty line follows the last line of
    data to conclude the movetext section.
  - ✗ Neither the first or the last character on an export format PGN line is a
    space.
- ✗ (8.2.2) Movetext move number indications
  - ✗ A move number indication is composed of one or more adjacent digits (an
    integer token) followed by zero or more periods.
  - ✗ The integer portion of the indication gives the move number of the
    immediately following white move (if present) and also the immediately
    following black move (if present).
  - ✗ (8.2.2.1) Import format move number indications
    - ✗ PGN import format does not require move number indications.
    - ✗ It does not prohibit superfluous move number indications anywhere in the
      movetext as long as the move numbers are correct.
    - ✗ PGN import format move number indications may have zero or more period
      characters following the digit sequence that gives the move number; one or
      more white space characters may appear between the digit sequence and the
      period(s).
  - ✗ (8.2.2.2) Export format move number indications
    - ✗ There are two export format move number indication formats, one for use
      appearing immediately before a white move element and one for use
      appearing immediately before a black move element.
    - ✗ A white move number indication is formed from the integer giving the
      fullmove number with a single period character appended.
    - ✗ A black move number indication is formed from the integer giving the
      fullmove number with three period characters appended.
    - ✗ All white move elements have a preceding move number indication.
    - ✗ A black move element has a preceding move number indication only in two
      cases:
      - ✗ first, if there is intervening annotation or commentary between the
        black move and the previous white move;
      - ✗ and second, if there is no previous white move in the special case
        where a game starts from a position where Black is the active player.
    - ✗ There are no other cases where move number indications appear in PGN
      export format.
- ✗ (8.2.3) Movetext SAN (Standard Algebraic Notation)
  - ✗ SAN (Standard Algebraic Notation) is a representation standard for chess
    moves using the ASCII Latin alphabet.
  - ✗ An alternative to SAN is FAN (Figurine Algebraic Notation).
  - ✗ FAN uses miniature piece icons instead of single letter piece
    abbreviations.
  - ✗ The two notations are otherwise identical.
  - ✓ (8.2.3.1) Square identification
    - ✓ SAN identifies each of the sixty four squares on the chessboard with a
      unique two character name.
    - ✓ The first character of a square identifier is the file of the square; a
      file is a column of eight squares designated by a single lower case letter
      from "a" (leftmost or queenside) up to and including "h" (rightmost or
      kingside).
    - ✓ The second character of a square identifier is the rank of the square; a
      rank is a row of eight squares designated by a single digit from "1"
      (bottom side [White's first rank]) up to and including "8" (top side
      [Black's first rank]).
  - ✓ (8.2.3.2) Piece identification
    - ✓ SAN identifies each piece by a single upper case letter.
    - ✓ The standard English values: pawn = "P", knight = "N", bishop = "B",
      rook = "R", queen = "Q", and king = "K".
    - ✓ The letter code for a pawn is not used for SAN moves in PGN export
      format movetext.
    - ✗ However, some PGN import software disambiguation code may allow for the
      appearance of pawn letter codes.
    - Also, pawn and other piece letter codes are needed for use in some tag
      pair and annotation constructs.
  - ✓ (8.2.3.3) Basic SAN move construction
    - ✓ A basic SAN move is given by listing the moving piece letter (omitted
      for pawns) followed by the destination square.
    - ✓ Capture moves are denoted by the lower case letter "x" immediately prior
      to the destination square;
    - ✓ pawn captures include the file letter of the originating square of the
      capturing pawn immediately prior to the "x" character.
    - ✓ SAN kingside castling is indicated by the sequence "O-O";
    - ✓ queenside castling is indicated by the sequence "O-O-O".
    - ✓ Note that the upper case letter "O" is used, not the digit zero.
    - ✓ The use of a zero character is not only incompatible with traditional
      text practices, but it can also confuse parsing algorithms which also have
      to understand about move numbers and game termination markers.
    - ✓ Also note that the use of the letter "O" is consistent with the practice
      of having all chess move symbols start with a letter; also, it follows the
      convention that all non-pawn move symbols start with an upper case letter.
    - ✓ En passant captures do not have any special notation; they are formed as
      if the captured pawn were on the capturing pawn's destination square.
    - ✓ Pawn promotions are denoted by the equal sign "=" immediately following
      the destination square with a promoted piece letter (indicating one of
      knight, bishop, rook, or queen) immediately following the equal sign.
    - ✓ As above, the piece letter is in upper case.
  - ✗ (8.2.3.4) Disambiguation
    - ✗ In the case of ambiguities (multiple pieces of the same type moving to
      the same square), the first appropriate disambiguating step of the three
      following steps is taken:
      - ✗ First, if the moving pieces can be distinguished by their originating
        files, the originating file letter of the moving piece is inserted
        immediately after the moving piece letter.
      - ✗ Second (when the first step fails), if the moving pieces can be
        distinguished by their originating ranks, the originating rank digit of
        the moving piece is inserted immediately after the moving piece letter.
      - ✗ Third (when both the first and the second steps fail), the two
        character square coordinate of the originating square of the moving
        piece is inserted immediately after the moving piece letter.
    - ✗ Note that the above disambiguation is needed only to distinguish among
      moves of the same piece type to the same square; it is not used to
      distinguish among attacks of the same piece type to the same square.
  - ✗ (8.2.3.5) Check and checkmate indication characters
    - ✓ If the move is a checking move, the plus sign "+" is appended as a
      suffix to the basic SAN move notation;
    - ✓ if the move is a checkmating move, the octothorpe sign "#" is appended
      instead.
    - ✗ Neither the appearance nor the absence of either a check or checkmating
      indicator is used for disambiguation purposes.
    - ✗ This means that if two (or more) pieces of the same type can move to the
      same square the differences in checking status of the moves does not
      allieviate the need for the standard rank and file disabiguation described
      above.
    - ✗ Note that a difference in checking status for the above may occur only
      in the case of a discovered check.
    - ✗ Neither the checking or checkmating indicators are considered annotation
      as they do not communicate subjective information.
    - ✗ Therefore, they are qualitatively different from move suffix annotations
      like "!" and "?".
    - ✗ Subjective move annotations are handled using Numeric Annotation Glyphs
      as described in a later section of this document.
    - ✗ There are no special markings used for double checks or discovered
      checks.
    - ✗ There are no special markings used for drawing moves.
  - ✗ (8.2.3.7) Import and export SAN
    - ✗ PGN export format always uses the above canonical SAN to represent moves
      in the movetext section of a PGN game.
    - ✗ Import format is somewhat more relaxed and it makes allowances for moves
      that do not conform exactly to the canonical format.
    - ✗ However, these allowances may differ among different PGN reader
      programs.
    - ✗ Only data appearing in export format is in all cases guaranteed to be
      importable into all PGN readers.
    - ✗ There are a number of suggested guidelines for use with implementing PGN
      reader software for permitting non-canonical SAN move representation.
    - ✗ The idea is to have a PGN reader apply various transformations to
      attempt to discover the move that is represented by non-canonical input.
    - ✗ Some suggested transformations include: letter case remapping, capture
      indicator insertion, check indicator insertion, and checkmate indicator
      insertion.
- ✗ (8.2.6) Game Termination Markers
  - ✗ Each movetext section has exactly one game termination marker;
  - ✗ the marker always occurs as the last element in the movetext.
  - ✗ The game termination marker is a symbol that is one of the following four
    values: "1-0" (White wins), "0-1" (Black wins), "1/2-1/2" (drawn game), and
    "\*" (game in progress, result unknown, or game abandoned).
  - ✗ Note that the digit zero is used in the above; not the upper case letter
    "O".
  - ✗ The game termination marker appearing in the movetext of a game must match
    the value of the game's Result tag pair.
  - ✗ While the marker appears as a string in the Result tag, it appears as a
    symbol without quotes in the movetext.

#### ✗ Forsyth-Edwards Notation (FEN)

- ✓ FEN is "Forsyth-Edwards Notation"; it is a standard for describing chess
  positions using the ASCII character set.
- ✓ A single FEN record uses one text line of variable length composed of six
  data fields.
- ✓ The first four fields of the FEN specification are the same as the first
  four fields of the EPD specification.
- ✗ A text file composed exclusively of FEN data records should have a file name
  with the suffix ".fen".
- ✓ (16.1.3) Data fields
  - ✓ FEN specifies the piece placement, the active color, the castling
    availability, the en passant target square, the halfmove clock, and the
    fullmove number.
  - ✓ These can all fit on a single text line in an easily read format.
  - ✓ A FEN description has six fields.
  - ✓ Each field is composed only of non-blank printing ASCII characters.
  - ✓ Adjacent fields are separated by a single ASCII space character.
  - ✓ (16.1.3.1) Piece placement data
    - ✓ The first field represents the placement of the pieces on the board.
    - ✓ The board contents are specified starting with the eighth rank and
      ending with the first rank.
    - ✓ For each rank, the squares are specified from file a to file h.
    - ✓ White pieces are identified by uppercase SAN piece letters ("PNBRQK")
      and black pieces are identified by lowercase SAN piece letters ("pnbrqk").
    - ✓ Empty squares are represented by the digits one through eight; the digit
      used represents the count of contiguous empty squares along a rank.
    - ✓ A solidus character "/" is used to separate data of adjacent ranks.
  - ✓ (16.1.3.2) Active color
    - ✓ The second field represents the active color.
    - ✓ A lower case "w" is used if White is to move; a lower case "b" is used
      if Black is the active player.
  - ✓ (16.1.3.3) Castling availability
    - ✓ The third field represents castling availability.
    - ✓ This indicates potential future castling that may of may not be possible
      at the moment due to blocking pieces or enemy attacks.
    - ✓ If there is no castling availability for either side, the single
      character symbol "-" is used.
    - ✓ Otherwise, a combination of from one to four characters are present.
    - ✓ If White has kingside castling availability, the uppercase letter "K"
      appears.
    - ✓ If White has queenside castling availability, the uppercase letter "Q"
      appears.
    - ✓ If Black has kingside castling availability, the lowercase letter "k"
      appears.
    - ✓ If Black has queenside castling availability, then the lowercase letter
      "q" appears.
    - ✓ Those letters which appear will be ordered first uppercase before
      lowercase and second kingside before queenside.
    - ✓ There is no white space between the letters.
  - ✓ (16.1.3.4) En passant target square
    - ✓ The fourth field is the en passant target square.
    - ✓ If there is no en passant target square then the single character symbol
      "-" appears.
    - ✓ If there is an en passant target square then is represented by a
      lowercase file character immediately followed by a rank digit.
    - ✓ Obviously, the rank digit will be "3" following a white pawn double
      advance (Black is the active color) or else be the digit "6" after a black
      pawn double advance (White being the active color).
    - ✓ An en passant target square is given if and only if the last move was a
      pawn advance of two squares.
    - ✓ Therefore, an en passant target square field may have a square name even
      if there is no pawn of the opposing side that may immediately execute the
      en passant capture.
  - ✗ (16.1.3.5) Halfmove clock
    - ✓ The fifth field is a nonnegative integer representing the halfmove
      clock.
    - ✓ This number is the count of halfmoves (or ply) since the last pawn
      advance or capturing move.
    - ✗ This value is used for the fifty move draw rule.
  - ✓ (16.1.3.6) Fullmove number
    - ✓ The sixth and last field is a positive integer that gives the fullmove
      number.
    - ✓ This will have the value "1" for the first move of a game for both White
      and Black.
    - ✓ It is incremented by one immediately after each move by Black.
