package co.ello.android.ello


sealed class Event

data class ModelChanged(
    val type: MappingType,
    val id: String,
    val property: Model.Property,
    val value: Any) : Event() {

}
