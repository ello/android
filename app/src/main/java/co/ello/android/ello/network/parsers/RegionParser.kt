package co.ello.android.ello


class RegionParser {
    companion object {
        fun graphQLRegions(jsonList: JSON, isRepostContent: Boolean = false): List<Regionable> {
            val regions = jsonList.list ?: return emptyList()

            return regions.flatMap(fun (json: JSON): List<Regionable> {
                val kind = json["kind"].string?.let { RegionKind.create(it) } ?: return emptyList()

                val regionable: Regionable? = when(kind) {
                    RegionKind.Text -> textRegion(json)
                    RegionKind.Image -> imageRegion(json)
                    RegionKind.Embed -> embedRegion(json)
                }

                if (regionable != null) {
                    regionable.isRepost = isRepostContent
                    return listOf(regionable)
                }
                return emptyList()
            })
        }

        private fun textRegion(json: JSON): TextRegion? {
            return TextRegion(content = json["data"].stringValue)
        }

        private fun imageRegion(json: JSON): ImageRegion? {
            val url = json["data"]["url"].url ?: return null
            val buyButtonURL = json["linkUrl"].url
            val imageRegion = ImageRegion(url = url, buyButtonURL = buyButtonURL)

            json["links"]["assets"].string?.let { id ->
                imageRegion.addLinkObject("assets", id = id, type = MappingType.AssetsType)
            }

            return imageRegion
        }

        private fun embedRegion(json: JSON): EmbedRegion? {
            val id = json["data"]["id"].string ?: return null
            val service = json["data"]["service"].string?.let { EmbedRegion.Service.create(it) } ?: return null
            val url = json["data"]["url"].url ?: return null
            val thumbnailLargeUrl = json["data"]["thumbnail_large_url"].url ?: json["data"]["thumbnailLargeUrl"].url

            return EmbedRegion(
                id = id,
                service = service,
                url = url,
                thumbnailLargeUrl = thumbnailLargeUrl
            )
        }
    }
}
