data class Coordinate(var x: Int, var y: Int)


fun main() {
    var isGameOver = false
    var isXTurn = true
    var board = Array(6) {Array(6) {' '}}

    // Setting up the starting pieces in the center of the board
    board[2][2] = 'x'
    board[3][3] = 'x'
    board[3][2] = 'o'
    board[2][3] = 'o'

    // Run the game loop until the game is over
    while (!isGameOver) {
        displayBoard(board)
        requestInput(board, isXTurn)
        isGameOver = checkGameOver(board)
        isXTurn = !isXTurn
    }

    // Display the board a final time, determine who won and state who won
    displayBoard(board)
    determineWinner(board)
}


// Displays the board
fun displayBoard(board: Array<Array<Char>>) {

    // Print spaces to "Clear" the terminal
    println("\n\n\n\n\n\n\n\n\n")

    // Print the board
    println("   A  B  C  D  E  F")
    println("1  ${board[0][0]}  ${board[0][1]}  ${board[0][2]}  ${board[0][3]}  ${board[0][4]}  ${board[0][5]}")
    println("2  ${board[1][0]}  ${board[1][1]}  ${board[1][2]}  ${board[1][3]}  ${board[1][4]}  ${board[1][5]}")
    println("3  ${board[2][0]}  ${board[2][1]}  ${board[2][2]}  ${board[2][3]}  ${board[2][4]}  ${board[2][5]}")
    println("4  ${board[3][0]}  ${board[3][1]}  ${board[3][2]}  ${board[3][3]}  ${board[3][4]}  ${board[3][5]}")
    println("5  ${board[4][0]}  ${board[4][1]}  ${board[4][2]}  ${board[4][3]}  ${board[4][4]}  ${board[4][5]}")
    println("6  ${board[5][0]}  ${board[5][1]}  ${board[5][2]}  ${board[5][3]}  ${board[5][4]}  ${board[5][5]}\n\n")
}



// Requests a coordinate to place a piece and acts accordingly
fun requestInput(board: Array<Array<Char>>, isXTurn: Boolean) {
    var wasCoordinateValid = false

    // Requests a coordinate from the user to place a piece until a valid one is provided
    while (wasCoordinateValid == false) {
        // Request coordinate from user
        if (isXTurn)
            println("\nX's Turn - Please enter a coordinate to place a piece: ")
        else
            println("\nO's Turn - Please enter a coordinate to place a piece: ")

        // Read in user's input
        var userInput: String = readln()

        // Ensure input is a coordinate with a col and row (alpha and numerical)
        if (userInput.length == 2) {
            if ((userInput[0].isLetter() && userInput[1].isDigit()) || (userInput[1].isLetter() && userInput[0].isDigit())) {
                wasCoordinateValid = placePiece(board, inputToCoordinate(userInput), isXTurn)
            }
        }

        if (!wasCoordinateValid)
            println("\nERROR - Invalid Coordinate")
    }
}



// Determines which player won the game
fun determineWinner(board: Array<Array<Char>>) {
    var xCount = 0
    var oCount = 0

    // Counts the number of each player's pieces on the board
    for (row in 0..5) {
        for (col in 0..5) {
            if (board[row][col] == 'x')
                xCount++
            else if (board[row][col] == 'o')
                oCount++
        }
    }

    // Display the winner
    if (xCount > oCount)
        println("\nPlayer X Wins!")
    else if (xCount < oCount)
        println("\nPlayer O Wins!")
    else
        println("\nThe Game Is A Tie!")
}



// Determines whether or not the game is over (Board filled with pieces)
fun checkGameOver(board: Array<Array<Char>>) : Boolean  {
    var anyEmptySpaces = false

    // Check if there are any empty spaces on the board (AKA, if game is still active)
    for (row in 0..5) {
        for (col in 0..5) {
            if (board[row][col] == ' ')
                anyEmptySpaces = true
        }
    }

    // The game is over if the board is filled
    if (anyEmptySpaces == true)
        return false
    else
        return true
}



