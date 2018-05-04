package co.ello.android.ello


enum class AuthState {
    Initial,
    NoToken,
    Anonymous,
    Authenticated,
    RefreshTokenSent,
    ShouldTryRefreshToken,
    AnonymousCredsSent,
    ShouldTryAnonymousCreds;

    val nextStates: List<AuthState> get() = when(this) {
        Initial -> listOf(AuthState.NoToken, AuthState.ShouldTryAnonymousCreds, AuthState.Anonymous, AuthState.Authenticated)
        NoToken -> listOf(AuthState.Authenticated, AuthState.AnonymousCredsSent, AuthState.ShouldTryAnonymousCreds)
        Anonymous -> listOf(AuthState.Authenticated, AuthState.NoToken)
        Authenticated -> listOf(AuthState.RefreshTokenSent, AuthState.NoToken)
        RefreshTokenSent -> listOf(AuthState.NoToken, AuthState.Authenticated)
        ShouldTryRefreshToken -> listOf(AuthState.AnonymousCredsSent)
        AnonymousCredsSent -> listOf(AuthState.NoToken, AuthState.Anonymous)
        ShouldTryAnonymousCreds -> listOf(AuthState.AnonymousCredsSent)
    }

    val isAuthenticated: Boolean get() = when(this) {
        AuthState.Authenticated -> true
        else -> false
    }

    val isUndetermined: Boolean get() = when(this) {
        AuthState.Initial, AuthState.NoToken -> true
        else -> false
    }

    val isTransitioning: Boolean get() = when(this) {
        AuthState.Authenticated, AuthState.Anonymous -> false
        else -> true
    }

    fun canTransitionTo(state: AuthState): Boolean {
        return nextStates.contains(state)
    }
}
