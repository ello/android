package co.ello.android.ello


class RelationshipController(val queue: Queue) {
    suspend fun updateRelationship(userId: String, next: RelationshipPriority): Result<RelationshipPriority> {
        val result = API().updateRelationship(userId = userId, relationship = next).enqueue(queue)
        when (result) {
            is Success -> return Success(next)
            is Failure -> return Failure(result.error)
        }
    }
}
