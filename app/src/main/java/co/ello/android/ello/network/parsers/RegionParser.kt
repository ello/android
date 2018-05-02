package co.ello.android.ello


class RegionParser {
    companion object {
        fun graphQLRegions(jsonList: JSON, isRepostContent: Boolean = false): List<Regionable> {
            val regions = jsonList.list ?: return emptyList()

            return regions.flatMap(fun (json: JSON): List<Regionable> {
                val kind = json["kind"].string?.let { RegionKind.create(it) } ?: return emptyList()

                val regionable: Regionable? = when(kind) {
                    RegionKind.Text -> TextRegion.fromJSON(json)
                    RegionKind.Image -> ImageRegion.fromJSON(json)
                    RegionKind.Embed -> EmbedRegion.fromJSON(json)
                }

                if (regionable != null) {
                    regionable.isRepost = isRepostContent
                    return listOf(regionable)
                }
                return emptyList()
            })
        }
    }
}
