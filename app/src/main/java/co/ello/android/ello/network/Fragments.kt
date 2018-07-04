package co.ello.android.ello


class Fragments {
    val string: String
    val needs: List<Fragments>

    constructor(string: String, needs: List<Fragments> = emptyList()) {
        this.string = string.trimIndent()
        this.needs = needs
    }

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

        val categoryProps = Fragments("""
            fragment categoryProps on Category {
              id name slug order allowInOnboarding isCreatorType level
              tileImage { ...tshirtProps }
            }
            """, needs = listOf(tshirtProps))

        val assetProps = Fragments("""
            fragment assetProps on Asset {
              id
              attachment { ...responsiveProps }
            }
            """, needs = listOf(responsiveProps))

        val contentProps = Fragments("""
            fragment contentProps on ContentBlocks {
              linkUrl
              kind
              data
              links { assets }
            }
            """)

        val postSummary = Fragments("""
            fragment postContent on Post {
              content { ...contentProps }
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
            """, needs = listOf(assetProps, authorProps, contentProps))
        val postDetails = Fragments("""
            fragment postDetails on Post {
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
            """, needs = listOf(postSummary, categoryPostActions, categoryProps, contentProps))
        val commentDetails = Fragments("""
            fragment commentDetails on Comment {
              id
              createdAt
              parentPost { id }
              content { ...contentProps }
              summary { ...contentProps }
              author { ...authorProps }
              assets { ...assetProps }
            }
            """, needs = listOf(contentProps, authorProps, assetProps))

        val userDetails = Fragments("""
            fragment userDetails on User {
              id
              username
              name
              formattedShortBio
              location
              badges
              externalLinksList { url text icon }
              # isCommunity
              userStats { totalViewsCount postsCount lovesCount followersCount followingCount }
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

        val editorialsContent = Fragments("""
            fragment editorialImageVersions on ResponsiveImageVersions {
                xhdpi { ...imageProps }
                original { ...imageProps }
            }

            fragment editorial on Editorial {
                id
                kind
                title
                subtitle
                url
                path
                oneByOneImage { ...editorialImageVersions }
                twoByTwoImage { ...editorialImageVersions }
                stream { query tokens }
                post { ...postSummary }
            }
            """, needs = listOf(imageProps, postSummary))

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
                ...postDetails
            }
            """, needs = listOf(postDetails))
        val editorialsBody = Fragments("""
            next isLastPage
            editorials {
                ...editorial
            }
            """, needs = listOf(editorialsContent))
        val postBody = Fragments("""
            ...postDetails
            """, needs = listOf(postDetails))
        val commentStreamBody = Fragments("""
            next isLastPage
            comments {
                ...commentDetails
            }
            """, needs = listOf(commentDetails))
        val userBody = Fragments("""
            ...userDetails
            """, needs = listOf(userDetails))
    }

    val dependencies: List<Fragments> get() {
        return listOf(needs, needs.flatMap { it.dependencies as Iterable<Fragments> }).flatten()
    }

}
