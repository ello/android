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

        val avatarImageVersion = Fragments("""
            fragment avatarImageVersion on TshirtImageVersions {
              small { ...imageProps }
              regular { ...imageProps }
              large { ...imageProps }
              original { ...imageProps }
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

        val authorSummary = Fragments("""
            fragment authorSummary on User {
              id
              username
              name
              currentUserState { relationshipPriority }
              avatar {
                ...avatarImageVersion
              }
            }
            """, needs = listOf(avatarImageVersion))

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

        val commentSummary = Fragments("""
            fragment commentSummary on Comment {
              id
              createdAt
              author { ...authorSummary }
              summary { ...contentProps }
              content { ...contentProps }
              assets { id attachment { ...responsiveImageVersions } }
            }
            """)

        val categorySummary = Fragments("""
            fragment categorySummary on Category {
              id slug name
            }
            """)

        val categoryPostSummary = Fragments("""
            fragment categoryPostSummary on CategoryPost {
              id
              status
              category { ...categorySummary }
              post { ...postSummary repostedSource { ...postSummary } }
              featuredBy { ...authorSummary }
            }
            """, needs = listOf(categorySummary, postSummary, authorSummary))

        val categoryUserSummary = Fragments("""
            fragment categoryUserSummary on CategoryUser {
              id
              role
              category { ...categorySummary }
              user { ...authorSummary }
            }
            """, needs = listOf(categorySummary, authorSummary))

        val artistInviteSubmissionSummary = Fragments("""
            fragment artistInviteSubmissionSummary on ArtistInviteSubmission {
              id
              status
              post { ...postSummary repostedSource { ...postSummary } }
              artistInvite { id title slug }
            }
            """, needs = listOf(postSummary))

        val loveSummary = Fragments("""
            fragment loveSummary on Love {
              id
              post { ...postSummary repostedSource { ...postSummary } }
              user { ...authorSummary }
            }
            """, needs = listOf(postSummary, authorSummary))

        val watchSummary = Fragments("""
            fragment watchSummary on Watch {
              id
              post { ...postSummary repostedSource { ...postSummary } }
            }
            """, needs = listOf(postSummary))

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

        val notificationDetails = Fragments("""
            fragment notificationDetails on Notification {
              id
              kind
              subjectType
              createdAt
              subject {
              __typename
                ... on Post { ...postSummary repostedSource { ...postSummary } }
                ... on Comment { ...commentSummary parentPost { ...postSummary repostedSource { ...postSummary } } }
                ... on User { ...authorSummary }
                ... on CategoryUser { ...categoryUserSummary }
                ... on CategoryPost { ...categoryPostSummary }
                ... on Love { ...loveSummary }
                ... on ArtistInviteSubmission { ...artistInviteSubmissionSummary }
                ... on Watch { ...watchSummary }
              }
            }
            """, needs = listOf(postSummary, commentSummary, authorSummary, categoryUserSummary, categoryPostSummary, loveSummary, artistInviteSubmissionSummary, watchSummary))


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
        val notificationStreamBody = Fragments("""
            next
            isLastPage
            notifications {
                ...notificationDetails
            }
            """, needs = listOf(notificationDetails))
        val userBody = Fragments("""
            ...userDetails
            """, needs = listOf(userDetails))
    }

    val dependencies: List<Fragments> get() {
        return listOf(needs, needs.flatMap { it.dependencies as Iterable<Fragments> }).flatten()
    }

}
