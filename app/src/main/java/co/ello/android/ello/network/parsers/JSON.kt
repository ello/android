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
import java.net.URL
import java.util.*


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

    constructor(bytes: ByteArray) : this(String(bytes, Charsets.UTF_8))

    constructor(string: String) {
        try {
            jsonObject = JSONObject(string)
        } catch (e: JSONException) {
            try {
                jsonArray = JSONArray(string)
            } catch (e2: JSONException) {
                val parent = JSON("[$string]".toByteArray(Charsets.UTF_8))
                if (parent.jsonArray?.length() == 1) {
                    this.parent = parent
                    index = 0
                }
            }
        }
    }

    private constructor(parent: JSON, name: String) {
        this.parent = parent
        this.name = name
        jsonObject = parent.jsonObject?.optJSONObject(name)
        jsonArray = parent.jsonObject?.optJSONArray(name)
    }

    private constructor(parent: JSON, index: Int) {
        this.parent = parent
        this.index = index
        jsonObject = parent.jsonArray?.optJSONObject(index)
        jsonArray = parent.jsonArray?.optJSONArray(index)
    }

    constructor(value: List<JSON>) {
        jsonArray = JSONArray(value)
    }

    constructor(value: Map<String, Any>) {
        jsonObject = JSONObject(value)
    }

    fun merge(mergeJson: JSON) {
        val jsonObject = this.jsonObject
        val mergeJsonObject = mergeJson.jsonObject
        if (jsonObject != null && mergeJsonObject != null) {
            val names = mergeJsonObject.names()
            for (index in 0..(names.length() - 1)) {
                val name = names.getString(index)
                if (mergeJsonObject.isNull(name)) {
                    jsonObject.put(name, null)
                }
                else {
                    jsonObject.put(name, mergeJsonObject.get(name))
                }
            }
        }
    }

    operator fun set(name: String, value: JSON) {
        jsonObject?.put(name, value)
    }

    operator fun get(name: String): JSON {
        return JSON(this, name)
    }

    operator fun get(index: Int): JSON {
        return JSON(this, index)
    }

    private fun <T : Any> getValue(fromParentObject: (JSONObject, String) -> T?, fromParentArray: (JSONArray, Int) -> T?): T? {
        try {
            val name = this.name
            val index = this.index
            if (name != null) {
                val jsonObject = parent?.jsonObject
                if (jsonObject is JSONObject) {
                    if (jsonObject.has(name) && !jsonObject.isNull(name)) {
                        return fromParentObject(jsonObject, name)
                    }
                    return null
                }
            } else if (index != null) {
                val jsonArray = parent?.jsonArray
                if (jsonArray is JSONArray) {
                    if (index < jsonArray.length() && !jsonArray.isNull(index)) {
                        return fromParentArray(jsonArray, index)
                    }
                    return null
                }
            }
        } catch(e: JSONException) {
        }

        return null
    }

    val exists: Boolean get() = getValue({ o, n -> o.has(n) && !o.isNull(n) }, { a, i -> i >= 0 && i < a.length() }) ?: false
    val boolean: Boolean? get() = getValue({ o, n -> o.getBoolean(n) }, { a, i -> a.getBoolean(i) })
    val int: Int? get() = getValue({ o, n -> o.getInt(n) }, { a, i -> a.getInt(i) })
    val long: Long? get() = getValue({ o, n -> o.getLong(n) }, { a, i -> a.getLong(i) })
    val double: Double? get() = getValue({ o, n -> o.getDouble(n) }, { a, i -> a.getDouble(i) })
    val string: String? get() = getValue({ o, n -> o.getString(n) }, { a, i -> a.getString(i) })
    val id: String? get() = string ?: int?.let { "$it" }

    val list: List<JSON>?
        get() {
            val length = jsonArray?.length()
            if (length != null) {
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
            val jsonObject = jsonObject ?: return null
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
            val jsonObject = jsonObject ?: return null
            val names = jsonObject.keys()
            val result = HashMap<String, Any>()
            while (names.hasNext()) {
                val name = names.next()
                val value = jsonObject.opt(name) ?: continue
                result.put(name, value)
            }
            return result

        }

    fun rawString(): String? {
        val jsonObject = jsonObject
        if (jsonObject != null) {
            return jsonObject.toString()
        }

        val jsonArray = jsonArray
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

val JSON.date: Date?
    get() = string?.toDate()

val JSON.url: URL?
    get() = string?.toURL()

val JSON.idValue: String
    get() = string ?: int?.let { "$it" } ?: ""

val JSON.listValue: List<JSON>
    get() = list ?: listOf()

val JSON.mapValue: Map<String, JSON>
    get() = map ?: mapOf()
