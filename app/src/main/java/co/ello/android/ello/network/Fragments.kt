package co.ello.android.ello


class Fragments(val string: String, val needs: List<Fragments> = emptyList()) {
    companion object {
        val categoryPostActions = Fragments("""
            fragment categoryPostActions on CategoryPostActions {
                feature { href label method }
                unfeature { href label method }
            }
            """)

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
            """, needs = listOf(imageProps))

        val responsiveProps = Fragments("""
            fragment responsiveProps on ResponsiveImageVersions {
              mdpi { ...imageProps }
              hdpi { ...imageProps }
              xhdpi { ...imageProps }
              optimized { ...imageProps }
            }
            """, needs = listOf(imageProps))

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
            """, needs = listOf(tshirtProps, responsiveProps))

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
            """, needs = listOf(tshirtProps, responsiveProps))

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
            """, needs = listOf(imageProps, tshirtProps, responsiveProps, authorProps))

        val categoriesBody = Fragments("""
            id
            name
            slug
            order
            allowInOnboarding
            isCreatorType
            level
            tileImage { ...tshirtProps }
            """, needs = listOf(tshirtProps))
        val pageHeaderBody = Fragments("""
            id
            postToken
            category { id }
            kind
            header
            subheader
            image { ...responsiveProps }
            ctaLink { text url }
            user { ...pageHeaderUserProps }
            """, needs = listOf(responsiveProps, pageHeaderUserProps))
        val postStreamBody = Fragments("""
            next isLastPage
            posts {
                ...postSummary
                ...postContent
                repostContent { ...contentProps }
                categoryPosts {
                    id actions { ...categoryPostActions } status
                    category { ...categoryProps }
                    featuredAt submittedAt removedAt unfeaturedAt
                    featuredBy { id username name } submittedBy { id username name }
                }
                repostedSource {
                    ...postSummary
                }
            }
            """, needs = listOf(postStream, categoryPostActions))
    }

    val dependencies: List<Fragments> get() {
        return listOf(needs, needs.flatMap { it.dependencies as Iterable<Fragments> }).flatten()
    }

}
