package co.ello.android.ello


sealed class Token
class ID(val id: String) : Token()
class Slug(val slug: String) : Token()
