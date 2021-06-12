package customer

import java.util.*

class Profile (
    val id: String? = UUID.randomUUID().toString(),
    val name: String? = null,
    val email: String? = null,
)