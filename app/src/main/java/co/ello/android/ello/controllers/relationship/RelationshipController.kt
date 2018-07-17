package co.ello.android.ello

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class RelationshipController(val queue: Queue) {
    fun updateRelationship(userId: String, prev: RelationshipPriority?, next: RelationshipPriority) {
        launch(UI) {
            val result = API().updateRelationship(userId = userId, relationship = next).enqueue(queue)
            when (result) {
                is Success -> App.eventBus.post(RelationshipPriorityChanged(userId = userId, priority = next))
                is Failure -> prev?.let { App.eventBus.post(RelationshipPriorityChanged(userId = userId, priority = it)) }
            }
        }

    }
}
