package co.ello.android.ello


sealed class Event

data class RelationshipPriorityChanged(val userId: String, val priority: RelationshipPriority) : Event()
