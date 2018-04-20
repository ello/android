package co.ello.android.ello


class Fragments(val string: String, val needs: List<Fragments> = emptyList()) {
    companion object {
        fun flatten(fragments: List<Fragments>): String {
            return fragments
                .flatMap { frag -> frag.dependencies as Iterable<Fragments> }
                .distinctBy { it.string }
                .map { it.string }
                .joinToString("\n")
        }

        val imageProps = Fragments("""
            fragment imageProps on Image {
              url
              metadata { height width type size }
            }
            """)

        val tshirtProps = Fragments("""
            fragment tshirtProps on TshirtImageVersions {
              regular { ...imageProps }
              large { ...imageProps }
              original { ...imageProps }
            }
            """, listOf(imageProps))

        val responsiveProps = Fragments("""
            fragment responsiveProps on ResponsiveImageVersions {
              mdpi { ...imageProps }
              hdpi { ...imageProps }
              xhdpi { ...imageProps }
              optimized { ...imageProps }
            }
            """, listOf(imageProps))

        val authorProps = Fragments("""
            fragment authorProps on User {
              id
              username
              name
              currentUserState { relationshipPriority }
              settings {
                hasCommentingEnabled hasLovesEnabled hasRepostingEnabled hasSharingEnabled
                isCollaborateable isHireable
              }
              avatar {
                ...tshirtProps
              }
              coverImage {
                ...responsiveProps
              }
            }
            """, listOf(tshirtProps, responsiveProps))

        val pageHeaderUserProps = Fragments("""
            fragment pageHeaderUserProps on User {
              id
              username
              name
              avatar {
                ...tshirtProps
              }
              coverImage {
                ...responsiveProps
              }
            }
            """, listOf(tshirtProps, responsiveProps))

        val postStream = Fragments("""
            fragment contentProps on ContentBlocks {
              linkUrl
              kind
              data
              links { assets }
            }

            fragment assetProps on Asset {
              id
              attachment { ...responsiveProps }
            }

            fragment postContent on Post {
              content { ...contentProps }
            }

            fragment categoryProps on Category {
              id name slug order allowInOnboarding isCreatorType level
              tileImage { ...tshirtProps }
            }

            fragment postSummary on Post {
              id
              token
              createdAt
              summary { ...contentProps }
              author { ...authorProps }
              artistInviteSubmission { id artistInvite { id } }
              assets { ...assetProps }
              postStats { lovesCount commentsCount viewsCount repostsCount }
              currentUserState { watching loved reposted }
            }
            """, listOf(imageProps, tshirtProps, responsiveProps, authorProps))

        const val categoriesBody = """
            id
            name
            slug
            order
            allowInOnboarding
            isCreatorType
            level
            tileImage { ...tshirtProps }
            """
        const val pageHeaderBody = """
            id
            postToken
            category { id }
            kind
            header
            subheader
            image { ...responsiveProps }
            ctaLink { text url }
            user { ...pageHeaderUserProps }
            """
        const val postStreamBody = """
            next isLastPage
            posts {
                ...postSummary
                ...postContent
                repostContent { ...contentProps }
                categories { ...categoryProps }
                currentUserState { loved reposted watching }
                repostedSource {
                    ...postSummary
                }
            }
            """
    }

    val dependencies: List<Fragments> get() {
        return listOf(listOf(this), needs, needs.flatMap { it.dependencies as Iterable<Fragments>}).flatten()
    }

}
