package co.ello.android.ello

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.android.UI


class RelationshipController(val queue: Queue) {
    fun updateRelationship(userId: String, prev: RelationshipPriority?, next: RelationshipPriority) {
        launch(UI) {
            val result = API().updateRelationship(userId = userId, relationship = next).enqueue(queue)
            when (result) {
                is Success -> App.eventBus.post(ModelChanged(type = MappingType.UsersType, id = userId, property = Model.Property.relationshipPriority, value = next))
                is Failure -> prev?.let { App.eventBus.post(ModelChanged(type = MappingType.UsersType, id = userId, property = Model.Property.relationshipPriority, value = it)) }
            }
        }

    }
}
