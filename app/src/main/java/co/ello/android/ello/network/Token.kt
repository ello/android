package co.ello.android.ello


sealed class Token {
    abstract val asId: String
    abstract val variable: GraphQLRequest.Variable

    abstract fun matches(id: String, slug: String): Boolean
}

class ID(val id: String) : Token() {
    override val asId = id
    override val variable: GraphQLRequest.Variable = GraphQLRequest.Variable.optionalID("id", id)

    override fun matches(id: String, slug: String): Boolean = id == this.id
    override fun equals(other: Any?): Boolean = other is ID && other.id == this.id
}

class Slug(val slug: String) : Token() {
    override val asId = "~$slug"
    override val variable: GraphQLRequest.Variable = GraphQLRequest.Variable.optionalString("token", slug)

    override fun matches(id: String, slug: String): Boolean = slug == this.slug
    override fun equals(other: Any?): Boolean = other is Slug && other.slug == this.slug
}