// Attempts to place a piece in the given location
fun placePiece(board: Array<Array<Char>>, coordinate: Coordinate, isXTurn: Boolean) : Boolean {   
    var didOutflank = false
    var pieceTypePlaced = '-'
    var enemyPieceType = '-'
    var tempXPos = 0
    var tempYPos = 0
    
    // Return immediently if the coordinate is out of bounds of the board or the spot is taken
    if (coordinate.x < 0 || coordinate.x >= 6 || coordinate.y < 0 || coordinate.y >= 6 || board[coordinate.x][coordinate.y] != ' ')
        return false

    // Place the piece
    if (isXTurn) {
        board[coordinate.x][coordinate.y] = 'x'
        pieceTypePlaced = 'x'
        enemyPieceType = 'o'
    }
    else {
        board[coordinate.x][coordinate.y] = 'o'
        pieceTypePlaced = 'o'
        enemyPieceType = 'x'
    }
    

    //********************************************
    //****** Check Outflanking to the RIGHT ****** 
    //********************************************
    tempXPos = coordinate.x
    tempYPos = coordinate.y + 1

    // If next the next piece RIGHT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempYPos < 6 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempYPos++

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempYPos < 6 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the RIGHT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempYPos--

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //****** Check Outflanking to the LEFT ****** 
    //*******************************************
    tempXPos = coordinate.x
    tempYPos = coordinate.y - 1

    // If next the next piece LEFT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempYPos >= 0 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempYPos--

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempYPos >= 0 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the LEFT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempYPos++

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //******     Check Outflanking UP      ****** 
    //*******************************************
    tempXPos = coordinate.x - 1
    tempYPos = coordinate.y

    // If next the next piece UP is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos >= 0 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos--

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos >= 0 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the UP, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos++

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //******    Check Outflanking DOWN     ****** 
    //*******************************************
    tempXPos = coordinate.x + 1
    tempYPos = coordinate.y

    // If next the next piece DOWN is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos < 6 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos++

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos < 6 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the DOWN, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos--

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //** Check Outflanking diagional UP/RIGHT  **
    //*******************************************
    tempXPos = coordinate.x - 1
    tempYPos = coordinate.y + 1

    // If next the next piece UP/RIGHT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos >= 0 && tempYPos < 6 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos--
        tempYPos++

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos >= 0 && tempYPos < 6 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the UP/RIGHT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos++
        tempYPos--

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //** Check Outflanking diagional UP/LEFT  **
    //*******************************************
    tempXPos = coordinate.x - 1
    tempYPos = coordinate.y - 1

    // If next the next piece UP/LEFT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos >= 0 && tempYPos >= 0 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos--
        tempYPos--

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos >= 0 && tempYPos >= 0 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the UP/LEFT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos++
        tempYPos++

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //*******************************************
    //** Check Outflanking diagional DOWN/LEFT **
    //*******************************************
    tempXPos = coordinate.x + 1
    tempYPos = coordinate.y - 1

    // If next the next piece DOWN/LEFT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos < 6 && tempYPos > 0 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos++
        tempYPos--

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos < 6 && tempYPos > 0 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the DOWN/LEFT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos--
        tempYPos++

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }


    //********************************************
    //** Check Outflanking diagional DOWN/RIGHT **
    //********************************************
    tempXPos = coordinate.x + 1
    tempYPos = coordinate.y + 1

    // If next the next piece DOWN/RIGHT is an enemy piece, check the pieces after
    // for another of the current player's pieces, indicating an outflanking
    while (tempXPos < 6 && tempYPos < 6 && board[tempXPos][tempYPos] == enemyPieceType) {
        tempXPos++
        tempYPos++

        // If theres another of the current player's pieces, then they outflanked their opponent
        if (tempXPos < 6 && tempYPos < 6 && board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = true
    }

    // If the placed piece outflanks opponent's pieces to the DOWN/RIGHT, then set them to the current player's pieces
    while (didOutflank == true) {
        tempXPos--
        tempYPos--

        // If the piece is from the current player, then the outflanking is finished, otherwise they take control of the piece 
        if (board[tempXPos][tempYPos] == pieceTypePlaced)
            didOutflank = false
        else
            board[tempXPos][tempYPos] = pieceTypePlaced
    }

    // Update the board display
    displayBoard(board)

    // The piece was successfuly placed in a valid location
    return true
}



// Converts an input coordinate (string) to a real Coordinate object
fun inputToCoordinate(userInput: String) : Coordinate {
    var coordinate = Coordinate(-1, -1)
    
    // Convert the user's input into an actual Coordinate object
    for (char in userInput) {
        // Convert alphabetical character to a col position (y)
        if (char.isLetter()) {
            if (char.uppercaseChar() == 'A')
                coordinate.y = 0
            else if (char.uppercaseChar() == 'B')
                coordinate.y = 1
            else if (char.uppercaseChar() == 'C')
                coordinate.y = 2
            else if (char.uppercaseChar() == 'D')
                coordinate.y = 3
            else if (char.uppercaseChar() == 'E')
                coordinate.y = 4
            else if (char.uppercaseChar() == 'F')
                coordinate.y = 5
        }

        // Convert digit character to a row position (x)
        else if (char.isDigit()) {
            if (char == '1')
                coordinate.x = 0
            else if (char == '2')
                coordinate.x = 1
            else if (char == '3')
                coordinate.x = 2
            else if (char == '4')
                coordinate.x = 3
            else if (char == '5')
                coordinate.x = 4
            else if (char == '6')
                coordinate.x = 5
        }
    }

    return coordinate
}