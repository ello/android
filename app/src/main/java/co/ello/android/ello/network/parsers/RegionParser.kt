package co.ello.android.ello


class RegionParser {
    companion object {
        fun graphQLRegions(jsonList: JSON, isRepostContent: Boolean = false): List<Regionable> {
            val regions = jsonList.list ?: return emptyList()

            return regions.flatMap(fun (json: JSON): List<Regionable> {
                val kind = json["kind"].string?.let { RegionKind.create(it) } ?: return emptyList()

                val regionable: Regionable = when(kind) {
                    RegionKind.Text -> TextRegion.fromJSON(json) as Regionable
                    RegionKind.Image -> ImageRegion.fromJSON(json) as Regionable
                    RegionKind.Embed -> EmbedRegion.fromJSON(json) as Regionable
                }

                regionable.isRepost = isRepostContent
                return listOf(regionable)
            })
        }
    }
}
