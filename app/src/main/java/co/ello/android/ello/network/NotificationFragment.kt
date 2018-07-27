package co.ello.android.ello

class NotificationFragment(string: String, needs: List<Fragments>) : Fragments(string, needs) {

    companion object {

        val imageVersionProps = NotificationFragment("""
            fragment imageVersionProps on Image {
                url
                metadata { height width type size }
            }
            """, needs = listOf())

        val responsiveImageVersions = NotificationFragment("""
            fragment responsiveImageVersions on ResponsiveImageVersions {
                xhdpi { ...imageVersionProps }
                hdpi { ...imageVersionProps }
                mdpi { ...imageVersionProps }
                ldpi { ...imageVersionProps }
                optimized { ...imageVersionProps }
                original { ...imageVersionProps }
                video { ...imageVersionProps }
            }
            """, needs = listOf(imageVersionProps))

        val avatarImageVersion = NotificationFragment("""
            fragment avatarImageVersion on TshirtImageVersions {
                small { ...imageVersionProps }
                regular { ...imageVersionProps }
                large { ...imageVersionProps }
                original { ...imageVersionProps }
            }
            """, needs = listOf(imageVersionProps))

        val authorSummary = NotificationFragment("""
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

        val contentProps = NotificationFragment("""
            fragment contentProps on ContentBlocks {
                linkUrl
                kind
                data
                links { assets }
              }
            """, needs = listOf())

        val artistInviteSubmissionProps = NotificationFragment("""
            fragment artistInviteSubmissionProps on ArtistInviteSubmission {
                id
                status
                post { id }
                artistInvite { id title slug }
            }
            """, needs = emptyList())

        val postSummary = NotificationFragment("""
            fragment postSummary on Post {
                id
                token
                createdAt
                summary { ...contentProps }
                author { ...authorSummary }
                artistInviteSubmission { ...artistInviteSubmissionProps }
                assets { id attachment { ...responsiveImageVersions } }
                postStats { lovesCount commentsCount viewsCount repostsCount }
                currentUserState { watching loved reposted }
            }
            """, needs = listOf(contentProps, authorSummary, responsiveImageVersions, artistInviteSubmissionProps))

        val artistInviteSubmissionSummary = NotificationFragment("""
            fragment artistInviteSubmissionSummary on ArtistInviteSubmission {
                id
                status
                post { ...postSummary repostedSource { ...postSummary } }
                artistInvite { id title slug }
            }
            """, needs = listOf(postSummary))

        val commentSummary = NotificationFragment("""
            fragment commentSummary on Comment {
                id
                createdAt
                author { ...authorSummary }
                summary { ...contentProps }
                content { ...contentProps }
                assets { id attachment { ...responsiveImageVersions } }
            }
            """, needs = listOf(authorSummary, contentProps, responsiveImageVersions))

        val categorySummary = NotificationFragment("""
            fragment categorySummary on Category {
                id slug name
            }
            """, needs = listOf())

        val categoryPostSummary = NotificationFragment("""
            fragment categoryPostSummary on CategoryPost {
                id
                status
                category { ...categorySummary }
                post { ...postSummary repostedSource { ...postSummary } }
                featuredBy { ...authorSummary }
            }
            """, needs = listOf(categorySummary, postSummary, authorSummary))

        val categoryUserSummary = NotificationFragment("""
            fragment categoryUserSummary on CategoryUser {
                id
                role
                category { ...categorySummary }
                user { ...authorSummary }
            }
            """, needs = listOf(categorySummary, authorSummary))


        val loveSummary = NotificationFragment("""
            fragment loveSummary on Love {
                id
                post { ...postSummary repostedSource { ...postSummary } }
                user { ...authorSummary }
            }
            """, needs = listOf(postSummary, authorSummary))

        val watchSummary = NotificationFragment("""
            fragment watchSummary on Watch {
                id
                post { ...postSummary repostedSource { ...postSummary } }
            }
            """, needs = listOf(postSummary))

        val notificationDetails = NotificationFragment("""
            fragment notificationDetails on Notification {
                id
                kind
                subjectType
                createdAt
                subject {
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

        val notificationStreamBody = NotificationFragment("""
            next
            isLastPage
            notifications {
                ...notificationDetails
            }
            """, needs = listOf(notificationDetails))
    }

}