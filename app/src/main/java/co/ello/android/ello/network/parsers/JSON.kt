/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 koher
 * https://github.com/koher/KotlinyJSON
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package co.ello.android.ello

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.ArrayList
import java.util.HashMap


class JSON {
    private var jsonObject: JSONObject?
    private var jsonArray: JSONArray?

    private var parent: JSON?
    private var name: String?
    private var index: Int?

    init {
        jsonObject = null
        jsonArray = null

        parent = null
        name = null
        index = null
    }

    constructor(bytes: ByteArray) {
        val string = String(bytes, Charsets.UTF_8)
        try {
            jsonObject = JSONObject(string)
        } catch (e: JSONException) {
            try {
                jsonArray = JSONArray(string)
            } catch (e2: JSONException) {
                try {
                    val parent = JSON("[$string]".toByteArray(Charsets.UTF_8))
                    if (parent.getJSONArray()?.length() == 1) {
                        this.parent = parent
                        index = 0
                    }
                } catch (e3: JSONException) {
                }
            }
        }
    }

    constructor(inputStream: InputStream) : this(inputStreamToByteArray(inputStream) ?: ByteArray(0)) {
    }

    constructor(file: File) : this(inputStreamToByteArray(FileInputStream(file), true) ?: ByteArray(0)) {
    }

    constructor(value: Boolean) {
        val jsonArray = JSONArray()
        jsonArray.put(value)
        parent = JSON(jsonArray)
        index = 0
    }

    constructor(value: Int) {
        val jsonArray = JSONArray()
        jsonArray.put(value)
        parent = JSON(jsonArray)
        index = 0
    }

    constructor(value: Long) {
        val jsonArray = JSONArray()
        jsonArray.put(value)
        parent = JSON(jsonArray)
        index = 0
    }

    constructor(value: Double) {
        val jsonArray = JSONArray()
        jsonArray.put(value)
        parent = JSON(jsonArray)
        index = 0
    }

    constructor(value: String) {
        val jsonArray = JSONArray()
        jsonArray.put(value)
        parent = JSON(jsonArray)
        index = 0
    }

    constructor(value: List<JSON>) {
        jsonArray = JSONArray(value)
    }

    constructor(value: Map<String, Any>) {
        jsonObject = JSONObject(value)
    }

    private constructor(parent: JSON, name: String) {
        this.parent = parent
        this.name = name
    }

    private constructor(parent: JSON, index: Int) {
        this.parent = parent
        this.index = index
    }

    private constructor(jsonArray: JSONArray) {
        this.jsonArray = jsonArray
    }

    operator fun set(name: String, value: JSON) {
        parent?.getJSONObject()?.put(name, value)
    }

    operator fun get(name: String): JSON {
        return JSON(this, name)
    }

    operator fun get(index: Int): JSON {
        return JSON(this, index)
    }

    private fun <T : Any> getValue(fromParentObject: (JSONObject, String) -> T?, fromParentArray: (JSONArray, Int) -> T?): T? {
        try {
            if (name is String) {
                val jsonObject = parent?.getJSONObject()
                if (jsonObject is JSONObject) {
                    return fromParentObject(jsonObject, name!!)
                }
            } else if (index is Int) {
                val jsonArray = parent?.getJSONArray()
                if (jsonArray is JSONArray) {
                    return fromParentArray(jsonArray, index!!)
                }
            }
        } catch(e: JSONException) {
        }

        return null
    }

    private fun getJSONObject(): JSONObject? {
        if (jsonObject !is JSONObject) {
            jsonObject = getValue({ o, n -> o.getJSONObject(n) }, { a, i -> a.getJSONObject(i) })
        }

        return jsonObject
    }

    private fun getJSONArray(): JSONArray? {
        if (jsonArray !is JSONArray) {
            jsonArray = getValue({ o, n -> o.getJSONArray(n) }, { a, i -> a.getJSONArray(i) })
        }

        return jsonArray
    }

    val boolean: Boolean?
        get() {
            return getValue({ o, n -> o.getBoolean(n) }, { a, i -> a.getBoolean(i) })
        }

    val int: Int?
        get() {
            return getValue({ o, n -> o.getInt(n) }, { a, i -> a.getInt(i) })
        }

    val long: Long?
        get() {
            return getValue({ o, n -> o.getLong(n) }, { a, i -> a.getLong(i) })
        }

    val double: Double?
        get() {
            return getValue({ o, n -> o.getDouble(n) }, { a, i -> a.getDouble(i) })
        }

