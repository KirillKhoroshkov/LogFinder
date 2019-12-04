package exception

import java.lang.IllegalArgumentException

class IllegalSearchQueryException(message: String) : IllegalArgumentException(message)