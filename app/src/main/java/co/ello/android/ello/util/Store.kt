package co.ello.android.ello


typealias StoreTable = MutableMap<String, Model>
typealias StoreDatabase = MutableMap<String, StoreTable>

class Store {
    private val db: StoreDatabase = mutableMapOf()

    companion object {
        val store = Store()

        inline fun <T: Model> read(transaction: ((ReadTransaction) -> T?)): T? {
            return transaction(ReadTransaction(store))
        }

        fun write(transaction: ((WriteTransaction) -> Unit)) {
            transaction(WriteTransaction(store))
        }
    }

    open class ReadTransaction(val store: Store) {
        fun getObject(key: String, collection: MappingType): Model? {
            return store.db[collection.name]?.let { it[key] }
        }
    }

    class WriteTransaction(store: Store) : ReadTransaction(store) {
        fun setObject(model: Model, key: String, collection: MappingType) {
            val table: StoreTable = store.db[collection.name] ?: mutableMapOf()
            table[key] = model
        }
    }
}
