package co.ello.android.ello


class BadgeParser() : Parser() {
    override fun parse(json: JSON): Badge {
        return Badge(
            slug = json["slug"].stringValue,
            name = json["name"].stringValue,
            caption = json["learn_more_caption"].stringValue,
            url = json["learn_more_href"].url,
            imageURL = json["image"]["url"].url
        )
    }
}
