package co.ello.android.ello

import com.android.volley.Request
import java.util.UUID


interface AuthenticationEndpoint {
    val requiresAnyToken: Boolean
    val supportsAnonymousToken: Boolean
}

typealias RequestAttempt = Triple<AuthenticationEndpoint, Block, Block>

class AuthenticationManager(val requestQueue: Queue) {

    private var waitList: MutableList<RequestAttempt> = mutableListOf()
    private var uuid: UUID = randomUUID()
    private var authState: AuthState
        get() { return AuthToken.state }
        set(value) { AuthToken.state = value }

    // set queue to null in specs, and reauth requests are sent synchronously.
    var queue: DispatchQueue? = DispatchQueue()

    fun attemptRequest(target: AuthenticationEndpoint, retry: (() -> Unit), proceed: ((UUID) -> Unit), cancel: (() -> Unit)) {
        val uuid = this.uuid

        if (authState.isUndetermined) {
            attemptAuthentication(uuid, RequestAttempt(target, retry, cancel))
        }
        else if (authState.isTransitioning && target.requiresAnyToken) {
            appendRequest(RequestAttempt(target, retry, cancel))
        }
        else {
            if (canMakeRequest(target)) {
                proceed(uuid)
            }
            else {
                cancel()
            }
        }
    }

    fun canMakeRequest(target: AuthenticationEndpoint): Boolean {
        if (!target.requiresAnyToken) {
            return true
        }

        if (authState.isTransitioning) {
            return false
        }

        if (authState.isAuthenticated) {
            return true
        }

        return target.supportsAnonymousToken && authState == AuthState.Anonymous
    }

    fun appendRequest(request: RequestAttempt) {
        waitList.add(request)
    }

    fun attemptAuthentication(uuid: UUID, request: RequestAttempt? = null) {
        inBackground(fun () {
            val shouldResendRequest = uuid != this.uuid
            val retry = request?.let { (_, retry, _) -> retry }
            if (retry != null && shouldResendRequest) {
                retry()
                return
            }

            if (request != null) {
                this.waitList.add(request)
            }

            when(this.authState) {
                AuthState.Initial -> {
                    val authToken = AuthToken.shared
                    this.advanceAuthState(when(authToken.type) {
                        AuthToken.Type.Password -> AuthState.Authenticated
                        AuthToken.Type.Anonymous -> AuthState.Anonymous
                        AuthToken.Type.None -> AuthState.ShouldTryAnonymousCreds
                    })
                }
                AuthState.Anonymous -> {
                    // an anonymous-authenticated request resulted in a 401 - we
                    // should log the user out
                    this.advanceAuthState(AuthState.NoToken)
                }
                AuthState.Authenticated, AuthState.ShouldTryRefreshToken -> {
                    this.authState = AuthState.RefreshTokenSent

                    val refreshToken= AuthToken.shared.refreshToken
                    if (refreshToken != null) {
                        API().reauth(refreshToken)
                            .enqueue(requestQueue)
                            .onSuccess { credentials ->
                                AuthToken.update(credentials)
                                this.advanceAuthState(AuthState.Authenticated)
                            }
                            .onFailure {
                                this.advanceAuthState(AuthState.ShouldTryAnonymousCreds)
                                // this.advanceAuthState(AuthState.shouldTryRefreshToken)
                            }
                    }
                    else {
                        this.advanceAuthState(AuthState.ShouldTryAnonymousCreds)
                    }
                }
                AuthState.ShouldTryAnonymousCreds, AuthState.NoToken -> {
                    this.authState = AuthState.AnonymousCredsSent

                    API().anonymousCreds()
                        .enqueue(requestQueue)
                        .onSuccess { credentials ->
                            AuthToken.update(credentials)
                            this.advanceAuthState(AuthState.Anonymous)
                        }.onFailure {
                            this.advanceAuthState(AuthState.NoToken)
                            // this.advanceAuthState(AuthState.shouldTryAnonymousCreds)
                        }
                }
                AuthState.RefreshTokenSent, AuthState.AnonymousCredsSent -> {}
            }
        })
    }

    private fun inBackground(block: Block) {
        queue?.let {
            it.async(block)
        } ?: block()
    }

    private fun advanceAuthState(nextState: AuthState) {
        inBackground {
            this.authState = nextState

            if (nextState == AuthState.NoToken) {
                this.uuid = randomUUID()
                AuthToken.reset()

                this.flushWaitList()
            }
            else if (nextState == AuthState.Anonymous || nextState.isAuthenticated) {
                // if you were using the app, but got logged out, you will
                // quickly receive an anonymous token.  If any Requests don't
                // support this flow , we should kick you out and present the
                // log in screen.  During login/join, though, all the Requests
                // *will* support an anonymous token.
                //
                // if, down the road, we have anonymous browsing, we should
                // require and implement robust invalidToken handlers for all
                // Controllers & Services

                this.uuid = randomUUID()
                this.flushWaitList()
            }
            else {
                this.attemptAuthentication(this.uuid)
            }
        }
    }

    private fun flushWaitList() {
        val currentWaitList = waitList
        waitList = mutableListOf()

        val runWaitList = {
            for (waitListEntry in currentWaitList) {
                val (target, makeRequest, cancelRequest) = waitListEntry
                if (this.canMakeRequest(target)) {
                    makeRequest()
                }
                else {
                    cancelRequest()
                }
            }
        }

        if (queue == null) {
            runWaitList()
        }
        else {
            DispatchQueue.main.async(runWaitList)
        }
    }
}