    val string: String?
        get() {
            return getValue({ o, n -> o.getString(n) }, { a, i -> a.getString(i) })
        }

    val id: String?
        get() {
            return string ?: int?.let { "$it" }
        }

    val list: List<JSON>?
        get() {
            val length = getJSONArray()?.length()
            if (length is Int) {
                val result = ArrayList<JSON>()
                for (index in 0..(length - 1)) {
                    result.add(JSON(this, index))
                }
                return result
            } else {
                return null
            }
        }

    val map: Map<String, JSON>?
        get() {
            val jsonObject = getJSONObject() ?: return null
            val names = jsonObject.keys()
            val result = HashMap<String, JSON>()
            while (names.hasNext()) {
                val name = names.next()
                result.put(name, this.get(name))
            }
            return result
        }

    val obj: Map<String, Any>?
        get() {
            val jsonObject = getJSONObject() ?: return null
            val names = jsonObject.keys()
            val result = HashMap<String, Any>()
            while (names.hasNext()) {
                val name = names.next()
                val value = jsonObject.opt(name) ?: continue
                result.put(name, value)
            }
            return result

        }

    fun rawBytes(): ByteArray? {
        return rawString()?.toByteArray(Charsets.UTF_8)
    }

    fun rawString(): String? {
        val jsonObject = getJSONObject()
        if (jsonObject != null) {
            return jsonObject.toString()
        }

        val jsonArray = getJSONArray()
        if (jsonArray != null) {
            return jsonArray.toString()
        }

        val booleanValue = boolean
        if (booleanValue != null) {
            return booleanValue.toString()
        }

        val doubleValue = double
        if (doubleValue != null) {
            if (doubleValue.toLong().toDouble() != doubleValue) { // has decimals
                return doubleValue.toString()
            }
        }

        val longValue = long // int is included
        if (longValue != null) {
            return longValue.toString()
        }

        val stringValue = string
        if (stringValue != null) {
            return JSONObject.quote(stringValue)
        }

        return null
    }

    override fun toString(): String {
        return rawString() ?: ""
    }

    override fun equals(other: Any?): Boolean {
        if (!(other is JSON)) {
            return false
        }

        val list1 = list
        if (list1 != null) {
            val list2 = other.list
            if (list2 != null) {
                return list1 == list2
            } else {
                return false
            }
        }

        val map1 = map
        if (map1 != null) {
            val map2 = other.map
            if (map2 != null) {
                return map1 == map2
            } else {
                return false
            }
        }

        val boolean1 = boolean
        if (boolean1 != null) {
            val boolean2 = other.boolean
            if (boolean2 != null) {
                return boolean1 == boolean2
            } else {
                return false
            }
        }

        val double1 = double
        if (double1 != null) {
            if (double1.toLong().toDouble() != double1) {
                val double2 = other.double
                if (double2 != null) {
                    return double1 == double2
                }
            }
        }

        val long1 = long
        if (long1 != null) {
            val long2 = other.long
            if (long2 != null) {
                return long1 == long2
            } else {
                return false
            }
        }

        val string1 = string
        if (string1 != null) {
            val string2 = other.string
            if (string2 != null) {
                return string1 == string2
            } else {
                return false
            }
        }

        return true // null == null
    }
}

val JSON.booleanValue: Boolean
    get() = boolean ?: false

val JSON.intValue: Int
    get() = int ?: 0

val JSON.longValue: Long
    get() = long ?: 0L

val JSON.doubleValue: Double
    get() = double ?: 0.0

val JSON.stringValue: String
    get() = string ?: ""

val JSON.idValue: String
    get() = string ?: int?.let { "$it" } ?: ""

val JSON.listValue: List<JSON>
    get() = list ?: listOf()

val JSON.mapValue: Map<String, JSON>
    get() = map ?: mapOf()

private fun inputStreamToByteArray(inputStream: InputStream, closeWhenDone: Boolean = false): ByteArray? {
    try {
        val buffer = ByteArray(0x1000)
        val outputStream = ByteArrayOutputStream()

        while (true) {
            val length = inputStream.read(buffer)
            if (length < 0) {
                break
            }

            outputStream.write(buffer, 0, length)
        }

        return outputStream.toByteArray()
    } catch (e: IOException) {
        return null
    } finally {
        if (closeWhenDone) {
            try {
                inputStream.close()
            } catch (e: IOException) {
            }
        }
    }
}
